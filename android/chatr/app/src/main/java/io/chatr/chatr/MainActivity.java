package io.chatr.chatr;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PLACE_PICKER = 1001;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            checkPlayServices();
        }
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        FloatingActionButton fabCheckIn = (FloatingActionButton) findViewById(R.id.fab_check_in);
        fabCheckIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                locationCheckIn();
            }
        });
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.showErrorDialogFragment(this, result, REQUEST_GOOGLE_PLAY_SERVICES);
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_PLACE_PICKER:
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_GOOGLE_PLAY_SERVICES:
                // not sure what to do here
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void locationCheckIn() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), REQUEST_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void openLogin(View view) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
}
