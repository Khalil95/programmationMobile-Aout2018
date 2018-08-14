package com.henallux.primodeal.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.henallux.primodeal.Adapters.PublicationAdapter;
import com.henallux.primodeal.DataAccess.PersonDao;
import com.henallux.primodeal.DataAccess.PublicationDao;
import com.henallux.primodeal.Exception.BadRequestException;
import com.henallux.primodeal.Exception.ServerErrorException;
import com.henallux.primodeal.Model.Publication;
import com.henallux.primodeal.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bil on 26-02-18.
 */

public class NewsfeedActivity extends AppCompatActivity {
    private List<Publication> publicationList;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

        new PublicationList().execute();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new PublicationList().execute();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newsfeed, menu);
        if(PersonDao._user != null){
            if("seller".equals(PersonDao._user.getStatus()))
                menu.findItem(R.id.action_deconnection).setVisible(false);
            return true;
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_deconnection:
                PersonDao.set_user(null);
                PersonDao.setUserToken(null);
                startActivity(new Intent(NewsfeedActivity.this, LoginActivity.class));
                return true;

            case R.id.action_back:
                startActivity(new Intent(NewsfeedActivity.this, SellerMenuActivity.class));
                return true;
        }

        return true;
    }



    private void publicationView()
    {
        List<Publication>  publications = new ArrayList<>();

        publications.addAll(publicationList);

        PublicationAdapter publicationAdapter = new PublicationAdapter(this, publications);
        final ListView listView = (ListView)findViewById(R.id.gardenList);
        listView.setAdapter(publicationAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Object listItem = listView.getItemAtPosition(position);
                PublicationDetailResponseActivity.setPublication(null);
                Publication publication = (Publication) listView.getItemAtPosition(position);
                startActivity(new Intent(NewsfeedActivity.this, PublicationDetailResponseActivity.class).putExtra("publication", publication));
            }
        });
    }




    private class PublicationList extends AsyncTask<Void,Void,List<Publication>>
    {
        private String errorMsg;
        @Override
        protected List<Publication> doInBackground(Void... strings) {
            List<Publication> list = new ArrayList<>();
            PublicationDao publicationDao = new PublicationDao();
            try {
                list = publicationDao.Get();
            }
            catch (BadRequestException e){
                errorMsg = e.message;
            }
            catch (ServerErrorException e){
                errorMsg = getString(R.string.errorserver);
            }
            catch (Exception e) {
                errorMsg = getString(R.string.errorSession);
            }

            return list;
        }

        protected void onPostExecute(List<Publication> list) {
            if(this.errorMsg != null){
                Toast.makeText(NewsfeedActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                if(errorMsg == getString(R.string.errorSession)){
                    PersonDao.set_user(null);
                    PersonDao.setUserToken(null);
                    startActivity(new Intent(NewsfeedActivity.this, LoginActivity.class));
                }
            } else {
                publicationList = list;
                Collections.reverse(publicationList);
                publicationView();
            }
        }
    }

}
