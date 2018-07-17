package dropdevs.fcmone.gettersetters;

import android.util.Log;

public class FirebaseServerKey {

    public String getServer_key() {
        Log.d("server_key_fcm","In get server key");
        return server_key;
    }

    public void setServer_key(String server_key) {
        this.server_key = server_key;
        Log.d("server_key_fcm","In set server key");
        Log.d("server_key_fcm","server_key_set: "+server_key);
    }

    private String server_key;


}
