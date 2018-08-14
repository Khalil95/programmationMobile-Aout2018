package com.henallux.primodeal.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.henallux.primodeal.Model.PersonReturnModel;
import com.henallux.primodeal.R;

/**
 * Created by bil on 05-04-18.
 */

public class StoreCardActivity  extends AppCompatActivity {

    private PersonReturnModel shop;
    private TextView nameShopTextView, addressShopTextView;
    private Button googleMapButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_card);
        Bundle bundle = this.getIntent().getExtras();
        /*final Publication*/ shop = (PersonReturnModel) bundle.getSerializable("shop");

        nameShopTextView = (TextView) findViewById(R.id.textViewNameShopCard);
        addressShopTextView = (TextView) findViewById(R.id.textViewAddressShopCard);

        googleMapButton = (Button) findViewById(R.id.buttonSeeOnGoogleMap);

        nameShopTextView.setText(shop.getNameShop());
        addressShopTextView.setText(shop.getAddressShop());

        googleMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+shop.getNameShop());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }});

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
                startActivity(new Intent(StoreCardActivity.this, PublicationDetailResponseActivity.class));
                return true;
        }

        return true;
    }


}
