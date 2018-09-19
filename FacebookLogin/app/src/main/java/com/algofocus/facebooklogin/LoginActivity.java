package com.algofocus.facebooklogin;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.ImageRequest;
import com.facebook.login.Login;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {


    private CallbackManager callbackManager;
    ImageView imglogin;
    private LoginButton loginButton;
    private LocationManager locationManager;
    private String lattitude ="";
    private String longitude="";
    String first_name = "",last_name = "",email = "",id = "",gender = "",profileImageUrl= "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //views Initialisation
        loginButton = (LoginButton) findViewById(R.id.login_button);
        imglogin = (ImageView) findViewById(R.id.login);
        loginButton.setReadPermissions(Arrays.asList(ConsField.EMAIL));

        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        imglogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Login with Facebook", Toast.LENGTH_SHORT).show();
            }
        });


       //Session Management
        /*
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

         if(isLoggedIn)
             startActivity(new Intent(LoginActivity.this,DetailsActivity.class));*/


        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                String userid = loginResult.getAccessToken().getUserId();
                   //Graph API to get the Information
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        userInfo(object);
                    }
                });

                Bundle data = new Bundle();
                data.putString("fields","first_name,last_name,email,id,gender");
                graphRequest.setParameters(data);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }
    //parsing Information
    private void userInfo(JSONObject object) {

        try {
            first_name = object.getString("first_name");
            last_name = object.getString("last_name");
            email = object.getString("email");
            id = object.getString("id");

             profileImageUrl = ImageRequest.getProfilePictureUri(object.optString("id"), 500, 500).toString();
             Log.i("image", profileImageUrl);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Calling the Alert Dialog to enable GPs
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();

            Intent intent = new Intent(LoginActivity.this,DetailsActivity.class);
            intent.putExtra(ConsField.fname,first_name);
            intent.putExtra(ConsField.lname,last_name);
            intent.putExtra(ConsField.email,email);
            intent.putExtra(ConsField.id,id);
            intent.putExtra(ConsField.profilestring,profileImageUrl);
            intent.putExtra(ConsField.latitude,lattitude);
            intent.putExtra(ConsField.longitude,longitude);
            startActivity(intent);
            finish();
        }

    }

    //Gps opening dialog
    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),100);



                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //Getting the location from device via NETWORK_PROVIDER or GPS_PROVIDER or PASSIVE_PROVIDER
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                  Toast.makeText(this, ""+lattitude+longitude, Toast.LENGTH_SHORT).show();
                Log.d("latlng1","as"+lattitude+longitude);
            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);


                 Toast.makeText(this, ""+lattitude+longitude, Toast.LENGTH_SHORT).show();
                Log.d("latlng1","ab"+lattitude+longitude);

            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);


                 Toast.makeText(this, ""+lattitude+longitude, Toast.LENGTH_SHORT).show();
                Log.d("latlng1","ac"+lattitude+longitude);
            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }

            if(!(lattitude.equals("") && longitude.equals("")))
            {


            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100)
        {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getLocation();

                Intent intent = new Intent(LoginActivity.this,DetailsActivity.class);
                intent.putExtra(ConsField.fname,first_name);
                intent.putExtra(ConsField.lname,last_name);
                intent.putExtra(ConsField.email,email);
                intent.putExtra(ConsField.id,id);
                intent.putExtra(ConsField.profilestring,profileImageUrl);
                intent.putExtra(ConsField.latitude,lattitude);
                intent.putExtra(ConsField.longitude,longitude);
                startActivity(intent);
                finish();
            }
            else
            {
                buildAlertMessageNoGps();
            }

        }
    }
}
