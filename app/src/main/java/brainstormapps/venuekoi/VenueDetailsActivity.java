package brainstormapps.venuekoi;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import brainstormapps.venuekoi.Model.VenueModel;

public class VenueDetailsActivity extends AppCompatActivity {

    TextView venueNameX, venuePriceX, venueDetailsX, venueLocationX;
    ImageView venueImageX;
    Button venueAvailableBtnX;
    CollapsingToolbarLayout collapsingToolbarX;
    String venueIdX = "";
    FirebaseDatabase databaseX;
    DatabaseReference dbReferenceX;
    String categoryNameX = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_details);
        getSupportActionBar().hide();

        databaseX = FirebaseDatabase.getInstance();
        dbReferenceX = databaseX.getReference("VenueList");

        Log.d("phoneNo12vid", "" + dbReferenceX);

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

        if (getIntent() != null) {
            venueIdX = getIntent().getStringExtra("VenueId");
            categoryNameX = getIntent().getStringExtra("CategoryName");
        }

        Log.d("phoneNo14vid", venueIdX);


        if (!venueIdX.isEmpty()) {
            getDetailsVenue(venueIdX);
        }

        venueAvailableBtnX.setOnClickListener(view -> {
            Intent bookingIntent = new Intent(VenueDetailsActivity.this, CalendarBookingActivity.class);
            bookingIntent.putExtra("bookingVenueId", venueIdX);
            bookingIntent.putExtra("CategoryName", categoryNameX);
            startActivity(bookingIntent);
        });

    }

    private void getDetailsVenue(String venueIdX) {
        dbReferenceX.child(venueIdX).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                VenueModel venueModel = dataSnapshot.getValue(VenueModel.class);

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

            }
        });
    }
}
