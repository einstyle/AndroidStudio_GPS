package app.locale.list.testmobile;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private TextView mGPSLonView;
    private TextView mGPSLatView;
    private LocationManager mLocationManager;
    private String provider;
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 30 * 1; // 30 sec
    private boolean canGetLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        mTextView = (TextView) findViewById(R.id.text);
        mGPSLonView = (TextView) findViewById(R.id.gps_latitude);
        mGPSLatView = (TextView) findViewById(R.id.gps_longitude);

        // Get the location manager
        Location loc = null;
        Location location = getLocation(loc);
        Log.d(String.valueOf(location), "{LOGGING} Location? ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public Location getLocation(Location location) {
        try {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            location = null;
            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d("No Network", "{LOGGING} !isGPSEnabled && !isNetworkEnabled");
            }
            else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    Log.d("Network", "{LOGGING} Network Enabled true");
                    mLocationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);

                    if (mLocationManager != null) {
                        location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        Log.d("Location Manager", "{LOGGING} mLocationManager not null");
                        if (location != null) {
                            Log.d(String.valueOf(1000 * 60 * location.getLatitude()), "{LOGGING} location.getLatitude() ");
                            Log.d(String.valueOf(1000 * 60 * location.getLongitude()), "{LOGGING} location.getLatitude() ");
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            mGPSLonView.setText(String.valueOf(longitude));
                            mGPSLatView.setText(String.valueOf(latitude));
                        }
                    }
                }
                else {
                    Log.d("Network", "{LOGGING} Network Enabled false");
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    Log.d("isGPSEnabled", "{LOGGING} isGPSEnabled true");
                    if (location == null) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return null;
                        }
                        mLocationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                        if (mLocationManager != null) {
                            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                Log.d(String.valueOf(1000 * 60 * location.getLatitude()), "{LOGGING} location.getLatitude() ");
                                Log.d(String.valueOf(1000 * 60 * location.getLongitude()), "{LOGGING} location.getLatitude() ");
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                mGPSLonView.setText(String.valueOf(longitude));
                                mGPSLatView.setText(String.valueOf(latitude));
                            }
                        }
                    }
                }
                else {
                    Log.d("isGPSEnabled", "{LOGGING} isGPSEnabled false");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled provider " + provider, Toast.LENGTH_SHORT).show();
    }

    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Enabled provider " + provider, Toast.LENGTH_SHORT).show();
    }

    public void onLocationChanged(Location location) {
        Log.d(String.valueOf(1000 * 60 * location.getLatitude()), "{LOGGING} location.getLatitude() ");
        Log.d(String.valueOf(1000 * 60 * location.getLongitude()), "{LOGGING} location.getLatitude() ");
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        mGPSLonView.setText(String.valueOf(longitude));
        mGPSLatView.setText(String.valueOf(latitude));
    }

    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates((LocationListener) this);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Integer.parseInt(Manifest.permission.ACCESS_COARSE_LOCATION));

            } else {
                // permission has been granted, continue as usual
            }
        } catch (Exception e) {
            Log.e("ERROR", "ERROR IN CODE. ");
            e.printStackTrace();
        }
    }
}
