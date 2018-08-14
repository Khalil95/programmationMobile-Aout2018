package com.henallux.primodeal.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.henallux.primodeal.DataAccess.PersonDao;
import com.henallux.primodeal.DataAccess.PublicationDao;
import com.henallux.primodeal.Exception.BadRequestException;
import com.henallux.primodeal.Exception.ServerErrorException;
import com.henallux.primodeal.Model.Publication;
import com.henallux.primodeal.R;

import java.util.List;

/**
 * Created by bil on 22-11-17.
 */

public class NewPublicationSellerActivity extends AppCompatActivity {

    private Button postButton, cancelButton;
    private EditText titlePublication_path, descriptionPublication_path;
    private int yes, no, dontknow;
    private PublicationDao publicationDao = new PublicationDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitvity_seller_new_publication);
        //QuestionFragment question = (QuestionFragment)findViewById(R.id.fragmentQuestion);

        titlePublication_path = (EditText) findViewById(R.id.input_titlePublication);
        descriptionPublication_path = (EditText) findViewById(R.id.input_descriptionPublication);


        postButton = (Button) findViewById(R.id.buttonPostPublication);
        cancelButton = (Button) findViewById(R.id.buttonCancelPublication);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new NewPostAsync().execute(titlePublication_path.getText().toString(), descriptionPublication_path.getText().toString());

            }
        });



        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewPublicationSellerActivity.this, SellerMenuActivity.class);
                startActivity(intent);
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
                startActivity(new Intent(NewPublicationSellerActivity.this, SellerMenuActivity.class));
                return true;
        }

        return true;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButtonActiveAnswer:
                if (checked)
                    yes = no = dontknow = 0;
        }
    }


    private class NewPostAsync extends AsyncTask<String, Void, Integer> {
        int code = 0;
        String errorMsg;

        @Override
        protected Integer doInBackground(String... strings){

            try {
                code = publicationDao.postPublication(strings[0], strings[1], yes, no, dontknow);
               if(code == 201){
                    startActivity(new Intent(NewPublicationSellerActivity.this, SellerMenuActivity.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return code;
        }


    }

}


