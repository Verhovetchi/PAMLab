package com.example.dumitru.lab5;


import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    DocProfileFragment docProfileFragment;

    TextView tvName, tvDisease, tvLocation, tvDescription;

    final String TAG = "MYERRORS";

    public NotificationFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_notification, container,
                false);

        if(GlobalVariables.getInstance().CheckNotif()) {

            tvName = inflatedView.findViewById(R.id.notifName);
            tvDisease = inflatedView.findViewById(R.id.notifDisease);
            tvLocation = inflatedView.findViewById(R.id.notifLocation);
            tvDescription = inflatedView.findViewById(R.id.notifDescription);

            JSONObject etContent = GlobalVariables.getInstance().GetJSONNotifInfo();

            try {
                tvName.setText(etContent.getString("Name"));
                tvDisease.setText(etContent.getString("Disease"));
                tvLocation.setText(etContent.getString("Location"));
                tvDescription.setText(etContent.getString("Description"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            docProfileFragment = new DocProfileFragment();
            LinearLayout linearLayout = inflatedView.findViewById(R.id.selecteddoclayout);

            JSONArray JSONADocProf = GlobalVariables.getInstance().GetJSONADocList();

            ImageView ivDocPhoto = linearLayout.findViewById(R.id.docprofphoto);
            TextView tvDocName = linearLayout.findViewById(R.id.docprofname),
                    tvDocSpecs = linearLayout.findViewById(R.id.docprofspecs),
                    tvDocRate = linearLayout.findViewById(R.id.docprofrate);
            RatingBar rbDocStars = linearLayout.findViewById(R.id.docprofstars);

            JSONObject JSONDocProf;
            try {
                JSONDocProf = JSONADocProf.getJSONObject(GlobalVariables.getInstance().GetDocID() - 1);
                tvDocName.setText(JSONDocProf.getString("FullName"));
                tvDocSpecs.setText(JSONDocProf.getString("Specs"));
                tvDocRate.setText(JSONDocProf.getString("Stars"));
                rbDocStars.setRating(Float.parseFloat(JSONDocProf.getString("Stars")));

                byte[] decodedString = Base64.decode(JSONDocProf.getString("Photo"), Base64.DEFAULT | Base64.NO_WRAP);
                ivDocPhoto.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            linearLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    FragmentTransaction fragmentTransaction;
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, docProfileFragment, "HomeFragment");
                    fragmentTransaction.commit();
                    return false;
                }
            });

            Button btnConfirm = inflatedView.findViewById(R.id.notifConfimBtn);
            Button btnCancel = inflatedView.findViewById(R.id.notifCancelBtn);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobalVariables.getInstance().SetNotifCheck(false);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    //ft.detach(NotificationFragment.this).attach(NotificationFragment.this).commit();
                    ft.replace(R.id.frameLayout, new AddFragment(), "ProfileFragment");
                    ft.commit();
                    //ft.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            });

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onClick(View v) {
/*                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            POSTCONSULTATION();
                            return null;
                        }
                    }.execute();*/
                }
            });

        }

        return inflatedView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    boolean POSTCONSULTATION()
    {
        String urltopost = "ConsId=1" + "&" +
                "Name=" + tvName.getText().toString() + "&" +
                "Disease=" + tvDisease.getText().toString() + "&" +
                "Address=" + tvLocation.getText().toString() + "&" +
                "Description=" + tvDescription.getText().toString() + "&" +
                "DocId=" + GlobalVariables.getInstance().GetDocID() + "&" +
                "IsConfirmed=" + "true";

        String address = "http://81.180.72.17:80/api/Doctor/AddConsultation";
        Log.d(TAG, "Connecting for post consultation...");
        try {
            URL url = new URL(address);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded; charset=utf-8");
            httpURLConnection.setRequestProperty("token", GlobalVariables.getInstance().GetToken());

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
            Log.d(TAG, "Message after post consultation: " + response.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
            return false;
        }
    }
}
