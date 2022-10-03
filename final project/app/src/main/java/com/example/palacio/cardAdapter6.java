package com.example.palacio;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class cardAdapter6 extends BaseAdapter {

    private ArrayList<String> arrPicNames = new ArrayList<>();
    private ArrayList<String> arrTitles = new ArrayList<>();
    private ArrayList<String> arrPrices = new ArrayList<>();
    private ArrayList<String> arrAmount = new ArrayList<>();
    private ProgressDialog prg;
    private Activity ctx;

    private String mypath = "gs://final-project-9dd22.appspot.com";

    public cardAdapter6(ArrayList<String> arrPicNames, ArrayList<String> arrTitles, ArrayList<String> arrPrices, ArrayList<String> arrAmount, ProgressDialog prg, Activity ctx) {
        this.arrPicNames = arrPicNames;
        this.arrTitles = arrTitles;
        this.arrPrices = arrPrices;
        this.arrAmount = arrAmount;
        this.prg = prg;
        this.ctx = ctx;
    }


    @Override
    public int getCount() {
        return arrTitles.size();
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
        prg.show();
        LayoutInflater inflater = ctx.getLayoutInflater();

        View myrow = inflater.inflate(R.layout.cardviewdetails6, null, true);

        TextView txtproname = myrow.findViewById(R.id.txtproname);
        txtproname.setText(this.arrTitles.get(position));

        TextView txtproprice = myrow.findViewById(R.id.txtproprice);
        txtproprice.setText(this.arrPrices.get(position));

        TextView txtproamount = myrow.findViewById(R.id.txtproamount);
        txtproamount.setText(this.arrAmount.get(position));

        final ImageView imgur = myrow.findViewById(R.id.imgur);


        String suffix = arrPicNames.get(position).substring(arrPicNames.get(position).lastIndexOf(".") + 1);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(mypath).child("stockpics").child(arrPicNames.get(position));
        try {
            final File localFile = File.createTempFile(arrPicNames.get(position), suffix);
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imgur.setImageBitmap(bitmap);
                    prg.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    prg.dismiss();

                    String message = exception.getMessage();
                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }

        return myrow;
    }

}
