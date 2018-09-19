package com.algofocus.facebooklogin;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import java.util.List;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    //Views
    TextView txtName,txtemail,txtid,txtimageurl,locationText;
    ImageView imglogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Views initialisation
        txtName = (TextView) findViewById(R.id.txt_name);
        txtemail = (TextView) findViewById(R.id.txt_email);
        txtid = (TextView) findViewById(R.id.txt_ID);
        txtimageurl = (TextView) findViewById(R.id.txt_image_url);
        locationText = (TextView) findViewById(R.id.txt_address);
        imglogout = (ImageView) findViewById(R.id.imageView);


       //Varibales storing the values
        String fname = getIntent().getStringExtra(ConsField.fname);
        String lname = getIntent().getStringExtra(ConsField.lname);
        String email = getIntent().getStringExtra(ConsField.email);
        String  id   = getIntent().getStringExtra(ConsField.id);
        String  profileImage   = getIntent().getStringExtra(ConsField.profilestring);
        String  latitude   = getIntent().getStringExtra(ConsField.latitude);
        String  longitude   = getIntent().getStringExtra(ConsField.longitude);

        //Setting the Text
        txtName.setText("NAME" + "\n" + fname + " " + lname);
        txtemail.setText("EMAIL" + "\n" + email);
        txtid.setText("ID" + "\n" + id);
        txtimageurl.setText("IMAGEURL" + "\n" + profileImage);

        try {
            //Reverse Geocoding
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
        }catch(Exception e)
        {

        }

       //Loging Out Session Management
        imglogout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              LoginManager.getInstance().logOut();
              startActivity(new Intent(DetailsActivity.this, LoginActivity.class));
              finish();
          }
      });

    }
}
