package brainstormapps.venuekoi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import brainstormapps.venuekoi.Common.Common;
import brainstormapps.venuekoi.Model.VenueRequest;

public class VenueBookingActivity extends AppCompatActivity {

    TextView bookingCatgName;
    TextView bookingVenueId, bookingVenueName, bookingVenuePrice;
    TextView bookingUserId, bookingUserName, bookingUserPhone, bookingDate;
    String currentVenueId, currentUserId, bookingId, currentBookingDate;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userDbReference, venueDbReference, bookingRequestReference;
    String set_user_name, set_user_phone, set_venue_name, set_venue_price;
    Button bookingConfirmBtn;
    String bCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_booking);

        bookingCatgName = findViewById(R.id.booking_categoryName);
        bookingVenueId = findViewById(R.id.booking_venueID);
        bookingVenueName = findViewById(R.id.booking_venueName);
        bookingVenuePrice = findViewById(R.id.booking_venuePrice);
        bookingUserId = findViewById(R.id.booking_userID);
        bookingUserName = findViewById(R.id.booking_userName);
        bookingUserPhone = findViewById(R.id.booking_userPhone);
        bookingDate = findViewById(R.id.booking_date);
        bookingConfirmBtn = findViewById(R.id.booking_confirmBtn);

        bCategoryName = getIntent().getStringExtra("CategoryName");
        currentVenueId = getIntent().getStringExtra("bookingVenueId");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentBookingDate = getIntent().getStringExtra("selectedBookingDate");

        firebaseDatabase = FirebaseDatabase.getInstance();
        userDbReference = firebaseDatabase.getReference().child("UserList");
        venueDbReference = firebaseDatabase.getReference().child("VenueList");
        bookingRequestReference = firebaseDatabase.getReference().child("BookingRequest");

        bookingVenueId.setText(currentVenueId);
        bookingUserId.setText(currentUserId);
        bookingDate.setText(currentBookingDate);
        bookingCatgName.setText(bCategoryName);
        bookingVenueName.setText(Common.currentVenueModel.getName());
        bookingVenuePrice.setText(Common.currentVenueModel.getPrice());
        bookingUserName.setText(Common.currentUserModel.getName());
        bookingUserPhone.setText(Common.currentUserModel.getPhone());

        set_venue_name = Common.currentVenueModel.getName();
        set_venue_price = Common.currentVenueModel.getPrice();
        set_user_name = Common.currentUserModel.getName();
        set_user_phone = Common.currentUserModel.getPhone();

        // if booking Confirm Button is clicked it will show a dialog and then go to the VenueCategoryActivity
        bookingConfirmBtn.setOnClickListener(view -> {

            Log.d("phoneNoCC1", set_user_name);
            Log.d("phoneNoCC2", set_user_phone);
            Log.d("phoneNoCC3", set_venue_name);
            Log.d("phoneNoCC4", set_venue_price);
            Log.d("phoneNoCC5", currentBookingDate);
            Log.d("phoneNoCC6", bCategoryName);

            setBookingRequest(set_user_name, set_user_phone, set_venue_name, set_venue_price,
                    currentBookingDate, bCategoryName);

            AlertDialog.Builder builder = new AlertDialog.Builder(VenueBookingActivity.this);
            builder.setTitle("Your Booking Status")
                    .setMessage("Your booking request is done. We'll contact with you within 5 minutes for further process.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        Intent intent = new Intent(VenueBookingActivity.this, VenueCategoryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

    }

    // it will store booking request information in firebase database
    private void setBookingRequest(String set_user_name, String set_user_phone, String set_venue_name,
                                   String set_venue_price, String set_booking_date, String bCategoryName) {
        if (TextUtils.isEmpty(bookingId)) {
            Calendar calendarBookingId = Calendar.getInstance();
            long bookingTime = calendarBookingId.getTimeInMillis();
            bookingId = String.valueOf(bookingTime);
        }
        VenueRequest venueRequest = new VenueRequest(bookingId, set_user_name, set_user_phone, set_venue_name,
                set_venue_price, set_booking_date, bCategoryName);
        bookingRequestReference.child(bookingId).setValue(venueRequest);
    }

}
