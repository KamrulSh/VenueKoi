package brainstormapps.venuekoi;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import brainstormapps.venuekoi.Common.Common;
import brainstormapps.venuekoi.Model.VenueModel;
import brainstormapps.venuekoi.ViewHolder.VenueViewHolder;

public class VenueListActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference venueItemRef;
    RecyclerView recyclerVenue;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<VenueModel, VenueViewHolder> adapter;

    // search adapter for search functionality
    FirebaseRecyclerAdapter<VenueModel, VenueViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;
    String categoryId = "", categoryName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_list);

        // initialize firebase
        database = FirebaseDatabase.getInstance();
        venueItemRef = database.getReference("VenueList");
        // load venue item in recyclerView
        recyclerVenue = findViewById(R.id.recycler_venueItem);
        recyclerVenue.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerVenue.setLayoutManager(layoutManager);

        // get intent here from VenueCategoryActivity
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("CategoryId");
            categoryName = getIntent().getStringExtra("CategoryName");
            Log.d("phoneNoCategoryName", categoryName);
        }
        if (!categoryId.isEmpty() && categoryId != null)
            loadVenueListItem(categoryId, categoryName);

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

    // it will show venue list in recycler view
    private void loadVenueListItem(String categoryId, String categoryName) {
        adapter = new FirebaseRecyclerAdapter<VenueModel, VenueViewHolder>(
                VenueModel.class, R.layout.custom_venue_item,
                VenueViewHolder.class, venueItemRef.orderByChild("CatId").equalTo(categoryId)
                // like SELECT * FROM VenueList WHERE catId = categoryId
        ) {
            @Override
            protected void populateViewHolder(VenueViewHolder venueViewHolder, VenueModel venueModel, int i) {
                venueViewHolder.txtVenueName.setText(venueModel.getName());
                venueViewHolder.txtVenuePrice.setText(venueModel.getPrice());
                Picasso.get().load(venueModel.getImage()).into(venueViewHolder.imgVenue);

                // send venueModel id to venueModel details activity for showing details
                venueViewHolder.setItemClickListener((view, position, isLongClick) -> {
                    if (Common.isConnectedToInternet(getBaseContext())) {
                        Intent venueIntent = new Intent(VenueListActivity.this, VenueDetailsActivity.class);
                        venueIntent.putExtra("VenueId", adapter.getRef(position).getKey());
                        venueIntent.putExtra("CategoryName", categoryName);
                        startActivity(venueIntent);
                    } else
                        Toast.makeText(VenueListActivity.this, "Please check internet connection.", Toast.LENGTH_LONG).show();
                });
            }
        };
        recyclerVenue.setAdapter(adapter);
    }

    // when search btn is clicked it will show the desired search result
    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<VenueModel, VenueViewHolder>(
                VenueModel.class, R.layout.custom_venue_item,
                VenueViewHolder.class, venueItemRef.orderByChild("Name").equalTo(text.toString())) {
            @Override
            protected void populateViewHolder(VenueViewHolder venueViewHolder, VenueModel venueModel, int i) {

                venueViewHolder.txtVenueName.setText(venueModel.getName());
                venueViewHolder.txtVenuePrice.setText(venueModel.getPrice());
                Picasso.get().load(venueModel.getImage()).into(venueViewHolder.imgVenue);

                // send venueModel id to venueModel details activity for showing details
                venueViewHolder.setItemClickListener((view, position, isLongClick) -> {
                    Intent venueIntent = new Intent(VenueListActivity.this, VenueDetailsActivity.class);
                    venueIntent.putExtra("VenueId", searchAdapter.getRef(position).getKey());
                    venueIntent.putExtra("CategoryName", categoryName);
                    startActivity(venueIntent);
                });
            }
        };
        recyclerVenue.setAdapter(searchAdapter); // set adapter for Recycler view in search result
    }

    // it will show the search suggestions when search option is clicked
    private void loadSuggestion() {

        venueItemRef.orderByChild("CatId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postDataSnapshot:dataSnapshot.getChildren()) {
                    VenueModel venueModelItem = postDataSnapshot.getValue(VenueModel.class);
                    suggestList.add(venueModelItem.getName()); // add name of venue to suggest list
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(VenueListActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
