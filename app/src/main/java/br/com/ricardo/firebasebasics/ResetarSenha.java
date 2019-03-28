package br.com.ricardo.firebasebasics;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetarSenha extends AppCompatActivity {

    private EditText editResetarEmail;
    private Button buttonResetar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetar_senha);

        editResetarEmail = (EditText) findViewById(R.id.edit_resetar_email);
        buttonResetar = (Button) findViewById(R.id.button_resetar);

        buttonResetar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editResetarEmail.getText().toString().trim();
                resetSenha(email);

            }
        });
    }

    private void resetSenha(String email) {

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(ResetarSenha.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(ResetarSenha.this, "Um email foi encaminhado para o seu email.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ResetarSenha.this, "Email não registrado.", Toast.LENGTH_SHORT).show();
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

