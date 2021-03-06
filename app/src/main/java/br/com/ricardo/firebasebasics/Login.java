package br.com.ricardo.firebasebasics;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText editLoginEmail;
    private EditText editLoginSenha;
    private Button buttonLoginLogar;
    private Button buttonLoginNovoUsuario;
    private SignInButton buttonGoogle;
    private LoginButton buttonFacebook;
    private TextView textLoginEsqueciSenha;

    private FirebaseAuth auth;

    //Google Login
    private GoogleApiClient googleApiClient;

    //Facebook Login
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editLoginEmail = (EditText) findViewById(R.id.edit_login_email);
        editLoginSenha = (EditText) findViewById(R.id.edit_login_senha);
        buttonLoginLogar = (Button) findViewById(R.id.button_login_registrar);
        buttonLoginNovoUsuario = (Button) findViewById(R.id.button_login_voltar);
        buttonGoogle = (SignInButton) findViewById(R.id.button_google);
        buttonFacebook = (LoginButton) findViewById(R.id.button_facebook);
        textLoginEsqueciSenha = (TextView) findViewById(R.id.text_login_esqueci_senha);

        buttonFacebook.setReadPermissions("email", "public_profile");

        criaConexaoGoogle();
        inicializaFacebookCallback();


        buttonLoginLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editLoginEmail.getText().toString().trim();
                String senha = editLoginSenha.getText().toString().trim();

                login(email, senha);
            }
        });


        buttonLoginNovoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, Cadastro.class);
                startActivity(intent);
            }
        });

        //Google
        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Fazendo um intent pra chamar a janelinha do google com as contas de email.
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, 1);

            }
        });

        //Facebook
        buttonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseFacebookLogin(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(Login.this, "Operação Cancelada.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Login.this, "Erro no login com o Facebook.", Toast.LENGTH_SHORT).show();
            }
        });


        textLoginEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, ResetarSenha.class);
                startActivity(intent);
            }
        });

    }

    private void login(String email, String senha){

        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Logado com sucesso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, Perfil.class);
                            startActivity(intent);

                            editLoginEmail.setText("");
                            editLoginSenha.setText("");

                        } else {
                            Toast.makeText(Login.this, "Email ou senha errados.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void criaConexaoGoogle(){

        //Inicializa a conexão com o Authentication do Firebase.
        auth = FirebaseAuth.getInstance();

        //Cria a conexão com SignIn do Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Montando a "telinha" com os usuários.
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    public void inicializaFacebookCallback(){

        //Inicializa a conexão com o Authentication do Firebase.
        auth = FirebaseAuth.getInstance();

        //Criando a chamada pro SDK do Facebook.
        callbackManager = CallbackManager.Factory.create();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //se o resquestCode for o mesmo, recebe o retorno trazendo a janelinha.
        if(requestCode == 1){

            //Associando ao atributo resultado, o retorno do ActivityForResults.
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()){
                //Se o resultado for sucesso, cria a conta vinculada.
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseGoogleLogin(account);
            }
        } else {
            //Facebook
            callbackManager.onActivityResult(requestCode,resultCode, data);
        }

    }

    //Google
    private void firebaseGoogleLogin(GoogleSignInAccount account) {

        //Criando uma credencial com os dados capturados lá da conexão da janelinha.
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        //Manda a credencial pra Api do Google se logar com ela.
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            Intent intent = new Intent(Login.this, Perfil.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(Login.this, "Falha na conexão.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //Facebook
    private void firebaseFacebookLogin(AccessToken accessToken){

        //Criando uma credencial com os dados capturados da janelinha.
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

        //Manda a credencial pra Api do Facebook se logar com ela.
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Intent intent = new Intent(Login.this, Perfil.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(Login.this, "Erro de autenticação com o Firebase.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();

        auth = Conexao.getFirebaseAuth();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Falha na conexão.", Toast.LENGTH_SHORT).show();
    }
}
