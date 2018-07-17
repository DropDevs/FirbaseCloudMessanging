package dropdevs.fcmone.fcm;

import android.util.Log;

import java.util.Arrays;

public class FirebaseCloudMessage {


    private Data data;

    public FirebaseCloudMessage(Data data, String to) {
        this.data = data;
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "FirebaseCloudMessage{" +
                "data=" + data +
                ", to='" + to + '\'' +
                '}';
    }

    private String to;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public FirebaseCloudMessage(){

    }


}
