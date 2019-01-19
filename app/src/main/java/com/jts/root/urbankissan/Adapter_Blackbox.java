package com.jts.root.urbankissan;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Adapter_Blackbox extends ArrayAdapter<String> {

    private Context context;
    String [] rc;
    String [] ec;
    String [] ppm;
    String [] temp;
    String [] wl;
    String [] ms;
    String [] light;

    public Adapter_Blackbox(Context context, String[] rcS,String[] ecS,String[] ppmS,String[] tempS,String[] wlS,String[] msS,String[] lightS) {
        super(context, R.layout.activity_urbankisssan__allvalues, rcS);
        this.rc = rcS;
        this.ec = ecS;
        this.ppm = ppmS;
        this.temp = tempS;
        this.wl = wlS;
        this.ms = msS;
        this.light = lightS;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Adapter_Blackbox.ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.activity_adapter__blackbox, null);
            viewHolder = new Adapter_Blackbox.ViewHolder();
            //viewHolder.Switch.setChecked(isChecked);

            viewHolder.rcT = (TextView) convertView.findViewById(R.id.rc);
            viewHolder.ecT = (TextView) convertView.findViewById(R.id.ec);
            viewHolder.ppmT = (TextView) convertView.findViewById(R.id.ppm);
            viewHolder.tempT = (TextView) convertView.findViewById(R.id.temp);
            viewHolder.wlT = (TextView) convertView.findViewById(R.id.wl);
            viewHolder.msT = (TextView) convertView.findViewById(R.id.ms);
            viewHolder.lightT = (TextView) convertView.findViewById(R.id.light);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Adapter_Blackbox.ViewHolder) convertView.getTag();
        }
        //final String temp = getItem(position);
        viewHolder.rcT.setText(rc[position]);
        viewHolder.ecT.setText(ec[position]);
        viewHolder.ppmT.setText(ppm[position]);
        viewHolder.tempT.setText(temp[position]);
        viewHolder.wlT.setText(wl[position]);
        viewHolder.msT.setText(ms[position]);
        viewHolder.lightT.setText(light[position]);



        return convertView;
    }

    public class ViewHolder {
        TextView rcT,ecT,ppmT,tempT,wlT,msT,lightT;
    }
}
