package br.com.ricardo.firebasebasics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

public class CloudMessaging extends AppCompatActivity {


    private TextView textCloudMessaging;
    private Button buttonErro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_messaging);



        textCloudMessaging = (TextView) findViewById(R.id.textNotification);
        buttonErro = (Button) findViewById(R.id.button_erro);


        if(getIntent().getExtras() != null){

            //Varrendo o valor do GetExtras pra concatenar o texto dentro da string.
            for(String valor : getIntent().getExtras().keySet()){
                String v = getIntent().getExtras().getString(valor);
                textCloudMessaging.setText(v);
            }
        }

        buttonErro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CloudMessaging.this, MyFirebaseMessagingService.class);
                startActivity(intent);

                Crashlytics.getInstance().crash();


            }
        });


    }
}
