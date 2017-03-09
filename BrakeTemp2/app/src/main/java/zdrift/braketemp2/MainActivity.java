package zdrift.braketemp2;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;
import android.os.Bundle;

import android.location.LocationListener;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.content.Context;
import android.os.Bundle;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.support.v4.content.PermissionChecker;
import android.view.View;

import zdrift.braketemp2.Brake;

import org.w3c.dom.Text;

import android.os.Handler;
import android.widget.ToggleButton;

import java.lang.Math;
import java.text.NumberFormat;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    TextView txt_speed;
    TextView txt_accuracy;
    TextView txt_gforce_x;
    TextView txt_gforce_y;
    TextView txt_gforce_z;
    TextView txt_gforce_sum;
    TextView txt_vehicleweight;
    TextView txt_ekin;
    TextView txt_delta_speed;
    TextView txt_status;
    TextView txt_delta_Temp;
    TextView txt_absolute_temp;
    TextView in_sim_speed;

    Button but_calibrate;
    ToggleButton but_sim_speed;
    float x,y,z,sum;
    float xd,yd,zd,sumd;

    private int mInterval = 1000; //timer
    private Handler mHandler;
    Track mTrack;
    Vehicle mVehicle;
    Brake mBrake;
    ThermalModel mThermalModel;
    StateObserver mStateObserver;

    double speed_old;

    long time=0;

    //active model test sequence
    int TEST=0;

    //FIXME setText causes exeption ??? _> Exception handling !!! TODO


    @Override
    protected void onCreate(Bundle savedInstanceState) {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            mHandler = new Handler();
            mTrack = new Track(20,"RedBullRing");

            //Create stock Z4 Brake
            //the first test show that calculation has shows temp 100x to high

            mBrake = new Brake(20.0,10.0,460.0,0.300,0.079,0.022,false);
            //Z4 E85
            mVehicle = new Vehicle(1290,0.8,30,0.3,mBrake,20.0,0.8);
            mStateObserver = new StateObserver(1,0.1,1);
            mThermalModel = new ThermalModel();

            startRepeatingTask();

            txt_speed = (TextView) findViewById(R.id.display_speed);
            txt_accuracy = (TextView) findViewById(R.id.display_sea_level);
            txt_gforce_x = (TextView) findViewById(R.id.txt_force_x);
            txt_gforce_y = (TextView) findViewById(R.id.txt_force_y);
            txt_gforce_z = (TextView) findViewById(R.id.txt_force_z);
            txt_gforce_sum = (TextView) findViewById(R.id.txt_gforce_sum);
            but_calibrate = (Button) findViewById(R.id.but_calib);
            txt_vehicleweight = (TextView) findViewById(R.id.txtVehicleWeight);
            txt_ekin = (TextView) findViewById(R.id.txt_ek);
            txt_delta_speed = (TextView) findViewById(R.id.txt_delta_speed);
            txt_status = (TextView) findViewById(R.id.txt_status);
            txt_delta_Temp = (TextView) findViewById(R.id.txt_deltaT);
            txt_absolute_temp = (TextView) findViewById(R.id.txt_temp1);
            in_sim_speed = (TextView) findViewById(R.id.input_sim_speed);

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                location.getLatitude();
                try {

                    //only for testing simulate the speed

                          if(TEST!=1) {
                              mVehicle.speed = location.getSpeed();
                          }



                    txt_speed.setText("" + String.format("%.2f", mVehicle.speed));
                    // txt_ekin.setText(""+ String.format("%.2f",mPhysics.fct_Ekin(mVehicle.speed,mVehicle.mass)));
                    txt_accuracy.setText("" + String.format("%.2f", location.getAccuracy()));
                }catch(Exception e){
                    Log.e(TAG,"speed and accuracy - exception");
                   //txt_speed.setText("error calc");
                    // txt_accuracy.setText("error");
                }
            }

            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }


        };

// Register the listener with the Location Manager to receive location updates

        if(checkPermission()==true) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Log.d(TAG,"check permission ok");
        }else{
            Log.d(TAG,"check permission failed");
        }



        }


    private boolean checkPermission() { //context or this for chekc permission ???
        /*
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
        */
        int result = PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(result == PermissionChecker.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }





    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
               // updateStatus(); //this function can change value of mInterval.
                //Log.d(TAG,"update status status checker");


            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception

                    //Log.d(TAG, "timer delta T");

                    //call state observer

                /*
                    if (mStateObserver.fct_BrakeActive(mVehicle.speed)) {
                        txt_status.setText("Brake active !!!");
                    } else {
                        txt_status.setText("");
                    }
                    */


                    double disp_temp=0;


                if(TEST==1) {
                    time = time + mInterval;

                    //test run for verification 3x braking within 50s from 30m/s to 5m/s
                    if (time < 5000) {
                        mVehicle.speed = 0;
                    } else if (time < 10000) {
                        mVehicle.speed = 30;
                    } else if (time < 15000) {
                        mVehicle.speed = 5;
                    } else if (time < 25000) {
                        mVehicle.speed = 30;
                    } else if (time < 30000) {
                        mVehicle.speed = 5;
                    } else if (time < 40000) {
                        mVehicle.speed = 30;
                    } else if (time < 45000) {
                        mVehicle.speed = 5;
                    }

                }





                    try {
                        disp_temp = mThermalModel.fct_calc_Temp(mVehicle);
                        Log.d("v speed:",""+mVehicle.speed);
                        Log.d("disp_temp",""+disp_temp);
                        txt_absolute_temp.setText("T1:"+disp_temp+"°C");
                        Log.d("sim temp",""+in_sim_speed.getText());


                        if (disp_temp > 500) {
                            txt_absolute_temp.setTextColor(getResources().getColor(R.color.colorRed));
                        } else {
                            txt_absolute_temp.setTextColor(getResources().getColor(R.color.colorBlue));
                        }


                    }catch (Exception e){
                        Log.d("exception","disp_temp");
                    }

                    if(disp_temp >0) {
                        //FIXME set text cause exeption
                        //txt_delta_speed.setText("" + disp_temp);



                    }else
                    {
                      //  txt_delta_Temp.setText("ERROR CALC");
                    }

                    // txt_speed.setText("" + String.format("%.2f",mVehicle.speed));

            mHandler.postDelayed(mStatusChecker, mInterval);

            //Toast.makeText(MainActivity.this, "EXCEPTION cnt:" + errorcnt,
             //       Toast.LENGTH_SHORT).show();
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }


}
