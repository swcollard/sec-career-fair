package mobile.SEC;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class RetrieveFavoritesTask extends AsyncTask<String,Integer,String> {

    private final Context context;
    private List<Company> savedCompanies;
    public RetrieveFavoritesTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            SECDataSource dataSource = new SECDataSource(context);
            dataSource.open();
            savedCompanies = dataSource.getSavedCompanies();
            dataSource.close();
        } catch(Exception e) {
            Log.e(RetrieveFavoritesTask.class.getSimpleName(), e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        if (savedCompanies == null || savedCompanies.size() < 1) {
            Toast.makeText(context, "No Saved Companies Available", Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle companyList = new Bundle();
        for (Company company : savedCompanies) {
            companyList.putString(company.getName(), company.getGuid());
        }
        // Pass companies to the SearchResultsActivity to be displayed
        Intent intent = new Intent(context, SearchResultsActivity.class);
        intent.putExtras(companyList);
        context.startActivity(intent);
    }
}
