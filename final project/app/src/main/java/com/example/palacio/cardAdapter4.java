package com.example.palacio;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class cardAdapter4 extends BaseAdapter {
    private ArrayList<myDate> arrMyDate = new ArrayList<>();
    private ArrayList<String> arrAmount = new ArrayList<>();
    private ArrayList<String> arrOrderCode = new ArrayList<>();
    private Activity ctx;

    private String mypath = "gs://final-project-9dd22.appspot.com";

    public cardAdapter4(ArrayList<myDate> arrMyDate, ArrayList<String> arrAmount, ArrayList<String> arrOrderCode, Activity ctx) {
        this.arrMyDate = arrMyDate;
        this.arrAmount = arrAmount;
        this.arrOrderCode = arrOrderCode;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return arrMyDate.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater =ctx.getLayoutInflater();

        View myrow= inflater.inflate(R.layout.cardviewdetails4,null,true);

        TextView txtOrderDate = myrow.findViewById(R.id.txtorderdate);
        txtOrderDate.setText(this.arrMyDate.get(position).getDay()+"/"+this.arrMyDate.get(position).getMonth()+"/"+this.arrMyDate.get(position).getYear());


        TextView txtAmount = myrow.findViewById(R.id.txtorderitems);
        txtAmount.setText(this.arrAmount.get(position));

        TextView txtOrderCode = myrow.findViewById(R.id.txtordercode);
        txtOrderCode.setText(this.arrOrderCode.get(position));
        return myrow;
    }


}