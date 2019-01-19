package com.jts.root.urbankissan;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class Adapter_growbox1 extends ArrayAdapter<String> {


    private Context context;
    String [] cht1;
    String [] cht2;
    String [] cht3;
    String [] chh1;
    String [] chh2;
    String [] chh3;
    String [] chillc;

    public Adapter_growbox1(Context context, String[] cht1S,String[] cht2S,String[] cht3S,String[] chh1S,String[] chh2S,String[] chh3S,String[] chillcS) {
        super(context, R.layout.activity_urbankisssan__allvalues, cht1S);
        this.cht1 = cht1S;
        this.cht2 = cht2S;
        this.cht3 = cht3S;
        this.chh1 = chh1S;
        this.chh2 = chh2S;
        this.chh3 = chh3S;
        this.chillc = chillcS;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.activity_adapter_growbox1, null);
            viewHolder = new ViewHolder();
            //viewHolder.Switch.setChecked(isChecked);

            viewHolder.cht1T = (TextView) convertView.findViewById(R.id.cht1);
            viewHolder.cht2T = (TextView) convertView.findViewById(R.id.cht2);
            viewHolder.cht3T = (TextView) convertView.findViewById(R.id.cht3);
            viewHolder.chh1T = (TextView) convertView.findViewById(R.id.chh1);
            viewHolder.chh2T = (TextView) convertView.findViewById(R.id.chh2);
            viewHolder.chh3 = (TextView) convertView.findViewById(R.id.chh3);
            viewHolder.chillcT = (TextView) convertView.findViewById(R.id.chillc);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String temp = getItem(position);
        viewHolder.cht1T.setText(cht1[position]);
        viewHolder.cht2T.setText(cht2[position]);
        viewHolder.cht3T.setText(cht3[position]);
        viewHolder.chh1T.setText(chh1[position]);
        viewHolder.chh2T.setText(chh2[position]);
        viewHolder.chh3.setText(chh3[position]);
        viewHolder.chillcT.setText(chillc[position]);



        return convertView;
    }

    public class ViewHolder {
        TextView cht1T,cht2T,cht3T,chh1T,chh2T,chh3,chillcT;
    }

}
