package brainstormapps.venuekoi;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

import brainstormapps.venuekoi.Common.Common;
import brainstormapps.venuekoi.Model.UserModel;
import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText editTextCountryCode, editTextPhone;
    public AppCompatButton buttonContinue;
    private String phoneNumber;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        editTextCountryCode = findViewById(R.id.editTextCountryCode);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonContinue = findViewById(R.id.buttonContinue);

        alertDialog = new SpotsDialog.Builder().setCancelable(false).setContext(this).build();

        buttonContinue.setOnClickListener(v -> {
            String code = editTextCountryCode.getText().toString().trim();
            String number = editTextPhone.getText().toString().trim();

            if (number.isEmpty() || number.length() < 10) {
                editTextPhone.setError("Valid number is required");
                editTextPhone.requestFocus();
                return;
            }

            phoneNumber = code + number;

            Log.i("phoneNo1", phoneNumber);

            alertDialog.show();
            Intent intent = new Intent(MainActivity.this, VerifyPhoneActivity.class);
            intent.putExtra("userPhoneNumber", phoneNumber);
            startActivity(intent);

        });

    }

    // this method will run if user have not pressed log out button or user has been in login state
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            String currentUserPhone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
            String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Log.d("phoneNo2", currentUserPhone);
            Log.d("phoneNo2id", currentUid);

            DatabaseReference postReference = FirebaseDatabase.getInstance().getReference().child("UserList");

            //alertDialog = new SpotsDialog.Builder().setCancelable(false).setContext(this).build();

            /*final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait...");*/
            alertDialog.show();

            //postReference.orderByChild("phone").equalTo(currentUserPhone)
            postReference.child(currentUid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Log.d("phoneNo3", currentUserPhone);
                                Log.d("phoneNo3id", currentUid);
                                alertDialog.dismiss();
                                Common.currentUserModel = dataSnapshot.getValue(UserModel.class);
                                Intent intent = new Intent(MainActivity.this, VenueCategoryActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Log.d("phoneNo4", currentUserPhone);
                                Log.d("phoneNo4id", currentUid);
                                alertDialog.dismiss();
                                Common.currentUserModel = dataSnapshot.getValue(UserModel.class);
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
