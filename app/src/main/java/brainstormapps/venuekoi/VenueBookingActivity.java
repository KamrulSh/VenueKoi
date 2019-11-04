package brainstormapps.venuekoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VenueBookingActivity extends AppCompatActivity {

    TextView bookingVenueId, bookingVenueName, bookingVenuePrice;
    TextView bookingUserId, bookingUserName, bookingUserPhone;
    String currentVenueIdStr, currentUserIdStr;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userDbReference, venueDbReference;
    String user_name, user_phone, venue_name, venue_price;

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

        currentVenueIdStr = getIntent().getStringExtra("bookingVenueId");
        bookingVenueId.setText(currentVenueIdStr);
        currentUserIdStr = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bookingUserId.setText(currentUserIdStr);
        firebaseDatabase = FirebaseDatabase.getInstance();
        userDbReference = firebaseDatabase.getReference().child("UserList");
        venueDbReference = firebaseDatabase.getReference().child("VenueList");

        userDbReference.child(currentUserIdStr).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_name = dataSnapshot.child("name").getValue().toString();
                user_phone = dataSnapshot.child("phone").getValue().toString();
                bookingUserName.setText(user_name);
                bookingUserPhone.setText(user_phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        venueDbReference.child(currentVenueIdStr).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                venue_name = dataSnapshot.child("Name").getValue().toString();
                venue_price = dataSnapshot.child("Price").getValue().toString();
                bookingVenueName.setText(venue_name);
                bookingVenuePrice.setText(venue_price);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
