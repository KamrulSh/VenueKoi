package brainstormapps.venuekoi;

import android.os.Bundle;
import android.util.Log;
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
    /*FirebaseAuth uFirebaseAuth;
    FirebaseUser uFirebaseUser;
    FirebaseDatabase uFirebaseDatabase;
    DatabaseReference uDatabaseReference;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /*uFirebaseAuth = FirebaseAuth.getInstance();
        uFirebaseUser = uFirebaseAuth.getCurrentUser();
        uFirebaseDatabase = FirebaseDatabase.getInstance();
        uDatabaseReference = uFirebaseDatabase.getReference("UserList").child(uFirebaseUser.getUid());*/

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
                Log.d("phoneNo12id", u_phone);
                Toast.makeText(ProfileActivity.this, "name: "+ u_name + "\nphone: "+ u_phone
                        + "\naddress: " + u_address, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
