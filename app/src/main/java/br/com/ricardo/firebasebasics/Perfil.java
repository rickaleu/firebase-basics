package br.com.ricardo.firebasebasics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Perfil extends AppCompatActivity {

    private TextView textPerfilEmail;
    private TextView textPerfilId;
    private Button buttonPerfilDeslogar;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        textPerfilEmail = (TextView) findViewById(R.id.text_resetar_email);
        textPerfilId = (TextView) findViewById(R.id.text_perfil_id);
        buttonPerfilDeslogar = (Button) findViewById(R.id.button_resetar);

        buttonPerfilDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Conexao.logOut();
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

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
}
