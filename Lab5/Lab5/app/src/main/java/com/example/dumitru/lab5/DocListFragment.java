package com.example.dumitru.lab5;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class DocListFragment extends Fragment {

    String TAG = "MYERRORS";

    String[] sDocName, sDocSpecs, sDocAddress;
    float[] fDocStars;
    Bitmap[] bmpDocPhoto;
    ListView listView;


    JSONArray jsonlistresult;

    public DocListFragment() {
        // Required empty public constructor
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doc_list, container, false);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                jsonlistresult = GETDOCLIST();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if(jsonlistresult != null){
                    sDocName = new String[jsonlistresult.length()];
                    sDocAddress = new String[jsonlistresult.length()];
                    sDocSpecs = new String[jsonlistresult.length()];
                    fDocStars = new float[jsonlistresult.length()];
                    bmpDocPhoto = new Bitmap[jsonlistresult.length()];
                    for(int i = 0; i < jsonlistresult.length(); i++)
                    {
                        try {
                            JSONObject temp = jsonlistresult.getJSONObject(i);
                            sDocName[i] = temp.getString("FullName");
                            sDocSpecs[i] = temp.getString("Specs");
                            sDocAddress[i] = temp.getString("Address");
                            fDocStars[i] = Float.parseFloat(temp.getString("Stars"));
                            byte[] decodedString = Base64.decode(temp.getString("Photo"), Base64.DEFAULT|Base64.NO_WRAP);
                            bmpDocPhoto[i] = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            Log.d(TAG, temp.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    CustomListAdapter adapter = new CustomListAdapter( getActivity(), sDocName, sDocSpecs,
                            sDocAddress, fDocStars, bmpDocPhoto);
                    listView.setAdapter(adapter);
                }


                super.onPostExecute(aVoid);
            }
        }.execute();

        listView= view.findViewById(R.id.androidlist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position++;
                GlobalVariables.getInstance().SetDocID(position);
                Log.d(TAG, "Position(ID) of doctor: " + position); //TODO temp
                DocProfileFragment docProfileFragment = new DocProfileFragment();
                FragmentTransaction fragmentTransaction;
                assert getFragmentManager() != null;
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, docProfileFragment, "HomeFragment");
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    JSONArray GETDOCLIST()
    {
        String address = "http://81.180.72.17:80/api/Doctor/GetDoctorList";
        Log.d(TAG, "Conecting for doctor list...");
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

            return new JSONArray(response.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
            return null;
        }
    }

}
