/**
 * StudentEngineersActivity.java
 * @author Samuel Collard
 * 
 * Main Activity for application
 * Displays Navigation screen between other activities
 */
package mobile.SEC;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class StudentEngineersCouncilActivity extends Activity {

    private Context SECActivityContext;
    private static final String SEARCH_ALL = "http://sec.tamu.edu/Students/CareerFair/MobileSearch.aspx" +
            "?q=ALL&days=0&welcome=false&major=-1&employment=0&golf=false&degree=0";

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SECActivityContext = this;
        new UpdateDatabaseTask().execute("http://www.google.com", "http://www.google.com");
    }

    /**
     * Click Handler for views in main activity
     * @param v View which was clicked on
     */
    public void navigateToActivity (View v) {
        Log.d("SEC", "navigateToActivity called");
        int id = v.getId();
        Intent intent = null;
        switch (id) {
            case R.id.allCompanies:
                new SearchTask(this).execute(SEARCH_ALL);
                break;
            case R.id.savedCompanies:
                new RetrieveFavoritesTask(this).execute("");
                break;
            case R.id.advancedSearch:
                intent = new Intent(this, CompanySearchActivity.class);
                break;
            case R.id.genInfo:
                intent = new Intent(this, AboutActivity.class);
                break;
            case R.id.schedule:
                intent = new Intent(this, ScheduleActivity.class);
                break;
            case R.id.map:
                intent = new Intent(this, MapActivity.class);
                break;
            default:
                Log.d("SEC", "I don't know what to do");
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    /**
     * Async Task to perform search off the UI thread
     */
    private class UpdateDatabaseTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... url) {
            try {
                String about = NetworkHelper.executeHttpGet(url[0]);
                String itinerary = NetworkHelper.executeHttpGet(url[1]);
                SECDataSource dataSource = new SECDataSource(SECActivityContext);
                dataSource.open();
                dataSource.updateSecDB(System.currentTimeMillis()/1000, about, itinerary);
                dataSource.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}