package com.example.richu.locationtacker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.richu.locationtacker.other.MyService;
import com.example.richu.locationtacker.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;

public class Loginpage extends AppCompatActivity {


    EditText email,password;
    CardView login,cardalertloginpage,cardalertclick;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    DatabaseReference mdatabase;
    String  userid,emailuser,phone,name,profilepicuniquid;
    SharedPreferences .Editor edit ;
    TextView signuphere,cartalerttext;
    Uri firebaseimageuri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        login = (CardView)findViewById(R.id.login);
        cardalertloginpage = (CardView)findViewById(R.id.cardalertloginpage);
        cardalertclick = (CardView)findViewById(R.id.cardalertclick);
        edit = getApplicationContext().getSharedPreferences("Mainshared",0).edit();
        cartalerttext = (TextView)findViewById(R.id.cartalerttext);
        signuphere = (TextView)findViewById(R.id.signuphere);
        mdatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
                signuphere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Signup_class.class);
                startActivity(i);
            }
        });

                 login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(email.getText().toString().equals("")||password.getText().toString().equals("")){

                alertdialog("Empty fields","Please fill all fields");
            }
            else{

                //authenticate user
                auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(Loginpage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.getText().toString().length() < 6) {
                                        password.setError("Password too short");
                                    } else {
                                        alertdialog("Alert",task.getException().getMessage());

                                    }
                                } else {

                                    new valueclass().execute();


                                    Utils util = new Utils();
                                    util.progressdialog(Loginpage.this);
//                                    cardalertshow("sds",cardalertloginpage,cartalerttext,cardalertclick);
                                    SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("login",0).edit();
                                    editor.putBoolean("loginremember",true);
                                    editor.commit();


                                }
                            }
                        });

            }

        }
    });

    }

public void getvalues(){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    final String uid = user.getUid();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference fddb = database.getReference("Users").child(uid);
                    fddb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Object> list = new ArrayList<>();

                    //        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                DataSnapshot DSname = dataSnapshot.child("name");
                                DataSnapshot DSemail = dataSnapshot.child("Username");
                                DataSnapshot DSphone = dataSnapshot.child("phonenumber");
                                DataSnapshot DSuserid = dataSnapshot.child("UserID");
                                DataSnapshot DSprofilepicuniqueid = dataSnapshot.child("profilepic");

                    //            list.add(childDataSnapshot.getValue().toString());
                                name = String.valueOf(DSname.getValue());
                                emailuser = String.valueOf(DSemail.getValue());
                                phone = String.valueOf(DSphone.getValue());
                                userid = String.valueOf(DSuserid.getValue());
                                profilepicuniquid=String.valueOf(DSprofilepicuniqueid.getValue());

                                 downloadFile();


                                edit.putString("Name",name);
                                edit.putString("Email",emailuser);
                                edit.putString("Phone",phone);
                                edit.putString("Useid",userid);
                                edit.commit();
                                Log.e("List",""+list);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    } );



}


    
    private void downloadFile() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://testproject-37c18.appspot.com");
        StorageReference  islandRef = storageRef.child(profilepicuniquid+".jpg");
        storage.getReference().child(profilepicuniquid+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                firebaseimageuri = uri;
                edit.putString("Uri",firebaseimageuri.toString());
                edit.commit();

                Log.e("URI",""+uri);

            }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors


                }
            });

    }




    public void alertdialog(String s1,String s2)
    {
        new LovelyStandardDialog(Loginpage.this)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setButtonsColorRes(R.color.colorPrimaryDark)
                .setTitle(s1)
                .setMessage(s2)
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })

                .show();
    }

    class valueclass extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {
            getvalues();

        return "success";
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            startService(new Intent(getBaseContext(), MyService.class));
            Intent intent = new Intent(Loginpage.this, Homepage.class);

            startActivity(intent);
            finish();
        }


    }




}
