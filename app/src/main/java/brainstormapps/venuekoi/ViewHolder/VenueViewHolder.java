package brainstormapps.venuekoi.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import brainstormapps.venuekoi.Interface.ItemClickListener;
import brainstormapps.venuekoi.R;

public class VenueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtVenueName;
    public TextView txtVenuePrice;
    public ImageView imgVenue;
    private ItemClickListener itemClickListener;

    public VenueViewHolder(@NonNull View itemView) {
        super(itemView);

        txtVenueName = itemView.findViewById(R.id.venue_name);
        imgVenue = itemView.findViewById(R.id.venue_image);
        txtVenuePrice = itemView.findViewById(R.id.venue_price);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
