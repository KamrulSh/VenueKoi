package brainstormapps.venuekoi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import brainstormapps.venuekoi.Model.User;

public class SetUserDataActivity extends AppCompatActivity implements View.OnClickListener {

    Button userSaveBtn;
    EditText userName, userAddress;
    String userPhone, userId;
    FirebaseDatabase userFirebaseDatabase;
    DatabaseReference userDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_data);

        userName = findViewById(R.id.user_name);
        userAddress = findViewById(R.id.user_address);
        userSaveBtn = findViewById(R.id.user_save_btn);

        userSaveBtn.setOnClickListener(this);

        userPhone = getIntent().getStringExtra("userPhoneNumber");
        Log.i("phoneNo10", userPhone);

        userFirebaseDatabase = FirebaseDatabase.getInstance();
        userDatabaseReference = userFirebaseDatabase.getReference("UserList");

    }

    @Override
    public void onClick(View view) {

        String name = userName.getText().toString();
        String phone = userPhone;
        String address = userAddress.getText().toString();

        if (name.isEmpty()) {
            userName.setError("Please fill this field.");
            return;
        } else if (address.isEmpty()) {
            userAddress.setError("Please fill this field.");
            return;
        } else {
            // check for already existed user
            if (TextUtils.isEmpty(userId)) {
                createUserData(phone, name, address);
            }
        }

        Intent intent = new Intent(SetUserDataActivity.this, VenueListActivity.class);
        startActivity(intent);

    }

    private void createUserData(String phone, String name, String address) {

        if (TextUtils.isEmpty(userId)) {
            userId = userDatabaseReference.push().getKey();
        }

        User user = new User(name, phone, address);
        userDatabaseReference.child(userId).setValue(user);
    }

}
