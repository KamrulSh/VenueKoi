package brainstormapps.venuekoi;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import brainstormapps.venuekoi.Model.VenueRequest;
import brainstormapps.venuekoi.ViewHolder.BookedItemHolder;

public class PreviousBookedItem extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference bookedDbReference;
    RecyclerView bookedRecyclerView;
    List<VenueRequest> bookedList = new ArrayList<>();
    FirebaseRecyclerAdapter<VenueRequest, BookedItemHolder> bookedAdapter;
    String currentUserPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_booked_item);

        currentUserPhone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        bookedRecyclerView = findViewById(R.id.booked_item_recycler);
        bookedRecyclerView.setHasFixedSize(true);
        bookedRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        retrieveBookedItem();
    }

    private void retrieveBookedItem() {
        bookedList.clear();
        firebaseDatabase = FirebaseDatabase.getInstance();
        bookedDbReference = firebaseDatabase.getReference().child("BookingRequest");

        bookedAdapter = new FirebaseRecyclerAdapter<VenueRequest, BookedItemHolder>(
                VenueRequest.class, R.layout.booked_item_card,
                BookedItemHolder.class, bookedDbReference.orderByChild("uphone").equalTo(currentUserPhone)) {
            @Override
            protected void populateViewHolder(BookedItemHolder bookedItemHolder, VenueRequest venueRequest, int i) {
                bookedItemHolder.fetchBookingDate.setText(venueRequest.getBdate());
                bookedItemHolder.fetchVenueName.setText(venueRequest.getVname());
                bookedItemHolder.fetchVenuePrice.setText(venueRequest.getVprice());
                bookedItemHolder.fetchBookingId.setText(venueRequest.getBookingId());
            }
        };
        bookedRecyclerView.setAdapter(bookedAdapter);
    }
}
