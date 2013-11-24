/**
 * StudentEngineersActivity.java
 * @author Samuel Collard
 * 
 * Main Activity for application
 * Displays Navigation screen between other activities
 */
package mobile.SEC;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StudentEngineersCouncilActivity extends Activity {
	
	private Context SECActivityContext;
	
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SECActivityContext = this;
        new UpdateDatabaseTask().execute("http://www.google.com", "http://www.google.com");
        // Instantiate navigation menu
        List<NavItem> navItems = new ArrayList<NavItem>(5);
		navItems.add(new NavItem("Company Search", new Intent(this, CompanySearchActivity.class)));
		navItems.add(new NavItem("Schedule", new Intent(this, ScheduleActivity.class)));
		navItems.add(new NavItem("Map", new Intent(this, MapActivity.class)));
		navItems.add(new NavItem("About", new Intent(this, AboutActivity.class)));
		navItems.add(new NavItem("Contact", new Intent(Intent.ACTION_VIEW,
		        Uri.parse("http://sec.tamu.edu/contact.asp"))));
		// set up click listener
		final ListView navView = (ListView)findViewById(R.id.mainNav);
        navView.setAdapter(new NavAdapter(navItems));
        navView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
            	NavItem info = (NavItem) navView.getItemAtPosition(position);
                startActivity(info.intent);
            }
    	});
    }

    /**
     * NavItem is small pair class used to help launch activities
     */
    static class NavItem {
        String name;
        Intent intent;

        NavItem(String name, Intent intent) {
            this.name = name;
            this.intent = intent;
        }
    }

    /**
     * Adapter taken from an Android example
     * Starts Activity of selected NavItem
     */
    private class NavAdapter extends BaseAdapter {
        private List<NavItem> mItems;

        public NavAdapter(List<NavItem> items) {
            mItems = items;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1,
                        parent, false);
                convertView.setTag(convertView.findViewById(android.R.id.text1));
            }
            TextView tv = (TextView) convertView.getTag();
            tv.setText(mItems.get(position).name);
            return convertView;
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
	  			dataSource.updateDB(System.currentTimeMillis()/1000, about, itinerary);
	  			dataSource.close();
	  		} catch(Exception e) {
	  			e.printStackTrace();
	  		}
	  		return null;
	  	}
    }
}