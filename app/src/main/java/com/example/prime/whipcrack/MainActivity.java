package com.example.prime.whipcrack;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private  Sensor mAccelerometer;
    private final float Noice= (float) 7.0;
    private MediaPlayer player; //player = MediaPlayer.create(this,R.raw.whipcrack);
    private float mLastX, mLastY, mLastZ;
    private TextView textView ;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInitialized=false;
        player = MediaPlayer.create(this,R.raw.whipcrack);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        textView = (TextView)findViewById(R.id.textView2);
        if(textView==null)Log.d("","Not found");
        animation = AnimationUtils.loadAnimation(this, R.anim.animation);
        textView.startAnimation(animation);
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
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        if(!mInitialized)
        {
            mLastX=x;
            mLastY=y;
            mLastZ=z;

            mInitialized=true;
        }

        else
        {
            float deltax = Math.abs(mLastX-x);
            float deltay = Math.abs(mLastY-y);
            float deltaz = Math.abs(mLastZ-z);

            if(deltax<Noice) deltax=0;
            if(deltay<Noice) deltay=0;
            if(deltaz<Noice) deltaz=0;

            mLastX=x;
            mLastY=y;
            mLastZ=z;

            if(deltax>=Noice || deltay>=Noice || deltaz>=Noice)
            {
                //Play audio
                player.start();
                //Toast.makeText(MainActivity.this, "Hello...", Toast.LENGTH_SHORT).show();
                animation.cancel();
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
