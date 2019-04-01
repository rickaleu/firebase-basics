package br.com.ricardo.firebasebasics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CloudMessaging extends AppCompatActivity {

    private TextView textCloudMessaging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_messaging);

        textCloudMessaging = (TextView) findViewById(R.id.textNotification);


        if(getIntent().getExtras() != null){

            //Varrendo o valor do GetExtras pra concatenar o texto dentro da string.
            for(String valor : getIntent().getExtras().keySet()){
                String v = getIntent().getExtras().getString(valor);
                textCloudMessaging.setText(v);
            }
        }


    }
}
