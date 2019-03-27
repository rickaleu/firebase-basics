package br.com.ricardo.firebasebasics;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Conexao {

    private static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;
    private static FirebaseUser firebaseUser;

    //Construtor.
    private Conexao() {
    }

    //Getter para o atributo firebaseAuth, só que com uma verificação para validar se ele está nulo.
    public static FirebaseAuth getFirebaseAuth() {

        if(firebaseAuth == null){
            inicializaFirebaseAuth();
        }

        return firebaseAuth;
    }

    private static void inicializaFirebaseAuth() {

        //Pega a instancia do objeto firebaseAuth.
        firebaseAuth = FirebaseAuth.getInstance();

        //Listener pra receber os dados do próprio usuário.
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //Pegando o usuário atual
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //Caso o resultado não seja vazio, eu associo o usuário atual (getCurrentUser()) no atributo firebaseUser.
                if(user != null){
                    firebaseUser = user;
                }
            }
        };

        //O atributo do tipo firebaseAuth recebe os dados do listener (authStateListener).
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    //Getter para o atributo firebaseUser.
    public static FirebaseUser getFirebaseUser(){
        return firebaseUser;
    }

    //Método pra fazer logOut.
    public static void logOut(){

        firebaseAuth.signOut();
    }
}
