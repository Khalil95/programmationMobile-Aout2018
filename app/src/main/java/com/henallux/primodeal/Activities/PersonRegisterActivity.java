package com.henallux.primodeal.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.henallux.primodeal.DataAccess.PersonDao;
import com.henallux.primodeal.R;

public class PersonRegisterActivity extends AppCompatActivity {

    private Button createPersonButton;
    private EditText email_path, password_path, addressShopPath, nameShopPath ;
    private LinearLayout layoutNameShop, layoutAddressShop;
    private String status;
    private PersonDao personDao = new PersonDao();
    private boolean validation = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_register);

        email_path = (EditText) findViewById(R.id.inputEmailRegister);
        password_path = (EditText) findViewById(R.id.inputPasswordRegister);
        addressShopPath = (EditText) findViewById(R.id.inputAddressShop);
        nameShopPath = (EditText) findViewById(R.id.inputShopName);

        layoutAddressShop = (LinearLayout) findViewById(R.id.linearLayoutAddressShop);
        layoutNameShop = (LinearLayout)findViewById(R.id.linearLayoutNameShop);

        layoutNameShop.setVisibility(View.INVISIBLE);
        layoutAddressShop.setVisibility(View.INVISIBLE);

        createPersonButton = (Button) findViewById(R.id.buttonRegister);
        createPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidEmail(email_path.getText())){
                try {
                    new RecordPersonAsync().execute(email_path.getText().toString(), password_path.getText().toString(), addressShopPath.getText().toString(), nameShopPath.getText().toString());
                } catch (Exception e) {
                    Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                    toast.show();
                }}
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "incorrect email", Toast.LENGTH_LONG);
                    toast.show();
                }
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
                startActivity(new Intent(PersonRegisterActivity.this, LoginActivity.class));
                return true;
        }

        return true;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_costumer:
                if (checked)
                    status = "costumer";
                    layoutNameShop.setVisibility(View.INVISIBLE);
                    layoutAddressShop.setVisibility(View.INVISIBLE);
                    break;
            case R.id.radio_seller:
                if (checked)
                    status="seller";
                    layoutNameShop.setVisibility(View.VISIBLE);
                    layoutAddressShop.setVisibility(View.VISIBLE);
                    break;
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private class RecordPersonAsync extends AsyncTask<String, Void, Integer> {
        private Integer code;

        protected Integer doInBackground(String... strings) {

            try {
                code = personDao.inscription(strings[0],strings[1], strings[2],strings[3] ,status);
                if(code == 200)
                {

                    if(status.equals("costumer")){
                    Intent intent = new Intent(PersonRegisterActivity.this, NewsfeedActivity.class);
                        startActivity(intent);}

                    if(status.equals("seller"))
                    {
                        Intent intent = new Intent(PersonRegisterActivity.this, SellerMenuActivity.class);
                        startActivity(intent);
                    }

                }
            } catch (Exception e) {
               /* Toast toast = Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG);
                toast.show();*/
            }

            return code;
        }
    }
}
