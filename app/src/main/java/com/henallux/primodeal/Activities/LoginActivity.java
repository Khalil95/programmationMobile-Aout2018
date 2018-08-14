package com.henallux.primodeal.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.henallux.primodeal.DataAccess.PersonDao;
import com.henallux.primodeal.Exception.BadRequestException;
import com.henallux.primodeal.Model.LoginForm;
import com.henallux.primodeal.Model.PersonReturnModel;
import com.henallux.primodeal.R;

public class LoginActivity extends AppCompatActivity {

    private EditText email_path, password_path;
    private Button loginButton, inscriptionButton;
    private LoginForm loginForm;
    private PersonDao personDao = new PersonDao();
    private static PersonReturnModel personReturnModel;
    private ProgressBar connectionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_path = (EditText) findViewById(R.id.inputEmail);
        password_path = (EditText)findViewById(R.id.inputPassword);

        loginButton = (Button) findViewById(R.id.buttonLogin);
        inscriptionButton = (Button) findViewById(R.id.buttonRegister);

        connectionBar = (ProgressBar) findViewById(R.id.progressBar);

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(!isConnected){
            Toast.makeText(this,"pas de reseau", Toast.LENGTH_LONG).show();
            return;
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidEmail(email_path.getText())){
                try {
                    connectionBar.setVisibility(View.VISIBLE);
                    new LoginAsync().execute(email_path.getText().toString(), password_path.getText().toString()).get();

                    System.out.println(personReturnModel.getStatus());

                    if("seller".equals(personReturnModel.getStatus()))
                    {
                        startActivity(new Intent(LoginActivity.this, SellerMenuActivity.class));
                    }
                    else
                    {
                        startActivity(new Intent(LoginActivity.this, NewsfeedActivity.class));
                    }


                } catch (Exception e) {
                    connectionBar.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.unknowUser), Toast.LENGTH_LONG);
                    toast.show();
                }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "incorrect email", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PersonRegisterActivity.class);
                startActivity(intent);
            }

        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    private class LoginAsync extends AsyncTask<String, Void, PersonReturnModel> {

        @Override
        protected void onPreExecute() {
            connectionBar.setVisibility(View.VISIBLE);
        }

        protected PersonReturnModel doInBackground(String... strings) {

            try {
                PersonDao.set_user(null);
                PersonDao.setUserToken(null);
                personReturnModel = personDao.login(strings[0],strings[1]);

            }
            catch (Exception e) {

            }
            // connectionBar.setVisibility(View.GONE);
            return personReturnModel;
        }

        protected void onPostExecute(Void res) {
            connectionBar.setVisibility(View.GONE);}
    }


}


