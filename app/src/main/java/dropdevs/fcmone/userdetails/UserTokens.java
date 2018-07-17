package dropdevs.fcmone.userdetails;

import java.util.ArrayList;
import java.util.List;

public class UserTokens {

    private String token;

    public ArrayList<String> getArrayList() {
        return arrayList;
    }

    private ArrayList<String> arrayList;

    public void saveTokens(String userTokens){
       arrayList = new ArrayList<String>();
        arrayList.add(userTokens);
    }


}
