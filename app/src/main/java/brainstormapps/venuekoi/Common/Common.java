package brainstormapps.venuekoi.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import brainstormapps.venuekoi.Model.UserModel;
import brainstormapps.venuekoi.Model.VenueModel;

public class Common {
    // here currentUser data will be stored
    public static UserModel currentUserModel;
    // here currentVenue data will be stored
    public static VenueModel currentVenueModel;

    // check internet connectivity
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo networkInfo : info) {
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }

        return false;
    }
}
