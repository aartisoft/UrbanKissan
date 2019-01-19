package com.jts.root.urbankissan;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class Mqttdata extends AppCompatActivity implements MqttCallback {
    TextView rc,Ec,temp,wl,light,ppm,ms,motorid;
    Button settings;
    private ProgressDialog dialog_progress ;
    AlertDialog.Builder builderLoading;

    private static String TAG = "MQTT_android";
    String payload = "the payload";
    static MqttAndroidClient client;
    MqttConnectOptions options = new MqttConnectOptions();
    static String serverpopS,topicpopS,topic,server_ip;
    String serverip="cld003.jts-prod.in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqttdata);
        Ec=(TextView)findViewById(R.id.mcid);
        wl=(TextView)findViewById(R.id.wlid);
        light=(TextView)findViewById(R.id.lightid);
        motorid=(TextView)findViewById(R.id.motorid);

       /* rc=(TextView)findViewById(R.id.rcid);
        temp=(TextView)findViewById(R.id.tempid);
        ppm=(TextView)findViewById(R.id.ppmid);
        ms=(TextView)findViewById(R.id.msid);*/
        settings=(Button) findViewById(R.id.settings);

        dialog_progress = new ProgressDialog(Mqttdata.this);
        builderLoading = new AlertDialog.Builder(Mqttdata.this);

        subscribe_scada();

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings_popup();
            }
        });
    }
    public void subscribe_scada() {
        Log.d("Enetered ", "in sub func ");
        //Bundle b = getIntent().getExtras();
        dialog_progress.setMessage("connecting ...");
        dialog_progress.show();

        String clientId = MqttClient.generateClientId();
        //topic = "jts/dtd/response";
        //String server_ip = "tcp://jtha.in:1883";
        if (topicpopS!=null){
            topic=topicpopS;
            server_ip=serverpopS;

        }else {
            topic="jts/urbanKisaan/V_0_0_1/2s";
             server_ip = "tcp://"+serverip+":1883";

        }

        Log.d("Enetered ", "subscribeScada");
        client = new MqttAndroidClient(this.getApplicationContext(), server_ip,
                clientId);

        Log.d("Enetered ", "subscribeScada1");
        try {
            options.setUserName("esp");
            options.setPassword("ptlesp01".toCharArray());
            IMqttToken token = client.connect(options);
            Log.d("Enetered ", "subscribeScada2");
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    //t.cancel();
                    Log.d("Enetered ", "subscribeScada3");
                    client.setCallback(Mqttdata.this);
                    int qos = 2;
                    try {
                        IMqttToken subToken = client.subscribe(topic, qos);
                        Log.d("Enetered ", "subscribeScada4");
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
                        Log.d(TAG, "here we are");
                    } catch (MqttException e) {
                        e.printStackTrace();
                        Log.d("error", "!");
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
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        Log.d("messege arriv", "login"+message);

        JSONObject json = null;  //your response
        try {
            json = new JSONObject(String.valueOf(message));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject object = json.getJSONObject("Data");
        String rcS = object.getString("rc");
        String ecS = object.getString("ec");
        String ppmS = object.getString("ppm");
        String tempS = object.getString("temp");
        String wlS = object.getString("wl");
        String msS = object.getString("ms");
        String lightS = object.getString("light");

        Ec.setText(ecS);
        wl.setText(wlS);
        light.setText(lightS);
        motorid.setText(msS);

        /*ppm.setText(ppmS);
        temp.setText(tempS);
        ms.setText(msS);
        rc.setText(rcS);*/

        String topicjson = json.getString("topic");

        String dm = json.getString("dm");
        Log.d("login_accesstokenS::", "jsonresponse errcode:::" + dm);

        if (dm.contentEquals("Server")) {
            // Toast.makeText(getApplicationContext(), "Response=successfully ", Toast.LENGTH_LONG).show();
            //client.unsubscribe(topic);
           // client.disconnect();
            dialog_progress.dismiss();
            /*Intent intent = new Intent(Log.this, Device_list.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/
        } else {
            // Toast.makeText(getApplicationContext(), "Not added the notes", Toast.LENGTH_LONG).show();

            Log.d("login_StateS", "" + dm);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Mqttdata.this);
            builder.setMessage("Reasponse=Failed to login");

            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface1, int i) {

                    dialogInterface1.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
    public void settings_popup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Mqttdata.this);
        LayoutInflater inflater = (LayoutInflater) Mqttdata.this.getSystemService(getApplication().LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.popupback,
                null);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setView(dialogLayout, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        WindowManager.LayoutParams wlmp = dialog.getWindow()
                .getAttributes();
        wlmp.gravity = Gravity.CENTER;


      /*  final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setTitle("please enter room names");
        dialog.setContentView(R.layout.update_popup_forecast);*/

        Button ok = (Button) dialogLayout.findViewById(R.id.okpopbtn);
        final EditText topicE = (EditText) dialogLayout.findViewById(R.id.topicid);
        final EditText serverE = (EditText) dialogLayout.findViewById(R.id.serverid);

        //textnotesT.setMovementMethod(new ScrollingMovementMethod());
        //textnotesT.setText(notes);

        builder.setView(dialogLayout);
        topicE.setText(topic);
        serverE.setText(serverip);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //delgroup(grpnm);
                topicpopS = topicE.getText().toString();
                serverpopS = serverE.getText().toString();
                Intent intent = new Intent(Mqttdata.this, Mqttdata.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                // dialog.dismiss();
            }
        });

        dialog.show();
    }



}
