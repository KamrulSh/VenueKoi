package brainstormapps.venuekoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import brainstormapps.venuekoi.Model.Venue;

public class VenueDetailsActivity extends AppCompatActivity {

    TextView venueNameX, venuePriceX, venueDetailsX;
    ImageView venueImageX;
    CollapsingToolbarLayout collapsingToolbarX;
    String venueIdX = "";
    FirebaseDatabase databaseX;
    DatabaseReference dbReferenceX;

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
        collapsingToolbarX = findViewById(R.id.collapsingToolbarX);

        collapsingToolbarX.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarX.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        Log.d("phoneNo13vid", "" + getIntent());

        if (getIntent() != null)
            venueIdX = getIntent().getStringExtra("VenueId");

        Log.d("phoneNo14vid", venueIdX);


        if (!venueIdX.isEmpty()) {
            getDetailsVenue(venueIdX);
        }

    }

    private void getDetailsVenue(String venueIdX) {
        dbReferenceX.child(venueIdX).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Venue venue = dataSnapshot.getValue(Venue.class);

                collapsingToolbarX.setTitle("VenueKoi");

                //Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), R.font.texgyre);
                Typeface typeface = ResourcesCompat.getFont(VenueDetailsActivity.this, R.font.texgyre);
                collapsingToolbarX.setCollapsedTitleTypeface(typeface);
                // set image
                Picasso.get().load(venue.getImage()).into(venueImageX);
                venueNameX.setText(venue.getName());
                venuePriceX.setText(venue.getPrice());
                venueDetailsX.setText(venue.getDetail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
