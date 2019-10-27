package brainstormapps.venuekoi;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import brainstormapps.venuekoi.Model.Venue;
import brainstormapps.venuekoi.ViewHolder.VenueViewHolder;

public class VenueListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference venueItemRef;
    RecyclerView recyclerVenue;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Venue, VenueViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // init firebase
        database = FirebaseDatabase.getInstance();
        venueItemRef = database.getReference("VenueList");
        //Log.d("ffimg", venueItemRef+" item passed");
        // load venue item
        recyclerVenue = findViewById(R.id.recycler_venueItem);
        //Log.d("fffimg", recyclerVenue+" item passed");
        recyclerVenue.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerVenue.setLayoutManager(layoutManager);
        //Log.d("fimg", "item passed");
        loadVenueItem();

    }

    private void loadVenueItem() {
        //Log.d("fimg", "item passed");
        adapter = new FirebaseRecyclerAdapter<Venue, VenueViewHolder>(
                Venue.class, R.layout.home_venue_item, VenueViewHolder.class, venueItemRef) {
            @Override
            protected void populateViewHolder(VenueViewHolder venueViewHolder, Venue venue, int i) {
                venueViewHolder.txtVenueName.setText(venue.getName());
                Picasso.get().load(venue.getImage()).into(venueViewHolder.imgVenue);
                //Log.d("fimg", venue.getName()+" item passed");

                /*venueViewHolder.setItemClickListener((view, position, isLongClick) -> Toast.makeText(
                        VenueListActivity.this, ""+ venue.getName(), Toast.LENGTH_SHORT).show());*/

                // send venue id to venue details activity for showing details
                venueViewHolder.setItemClickListener((view, position, isLongClick) -> {
                    Intent venueIntent = new Intent(VenueListActivity.this, VenueDetailsActivity.class);
                    venueIntent.putExtra("VenueId", adapter.getRef(position).getKey());
                    startActivity(venueIntent);
                });
            }
        };
        recyclerVenue.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.venue, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();
        Intent intent = null;

        switch (id) {
            case R.id.nav_bookedItem:

            case R.id.nav_venueList:

            case R.id.nav_logout:

            case R.id.nav_profile:
                intent = new Intent(this, ProfileActivity.class);
                break;

        }
        startActivity(intent);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
