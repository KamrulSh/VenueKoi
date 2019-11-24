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
    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_booking);

        currentVenueId = getIntent().getStringExtra("bookingVenueId");
        categoryName = getIntent().getStringExtra("CategoryName");

        mCalendarView = findViewById(R.id.calendar_view);

        // it will show the minimum data as today in calender
        minimum_date = Calendar.getInstance();
        int currentDay = minimum_date.get(Calendar.DATE);
        minimum_date.set(Calendar.DATE, currentDay);
        long date = minimum_date.getTimeInMillis();
        mCalendarView.setMinDate(date);

        // when a date is selected it will go to the VenueBookingActivity
        mCalendarView.setOnDateChangeListener((calendarView, year, month, day) -> {
            getDate = day + "/" + (month+1) + "/" + year;
            Log.d("phoneNo21date", getDate);
            Intent intent = new Intent(CalendarBookingActivity.this, VenueBookingActivity.class);
            intent.putExtra("selectedBookingDate", getDate);
            intent.putExtra("bookingVenueId", currentVenueId);
            intent.putExtra("CategoryName", categoryName);
            startActivity(intent);
        });
    }
}
