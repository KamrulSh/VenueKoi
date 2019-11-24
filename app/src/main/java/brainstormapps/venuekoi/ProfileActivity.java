package brainstormapps.venuekoi;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import brainstormapps.venuekoi.Common.Common;

public class ProfileActivity extends AppCompatActivity {

    TextView nameTextView, phoneTextView, addressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameTextView = findViewById(R.id.firebase_user_name);
        phoneTextView = findViewById(R.id.firebase_user_phone);
        addressTextView = findViewById(R.id.firebase_user_address);

        nameTextView.setText(Common.currentUserModel.getName());
        phoneTextView.setText(Common.currentUserModel.getPhone());
        addressTextView.setText(Common.currentUserModel.getAddress());

        Log.d("phoneNo11Name", Common.currentUserModel.getName());
    }
}
