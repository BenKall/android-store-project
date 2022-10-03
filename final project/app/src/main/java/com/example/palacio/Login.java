package com.example.palacio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private Button btns,btnr;
    private EditText edite,editp;
    private ProgressDialog prg=null;
    //---------------------------------------
    private FirebaseAuth auth=null;
    private FirebaseDatabase db=FirebaseDatabase.getInstance();
    private DatabaseReference reference=null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnr=findViewById(R.id.btnr);
        btns=findViewById(R.id.btns);

        edite=findViewById(R.id.edite);
        editp=findViewById(R.id.editp);

        auth=FirebaseAuth.getInstance();
        reference=db.getReference("User");

        prg=new ProgressDialog(Login.this);
        prg.setTitle("Logging in...");
        prg.setMessage("Have a great day!");
        prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prg.setCancelable(false);
        prg.setIcon(R.drawable.unlock);

        btnr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

        btns.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                prg.show();
                final String email=edite.getText().toString();
                final String pass=editp.getText().toString();

                auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            Client.setUid(auth.getUid());

                            reference.child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {
                                    Client.setMyuser(dataSnapshot.getValue(User.class));
                                    prg.dismiss();
                                    startActivity(new Intent(Login.this,loading1.class));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError)
                                {

                                }
                            });
                        }
                        prg.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        prg.dismiss();
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
