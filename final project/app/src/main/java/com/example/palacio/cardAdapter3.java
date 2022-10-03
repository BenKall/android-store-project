package com.example.palacio;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class cardAdapter3 extends BaseAdapter {
    private ArrayList<String> arrPPU = new ArrayList<>();
    private ArrayList<String> arrAmount = new ArrayList<>();
    private ArrayList<String> arrOrderCode = new ArrayList<>();
    private Activity ctx;

    private String mypath = "gs://final-project-9dd22.appspot.com";

    public cardAdapter3(ArrayList<String> arrPPU, ArrayList<String> arrAmount, ArrayList<String> arrOrderCode, Activity ctx) {
        this.arrPPU = arrPPU;
        this.arrAmount = arrAmount;
        this.arrOrderCode = arrOrderCode;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return arrPPU.size();
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

        View myrow= inflater.inflate(R.layout.cardviewdetails3,null,true);

        TextView txtOrderPPU = myrow.findViewById(R.id.txtorderPPU);
        txtOrderPPU.setText(this.arrPPU.get(position));


        TextView txtItems = myrow.findViewById(R.id.txtorderitems);
        txtItems.setText(this.arrAmount.get(position));

        TextView txtOrderCode = myrow.findViewById(R.id.txtordercode);
        txtOrderCode.setText(this.arrOrderCode.get(position));
        return myrow;
    }


}
