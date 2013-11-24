/**
 * MapActivity.java
 * @author Samuel Collard
 * 
 * Displays map from asset folder into a WebView
 */
package mobile.SEC;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class MapActivity extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        	// Load Reed Arena map into WebView
        WebView webView = (WebView)findViewById(R.id.webView1);
        webView.loadDataWithBaseURL("file:///android_asset/", "<img src='reedmap.jpg' />", "text/html", "utf-8", null);
        webView.getSettings().setBuiltInZoomControls(true);
    }
}
