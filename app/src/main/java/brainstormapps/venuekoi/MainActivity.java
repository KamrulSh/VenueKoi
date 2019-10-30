package brainstormapps.venuekoi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    /*private static final int MY_REQUEST_CODE = 7772;
    List<AuthUI.IdpConfig> providers;
    Button btnVenue;*/

    TextInputEditText editTextCountryCode, editTextPhone;
    AppCompatButton buttonContinue;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        editTextCountryCode = findViewById(R.id.editTextCountryCode);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonContinue = findViewById(R.id.buttonContinue);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCountryCode.getText().toString().trim();
                String number = editTextPhone.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    editTextPhone.setError("Valid number is required");
                    editTextPhone.requestFocus();
                    return;
                }

                phoneNumber = code + number;

                Log.i("phoneNo1", phoneNumber);

                Intent intent = new Intent(MainActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("userPhoneNumber", phoneNumber);
                startActivity(intent);

            }
        });

    }

    // this method will run if user have not pressed log out button or user has been in login state
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            String currentUserPhone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
            Log.d("phoneNo2", currentUserPhone);
            DatabaseReference postReference = FirebaseDatabase.getInstance().getReference().child("UserList");

            final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            postReference.orderByChild("phone").equalTo(currentUserPhone)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Log.d("phoneNo3", currentUserPhone);
                                progressDialog.dismiss();
                                Intent intent = new Intent(MainActivity.this, VenueListActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Log.d("phoneNo4", currentUserPhone);
                                progressDialog.dismiss();
                                Intent intent = new Intent(MainActivity.this, SetUserDataActivity.class);
                                intent.putExtra("userPhoneNumber", currentUserPhone);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d("phoneNo5", currentUserPhone);
                        }
                    });
        }

    }

}
