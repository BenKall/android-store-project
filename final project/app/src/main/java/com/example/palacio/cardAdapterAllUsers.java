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



    public class cardAdapterAllUsers extends BaseAdapter {

        private ArrayList<String> arrPicNames = new ArrayList<>();
        private ArrayList<String> arrEmails = new ArrayList<>();
        private ArrayList<String> arrPhones = new ArrayList<>();
        private ProgressDialog prg;
        private Activity ctx;

        private String mypath = "gs://final-project-9dd22.appspot.com";

        public cardAdapterAllUsers(ArrayList<String> arrpicnames, ArrayList<String> arrtitles, ArrayList<String> arrmessages, ProgressDialog prg, Activity ctx) {
            this.arrPicNames = arrpicnames;
            this.arrEmails = arrtitles;
            this.arrPhones = arrmessages;
            this.prg = prg;
            this.ctx = ctx;
        }



        @Override
        public int getCount() {
            return arrEmails.size();
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

            View myrow = inflater.inflate(R.layout.cardviewdetailsallusers, null, true);

            TextView txtemail = myrow.findViewById(R.id.txtemail);
            txtemail.setText(this.arrEmails.get(position));

            TextView txtphone = myrow.findViewById(R.id.txtphone);
            txtphone.setText(this.arrPhones.get(position));

            final ImageView imgurUser = myrow.findViewById(R.id.imguruser);


            String suffix = arrPicNames.get(position).substring(arrPicNames.get(position).lastIndexOf(".") + 1);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(mypath).child("pics").child(arrPicNames.get(position));
            try {
                final File localFile = File.createTempFile(arrPicNames.get(position), suffix);
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        imgurUser.setImageBitmap(bitmap);
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

