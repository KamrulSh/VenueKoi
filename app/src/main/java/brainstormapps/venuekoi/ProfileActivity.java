package brainstormapps.venuekoi;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    String u_name, u_phone, u_address;
    TextView nameTextView, phoneTextView, addressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameTextView = findViewById(R.id.firebase_user_name);
        phoneTextView = findViewById(R.id.firebase_user_phone);
        addressTextView = findViewById(R.id.firebase_user_address);

        String currentUserPhone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.d("phoneNo11", currentUserPhone);
        Log.d("phoneNo11id", currentUid);

        DatabaseReference uDatabaseReference = FirebaseDatabase.getInstance().getReference().child("UserList");

        uDatabaseReference.child(currentUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                u_name = dataSnapshot.child("name").getValue().toString();
                u_phone = dataSnapshot.child("phone").getValue().toString();
                u_address= dataSnapshot.child("address").getValue().toString();

                nameTextView.setText(u_name);
                phoneTextView.setText(u_phone);
                addressTextView.setText(u_address);
                Log.d("phoneNo12id", u_phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
