package br.com.ricardo.firebasebasics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

public class CrashActivity extends AppCompatActivity {

    private static final String TAG = CrashActivity.class.getSimpleName();
    private Button buttonCrash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);

        buttonCrash = (Button) findViewById(R.id.button_crash);
        buttonCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Crashlytics.log("Clicou no botão");
                FirebaseCrash.logcat(Log.INFO, TAG, "Iniciando uma rotina perigosa");

                Intent intent = new Intent(CrashActivity.this, Conexao.class);
                startActivity(intent);

                try{
                    throw new RuntimeException("Ocorreu um erro");
                } catch (RuntimeException e){
                    FirebaseCrash.report(e);
                }


            }
        });

    }
}
