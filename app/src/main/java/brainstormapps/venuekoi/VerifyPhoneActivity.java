package brainstormapps.venuekoi;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import brainstormapps.venuekoi.Common.Common;
import brainstormapps.venuekoi.Model.UserModel;
import dmax.dialog.SpotsDialog;

public class VerifyPhoneActivity extends AppCompatActivity {

    private String verificationId;
    public String phoneNumber;
    private FirebaseAuth mAuth;
    private AlertDialog alertDialog;

    TextInputEditText editText;
    AppCompatButton buttonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        mAuth = FirebaseAuth.getInstance();

        editText = findViewById(R.id.editTextCode);
        buttonSignIn = findViewById(R.id.buttonSignIn);

        phoneNumber = getIntent().getStringExtra("userPhoneNumber");
        Log.i("phoneNo6", phoneNumber);

        alertDialog = new SpotsDialog.Builder().setCancelable(false).setContext(this).build();
        alertDialog.show();

        sendVerificationCode(phoneNumber);

        buttonSignIn.setOnClickListener(v -> {

            String code = editText.getText().toString().trim();
            if (code.isEmpty() || code.length() < 6) {
                editText.setError("Enter valid code.");
                editText.requestFocus();
                return;
            }
            alertDialog.show();
            //verifying the code entered manually
            verifyVerificationCode(code);
        });

    }

    private void sendVerificationCode(String number) {
        Log.d("phoneNofindCode0", "" + number);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks
        );
    }

    // the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            Log.d("phoneNofindCode00", "onVerificationCompleted:" + phoneAuthCredential);
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.e("phoneNofindCode1", "onVerificationFailed:", e);
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            //storing the verification id that is sent to the user
            verificationId = s;
            alertDialog.dismiss();
            Log.d("phoneNofindCode2", "onCodeSent:" + verificationId);
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        Log.d("phoneNofindCode3", "onVerifyVerificationCode:" + code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Log.d("phoneNofindCode4", "signInWithPhoneAuthCredential:" + credential);
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(VerifyPhoneActivity.this, task -> {
                if (task.isSuccessful() && FirebaseAuth.getInstance().getCurrentUser() != null) {
                    Log.d("phoneNofindCode5", "task.isSuccessful:" + task);

                    String currentUserPhone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                    String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Log.d("phoneNo7", currentUserPhone);
                    Log.d("phoneNo7id", currentUid);

                    DatabaseReference postReference = FirebaseDatabase.getInstance().getReference().child("UserList");

                    postReference.child(currentUid)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Log.d("phoneNo8", currentUserPhone);
                                        Log.d("phoneNo8id", currentUid);
                                        alertDialog.dismiss();
                                        Common.currentUserModel = dataSnapshot.getValue(UserModel.class);
                                        Intent intent = new Intent(VerifyPhoneActivity.this, VenueCategoryActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Log.d("phoneNo9", currentUserPhone);
                                        Log.d("phoneNo9id", currentUid);
                                        alertDialog.dismiss();
                                        Common.currentUserModel = dataSnapshot.getValue(UserModel.class);
                                        Intent intent = new Intent(VerifyPhoneActivity.this, SetUserDataActivity.class);
                                        intent.putExtra("userPhoneNumber", currentUserPhone);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                    //verification successful we will start the SetUserDataActivity
                    /*Intent intent = new Intent(VerifyPhoneActivity.this, SetUserDataActivity.class);
                    intent.putExtra("userPhoneNumber", phoneNumber);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);*/

                } else {

                    //verification unsuccessful.. display an error message

                    String message = "Something is wrong, we will fix it soon...";

                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        message = "Invalid code entered...";
                    }

                    Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                    snackbar.setAction("Dismiss", v -> {

                    });
                    snackbar.show();
                }
            });
    }

}
