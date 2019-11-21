package brainstormapps.venuekoi.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import brainstormapps.venuekoi.R;

public class BookedItemHolder extends RecyclerView.ViewHolder {

    public TextView fetchBookingId;
    public TextView fetchVenueName;
    public TextView fetchVenuePrice;
    public TextView fetchBookingDate;
    public TextView fetchCategoryName;
    public TextView fetchBookingStatus;

    public BookedItemHolder(@NonNull View itemView) {
        super(itemView);

        fetchBookingId = itemView.findViewById(R.id.fetch_booking_id);
        fetchBookingDate = itemView.findViewById(R.id.fetch_booking_date);
        fetchVenueName = itemView.findViewById(R.id.fetch_venue_name);
        fetchVenuePrice = itemView.findViewById(R.id.fetch_venue_price);
        fetchCategoryName = itemView.findViewById(R.id.fetch_category_name);
        fetchBookingStatus = itemView.findViewById(R.id.fetch_booking_status);
    }
}
