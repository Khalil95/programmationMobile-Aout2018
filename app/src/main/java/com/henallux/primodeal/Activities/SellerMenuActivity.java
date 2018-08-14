package com.henallux.primodeal.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.henallux.primodeal.DataAccess.PersonDao;
import com.henallux.primodeal.R;

/**
 * Created by bil on 22-11-17.
 */

public class SellerMenuActivity extends AppCompatActivity {

    private Button myPublicationsButton, newPublicationButton, allPublicationNamur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_menu);

        if(PersonDao.tokenString == null){
            PersonDao.set_user(null);
            Toast.makeText(SellerMenuActivity.this, getString(R.string.newInscription), Toast.LENGTH_LONG).show();
            startActivity(new Intent(SellerMenuActivity.this, LoginActivity.class));
        }

        myPublicationsButton = (Button) findViewById(R.id.buttonMyPublications);
        newPublicationButton = (Button) findViewById(R.id.buttonNewPublication);
        allPublicationNamur = (Button) findViewById(R.id.buttonAllPublication);

        myPublicationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerMenuActivity.this, NewsfeedFromSellerActivity.class);
                startActivity(intent);
            }
        });

        newPublicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerMenuActivity.this, NewPublicationSellerActivity.class);
                startActivity(intent);
            }
        });

        allPublicationNamur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerMenuActivity.this, NewsfeedActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newsfeed, menu);
        menu.findItem(R.id.action_back).setVisible(false);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_deconnection:
                PersonDao.set_user(null);
                startActivity(new Intent(SellerMenuActivity.this, LoginActivity.class));
                return true;

            case R.id.action_back:
                return true;
        }

        return true;
    }
}
