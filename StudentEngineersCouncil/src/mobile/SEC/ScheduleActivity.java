/**
 * ScheduleActivity.java
 * @author Samuel Collard
 * 
 * Displays information about the career fair itinerary
 */
package mobile.SEC;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class ScheduleActivity extends Activity {

    String backupItinerary = "<h2>Itinerary:</h2> <br>" +
            "<h3>Monday, January 27th</h3>" +
            "<p>  7:00pm - 10:00pm Welcome Dinner & Social, Memorial Student Center</p><br>" +
            "<h3>Tuesday, January 28th</h3>" +
            "<p>  9:00am - 4:00pm Exhibition - Reed Arena</p><br>" +
            "<br>* Pre-registration required on our website for the Welcome Dinner.<br>";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        ((TextView)findViewById(R.id.aboutText)).setText(Html.fromHtml(itineraryText()));
    }
    
    private String itineraryText() {
        /*
        try {
            SECDataSource dataSource = new SECDataSource(this);
            dataSource.open();
            String itinerary = dataSource.getItinerary();
            dataSource.close();
            if (itinerary == null || "".equals(itinerary))
                return backupItinerary;
            else
                return itinerary;
        }
        catch (Exception e){
            return backupItinerary;
        }
        */
        return backupItinerary;
    }
}
