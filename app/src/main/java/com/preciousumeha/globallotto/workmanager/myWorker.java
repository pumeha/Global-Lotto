package com.preciousumeha.globallotto.workmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.preciousumeha.globallotto.Constants;
import com.preciousumeha.globallotto.R;

import java.util.HashMap;
import java.util.Map;

import static com.preciousumeha.globallotto.Constants.TAG;

public class myWorker extends Worker {

    public myWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        //receiving data
       String value = getInputData().getString(TAG);
        goOnline();

        Data data = new Data.Builder().putString(TAG,"The work is over").build();

        return Result.success(data);
    }


    private void displayNotification(String title,String task){
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =new NotificationChannel("globallotto","globallotto",NotificationManager.IMPORTANCE_DEFAULT);
            //channel.setSound(Uri.parse(Settings.System.DEFAULT_NOTIFICATION_URI,));
            notificationManager.createNotificationChannel(channel);

        }
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(),"globallotto")
                .setContentTitle(title)
                .setContentText(task)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.drawable.logo_good);
        notificationManager.notify(1,notification.build());
    }
    private void goOnline(){
        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_signin, response -> {
            String value = getInputData().getString(TAG);
            displayNotification(String.valueOf(response.length()), response.substring(1,7));
            Log.i(TAG, response);
            if (response.contains("No User Found")){
               // WorkManager.getInstance().cancelWorkById(workRequest.getId());
            }


        }, error -> displayNotification(error.getMessage(),error.getMessage())){

            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put(Constants.ud_user,"08037200210");
                params.put(Constants.ud_password,"sopu");
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }
}
