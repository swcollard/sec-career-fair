/**
 * SearchResultsActivity.java
 * @author Samuel Collard
 * 
 * Displays Search results given to it by the CompanySearchActivity
 * The bundle uses the company name as the key and guid as the value
 */
package mobile.SEC;

import java.util.Arrays;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchResultsActivity extends Activity {

    private static Bundle results;
    private Context resultsContext;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results); 

        resultsContext = this;
        results = getIntent().getExtras();
        // Get results from bundle
        Set<String> companyNames = results.keySet();
        String[] resultsArray = companyNames.toArray(new String[0]);
        Arrays.sort(resultsArray, String.CASE_INSENSITIVE_ORDER);

        final ListView resultsView = (ListView) findViewById(R.id.resultsView);
        ArrayAdapter<String> resultsAdapter = new ArrayAdapter<String>(this, 
                android.R.layout.simple_list_item_1,resultsArray);
        resultsView.setAdapter(resultsAdapter);
        resultsView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                // Bring user to selected companies details page
                Intent i = new Intent(resultsContext, CompanyDetailsActivity.class);
                i.putExtra("guid",results.getString(resultsView.getItemAtPosition(position).toString()));
                startActivity(i);
            }
        });
    }
}
