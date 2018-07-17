package dropdevs.fcmone;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.squareup.okhttp.ResponseBody;

import org.apache.http.conn.ssl.X509HostnameVerifier;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import dropdevs.fcmone.fcm.Data;
import dropdevs.fcmone.fcm.FirebaseCloudMessage;
import dropdevs.fcmone.gettersetters.FirebaseServerKey;
import dropdevs.fcmone.userdetails.UserTokens;
import dropdevs.fcmone.utils.FCM;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://fcm.googleapis.com/";
    private EditText titleEditText, messageEditText;
    private Button sendButton;
    FirebaseServerKey serverKey = new FirebaseServerKey();
    FirebaseCloudMessage firebaseCloudMessage = new FirebaseCloudMessage();
    UserTokens userTokens = new UserTokens();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        deleteInstanceId();

        FirebaseMessaging.getInstance().subscribeToTopic("FCM");
        titleEditText = findViewById(R.id.enter_title_edit_text);
        messageEditText = findViewById(R.id.enter_message_edit_text);
        sendButton = findViewById(R.id.send_notification_button);

        final DatabaseReference serverKeyRef = FirebaseDatabase.getInstance().getReference()
                .child("server")
                .getRef();
        serverKeyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String server_key = (String) dataSnapshot.child("server_key").getValue();
                serverKey.setServer_key(server_key);
                Log.d("server_key_fcm","server_key: "+server_key);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notificationTitle = titleEditText.getText().toString().trim();
                String notificationMessage = messageEditText.getText().toString().trim();
                if (TextUtils.isEmpty(notificationTitle) && TextUtils.isEmpty(notificationMessage)){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Error")
                            .setMessage("Enter all fields to send!")
                            .setNeutralButton("Ok",null)
                            .show();
                }else {
                    sendNotification(notificationTitle,notificationMessage);
                }
            }
        });



    }

//    public void deleteInstanceId(){
//        try {
//            FirebaseInstanceId.getInstance().deleteInstanceId();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void sendNotification(String title, String message){
        Log.d("info","In_send_notification_method");
        Log.d("info","title: "+title);
        Log.d("info","message: "+message);
        String server_key = serverKey.getServer_key();
        Log.d("get_server_key",server_key);

        //---------------------From github ---------------
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
        httpClient.addInterceptor(logging);
        //------------------------------------------------------------

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FCM fcmApi = retrofit.create(FCM.class);
        //attach the headers
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content_Type","application/json;charset=UTF-8");
        headers.put("Authorization","key="+server_key);

        //send the message to all tokens
        sendTheNotificationToAllTokens(fcmApi, headers, title, message);

    }


    public void sendTheNotificationToAllTokens(FCM fcmApi, HashMap<String, String> headers,
                                               String title, String message){
        Log.d("info","In_send_notification_to_all_tokens_method");

        final Data data = new Data();
        data.setMessage(message);
        data.setTitle(title);
//        firebaseCloudMessage.setData(data);
//        ArrayList<String> tokenList = userTokens.getArrayList();
//        for (String aTokenList : tokenList) {
//         firebaseCloudMessage.setTo(aTokenList);
//         Log.d("token_list","tokens: "+aTokenList.toString());
//        }

        firebaseCloudMessage.setData(data);
        firebaseCloudMessage.setTo("/topics/FCM");
//        final DatabaseReference allTokenRef = FirebaseDatabase.getInstance().getReference()
//                .child("user_info")
//                .getRef();
//        allTokenRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
//
//                    try {
//                        String usersTokens = (String) snapshot.child("user_token_ref").getValue();
////                        userTokens.saveTokens(usersTokens);
//                        firebaseCloudMessage.setRegistration_ids(usersTokens);
//                        Log.d("token_snapshot","token: "+usersTokens);
//                    }catch (Exception e){
//                        Log.d("get_all_tokens","Exception: "+e.toString());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });




        Call<ResponseBody> call = fcmApi.send(headers, firebaseCloudMessage);
        call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Notification send successful!", Toast.LENGTH_SHORT).show();
                        Log.d("notification_response","successful!");
                        try {
                            ResponseBody responseBody = response.body();
                            Log.d("response_body", "body: " + responseBody);
                        }catch (Exception e){
                            Log.d("response_body","Exception: "+e.toString());
                        }
                    }else if (!response.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Notification failed!", Toast.LENGTH_SHORT).show();
//                        try {
//                            ResponseBody responseBody = response.body();
//                            Log.d("response_body", "body: " + responseBody);
//                        }catch (Exception e){
//                            Log.d("response_body","Exception: "+e.toString());
//                        }
//                        Log.d("notification_response", "error: " + response.message());
//                        Log.d("notification_response", "error: " + response.body());
//                        Log.d("notification_response", "error: " + response.errorBody());
//
//                        Log.d("notification_response","Failed!");
//                        Log.d("notification_response","Error: "+response.errorBody());
//                        Log.d("notification_response","Error: "+response.body());
//                        Log.d("notification_response","Error: "+response.message());

                        // error case
                        switch (response.code()) {
                            case 404:
                                Toast.makeText(MainActivity.this, "not found", Toast.LENGTH_SHORT).show();
                                break;
                            case 500:
                                Toast.makeText(MainActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(MainActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Exception", Toast.LENGTH_SHORT).show();
                Log.d("notification_response","onFailure: "+t.getMessage());
            }
        });

    }




}
