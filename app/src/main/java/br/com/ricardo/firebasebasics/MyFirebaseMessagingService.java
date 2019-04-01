package br.com.ricardo.firebasebasics;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        mostrarNotificacao(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

    }

    private void mostrarNotificacao(String title, String body) {

        //Fazendo um PendingIntent pra mandar o conteúdo da notificação pra classe principal.
        Intent intent = new Intent(this, CloudMessaging.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Montando a notificação.
        NotificationCompat.Builder not = new NotificationCompat.Builder(this)
                .setSmallIcon(R.id.icon_only)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pi);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, not.build());

    }
}
