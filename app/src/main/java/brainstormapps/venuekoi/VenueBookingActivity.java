package brainstormapps.venuekoi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import brainstormapps.venuekoi.Model.VenueRequest;

public class VenueBookingActivity extends AppCompatActivity {

    TextView bookingVenueId, bookingVenueName, bookingVenuePrice;
    TextView bookingUserId, bookingUserName, bookingUserPhone, bookingDate;
    String currentVenueId, currentUserId, bookingId, currentBookingDate;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userDbReference, venueDbReference, bookingRequestReference;
    String user_name, user_phone, venue_name, venue_price;
    String set_user_name, set_user_phone, set_venue_name, set_venue_price;
    Button bookingConfirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_booking);

        bookingVenueId = findViewById(R.id.booking_venueID);
        bookingVenueName = findViewById(R.id.booking_venueName);
        bookingVenuePrice = findViewById(R.id.booking_venuePrice);
        bookingUserId = findViewById(R.id.booking_userID);
        bookingUserName = findViewById(R.id.booking_userName);
        bookingUserPhone = findViewById(R.id.booking_userPhone);
        bookingDate = findViewById(R.id.booking_date);
        bookingConfirmBtn = findViewById(R.id.booking_confirmBtn);

        currentVenueId = getIntent().getStringExtra("bookingVenueId");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentBookingDate = getIntent().getStringExtra("selectedBookingDate");

        bookingConfirmBtn.setOnClickListener(view -> {
            // set_user_name!=null || set_user_phone!=null || set_venue_name!=null || set_venue_price!=null || set_booking_date!=null
            Log.d("phoneNo22date", currentBookingDate);
            setBookingRequest(set_user_name, set_user_phone, set_venue_name, set_venue_price, currentBookingDate);

            AlertDialog.Builder builder = new AlertDialog.Builder(VenueBookingActivity.this);
            builder.setTitle("Your Booking Status")
                    .setMessage("Your booking is done. We'll contact with you within 5 minutes.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        Intent intent = new Intent(VenueBookingActivity.this, VenueListActivity.class);
                        startActivity(intent);
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        userDbReference = firebaseDatabase.getReference().child("UserList");
        venueDbReference = firebaseDatabase.getReference().child("VenueList");
        bookingRequestReference = firebaseDatabase.getReference().child("BookingRequest");

        bookingVenueId.setText(currentVenueId);
        bookingUserId.setText(currentUserId);
        bookingDate.setText(currentBookingDate);

        userDbReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_name = dataSnapshot.child("name").getValue().toString();
                user_phone = dataSnapshot.child("phone").getValue().toString();
                bookingUserName.setText(user_name);
                bookingUserPhone.setText(user_phone);
                sendUserData(user_name, user_phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        venueDbReference.child(currentVenueId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                venue_name = dataSnapshot.child("Name").getValue().toString();
                venue_price = dataSnapshot.child("Price").getValue().toString();
                bookingVenueName.setText(venue_name);
                bookingVenuePrice.setText(venue_price);
                sendVenueData(venue_name, venue_price);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setBookingRequest(String set_user_name, String set_user_phone, String set_venue_name,
                                   String set_venue_price, String set_booking_date) {
        if (TextUtils.isEmpty(bookingId)) {
            Calendar calendarBookingId = Calendar.getInstance();
            long bookingTime = calendarBookingId.getTimeInMillis();
            bookingId = String.valueOf(bookingTime);
        }
        VenueRequest venueRequest = new VenueRequest(set_user_name, set_user_phone, set_venue_name, set_venue_price, set_booking_date);
        bookingRequestReference.child(bookingId).setValue(venueRequest);
    }

    private void sendVenueData(String venue_name, String venue_price) {
        set_venue_name = venue_name;
        set_venue_price = venue_price;
        Log.d("phoneNo16v", set_venue_name);
        Log.d("phoneNo17v", set_venue_price);
    }

    private void sendUserData(String user_name, String user_phone) {
        set_user_name = user_name;
        set_user_phone = user_phone;
        Log.d("phoneNo16u", set_user_name);
        Log.d("phoneNo17u", set_user_phone);
    }

}
