package br.com.ricardo.firebasebasics;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.ricardo.firebasebasics.Modelo.Pessoa;

public class Pesquisa extends AppCompatActivity {

    private EditText editPesquisar;
    private RecyclerView recyclerPesquisar;

    private List<Pessoa> pessoaList;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);

        editPesquisar = (EditText) findViewById(R.id.edit_firebase_pesquisa);
        recyclerPesquisar = (RecyclerView) findViewById(R.id.recycler_firebase_pesquisa);
        recyclerPesquisar.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerPesquisar.setLayoutManager(linearLayoutManager);

        inicializaFirebase();

        editPesquisar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String palavra = editPesquisar.getText().toString().trim();

                pesquisarPalavra(palavra);
            }
        });

    }

    private void pesquisarPalavra(String palavra) {

        //Atributo do tipo Classe Query, que faz parte do pacote do Firebase.
        Query query;

        if(palavra.equals("")){
            //Faz a query de "select com where" pesquisando por nome.
            query = databaseReference.child("Pessoa").orderByChild("nome");
        } else {
            //Faz a query de "select com where" pesquisando por nome, mas começando pelo nome e terminando com o que tiver depois.
            query = databaseReference.child("Pessoa").orderByChild("nome").startAt(palavra).endAt(palavra + "\uf8ff");
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                pessoaList = new ArrayList<>();

                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    Pessoa p = ds.getValue(Pessoa.class);
                    pessoaList.add(p);

                }

                FirebasePesquisarAdapter adapter = new FirebasePesquisarAdapter(pessoaList);
                recyclerPesquisar.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inicializaFirebase() {

        FirebaseApp.initializeApp(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        pesquisarPalavra("");
    }
}
