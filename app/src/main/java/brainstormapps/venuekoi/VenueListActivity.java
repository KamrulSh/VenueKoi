package brainstormapps.venuekoi;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import brainstormapps.venuekoi.Model.Venue;
import brainstormapps.venuekoi.ViewHolder.VenueViewHolder;

public class VenueListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference venueItemRef;
    RecyclerView recyclerVenue;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Venue, VenueViewHolder> adapter;

    // search functionality
    FirebaseRecyclerAdapter<Venue, VenueViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

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

        // search function
        materialSearchBar = findViewById(R.id.venue_searchBar);
        loadSuggestion();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                // when user type text , it will change suggestion list
                List<String> suggest = new ArrayList<>();
                for (String search:suggestList) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }

                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                // when search bar is closed it will restore original adapter
                if (!enabled)
                    recyclerVenue.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                // when search finished it will show search adapter
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Venue, VenueViewHolder>(
                Venue.class, R.layout.home_venue_item,
                VenueViewHolder.class, venueItemRef.orderByChild("Name").equalTo(text.toString())) {
            @Override
            protected void populateViewHolder(VenueViewHolder venueViewHolder, Venue venue, int i) {

                venueViewHolder.txtVenueName.setText(venue.getName());
                venueViewHolder.txtVenuePrice.setText(venue.getPrice());
                Picasso.get().load(venue.getImage()).into(venueViewHolder.imgVenue);

                // send venue id to venue details activity for showing details
                venueViewHolder.setItemClickListener((view, position, isLongClick) -> {
                    Intent venueIntent = new Intent(VenueListActivity.this, VenueDetailsActivity.class);
                    venueIntent.putExtra("VenueId", searchAdapter.getRef(position).getKey());
                    startActivity(venueIntent);
                });
            }
        };
        recyclerVenue.setAdapter(searchAdapter); // set adapter for Recycler view in search result
    }

    private void loadSuggestion() {

        venueItemRef.orderByChild("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postDataSnapshot:dataSnapshot.getChildren()) {
                    Venue venueItem = postDataSnapshot.getValue(Venue.class);
                    suggestList.add(venueItem.getName()); // add name of venue to suggest list
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadVenueItem() {
        //Log.d("fimg", "item passed");
        adapter = new FirebaseRecyclerAdapter<Venue, VenueViewHolder>(
                Venue.class, R.layout.home_venue_item, VenueViewHolder.class, venueItemRef) {
            @Override
            protected void populateViewHolder(VenueViewHolder venueViewHolder, Venue venue, int i) {
                venueViewHolder.txtVenueName.setText(venue.getName());
                venueViewHolder.txtVenuePrice.setText(venue.getPrice());
                Picasso.get().load(venue.getImage()).into(venueViewHolder.imgVenue);

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
                intent = new Intent(this, PreviousBookedItem.class);
                break;

            case R.id.nav_logout:
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intentLogOut = new Intent(VenueListActivity.this, MainActivity.class);
                    startActivity(intentLogOut);
                    finish();
                }
                break;

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
