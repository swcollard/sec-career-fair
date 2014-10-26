package mobile.SEC;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Async Task to perform search off the UI thread
 * Uses Toast notifications to let user know what is happening
 */
public class SearchTask extends AsyncTask<String,Integer,String> {

    private Context searchContext;

    public SearchTask(Context context) {
        searchContext = context;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(searchContext, "Searching...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... url) {
        try {
            return NetworkHelper.executeHttpGet(url[0]);
        } catch(Exception e) {
            Log.e(SearchTask.class.getSimpleName(), e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        try {
            JSONObject jObject = new JSONObject(response);
            // Report number of search results
            int count = jObject.getInt("count");
            if(count < 0) {
                Toast.makeText(searchContext, "Server Failed to Perform Search", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (count == 0) {
                Toast.makeText(searchContext, "No Matches Found", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (count == 1) {
                Toast.makeText(searchContext, "Found 1 Match", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(searchContext, "Found " + count + " Matches", Toast.LENGTH_SHORT).show();
            }
            
            Bundle searchResults = new Bundle();
            JSONArray companyList = jObject.getJSONArray("companyList");
            // Build search results map
            for(int i = 0; i<companyList.length(); i++) {
                JSONObject company = companyList.getJSONObject(i);
                String name = company.getString("name");
                String guid = company.getString("guid");
                searchResults.putString(name, guid);

            }
            // Pass search results to the SearchResultsActivity to be displayed
            Intent i = new Intent(searchContext, SearchResultsActivity.class);
            i.putExtras(searchResults);
            searchContext.startActivity(i);
        } catch(Exception e) {
            Log.e(SearchTask.class.getSimpleName(), e.toString());
            Toast.makeText(searchContext, "Failed to Perform Search", Toast.LENGTH_SHORT).show();
        }
    }
}