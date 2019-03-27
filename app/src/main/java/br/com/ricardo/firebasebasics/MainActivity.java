package br.com.ricardo.firebasebasics;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.ricardo.firebasebasics.Modelo.Pessoa;


public class MainActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editEmail;
    private RecyclerView recyclerViewFirebase;
    private List<Pessoa> pessoaList;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNome = (EditText) findViewById(R.id.edit_nome);
        editEmail = (EditText) findViewById(R.id.edit_email);


        inicializaFirebase();
        carregaLista();

        recyclerViewFirebase = (RecyclerView) findViewById(R.id.recycler_firebase);
        recyclerViewFirebase.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewFirebase.setLayoutManager(linearLayoutManager);


    }

    private void carregaLista() {

        databaseReference.child("Pessoa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                pessoaList = new ArrayList<>();

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Pessoa p = ds.getValue(Pessoa.class);
                    pessoaList.add(p);
                }

                FirebaseCrudAdapter adapter = new FirebaseCrudAdapter(pessoaList);
                recyclerViewFirebase.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void inicializaFirebase(){

        FirebaseApp.initializeApp(this);

        //Pega a instancia da base com o Firebase, deixa atualizado os 2 lados (cliente e servidor) e pega a referência da base.
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.menu_novo){
            Pessoa p = new Pessoa();
            p.setUid(UUID.randomUUID().toString());
            p.setNome(editNome.getText().toString());
            p.setEmail(editEmail.getText().toString());

            //Cria a tabela e adiciona os campos.
            databaseReference.child("Pessoa").child(p.getUid()).setValue(p);

            limpaCampos();
        }

        return true;
    }

    public void limpaCampos(){
        editNome.setText("");
        editEmail.setText("");
    }
}
