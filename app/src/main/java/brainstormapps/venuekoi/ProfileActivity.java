package brainstormapps.venuekoi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;

public class ProfileActivity extends AppCompatActivity {

    Button btnSignOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // btn click for logout
        btnSignOut = findViewById(R.id.btn_sign_out);
        btnSignOut.setOnClickListener(view -> AuthUI.getInstance()
                .signOut(ProfileActivity.this)
                .addOnCompleteListener(task -> btnSignOut.setEnabled(false))
                .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show()));
    }
}
