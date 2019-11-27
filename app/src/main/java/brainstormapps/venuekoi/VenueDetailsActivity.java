package brainstormapps.venuekoi;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

import brainstormapps.venuekoi.Common.Common;
import brainstormapps.venuekoi.Model.RatingModel;
import brainstormapps.venuekoi.Model.VenueModel;

public class VenueDetailsActivity extends AppCompatActivity implements RatingDialogListener {

    TextView venueNameX, venuePriceX, venueDetailsX, venueLocationX;
    ImageView venueImageX;
    Button venueAvailableBtnX;
    CollapsingToolbarLayout collapsingToolbarX;
    String venueIdX = "";
    FirebaseDatabase databaseX;
    DatabaseReference dbReferenceX, ratingReferenceX;
    String categoryNameX = "";
    FloatingActionButton btnRatingX;
    RatingBar ratingBarX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_details);
        getSupportActionBar().hide();

        databaseX = FirebaseDatabase.getInstance();
        dbReferenceX = databaseX.getReference("VenueList");
        ratingReferenceX = databaseX.getReference("RatingList");

        Log.d("phoneNo12vid", "" + dbReferenceX);

        btnRatingX = findViewById(R.id.rating_btnX);
        ratingBarX = findViewById(R.id.rating_barX);
        venueNameX = findViewById(R.id.venue_nameX);
        venueImageX = findViewById(R.id.venue_imageX);
        venuePriceX = findViewById(R.id.venue_priceX);
        venueDetailsX = findViewById(R.id.venue_detailsX);
        venueLocationX = findViewById(R.id.venue_locationX);
        venueAvailableBtnX = findViewById(R.id.venue_availableBtnX);
        collapsingToolbarX = findViewById(R.id.collapsingToolbarX);

        collapsingToolbarX.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarX.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        Log.d("phoneNo13vid", "" + getIntent());

        // get Intent data from VenueListActivity
        if (getIntent() != null) {
            venueIdX = getIntent().getStringExtra("VenueId");
            categoryNameX = getIntent().getStringExtra("CategoryName");
        }

        Log.d("phoneNo14vid", venueIdX);

        if (!venueIdX.isEmpty()) {
            getDetailsVenue(venueIdX);
            getRatingVenue(venueIdX);
        }

        // when venue Available Button is clicked clicked it will go to CalendarBookingActivity
        venueAvailableBtnX.setOnClickListener(view -> {
            if (Common.isConnectedToInternet(getBaseContext())) {
                Intent bookingIntent = new Intent(VenueDetailsActivity.this, CalendarBookingActivity.class);
                bookingIntent.putExtra("bookingVenueId", venueIdX);
                bookingIntent.putExtra("CategoryName", categoryNameX);
                startActivity(bookingIntent);
            } else
                Toast.makeText(VenueDetailsActivity.this, "Please check internet connection.", Toast.LENGTH_LONG).show();
        });

        // when rating btn clicked it will show rating dialog
        btnRatingX.setOnClickListener(view -> showRatingDialog());

    }

    private void getRatingVenue(String venueIdX) {
        Query venueRating = ratingReferenceX.orderByChild("venueId").equalTo(venueIdX);
        venueRating.addValueEventListener(new ValueEventListener() {
            int count = 0, sum = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()) {
                    RatingModel item = postSnapshot.getValue(RatingModel.class);
                    sum += Integer.parseInt(item.getRateValue());
                    count++;
                }
                if (count != 0) {
                    float average = sum / count;
                    ratingBarX.setRating(average);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(VenueDetailsActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Quite Ok", "Very Good", "Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this venue")
                .setDescription("Please select some stars and write your feedback")
                .setTitleTextColor(R.color.darkBlueColor)
                .setDescriptionTextColor(R.color.darkBlueColor)
                .setHint("Write your feedback")
                .setHintTextColor(R.color.whiteText)
                .setCommentTextColor(R.color.whiteText)
                .setCommentBackgroundColor(R.color.mdtp_transparent_black)
                .setWindowAnimation(R.style.MyRatingDialogFadeAnimation)
                .create(VenueDetailsActivity.this)
                .show();
    }

    // it will show the venue details of clicked item using that items venueId
    private void getDetailsVenue(String venueIdX) {
        dbReferenceX.child(venueIdX).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                VenueModel venueModel = dataSnapshot.getValue(VenueModel.class);
                Common.currentVenueModel = dataSnapshot.getValue(VenueModel.class);
                collapsingToolbarX.setTitle("VenueKoi");

                //Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), R.font.texgyre);
                Typeface typeface = ResourcesCompat.getFont(VenueDetailsActivity.this, R.font.texgyre);
                collapsingToolbarX.setCollapsedTitleTypeface(typeface);
                // set image
                Picasso.get().load(venueModel.getImage()).into(venueImageX);
                venueNameX.setText(venueModel.getName());
                venuePriceX.setText(venueModel.getPrice());
                venueDetailsX.setText(venueModel.getDetail());
                venueLocationX.setText(venueModel.getLocation());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(VenueDetailsActivity.this,
                        ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, @NonNull String comment) {
        // get rating and upload to firebase
        RatingModel ratingModel = new RatingModel(Common.currentUserModel.getPhone(),
                venueIdX, String.valueOf(value), comment);

        ratingReferenceX.child(Common.currentUserModel.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Common.currentUserModel.getPhone()).exists()) {
                    // remove old rating value
                    //ratingReferenceX.child(Common.currentUserModel.getPhone()).removeValue();
                    // update new rating value
                    ratingReferenceX.child(Common.currentUserModel.getPhone()).setValue(ratingModel);
                } else
                    ratingReferenceX.child(Common.currentUserModel.getPhone()).setValue(ratingModel);

                Toast.makeText(VenueDetailsActivity.this, "Thank you for submit rating.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(VenueDetailsActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
