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

public class Cadastro extends AppCompatActivity {

    private EditText editCadastroEmail;
    private EditText editCadastroSenha;
    private Button buttonCadastroRegistrar;
    private Button buttonCadastroVoltar;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editCadastroEmail = (EditText) findViewById(R.id.edit_cadastro_email);
        editCadastroSenha = (EditText) findViewById(R.id.edit_cadastro_senha);
        buttonCadastroRegistrar = (Button) findViewById(R.id.button_cadastro_registrar);
        buttonCadastroVoltar = (Button) findViewById(R.id.button_cadastro_voltar);

        buttonCadastroVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonCadastroRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editCadastroEmail.getText().toString().trim();
                String senha = editCadastroSenha.getText().toString().trim();

                criarUsuario(email, senha);
            }
        });
        
    }

    private void criarUsuario(String email, String senha){

        //Aqui, é feito o cadastro na base do Firebase com os dados digitados nos campos.
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(Cadastro.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //Verificar através do parâmetro task, se o registro deu certo ou não.
                        if(task.isSuccessful()){
                            Toast.makeText(Cadastro.this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Cadastro.this, Perfil.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(Cadastro.this, "Errooooou no cadastro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Quando a activity startar, eu recebo no objeto auth, a conexão.
        auth = Conexao.getFirebaseAuth();
    }
}
