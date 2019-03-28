package br.com.ricardo.firebasebasics;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText editLoginEmail;
    private EditText editLoginSenha;
    private Button buttonLoginLogar;
    private Button buttonLoginNovoUsuario;
    private TextView textLoginEsqueciSenha;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editLoginEmail = (EditText) findViewById(R.id.edit_login_email);
        editLoginSenha = (EditText) findViewById(R.id.edit_login_senha);
        buttonLoginLogar = (Button) findViewById(R.id.button_login_registrar);
        buttonLoginNovoUsuario = (Button) findViewById(R.id.button_login_voltar);
        textLoginEsqueciSenha = (TextView) findViewById(R.id.text_login_esqueci_senha);

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

                        } else {
                            Toast.makeText(Login.this, "Email ou senha errados.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        auth = Conexao.getFirebaseAuth();
    }
}
