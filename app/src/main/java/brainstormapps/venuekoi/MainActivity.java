package brainstormapps.venuekoi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 7772;
    List<AuthUI.IdpConfig> providers;
    Button btnVenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVenue = findViewById(R.id.btn_veneu);
        btnVenue.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), VenueActivity.class);
            startActivity(intent);
        });

        // init providers
        providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(), // phone auth
                new AuthUI.IdpConfig.EmailBuilder().build(), // email auth
                new AuthUI.IdpConfig.GoogleBuilder().build() // gmail auth
        );

        showSignInOptions();
    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers).setIsSmartLockEnabled(false)
                .setTheme(R.style.MyTheme)
                .build(), MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // get user
                //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, "You have logged in.", Toast.LENGTH_SHORT).show();
                //btnSignOut.setEnabled(true);
            } else {
                //Objects.requireNonNull(response);
                //Objects.requireNonNull(response.getError());
                Toast.makeText(this, "Error! "+response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
