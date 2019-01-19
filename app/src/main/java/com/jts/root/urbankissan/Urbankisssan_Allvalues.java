package com.jts.root.urbankissan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.jts.root.urbankissan.Mqttdata.server_ip;

public class Urbankisssan_Allvalues extends AppCompatActivity implements MqttCallback {

    ListView gb1lst, gb2lst, bblst;
    String[] cht1;
    String[] cht2;
    String[] cht3;
    String[] chh1;
    String[] chh2;
    String[] chh3;
    String[] chillc;

    String [] rc;
    String [] ec;
    String [] ppm;
    String [] temp;
    String [] wl;
    String [] ms;
    String [] light;

    private ProgressDialog dialog_progress;
    AlertDialog.Builder builderLoading;

    private static String TAG = "MQTT_android";
    String payload = "the payload";
    static MqttAndroidClient client;
    static MqttAndroidClient clientblockbox;

    MqttConnectOptions options = new MqttConnectOptions();
    static String serverpopS, topicpopS, server_ip;
    String serverip = "cld003.jts-prod.in";
     String topicblockbox="jts/urbanKisaan/V_0_0_1/Data/2s";
     String topicgrowbox = "/test/e2s/data";

    Activity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urbankisssan__allvalues);
        gb1lst = (ListView) findViewById(R.id.gb1);
        gb2lst = (ListView) findViewById(R.id.gb2);
        bblst = (ListView) findViewById(R.id.bb);
        mActivity= this;

        dialog_progress = new ProgressDialog(Urbankisssan_Allvalues.this);
        builderLoading = new AlertDialog.Builder(Urbankisssan_Allvalues.this);

        growbox1();
        //growbox2();
    }

    public void growbox1() {
        Log.d("growbox1 ", "in sub func ");
        //Bundle b = getIntent().getExtras();
        dialog_progress.setMessage("connecting ...");
        dialog_progress.show();

        String clientId = MqttClient.generateClientId();
        //topic = "jts/dtd/response";
        //String server_ip = "tcp://jtha.in:1883";
        server_ip = "tcp://" + serverip + ":1883";


        Log.d("growbox1 ", "subscribeScada");
        client = new MqttAndroidClient(this.getApplicationContext(), server_ip,
                clientId);

        Log.d("growbox1 ", "subscribeScada1");
        try {
            options.setUserName("esp");
            options.setPassword("ptlesp01".toCharArray());
            IMqttToken token = client.connect(options);
            Log.d("growbox1 ", "subscribeScada2");
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    //t.cancel();
                    Log.d("growbox1 ", "subscribeScada3");
                    client.setCallback(Urbankisssan_Allvalues.this);
                    int qos = 2;
                    try {
                        //IMqttToken subToken = client.subscribe(topicgrowbox, qos);
                        setSub();
                        Log.d("growbox1 ", "subscribeScada4");
/*
                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                // successfully subscribed
                                //tv.setText("Successfully subscribed to: " + topic);
                                Log.d("success", "came here");


                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                // The subscription could not be performed, maybe the user was not
                                // authorized to subscribe on the specified topic e.g. using wildcards
                                // Toast.makeText(MainActivity.this, "Couldn't subscribe to: " + topic, Toast.LENGTH_SHORT).show();
                                Log.d("failure", "came here");
                                //tv.setText("Couldn't subscribe to: " + topic);

                            }
                        });
*/
                        Log.d(TAG, "here we are");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Log.d("error", "2");
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Log.d(TAG, "onFailure");
        }
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    private void setSub()
    {
        try{

            client.subscribe(topicgrowbox,2);
            Log.d("topicgrowbox", "login" + topicgrowbox);

            client.subscribe(topicblockbox,2);
            Log.d("messege arriv", "topicblockbox" + topicblockbox);


        }
        catch (MqttException e){
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(final String topic, final MqttMessage message) throws Exception {
        Log.d("messege arriv", "login" + message);

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                JSONObject json = null;  //your response
                try {
                    json = new JSONObject(String.valueOf(message));
                    dialog_progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                List<String> cht1L = new ArrayList<String>();
                List<String> cht2L = new ArrayList<String>();
                List<String> cht3L = new ArrayList<String>();
                List<String> chh1L = new ArrayList<String>();
                List<String> chh2L = new ArrayList<String>();
                List<String> chh3L = new ArrayList<String>();
                List<String> chillcL = new ArrayList<String>();

                if (topic.equals(topicgrowbox)) {
                    Log.d("messege of grow bx ", "login : " + message);
                    try {
                        JSONObject object = new JSONObject(String.valueOf(message));

                        String chT1 = object.getString("chT1");
                        String chT2 = object.getString("chT2");
                        String chT3 = object.getString("chT3");
                        String chH1 = object.getString("chH1");
                        String chH2 = object.getString("chH2");
                        String chH3 = object.getString("chH3");
                        String chillC = object.getString("chillC");
                        Log.d("chT1 ", chT1);


                        cht1L.add(chT1);
                        cht2L.add(chT2);
                        cht3L.add(chT3);
                        chh1L.add(chH1);
                        chh2L.add(chH2);
                        chh3L.add(chH3);
                        chillcL.add(chillC);


                        cht1 = new String[cht1L.size()];
                        cht2 = new String[cht2L.size()];
                        cht3 = new String[cht3L.size()];
                        chh1 = new String[chh1L.size()];
                        chh2 = new String[chh2L.size()];
                        chh3 = new String[chh3L.size()];
                        chillc = new String[chillcL.size()];


                        for (int l = 0; l < chillcL.size(); l++)

                        {
                            cht1[l] = cht1L.get(l);
                            cht2[l] = cht2L.get(l);
                            cht3[l] = cht3L.get(l);
                            chh1[l] = chh1L.get(l);
                            chh2[l] = chh2L.get(l);
                            chh3[l] = chh3L.get(l);
                            chillc[l] = chillcL.get(l);

                            Log.d("cht1 ", cht1[l]);
                            Log.d("cht2 ", cht2[l]);
                            Log.d("cht3 ", cht3[l]);
                            Log.d("chh1 ", chh1[l]);
                            Log.d("chh2 ", chh2[l]);
                            Log.d("chh3 ", chh3[l]);
                            Log.d("chillc ", chillc[l]);

                        }

                        Adapter_growbox1 growbox1 = new Adapter_growbox1(Urbankisssan_Allvalues.this, cht1, cht2, cht3, chh1, chh2, chh3, chillc);
                        gb1lst.setAdapter(growbox1);


                        Adapter_growbox1 growbox2 = new Adapter_growbox1(Urbankisssan_Allvalues.this, cht1, cht2, cht3, chh1, chh2, chh3, chillc);
                        gb2lst.setAdapter(growbox2);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                else{
                    List<String> rcL = new ArrayList<String>();
                    List<String> ecL = new ArrayList<String>();
                    List<String> ppmL = new ArrayList<String>();
                    List<String> tempL = new ArrayList<String>();
                    List<String> wlL = new ArrayList<String>();
                    List<String> msL = new ArrayList<String>();
                    List<String> lightL = new ArrayList<String>();

                    Log.d("messege of blk bx ", "login 1 - : " + message);

                    JSONObject Data = null;
                    try {
                        Data = json.getJSONObject("Data");
                        String rcS = Data.getString("rc");
                        String ecS = Data.getString("ec");
                        String ppmS = Data.getString("ppm");
                        String tempS = Data.getString("temp");
                        String wlS = Data.getString("wl");
                        String msS = Data.getString("ms");
                        String lightS = Data.getString("light");
                        Log.d("ec ", ecS);


                        rcL.add(rcS);
                        ecL.add(ecS);
                        ppmL.add(ppmS);
                        tempL.add(tempS);
                        wlL.add(wlS);
                        msL.add(msS);
                        lightL.add(lightS);


                        rc = new String[rcL.size()];
                        ec = new String[ecL.size()];
                        ppm = new String[ppmL.size()];
                        temp = new String[tempL.size()];
                        wl = new String[wlL.size()];
                        ms = new String[msL.size()];
                        light = new String[lightL.size()];


                        for (int l = 0; l < lightL.size(); l++)

                        {
                            rc[l] = rcL.get(l);
                            ec[l] = ecL.get(l);
                            ppm[l] = ppmL.get(l);
                            temp[l] = tempL.get(l);
                            wl[l] = wlL.get(l);
                            ms[l] = msL.get(l);
                            light[l] = lightL.get(l);

                            Log.d("rc ", rc[l]);
                            Log.d("ec ", ec[l]);
                            Log.d("ppm ", ppm[l]);
                            Log.d("temp ", temp[l]);
                            Log.d("wl ", wl[l]);
                            Log.d("ms ", ms[l]);
                            Log.d("light ", light[l]);

                        }

                        Adapter_Blackbox blockbox = new Adapter_Blackbox(Urbankisssan_Allvalues.this, rc, ec, ppm, temp, wl, ms, light);
                        bblst.setAdapter(blockbox);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }


}
