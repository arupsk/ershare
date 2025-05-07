package com.Andriod.ER.com;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;

import com.Andriod.ER.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

// refrence: https://github.com/jjoe64/GraphView/wiki/Realtime-chart
public class HistoryActivity extends Activity {
    private BluetoothLeService mBluetoothLeService;
    private MainActivity main;
    boolean voltpressed = false;
    boolean ampspressed = false;
    public float volts = (float) 00.0;
    public float amps = (float) 00.0;

    public  byte line1[]  = {(byte) 0xDD, 3, 0, 0x1B, 5, 23, 0, 0, 9, 45, 28, 0x5A, 0, 1, 29, 33, 0, 0, 0, 0, 0, 0, 17, 17, 3, 4, 2, 0xB, 26, 0xB, 0x2F, (byte) 0xFD, (byte) 0xEE, 77};
    public  byte line2[]  = {(byte) 0xDD, 3, 0, 0x1B, 5, 23, 0, 0, 9, 45, 28, 0x5A, 0, 1, 29, 33, 0, 0, 0, 0, 0, 0, 17, 17, 3, 4, 2, 0xB, 26, 0xB, 0x2F, (byte) 0xFD, (byte) 0xEE, 77};
    public  byte line3[]  = {(byte) 0xDD, 3, 0, 0x1B, 5, 23, 0, 0, 9, 45, 28, 0x5A, 0, 1, 29, 33, 0, 0, 0, 0, 0, 0, 17, 17, 3, 4, 2, 0xB, 26, 0xB, 0x2F, (byte) 0xFD, (byte) 0xEE, 77};
    public  byte line4[]  = {(byte) 0xDD, 3, 0, 0x1B, 5, 23, 0, 0, 9, 45, 28, 0x5A, 0, 1, 29, 33, 0, 0, 0, 0, 0, 0, 17, 17, 3, 4, 2, 0xB, 26, 0xB, 0x2F, (byte) 0xFD, (byte) 0xEE, 77};
    public  byte line5[]  = {(byte) 0xDD, 3, 0, 0x1B, 5, 23, 0, 0, 9, 45, 28, 0x5A, 0, 1, 29, 33, 0, 0, 0, 0, 0, 0, 17, 17, 3, 4, 2, 0xB, 26, 0xB, 0x2F, (byte) 0xFD, (byte) 0xEE, 77};
    public  byte line6[]  = {(byte) 0xDD, 3, 0, 0x1B, 5, 23, 0, 0, 9, 45, 28, 0x5A, 0, 1, 29, 33, 0, 0, 0, 0, 0, 0, 17, 17, 3, 4, 2, 0xB, 26, 0xB, 0x2F, (byte) 0xFD, (byte) 0xEE, 77};
    public  byte line7[]  = {(byte) 0xDD, 3, 0, 0x1B, 5, 23, 0, 0, 9, 45, 28, 0x5A, 0, 1, 29, 33, 0, 0, 0, 0, 0, 0, 17, 17, 3, 4, 2, 0xB, 26, 0xB, 0x2F, (byte) 0xFD, (byte) 0xEE, 77};
    public String[] cellenNamen = {"0"};
    public boolean HistoryWanted = false;
    public boolean connected = false;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        GraphView graph = (GraphView) findViewById(R.id.graph);
       // activate horizontal zooming and scrolling
        graph.getViewport().setScalable(true);
       // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScalableY(true);
        // activate vertical scrolling
       // graph.getViewport().setScrollableY(true);

        // styling
      /*  series.setDrawDataPoints(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });*/
    // draw values on top
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        HistoryWanted = true;
        startThread();
        //series.setValuesOnTopSize(50);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onResume() {
        super.onResume();
        HistoryWanted = true;
        startThread();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onPause() {
        super.onPause();
        HistoryWanted = false;
//        stopThread();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        HistoryWanted = false;
      //  stopThread();
    }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void setSlipper()
    {
        int Aantalcellen = mBluetoothLeService.AantalCellen;
        for(int x = 0; x < Aantalcellen/2; x = x + 1) {
            cellenNamen[x] = "Cel" + (x + 1);
        }
        cellenNamen[Aantalcellen/2] = "All";
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = null;
        //adapter.addAll(cellenNamen);
// Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
      //  spinner.setAdapter(adapter);

    }
    //button back
    public void Back(View view) {
        // Do something in response to button
        Intent i = new Intent(HistoryActivity.this, MainActivity.class);
        startActivity(i);
    }

    //button volt
    public void ShowVolt(View view) {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series;
        float volts1 = ((line1[4] & 0xFF) << 8 | (line1[5] & 0xFF));
        float volts2 = ((line1[4] & 0xFF) << 8 | (line1[5] & 0xFF));
        float volts3 = ((line1[4] & 0xFF) << 8 | (line1[5] & 0xFF));
        float volts4 = ((line1[4] & 0xFF) << 8 | (line1[5] & 0xFF));
        float volts5 = ((line1[4] & 0xFF) << 8 | (line1[5] & 0xFF));
        float volts6 = ((line1[4] & 0xFF) << 8 | (line1[5] & 0xFF));
        float volts7 = ((line1[4] & 0xFF) << 8 | (line1[5] & 0xFF));


        series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0,  volts1 / 100),
                new DataPoint(1, volts2 / 100),
                new DataPoint(2, volts3 / 100),
                new DataPoint(3, volts4 / 100),
                new DataPoint(4, volts5 / 100),
                new DataPoint(5, volts6 / 100),
                new DataPoint(6, volts7 / 100)
        });
        if(voltpressed)
        {
            //graph.removeSeries(series);
            graph.removeAllSeries();
            voltpressed = false;
        }
        else {
            voltpressed = true;
        graph.addSeries(series);
        series.setDrawDataPoints(true);
        series.setColor(Color.RED);
        series.setTitle("V");
        }
    }

    //button amps
    public void ShowAmps(View view) {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        float amps1 = ((line1[6] & 0xFF) << 8 | (line1[7] & 0xFF));
        float amps2 = ((line1[6] & 0xFF) << 8 | (line1[7] & 0xFF));
        float amps3 = ((line1[6] & 0xFF) << 8 | (line1[7] & 0xFF));
        float amps4 = ((line1[6] & 0xFF) << 8 | (line1[7] & 0xFF));
        float amps5 = ((line1[6] & 0xFF) << 8 | (line1[7] & 0xFF));
        float amps6 = ((line1[6] & 0xFF) << 8 | (line1[7] & 0xFF));
        float amps7 = ((line1[6] & 0xFF) << 8 | (line1[7] & 0xFF));


        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, amps1),
                new DataPoint(1, amps2),
                new DataPoint(2, amps3),
                new DataPoint(3, amps4),
                new DataPoint(4, amps5),
                new DataPoint(5, amps6),
                new DataPoint(6, amps7)
        });
        if(ampspressed)
        {
            //graph.removeSeries(series2);
            graph.removeAllSeries();
            ampspressed = false;
        }
        else {
            ampspressed = true;
            graph.addSeries(series2);
            //series2.setDrawDataPoints(true);
            series2.setTitle("A");
        }
    }

    public void startThread()
    {
        HistoryActivity.MyThread thread = new HistoryActivity.MyThread();
        thread.start();
    }
    public void stopThread()
    {
        HistoryActivity.MyThread thread = new HistoryActivity.MyThread();
        thread.stop();
    }


    public class MyThread extends Thread {


        MyThread()
        {

        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run(){
            while (true)
            {

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                       setSlipper();

            }
        }
    }

}