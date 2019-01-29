package com.life.shelter.people.homeless;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import custom_font.MyEditText;
import custom_font.MyTextView;

public class Register extends AppCompatActivity {

    private TextView shelter;

    private MyTextView singin, signup;
    private MyEditText editTextemail, editTextPassword, editTextCPassword;
    private ProgressBar progressBar;
    private Spinner spinnercountry, spinnertype;
    DatabaseReference databasereg;
    FirebaseAuth mAuth;
    //String TAG = "TECSTORE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        shelter = (TextView) findViewById(R.id.title_reister);
        Typeface custom_fonts = Typeface.createFromAsset(getAssets(), "fonts/ArgonPERSONAL-Regular.otf");
        shelter.setTypeface(custom_fonts);

        mAuth = FirebaseAuth.getInstance();
        databasereg = FirebaseDatabase.getInstance().getReference("reg_data");


        editTextemail = findViewById(R.id.edit_email);
        editTextPassword = findViewById(R.id.edit_password);
        editTextCPassword = findViewById(R.id.edit_c_password);
        signup = findViewById(R.id.getstarted);
        singin = (MyTextView) findViewById(R.id.login);

        spinnercountry = findViewById(R.id.spinner_country);
        spinnertype = findViewById(R.id.spinner_type);

        progressBar= findViewById(R.id.progressbar);

        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Register.this, Login.class);
                finish();
                startActivity(it);

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regesterUser();}
        });





// spinner for countries
        ArrayAdapter<CharSequence> adapterc = ArrayAdapter.createFromResource(
                Register.this, R.array.countries_array, android.R.layout.simple_spinner_item);
        adapterc.setDropDownViewResource(R.layout.array_list_item);
        spinnercountry.setAdapter(adapterc);

        spinnercountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorText));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



         // spinner for type
         ArrayAdapter<CharSequence> adaptert = ArrayAdapter.createFromResource(
                 Register.this, R.array.type_array, android.R.layout.simple_spinner_item);
         adaptert.setDropDownViewResource(R.layout.array_list_item);
        spinnertype.setAdapter(adaptert);

        spinnertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorText));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
        });

    }

    private void regesterUser() {
        final String mEmail = editTextemail.getText().toString().trim();
        String mPassword = editTextPassword.getText().toString().trim();
        String mCPassword = editTextCPassword.getText().toString().trim();

        final String mCountry = spinnercountry.getSelectedItem().toString().trim();
        final String mtype = spinnertype.getSelectedItem().toString().trim();

        if (mEmail.isEmpty()) {
            editTextemail.setError("Email is required");
            editTextemail.requestFocus();
            return;}

        if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            editTextemail.setError("Please enter a valid email");
            editTextemail.requestFocus();
            return;}

        if (mPassword.length()<6) {
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();
            return;}

        if (mPassword.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;}

        if (mCPassword.isEmpty()) {
            editTextCPassword.setError("you should confirm your password");
            editTextCPassword.requestFocus();
            return;}

        if (!mCPassword.equals(mPassword)) {
            editTextCPassword.setError("it must be the same as password");
            editTextCPassword.requestFocus();
            return;}

            progressBar.setVisibility(View.VISIBLE);
        if (isNetworkConnected()) {
        mAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "USER CREATED", Toast.LENGTH_SHORT).show();
                    RegisterClass regdata = new RegisterClass ( mEmail, mCountry, mtype);
                    databasereg.child(mAuth.getCurrentUser().getUid()).setValue(regdata);
                    Intent intend= new Intent(Register.this, home.class);
                    intend.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intend);

                } else {
                    //Log.e(TAG, task.getException().getMessage());
                    if (task.getException()instanceof FirebaseAuthUserCollisionException){
                    Toast.makeText(Register.this, "you are already registed", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Register.this, "REGISTER ERROR", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        } else {
            Toast.makeText(this, "please check the network connection", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    }
    //  check if network is connected
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            return true;
        } else {
            return false;
        }
    }


}