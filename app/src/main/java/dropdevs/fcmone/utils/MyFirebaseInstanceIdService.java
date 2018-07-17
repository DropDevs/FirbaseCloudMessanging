package dropdevs.fcmone.utils;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService{

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String newUserToken = "";
        try{
            newUserToken = s;
        }catch (Exception e){
            Log.d("NewToken","onNewTokenException: "+e.toString());
        }

        Log.d("NewToken","User_Token: "+newUserToken);

    }
}
