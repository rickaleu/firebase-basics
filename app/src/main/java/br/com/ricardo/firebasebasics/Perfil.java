package br.com.ricardo.firebasebasics;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Perfil extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private ImageView image;
    private TextView textPerfilEmail;
    private TextView textPerfilId;
    private Button buttonPerfilDeslogar;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStatelistener;
    private FirebaseUser user;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        image = (ImageView) findViewById(R.id.imageView);
        textPerfilEmail = (TextView) findViewById(R.id.text_resetar_email);
        textPerfilId = (TextView) findViewById(R.id.text_perfil_id);
        buttonPerfilDeslogar = (Button) findViewById(R.id.button_resetar);

        inicializaFirebase();
        criaConexaoGoogle();

        buttonPerfilDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Desconectando o Firebase.
                Conexao.logOut();

                //Desconectando o auth.
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Toast.makeText(Perfil.this, "Conta desconectada", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
        });
    }

    public void criaConexaoGoogle(){

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

    public void inicializaFirebase(){

        //Inicializei a conexão com o Firebase.
        auth = FirebaseAuth.getInstance();

        //Criei uma instancia do listener.
        authStatelistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //Atributo do tipo FirebaseUser recebendo o usuário.
                user = firebaseAuth.getCurrentUser();

                if(user != null){

                    //Carregando os dados nos componentes.
                    textPerfilEmail.setText(user.getEmail());
                    textPerfilId.setText(user.getUid());
                    Glide.with(Perfil.this).load(user.getPhotoUrl()).into(image);

                } else{
                    finish();
                }
            }
        };
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        //Método override apenas implementado do Listener pra falha.
        Toast.makeText(this, "Falha na conexão.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Quando houver alguma alteração, ao startar a activity, é chamado o listener pra carregar os componentes.
        auth.addAuthStateListener(authStatelistener);

        //Quando a activity startar, é recuperado através dos métodos getters a autenticação e o usuário.
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();

        if(user == null){
            finish();
        } else {
            textPerfilEmail.setText("Email: " + user.getEmail());
            textPerfilId.setText("ID: " + user.getUid());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Retira as informações pra não sobrecarregar o aplictivo ao fechar a activity.
        auth.removeAuthStateListener(authStatelistener);
    }
}
