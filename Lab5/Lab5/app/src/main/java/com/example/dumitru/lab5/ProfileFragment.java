package com.example.dumitru.lab5;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    String TAG = "MYERRORS";
    CircleImageView ciMyPhoto;
    TextView etMyFullName, etMyBirthday, etMyEmail, etMyPhoneNumber, etMyAddress, etMyUserName;
    JSONObject response;

    public ProfileFragment() {
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ciMyPhoto = view.findViewById(R.id.myphoto);
        etMyAddress = view.findViewById(R.id.myaddress);
        etMyBirthday = view.findViewById(R.id.mybirthday);
        etMyEmail = view.findViewById(R.id.myemail);
        etMyFullName = view.findViewById(R.id.myfullname);
        etMyPhoneNumber = view.findViewById(R.id.myphonenumber);
        etMyUserName = view.findViewById(R.id.myusername);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                response = GET();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(response != null)
                {
                    try {
                        byte[] decodedString = Base64.decode(response.getString("Base64Photo"), Base64.DEFAULT|Base64.NO_WRAP);
                        Bitmap bitmapMyPhot = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        ciMyPhoto.setImageBitmap(bitmapMyPhot);
                        etMyFullName.setText(response.getString("FullName"));
                        etMyBirthday.setText(response.getString("Birthday"));
                        etMyEmail.setText(response.getString("Email"));
                        etMyPhoneNumber.setText(response.getString("Phone"));
                        etMyAddress.setText(response.getString("Address"));
                        etMyUserName.setText(response.getString("Username"));

                    } catch (JSONException e) {
                        Log.d(TAG, e.toString());
                    }
                }
            }
        }.execute();

        return view;
    }

    JSONObject GET()
    {
        String address = "http://81.180.72.17:80/api/Profile/GetProfile";
        Log.d(TAG, "Conecting for get profile...");
        try {
            URL url = new URL(address);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded; charset=utf-8");
            httpURLConnection.setRequestProperty("token", GlobalVariables.getInstance().GetToken());
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
            Log.d(TAG, "Result of profileFragment: " + response);

            return new JSONObject(response.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
            return null;
        }
    }


}
