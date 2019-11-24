package brainstormapps.venuekoi;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import brainstormapps.venuekoi.Common.Common;
import brainstormapps.venuekoi.Model.CategoryModel;
import brainstormapps.venuekoi.ViewHolder.CategoryViewHolder;

public class VenueCategoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference venueCatRef;
    RecyclerView recyclerVenueCat;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<CategoryModel, CategoryViewHolder> adapter;
    TextView userFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // set user name in navigation drawer header for current user
        View headerView = navigationView.getHeaderView(0);
        userFullName = headerView.findViewById(R.id.header_fullName);
        userFullName.setText(Common.currentUserModel.getName());

        // initialize firebase for category list
        database = FirebaseDatabase.getInstance();
        venueCatRef = database.getReference("CategoryList");

        recyclerVenueCat = findViewById(R.id.recycler_venueCategory);
        recyclerVenueCat.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerVenueCat.setLayoutManager(layoutManager);

        loadVenueCategoryItem();

    }

    // load Venue Category Item from firebase in recyclerView
    private void loadVenueCategoryItem() {

        adapter = new FirebaseRecyclerAdapter<CategoryModel, CategoryViewHolder>(
                CategoryModel.class, R.layout.custom_category_item,
                CategoryViewHolder.class, venueCatRef
        ) {
            @Override
            protected void populateViewHolder(CategoryViewHolder categoryViewHolder, CategoryModel categoryModel, int i) {
                categoryViewHolder.categoryName.setText(categoryModel.getName());
                Picasso.get().load(categoryModel.getImage()).into(categoryViewHolder.categoryImage);

                final CategoryModel clickItem = categoryModel;
                categoryViewHolder.setItemClickListener((view, position, isLongClick) -> {
                    Toast.makeText(VenueCategoryActivity.this, ""+clickItem.getName(), Toast.LENGTH_SHORT).show();
                    // get CategoryId and send it to VenueListActivity
                    Intent venueList = new Intent(VenueCategoryActivity.this, VenueListActivity.class);
                    // as CategoryId is a key, so we need to get key of this item
                    venueList.putExtra("CategoryId", adapter.getRef(position).getKey());
                    venueList.putExtra("CategoryName", categoryModel.getName());
                    startActivity(venueList);
                });
            }
        };
        recyclerVenueCat.setAdapter(adapter);
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
        Intent intent;

        switch (id) {
            case R.id.nav_bookedItem:
                intent = new Intent(this, PreviousBookedItem.class);
                startActivity(intent);
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intentLogOut = new Intent(VenueCategoryActivity.this, MainActivity.class);
                intentLogOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentLogOut);
                break;

            case R.id.nav_profile:
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;

        }
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
