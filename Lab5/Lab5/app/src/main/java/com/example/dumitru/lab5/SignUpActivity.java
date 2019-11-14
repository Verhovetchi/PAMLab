package com.example.dumitru.lab5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;


import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    TextView fullnameText, birthdayText, youremailText, phonenumberText, locationaddressText;
    int GALLERY = 1;
    CircleImageView circleImageView;
    Bitmap bitmap;
    String urltopost, sPOSTResult, TAG = "MYERRORS";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(
                Color.parseColor("#32CD32")));
        fullnameText = findViewById(R.id.fullnameText);
        birthdayText = findViewById(R.id.birthdayText);
        youremailText = findViewById(R.id.youremailText);
        phonenumberText = findViewById(R.id.phonenumberText);
        locationaddressText = findViewById(R.id.locationadressText);
        circleImageView = findViewById(R.id.addimageview);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY);
            }
        });
        Button button = findViewById(R.id.nextBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                            sPOSTResult = POST();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SignUpActivity.this, sPOSTResult, Toast.LENGTH_LONG).show();
                                }
                            });
                        return null;
                    }
                }.execute();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            Uri contentURI = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                circleImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    String POST() {
        bitmap = ((BitmapDrawable)circleImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedphoto = Base64.encodeToString( byteArray, Base64.DEFAULT|Base64.NO_WRAP);
        //Log.d(TAG, encodedphoto);
        urltopost =
                "FullName=" + fullnameText.getText().toString() + "&" +
                "Birthday=" + birthdayText.getText().toString()+ "&" +
                "Email=" + youremailText.getText().toString()+ "&" +
                "Phone=" + phonenumberText.getText().toString() + "&" +
                "Address=" + locationaddressText.getText().toString() +  "&" +
                "Username=" + "duprijil" + "&" +
                "Password=" + "1234" + "&" +
                "Base64Photo=" + encodedphoto;
        String address = "http://81.180.72.17:80/api/Register/UserReg";
        try {
            URL url = new URL(address);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded; charset=utf-8");
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(urltopost);
            writer.flush();
            writer.close();
            outputStream.close();
            httpURLConnection.connect();
            InputStream inputStream;
            if (httpURLConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp;
            StringBuilder response = new StringBuilder();
            while ((temp = bufferedReader.readLine()) != null) {
                response.append(temp);
            }
            Log.d(TAG, "Result: " + response);
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
            return e.toString();
        }



/*        JSONObject params = new JSONObject();
        try {
            params.put("FullName", fullnameText.getText().toString());
            params.put("Birthday", birthdayText.getText().toString());
            params.put("Email", youremailText.getText().toString());
            params.put("Phone", phonenumberText.getText().toString());
            params.put("Address", locationaddressText.getText().toString());
            params.put("Username", "prijilevschi");
            params.put("Password", "1234");
            params.put("Base64Photo", imagefortest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, params.toString());
            Request request = new Request.Builder()
                    .url(address)
                    .post(body)
                    .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                Log.d(TAG, "Response: " + response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}



// TODO : Option button on actionbar
// TODO : Make compatible background
// TODO : Set custom actionbar