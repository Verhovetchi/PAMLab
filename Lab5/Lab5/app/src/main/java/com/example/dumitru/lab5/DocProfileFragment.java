package com.example.dumitru.lab5;


import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

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
public class DocProfileFragment extends Fragment implements OnMapReadyCallback {

    String TAG = "MYERRORS";
    JSONObject result;

    private MapView mapView;
    View view;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    public DocProfileFragment() {
        // Required empty public constructor
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_doc_profile, container, false);

        //getActivity().getActionBar().setTitle("Doctor Details");
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Doctor Details");

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                result = GETDOCPROFLE();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(result != null)
                {
                    Log.d(TAG, result.toString());
                    TextView tvDocDesc = view.findViewById(R.id.docprofdescription);
                    TextView tvDocLoc = view.findViewById(R.id.docproflocation);
                    LinearLayout llDocinclude = view.findViewById(R.id.docprofinclude);
                    ImageView ivDocImage = llDocinclude.findViewById(R.id.docprofphoto);
                    TextView tvDocName = llDocinclude.findViewById(R.id.docprofname);
                    TextView tvDocSpecs = llDocinclude.findViewById(R.id.docprofspecs);
                    TextView tvDocRate = llDocinclude.findViewById(R.id.docprofrate);
                    RatingBar rbDocStars = llDocinclude.findViewById(R.id.docprofstars);

                    try {
                        tvDocDesc.setText(result.getString("About"));
                        tvDocLoc.setText(result.getString("Address"));
                        tvDocName.setText(result.getString("FullName"));
                        tvDocSpecs.setText(result.getString("Specs"));
                        tvDocRate.setText(result.getString("Stars"));
                        rbDocStars.setRating(result.getInt("Stars"));

                        byte[] decodedString = Base64.decode(result.getString("Photo"), Base64.DEFAULT|Base64.NO_WRAP);
                        ivDocImage.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, e.toString());
                    }
                }
            }
        }.execute();

        Bundle mapViewBundle = null;

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        //mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        return view;
    }

    JSONObject GETDOCPROFLE()
    {
        String address = "http://81.180.72.17:80/api/Doctor/GetDoctor/" +
                GlobalVariables.getInstance().GetDocID();
        Log.d(TAG, "Conecting for doctor profile...");
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

            return new JSONObject(response.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
            return null;
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMinZoomPreference(12);
        LatLng ny = new LatLng(40.7143528, -74.0059731);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
