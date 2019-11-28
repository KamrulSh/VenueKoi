package brainstormapps.venuekoi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import brainstormapps.venuekoi.Common.Common;
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

        currentUserPhone = Common.currentUserModel.getPhone();

        bookedRecyclerView = findViewById(R.id.booked_item_recycler);
        bookedRecyclerView.setHasFixedSize(true);
        bookedRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        retrieveBookedItem(currentUserPhone);
    }

    // it will retrieve the previous booked item from firebase in recyclerView
    private void retrieveBookedItem(String phone) {
        bookedList.clear();
        firebaseDatabase = FirebaseDatabase.getInstance();
        bookedDbReference = firebaseDatabase.getReference().child("BookingRequest");

        Query phoneQuery = bookedDbReference.orderByChild("uphone").equalTo(phone);
        FirebaseRecyclerOptions<VenueRequest> recyclerOptions = new FirebaseRecyclerOptions.Builder<VenueRequest>()
                .setQuery(phoneQuery, VenueRequest.class)
                .build();

        bookedAdapter = new FirebaseRecyclerAdapter<VenueRequest, BookedItemHolder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull BookedItemHolder bookedItemHolder, int i, @NonNull VenueRequest venueRequest) {
                bookedItemHolder.fetchBookingDate.setText(venueRequest.getBdate());
                bookedItemHolder.fetchCategoryName.setText(venueRequest.getCatgName());
                bookedItemHolder.fetchVenueName.setText(venueRequest.getVname());
                bookedItemHolder.fetchVenuePrice.setText(venueRequest.getVprice());
                bookedItemHolder.fetchBookingId.setText(venueRequest.getBookingId());
                bookedItemHolder.fetchBookingStatus.setText(convertRequestStatus(venueRequest.getStatus()));
            }

            @NonNull
            @Override
            public BookedItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_booked_item, parent, false);
                return new BookedItemHolder(itemView);
            }
        };
        bookedAdapter.startListening();
        bookedRecyclerView.setAdapter(bookedAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    // it will convert Request Status from 0 => Placed, 1 => Confirmed, 2 => Payment Successful
    private String convertRequestStatus(String status) {
        Log.d("phoneNoStatus1", status);
        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "Confirmed";
        else
            return "Payment Successful";
    }
}
