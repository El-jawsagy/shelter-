package com.life.shelter.people.homeless;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class trampdata extends AppCompatActivity {
    EditText nameEditText, addressEditText, cityEditText;
    Button buttonsave;
    ImageView photoEdit;
    private ProgressBar progressBar;
    private Uri imagepath;
    public static final int PICK_IMAGE = 1;
    private StorageReference mStorageRef;
    DatabaseReference databasetramp;
  //  DatabaseReference databaseacount;
    DatabaseReference databasereg;

    String userphotoUri;
    String userName;
    String type,country;
    private FirebaseAuth mAuth;

    String s="a";
     // all Notification code in this file
    // The id of the channel.(for Notification)
    final String CHANNEL_ID = "my_channel_01";
    final String CHANNEL_NAME = "new_Tramp";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trampdata);
        mAuth = FirebaseAuth.getInstance();
        databasetramp= FirebaseDatabase.getInstance().getReference("trampoos");
        mStorageRef = FirebaseStorage.getInstance().getReference("trrrrr");

       // databaseacount= FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());



        nameEditText= (EditText)findViewById(R.id.edit_name);
        addressEditText= (EditText)findViewById(R.id.edit_address);
        cityEditText= (EditText)findViewById(R.id.edit_city);
        buttonsave= (Button) findViewById(R.id.button);
        photoEdit=(ImageView) findViewById(R.id.edit_photo);
        progressBar= findViewById(R.id.progressbar);


        photoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code here executes on main thread after user presses image
                showFileShooser();
            }
        });


        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {// try & catch for Notification)
                    addtramp();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        /////////////////////////////////////
        getRegData();

    }

    private void getRegData() {
        databasereg = FirebaseDatabase.getInstance().getReference("reg_data");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                type = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("ctype").getValue(String.class);
                country = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("ccountry").getValue(String.class);
                Toast.makeText(trampdata.this,type+ "+/+ "+country, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        databasereg .addValueEventListener(postListener);
    }


    private void addtramp() throws IOException {  //throws IOException because of try & catch vith method above
        String mtrampname = nameEditText.getText().toString();
        String mtrampaddress = addressEditText.getText().toString();
        String mtrampcity = cityEditText.getText().toString();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.getPhotoUrl() != null) {
                userphotoUri = user.getPhotoUrl().toString();
            }
            if (user.getDisplayName() != null) {
                userName = user.getDisplayName().toString();
            }

            //كuser name has space not null so i need to reset it to null
            if (user.getDisplayName().trim() =="") {
                userName = null;
            }
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String postdate = sdf.format(calendar.getTime());


            if ((!TextUtils.isEmpty(mtrampname)) && (!TextUtils.isEmpty(mtrampaddress)) && (!TextUtils.isEmpty(mtrampcity))) {
                if (!s.equals("a")) {
/**
//we here replace code with one has the same id for both home and account activity
                    DatabaseReference reference = databasetramp.push();
                     String id = reference.getKey();
                     HomeFirebaseClass homefirebaseclass = new HomeFirebaseClass(mtrampname, mtrampaddress, mtrampcity, s, userphotoUri, userName, postdate);
                     databasetramp.child(country).child(type).child("users").child(mAuth.getCurrentUser().getUid()).child(id).setValue(homefirebaseclass);
                     databaseacount.child(id).setValue(homefirebaseclass);
**/

                    HomeFirebaseClass homefirebaseclass = new HomeFirebaseClass(mtrampname, mtrampaddress, mtrampcity, s, userphotoUri, userName, postdate);
                  //  databasetramp.push().setValue(homefirebaseclass);
                    databasetramp.child(country).child(type).child("users").child(mAuth.getCurrentUser().getUid()).push().setValue(homefirebaseclass);

                    // databaseacount.push().setValue(homefirebaseclass);
                    Toast.makeText(this, "tramp data saved", Toast.LENGTH_LONG).show();
                    nameEditText.setText("");
                    addressEditText.setText("");
                    cityEditText.setText("");

                    //start notifacition
                    showNotification("Shelter","Someone need your help!" , MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagepath));
                    //end notifacition

                    Intent intent2 = new Intent(trampdata.this, home.class);

                    // Start the new activity
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent2);
                } else {
                    Toast.makeText(this, "you should add aphoto", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "you should fill all fields", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void showFileShooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null&& data.getData() != null) {
            imagepath=data.getData();
           // s = imagepath.toString();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
                photoEdit.setImageBitmap(bitmap);
                uploadimage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadimage(){
        if (imagepath != null) {
            progressBar.setVisibility(View.VISIBLE);

            StorageReference trampsRef = mStorageRef.child(imagepath.getLastPathSegment());

            trampsRef.putFile(imagepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);

                            s = taskSnapshot.getDownloadUrl().toString();

                            Toast.makeText(trampdata.this, "image uploaded", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(trampdata.this, "error ocoured while  uploading image", Toast.LENGTH_LONG).show();

                        }
                    });
        }else {                            Toast.makeText(trampdata.this, "error ocoured while  uploading image", Toast.LENGTH_LONG).show();
        }
    }
    // Create notification shown after adding tramp
    private void showNotification(String title, String content , Bitmap aBitmap) {

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(content);
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.drawable.ic_announcement_white_24dp) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(content)// message for notification
                .setSound(Uri.parse("android.resource://"
                        + this.getPackageName() + "/"
                        + R.raw.alarm)) // set alarm sound for notification
                .setLargeIcon(aBitmap)
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), home.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }


}

