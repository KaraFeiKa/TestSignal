package es.neci_desarrollo.GpsTracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements LocListenerInterface {
    private LocationManager locationManager;
    private TextView latu;
    private TextView lonu;
    private TextView Ss;
    private MyLocListener myLocListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new mListener(), PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        init();

    }
private void init()
{
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocListener = new MyLocListener();
        myLocListener.setLocListenerInterface(this);
        checkPermissions();
        latu = findViewById(R.id.lacu2);
        lonu = findViewById(R.id.lacu);
        Ss = findViewById(R.id.dBm);

}


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==10 && grantResults[0] == RESULT_OK)
        {
            checkPermissions();
        }
    }

    private void checkPermissions()
{
    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
    {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET}, 10);
    }
    else
    {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2,1,myLocListener);
    }
}

    class mListener extends PhoneStateListener
    {
        public void onSignalStrengthsChanged(SignalStrength signalStrength)
        {
            if (signalStrength != null) {
                Ss.setText(String.valueOf(signalStrength.getCellSignalStrengths()));
            }
        }
    }

    @Override
    public void onLocationChanged(Location loc) {
        latu.setText(String.valueOf(loc.getLatitude()));
        lonu.setText(String.valueOf(loc.getLongitude()));
    }
}