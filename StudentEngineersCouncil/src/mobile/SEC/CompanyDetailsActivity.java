/**
 * CompanyDetailsActivity.java
 * @author Samuel Collard
 * 
 * Requests information about a company from server using its GUID
 * Parses the JSON response and fills in form on screen
 */
package mobile.SEC;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CompanyDetailsActivity extends Activity {

    private Context detailsContext;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details); 
        Bundle extras = getIntent().getExtras();
        String guid = extras.getString("guid");
        detailsContext = this;
        new viewDetails().execute(guid);
    }


    /**
     * AsyncTask to download company details
     * Then fill in UI elements
     */
    private class viewDetails extends AsyncTask<String,Integer,String> {
        private boolean isSaved;
        private String guid;

        @Override
        protected String doInBackground(String... guid) {
            try {
                this.guid = guid[0];
                SECDataSource dataSource = new SECDataSource(detailsContext);
                dataSource.open();
                isSaved = dataSource.isSaved(guid);
                dataSource.close();
                return NetworkHelper.executeHttpGet("http://sec.tamu.edu/common/cf/MobileDetails.aspx?CoGUID="+guid[0]);
            } catch(Exception e) {
                Log.e(CompanyDetailsActivity.class.getSimpleName(), e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                final JSONObject company = new JSONObject(response);
                // name, website, description, attending, days
                // welcomedinner, majors, position, degree

                // Company Name
                TextView name = (TextView) findViewById(R.id.nameText);
                name.setText(company.getString("name"));

                // Saved Company Star
                final ImageView star = (ImageView) findViewById(R.id.fave_star);
                star.setImageResource(isSaved ? R.drawable.star_on : R.drawable.star_off);
                star.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            SECDataSource dataSource = new SECDataSource(detailsContext);
                            dataSource.open();
                            if (!isSaved) {
                                dataSource.saveCompany(new Company(guid, company.getString("name")));
                            } else {
                                dataSource.removeCompany(new Company(guid, company.getString("name")));
                            }
                            isSaved = dataSource.isSaved(new String[]{ guid });
                            dataSource.close();
                            star.setImageResource(isSaved ? R.drawable.star_on : R.drawable.star_off);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                // Company Web Site
                TextView website = (TextView) findViewById(R.id.websiteText);
                website.setText(company.getString("website"));	

                // Company Description
                TextView description = (TextView) findViewById(R.id.descriptionText);
                description.setText(Html.fromHtml(company.getString("description") + "<br>"));	

                // Days Company is Attending
                TextView days = (TextView) findViewById(R.id.daysText);
                days.setText("Days Attending: " + company.getString("days"));	

                // Is the Company attending welcome dinner
                TextView welcome = (TextView) findViewById(R.id.welcomeText);
                welcome.setText("Welcome Dinner: " + company.getString("welcomedinner"));

                // Majors Hired
                TextView major = (TextView) findViewById(R.id.majorsText);
                major.setText("Majors: " + company.getString("majors"));

                // Positions seeking
                TextView position = (TextView) findViewById(R.id.positionText);
                position.setText("Positions Available: " + company.getString("position"));

                // Degrees seeking
                TextView degree = (TextView) findViewById(R.id.degreeText);
                degree.setText("Degrees Seeking: " + company.getString("degree"));

                // Booth Location
                TextView booth = (TextView) findViewById(R.id.boothText);
                booth.setText("Booth: " + company.getString("booth"));
            } catch(Exception e) {
                Log.e(CompanyDetailsActivity.class.getSimpleName(), e.toString());
                Toast.makeText(detailsContext, "Could Not Load Company", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}