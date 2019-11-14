package com.example.dumitru.lab5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    String TAG = "MYERRORS";
    String sTokenOrError = "NULL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.signupBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {

/*                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();*/
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        boolean b = LOGIN();
                        if (!b)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, sTokenOrError, Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                        else
                        {
                            GlobalVariables.getInstance().SetToken(sTokenOrError);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        return null;
                    }
                }.execute();
            }
        });

    }

    boolean LOGIN()
    {

        EditText etEmail = findViewById(R.id.emailEditText);
        EditText etPassword = findViewById(R.id.passwordEditText);
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        String address = "http://81.180.72.17:80/api/Login/UserAuth";
        Log.d(TAG, "Conecting to login...");
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
            writer.write("Email=" + email + "&" + "Password=" + password);
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

            JSONObject reader = new JSONObject(response.toString());
            sTokenOrError = reader.getString("Status");
            if(sTokenOrError.equals("ERROR"))
            {
                sTokenOrError = reader.getString("Message");
                return false;
            }
            else
            {
                sTokenOrError = reader.getString("Message");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}