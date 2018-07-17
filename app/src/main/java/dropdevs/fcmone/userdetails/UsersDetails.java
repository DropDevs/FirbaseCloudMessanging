package dropdevs.fcmone.userdetails;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class UsersDetails {

  private String personName;
  private String personEmail;
  private String personId;

  public Uri getPersonImage() {
    return personImage;
  }

  public void setPersonImage(Uri personImage) {
    this.personImage = personImage;
  }

  private Uri personImage;

  public String getPersonName() {
    return personName;
  }

  public void setPersonName(String personName) {
    this.personName = personName;
  }

  public String getPersonEmail() {
    return personEmail;
  }

  public void setPersonEmail(String personEmail) {
    this.personEmail = personEmail;
  }

  public String getPersonId() {
    return personId;
  }

  public void setPersonId(String personId) {
    this.personId = personId;
  }

  public UsersDetails(Context context) {
    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);
    personName = acct.getDisplayName();
    personEmail = acct.getEmail();
    personId = acct.getId();
    personImage = acct.getPhotoUrl();
  }

}
