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

public class cardAdapter2 extends BaseAdapter {
    private ArrayList<Uri> arrdata = new ArrayList<>();
    private ArrayList<String> arrnumber = new ArrayList<>();
    private Activity ctx;

    public cardAdapter2(ArrayList<Uri> arrdata, ArrayList<String> arrnumber, Activity ctx) {
        this.arrdata = arrdata;
        this.arrnumber = arrnumber;
        this.ctx = ctx;
    }



    @Override
    public int getCount() {
        return arrdata.size();
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

        View myrow= inflater.inflate(R.layout.cardviewdetails2,null,true);

        TextView txtnumber = myrow.findViewById(R.id.txtnumber);
        txtnumber.setText(this.arrnumber.get(position));


        ImageView img=myrow.findViewById(R.id.proimg);
        img.setImageURI(arrdata.get(position));
        return myrow;
    }
}
