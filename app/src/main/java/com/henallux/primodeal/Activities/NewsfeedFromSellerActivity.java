package com.henallux.primodeal.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.henallux.primodeal.Adapters.PublicationAdapter;
import com.henallux.primodeal.DataAccess.PersonDao;
import com.henallux.primodeal.Exception.BadRequestException;
import com.henallux.primodeal.Exception.ServerErrorException;
import com.henallux.primodeal.Model.PersonReturnModel;
import com.henallux.primodeal.Model.Publication;
import com.henallux.primodeal.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by bil on 28-03-18.
 */

public class NewsfeedFromSellerActivity extends AppCompatActivity {

    private PersonReturnModel personReturnModel;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        List<Publication>  publications = new ArrayList<>();

        try {
            new UserSeller().execute().get();
            /*publications.addAll(PersonDao._user.getPublications());*/
                if(personReturnModel != null) {
                    Collections.reverse(personReturnModel.getPublications());
                    publications.addAll(personReturnModel.getPublications());
                }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        PublicationAdapter publicationAdapter = new PublicationAdapter(this, publications);
        final ListView listView = (ListView)findViewById(R.id.gardenList);
        listView.setAdapter(publicationAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Object listItem = listView.getItemAtPosition(position);
                PublicationDetailResponseActivity.setPublication(null);
                Publication publication = (Publication) listView.getItemAtPosition(position);
                startActivity(new Intent(NewsfeedFromSellerActivity.this, StatPublicationOfUserActivity.class).putExtra("publicationId", publication.getId()));
            }
        });

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
                startActivity(new Intent(NewsfeedFromSellerActivity.this, SellerMenuActivity.class));
                return true;
        }

        return true;
    }

    private class UserSeller extends AsyncTask<Void,Void,PersonReturnModel>
    {
        private String errorMsg;
        @Override
        protected PersonReturnModel doInBackground(Void... voids) {
            PersonDao personDao = new PersonDao();
            try {
                personReturnModel = personDao.getPerson(PersonDao._user.getEmail());
            } catch (BadRequestException e){
                errorMsg = e.message;
            }
            catch (ServerErrorException e){
                errorMsg = getString(R.string.errorserver);
            }
            catch (Exception e) {
                errorMsg = getString(R.string.errorSession);
            }
            return personReturnModel;
        }
    }


}
