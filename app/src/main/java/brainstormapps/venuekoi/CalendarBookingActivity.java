package brainstormapps.venuekoi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CalendarBookingActivity extends AppCompatActivity {

    CalendarView mCalendarView;
    String currentVenueId, getDate;
    Calendar minimum_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_booking);

        currentVenueId = getIntent().getStringExtra("bookingVenueId");

        mCalendarView = findViewById(R.id.calendar_view);

        minimum_date = Calendar.getInstance();
        minimum_date.set(Calendar.DAY_OF_MONTH, minimum_date.getActualMinimum(Calendar.DAY_OF_MONTH));
        long date = minimum_date.getTimeInMillis();
        mCalendarView.setMinDate(date);

        mCalendarView.setOnDateChangeListener((calendarView, year, month, day) -> {
            getDate = day + "/" + (month+1) + "/" + year;
            Log.d("phoneNo21date", getDate);
            Intent intent = new Intent(CalendarBookingActivity.this, VenueBookingActivity.class);
            intent.putExtra("selectedBookingDate", getDate);
            intent.putExtra("bookingVenueId", currentVenueId);
            startActivity(intent);
        });
    }
}
