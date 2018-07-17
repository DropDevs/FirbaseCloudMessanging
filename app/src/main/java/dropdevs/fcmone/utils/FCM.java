package dropdevs.fcmone.utils;

import com.squareup.okhttp.ResponseBody;

import java.util.Map;

import dropdevs.fcmone.fcm.FirebaseCloudMessage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface FCM {

    @POST("fcm/send")
    Call<ResponseBody> send(
            @HeaderMap Map<String, String> headers,
            @Body FirebaseCloudMessage message
    );
}
