package com.henallux.primodeal.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.henallux.primodeal.DataAccess.PublicationDao;
import com.henallux.primodeal.Exception.BadRequestException;
import com.henallux.primodeal.Exception.ServerErrorException;
import com.henallux.primodeal.Model.Publication;
import com.henallux.primodeal.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by bil on 06-04-18.
 */

public class StatPublicationOfUserActivity extends AppCompatActivity {

    private Publication publication;
    private Integer publicationId;
    private int cptYes, cptNo, cptIndifferent;
    private TextView titlePublicationTextView, descriptionPublicationView;
    private Button buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_publication_seller);

        Bundle bundle = this.getIntent().getExtras();
        publicationId = (Integer) bundle.getSerializable("publicationId");


        titlePublicationTextView = (TextView)findViewById(R.id.textViewStatTitle);
        descriptionPublicationView = (TextView) findViewById(R.id.textViewStatDescription);

        buttonDelete = (Button) findViewById(R.id.buttonDeletePublication);

        try {
            new PublicationSeller().execute().get();
            titlePublicationTextView.setText(publication.getTitle());
            descriptionPublicationView.setText(publication.getDescription());

            cptYes = cptNo = cptIndifferent = 0;
            if(publication.getResponses() != null){
                for (int i = 0; i < publication.getResponses().size(); i++) {
                    if ("yes".equals(publication.getResponses().get(i).getResponse()))
                        cptYes++;
                    if ("no".equals(publication.getResponses().get(i).getResponse()))
                        cptNo++;
                    if ("indifferent".equals(publication.getResponses().get(i).getResponse()))
                        cptIndifferent++;
                }}

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new DeletePublication().execute();
                    startActivity(new Intent(StatPublicationOfUserActivity.this, NewsfeedFromSellerActivity.class));

                }});



        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        BarChart chart = (BarChart) findViewById(R.id.chart);

        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("My Chart");
        chart.animateXY(2000, 2000);
        chart.invalidate();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newsfeed, menu);
        menu.findItem(R.id.action_deconnection).setVisible(false);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_deconnection:
                return true;

            case R.id.action_back:
                startActivity(new Intent(StatPublicationOfUserActivity.this, NewsfeedFromSellerActivity.class));
                return true;
        }

        return true;
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
       // BarEntry v2e1 = new BarEntry(150.000f, 0); // ind
        BarEntry v2e1 = new BarEntry(cptIndifferent, 0);
        valueSet2.add(v2e1);
        // BarEntry v2e2 = new BarEntry(90.000f, 1); // no
        BarEntry v2e2 = new BarEntry(cptNo, 1);
        valueSet2.add(v2e2);
        // BarEntry v2e3 = new BarEntry(120.000f, 2); // yes
        BarEntry v2e3 = new BarEntry(cptYes, 2);
        valueSet2.add(v2e3);

        int totalVote = cptIndifferent+cptNo+cptYes;

        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Total : "+totalVote);
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        // dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("IND");
        xAxis.add("NO");
        xAxis.add("YES");
        return xAxis;
    }

    private class PublicationSeller extends AsyncTask<Void,Void,Publication>
    {
        private String errorMsg;
        @Override
        protected Publication doInBackground(Void... voids) {
            PublicationDao publicationDao = new PublicationDao();
            try {
               publication = publicationDao.Get(publicationId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return publication;
        }
    }

    private class DeletePublication extends AsyncTask<Void, Void, String>
    {
        String msg;
        String errorMsg;

        @Override
        protected String doInBackground(Void... voids) {
            PublicationDao publicationDao = new PublicationDao();
            try {
                msg = publicationDao.deletePublication(publicationId);
            } catch (BadRequestException e){
                errorMsg = e.message;
            }
            catch (ServerErrorException e){
                errorMsg = getString(R.string.errorserver);
            }
            catch (Exception e) {
                errorMsg = getString(R.string.errorSession);
            }
            return msg;
        }
    }

}

