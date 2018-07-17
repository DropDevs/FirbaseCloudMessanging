package dropdevs.fcmone.fcm;

import android.util.Log;

public class Data {


    public Data(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String title;

    @Override
    public String toString() {
        return "Data{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    private String message;

    public Data(){

    }





}
