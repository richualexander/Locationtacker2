package com.example.richu.locationtacker;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.richu.locationtacker.other.RoundedImageView;
import com.example.richu.locationtacker.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class Signup_class extends AppCompatActivity {
    EditText emailsignup, passwordsignup, namesignup, phonesighnup;
    CardView signup, cardalert, cardalertclick;
    TextView alreadyuser, signupcardtext;
    LinearLayout signupfullview;
    RoundedImageView signupprofile;
    String identifier;
    private static final int SELECT_PICTURE = 100;
    private static final int CAMERA_REQUEST = 200;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    boolean selectedImage;
    Uri selectedImageURI,CameraURI;
    Bitmap photo;
    String image = "",profileuniqueid;
    private DatabaseReference mDatabase;
    byte[] inputData;
    byte[] byteArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_class);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        emailsignup = (EditText) findViewById(R.id.emailsignup);
        namesignup = (EditText) findViewById(R.id.namesignup);
        phonesighnup = (EditText) findViewById(R.id.phonesighnup);

        signupprofile = (RoundedImageView) findViewById(R.id.signupprofile);
        passwordsignup = (EditText) findViewById(R.id.passwordsignup);
        signup = (CardView) findViewById(R.id.signup);
        cardalert = (CardView) findViewById(R.id.cardalert);
        cardalertclick = (CardView) findViewById(R.id.cardalertclick);
        signupcardtext = (TextView) findViewById(R.id.signupcardtext);

        alreadyuser = (TextView) findViewById(R.id.alreadyuser);
        signupfullview = (LinearLayout) findViewById(R.id.signupfullview);

        auth = FirebaseAuth.getInstance();
        alreadyuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth.getCurrentUser() != null) {

                    Intent i = new Intent(getApplicationContext(), Loginpage.class);
                    startActivity(i);
                }

            else {

                    Toast.makeText(Signup_class.this, "You have to signup first", Toast.LENGTH_SHORT).show();


                }
            }
        });


        signupprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImages();


            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(emailsignup.getText().toString())) {
//                    cardalertshow("Enter email address", cardalert, signupcardtext, cardalertclick);
                    alertdialog("Alert","Enter Email address");
                    return;
                }

                if (TextUtils.isEmpty(namesignup.getText().toString())) {
//                    cardalertshow("Enter name", cardalert, signupcardtext, cardalertclick);
                    alertdialog("Alert","Please enter ame");

                    return;
                }
                if (TextUtils.isEmpty(passwordsignup.getText().toString())) {
//                    cardalertshow("Enter Password", cardalert, signupcardtext, cardalertclick);
                    alertdialog("Alert","Please enter a password");

                    return;
                }
                if (TextUtils.isEmpty(phonesighnup.getText().toString())) {
//                    cardalertshow("Enter the phone number", cardalert, signupcardtext, cardalertclick);
                    alertdialog("Alert","Please enter the phone number");

                    return;
                }

                if (passwordsignup.getText().toString().length() < 6) {
//                    cardalertshow("Password too short, enter minimum 6 characters!", cardalert, signupcardtext, cardalertclick);
                    alertdialog("Alert","Password too short, enter minimum 6 characters");

//                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (signupprofile.getDrawable() == null) {
                    alertdialog("Alert","Please select a profile picture");

//                    Toast.makeText(Signup_class.this, "hi", Toast.LENGTH_SHORT).show();
                    return;
                }
                //create user

                auth.createUserWithEmailAndPassword(emailsignup.getText().toString(), passwordsignup.getText().toString())
                        .addOnCompleteListener(Signup_class.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
//
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {

                                    alertdialog("Alert",task.getException().getLocalizedMessage());


                                } else {

                                    uploadfile(profileuniqueid);

                                    String userId = UUID.randomUUID().toString();


                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    String uid = user.getUid();
                                    final DatabaseReference userRef = mDatabase.child("Users").child(uid).getRef();

                                    userRef.child("Username").setValue("" + emailsignup.getText().toString());
                                    userRef.child("name").setValue("" + namesignup.getText().toString());
                                    userRef.child("phonenumber").setValue("" + phonesighnup.getText().toString());
                                    userRef.child("UserID").setValue(userId);
                                    userRef.child("profilepic").setValue(profileuniqueid);
//
                                    SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("richu", 0).edit();
                                    editor.putBoolean("remember", true);
                                    editor.commit();
//

                                    Utils util = new Utils();
                                    util.progressdialog(Signup_class.this);
                                    startActivity(new Intent(Signup_class.this, Loginpage.class));
                                    finish();

                                }
                            }
                        });

            }
        });

    }







    private void selectImages() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Signup_class.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    opencamera();


                } else if (items[item].equals("Choose from Library")) {
                    openImageChooser();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    void opencamera() {
        identifier = "camera";


        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }

    void openImageChooser() {
        identifier = "galary";
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            profileuniqueid = UUID.randomUUID().toString();

            if (requestCode == SELECT_PICTURE) {
                selectedImageURI = data.getData();
//
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageURI);

                    getImageData(bitmap);

                    signupprofile.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                selectedImage = true;
            }
                if (requestCode == CAMERA_REQUEST) {
                      CameraURI = data.getData();
                    //                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), CameraURI);
//                        Toast.makeText(Signup_class.this, ""+bitmap, Toast.LENGTH_SHORT).show();
                    photo = (Bitmap) data.getExtras().get("data");

                    getImageData(photo);

                    photo = (Bitmap) data.getExtras().get("data");
//                    uploadfile(profileuniqueid);
                    signupprofile.setImageBitmap(photo);
                    selectedImage = true;
                }
            }


    }


    public void getImageData(Bitmap bmp) {

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, bao); // bmp is bitmap from user image file
//        bmp.recycle();

        byteArray = bao.toByteArray();
//
    }



    public void uploadfile(String id) {



        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReferenceFromUrl("gs://testproject-37c18.appspot.com");    //change the url according to your firebase app

        StorageReference childRef = storageRef.child(id+".jpg");
        UploadTask uploadTask = childRef.putBytes(byteArray);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(Signup_class.this, "Upload successful", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Signup_class.this, "Upload not successful", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    public void alertdialog(String s1,String s2)
    {
        new LovelyStandardDialog(Signup_class.this)
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
}
