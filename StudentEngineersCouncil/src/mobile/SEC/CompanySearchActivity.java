/**
 * CompanySearchActivity.java
 * @author Samuel Collard
 * 
 * Performs Search request to server
 * Takes input from form, creates parameters and sends http request
 * Parses response and sends results to SearchResultsActivity
 */
package mobile.SEC;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CompanySearchActivity extends Activity {

    private Context searchContext;
    private static Map<String,Integer> majorMap;
    static {
        majorMap = new HashMap<String, Integer>(23);
        majorMap.put("-", -1);
        majorMap.put("Aerospace Engineering",1);
        majorMap.put("Agricultural Engineering",2);
        majorMap.put("Biological and Agricultural Engineering",24);
        majorMap.put("Biomedical Engineering",4);
        majorMap.put("Chemical Engineering",5);
        majorMap.put("Civil Engineering",6);
        majorMap.put("Computer Engineering",7);
        majorMap.put("Computer Science",8);
        majorMap.put("Electrical Engineering",9);
        majorMap.put("Engineering Systems Management",11);
        majorMap.put("Engineering Technology",12);
        majorMap.put("Health Physics",13);
        majorMap.put("Industrial and Systems Engineering",15);
        majorMap.put("Industrial Distribution",14);
        majorMap.put("Interdisciplinary Engineering",17);
        majorMap.put("Materials Science and Engineering",16);
        majorMap.put("Mechanical Engineering",18);
        majorMap.put("Nuclear Engineering",19);
        majorMap.put("Ocean Engineering",20);
        majorMap.put("Petroleum Engineering",21);
        majorMap.put("Radiological Health Engineering",22);
        majorMap.put("Safety Engineering",23);
    }
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search); 

        searchContext = this;
        // Days Attending Spinner
        Spinner daySpinner = (Spinner) findViewById(R.id.daySpinner);
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(
                this, R.array.DaysArray, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        // majorMap Spinner
        Spinner majorMappinner = (Spinner) findViewById(R.id.majorSpinner);
        ArrayAdapter<CharSequence> majorAdapter = ArrayAdapter.createFromResource(
                this, R.array.MajorsArray, android.R.layout.simple_spinner_item);
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        majorMappinner.setAdapter(majorAdapter);

        // Degree Spinner
        Spinner degreeSpinner = (Spinner) findViewById(R.id.degreeSpinner);
        ArrayAdapter<CharSequence> degreeAdapter = ArrayAdapter.createFromResource(
                this, R.array.DegreesArray, android.R.layout.simple_spinner_item);
        degreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        degreeSpinner.setAdapter(degreeAdapter);

        // Employment Spinner
        Spinner employmentSpinner = (Spinner) findViewById(R.id.employmentSpinner);
        ArrayAdapter<CharSequence> employmentAdapter = ArrayAdapter.createFromResource(
                this, R.array.EmploymentArray, android.R.layout.simple_spinner_item);
        employmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employmentSpinner.setAdapter(employmentAdapter);
    }

    /**
     *  Executes a search
     * @param view	
     */
    public void onSearchClick(View view) {
        String params = getParams();
        String url = "http://sec.tamu.edu/Students/CareerFair/MobileSearch.aspx" + params;
        new Search().execute(url);
    }

    /**
     * gets values from UI elements to include in search request
     * Java 1.6 does not support switch statements for String objects
     * @return a String of parameters for the search
     */
    private String getParams() {
        String query = ((EditText)findViewById(R.id.queryText)).getText().toString();
        if(query.equals(""))
            query = "ALL";

        // Configure day parameter
        String days = ((Spinner)findViewById(R.id.daySpinner)).getSelectedItem().toString();
        String dayParam = "0";

        if (days.equals("Tue"))
            dayParam = "T";
        else if (days.equals("Wed"))
            dayParam = "W";
        else if (days.equals("Both"))
            dayParam = "B";

        // Configure employment parameter
    	String employment = ((Spinner)findViewById(R.id.employmentSpinner)).getSelectedItem().toString();
    	String employmentParam = "0";

    	if(employment.equals("Intern"))
    		employmentParam = "I";
    	else if (employment.equals("Co-Op"))
    		employmentParam = "C";
    	else if (employment.equals("Intern or Co-Op"))
    		employmentParam = "B";
    	else if (employment.equals("Full-Time"))
    		employmentParam = "F";
    	else if (employment.equals("Part-Time"))
    		employmentParam = "P";
    	
    		// Configure degree parameter
    	String degree = ((Spinner)findViewById(R.id.degreeSpinner)).getSelectedItem().toString();
    	String degreeParam = "0";

    	if(degree.equals("Bachelors"))
    		degreeParam = "B";
    	else if (degree.equals("Masters"))
    		degreeParam = "M";
    	else if (degree.equals("Doctorate"))
    		degreeParam = "D";
    	
    		// Concatenate parameters
    	String params = "?q=" + URLEncoder.encode(query)
    			+ "&days=" + dayParam
    			+ "&welcome=" + ((CheckBox)findViewById(R.id.welcomeBox)).isChecked()
    			+ "&major=" + majorMap.get((String)((Spinner)findViewById(R.id.majorSpinner)).getSelectedItem())
    			+ "&employment=" + employmentParam
    			+ "&golf=false"
    			+ "&degree=" + degreeParam;
    	return params;
    }
    
    /**
     * Async Task to perform search off the UI thread
     * Uses Toast notifications to let user know what is happening
     */
    private class Search extends AsyncTask<String,Integer,String> {
    	
    	@Override
    	protected void onPreExecute() {
    		Toast.makeText(searchContext, "Searching...", Toast.LENGTH_SHORT).show();
    	}
    	
    	@Override
		protected String doInBackground(String... url) {
			try {
				return NetworkHelper.executeHttpGet(url[0]);
			} catch(Exception e) {
				e.printStackTrace();
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
	            startActivity(i);
    		} catch(Exception e) {
    			e.printStackTrace();
    			Toast.makeText(searchContext, "Failed to Perform Search", Toast.LENGTH_SHORT).show();
    		}
		}
    }
}
