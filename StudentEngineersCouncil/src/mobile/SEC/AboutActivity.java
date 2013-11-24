/**
 * AboutActivity.java
 * @author Samuel Collard
 * 
 * Displays general information about the career fair
 */
package mobile.SEC;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class AboutActivity extends Activity {

	private static String backupAbout = "<h2>General Career Fair Information:</h2> <p>The SEC Engineering Career Fair is the premier recruiting event for " +
    		"the Dwight Look College of Engineering. The career fair is planned, organized, and staffed by the Student Engineers’ " +
    		"Council and is one of the largest student-run career fairs in the nation. Historically, over 350 companies and 5,000 " +
    		"engineering students have attended the career fair during the Spring semester.</p> <br>" +
    		"* All Texas A&M System students are invited to attend the career fair exhibition. No registration is necessary.<br>" +
    		"* Students must present a valid Texas A&M Student ID to be admitted to the Career Fair exhibition.<br>" +
    		"* The Career Fair and all of its events are free for students.<br>" +
    		"* Dress for the career fair exhibition is business casual.<br>" +
    		"* Students interested in attending the Welcome Dinner must register through our website at a later date.<br>";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        ((TextView)findViewById(R.id.aboutText)).setText(Html.fromHtml(aboutText()));
    }
    
    private String aboutText() {
    	try {
	    	SECDataSource dataSource = new SECDataSource(this);
	    	dataSource.open();
	    	String about = dataSource.getAbout();
	    	dataSource.close();
	    	if (about == null || "".equals(about))
	    		return backupAbout;
	    	else
	    		return about;
    	}
    	catch (Exception e){
    		return backupAbout;
    	}
    }
}