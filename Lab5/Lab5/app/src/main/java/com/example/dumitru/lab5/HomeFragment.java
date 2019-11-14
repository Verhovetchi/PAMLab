package com.example.dumitru.lab5;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    final String TAG = "MYERRORS";

    final int TERAPEUT = 4, OCULIST = 1, PEDIATRU = 2, CHIRURG = 3, NODOCTOR = 5;

    int DOCTOR = 5;

    View oncreatedview;

    EditText etName, etDesease, etLocation, etDescription;

    JSONArray resultArray;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //getContext().getTheme().applyStyle(R.style.MainTheme, true);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        etName = view.findViewById(R.id.homeName);
        etDescription = view.findViewById(R.id.homeDescrition);
        etDesease = view.findViewById(R.id.homeDesease);
        etLocation = view.findViewById(R.id.homeLocation);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        oncreatedview = view;

        Button btn = view.findViewById(R.id.requesnotifbtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {


                if(etDescription.getText().toString().isEmpty() ||
                        etDesease.getText().toString().isEmpty() ||
                        etName.getText().toString().isEmpty() ||
                        etLocation.getText().toString().isEmpty()) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Incomplete lines!", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    JSONObject NotifContent = new JSONObject();
                    try {
                        NotifContent.put("Name", etName.getText().toString());
                        NotifContent.put("Description", etDescription.getText().toString());
                        NotifContent.put("Disease", etDesease.getText().toString());
                        NotifContent.put("Location", etLocation.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    GlobalVariables.getInstance().SetJSONNotifInfo(NotifContent);

                    try {
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                resultArray = GETDOCLIST();
                                GlobalVariables.getInstance().SetJSONADocList(resultArray);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                            }
                        }.execute().get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                POSTCONSULTATION();
                                return null;
                            }
                        }.execute().get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    GlobalVariables.getInstance().SetNotifCheck(true);
                    getActivity().findViewById(R.id.button2).setBackgroundResource(
                            R.drawable.ic_notifefect_image);
                }
            }
        });
    }

    int GetSpecificDoctor(String Sentence)
    {
        if(Sentence.toLowerCase().contains("ochi") ||
                Sentence.toLowerCase().contains("ochiul") ||
                Sentence.toLowerCase().contains("ochelari") ||
                Sentence.toLowerCase().contains("ochii") ||
                Sentence.toLowerCase().contains("vederea") ||
                Sentence.toLowerCase().contains("vedere") ||
                Sentence.toLowerCase().contains("vad")) {
            return OCULIST;
        }
        else if(Sentence.toLowerCase().contains("picior") ||
                Sentence.toLowerCase().contains("mina") ||
                Sentence.toLowerCase().contains("coloana") ||
                Sentence.toLowerCase().contains("vertebrala") ||
                Sentence.toLowerCase().contains("muschi") ||
                Sentence.toLowerCase().contains("ligamente") ||
                Sentence.toLowerCase().contains("fractura") ||
                Sentence.toLowerCase().contains("fracturat") ||
                Sentence.toLowerCase().contains("spinare") ||
                Sentence.toLowerCase().contains("vertebre") ||
                Sentence.toLowerCase().contains("dislocat") ||
                Sentence.toLowerCase().contains("dislocate")) {
            return TERAPEUT;
        }
        else if(Sentence.toLowerCase().contains("copilul") ||
                Sentence.toLowerCase().contains("copil") ||
                Sentence.toLowerCase().contains("baiatul") ||
                Sentence.toLowerCase().contains("fetita")) {
            return PEDIATRU;
        }
        else if(Sentence.toLowerCase().contains("operatie") ||
                Sentence.toLowerCase().contains("operat") ||
                Sentence.toLowerCase().contains("interventie") ||
                Sentence.toLowerCase().contains("chirurgical") ||
                Sentence.toLowerCase().contains("chirurgicala")) {
            return CHIRURG;
        }

        return NODOCTOR;
    }

    void POSTCONSULTATION()
    {
        String urltopost = "ConsId=1" + "&" +
                "Name=" + etName.getText().toString() + "&" +
                "Disease=" + etDesease.getText().toString() + "&" +
                "Address=" + etLocation.getText().toString() + "&" +
                "Description=" + etDescription.getText().toString() + "&" +
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
            JSONObject jsonresult = new JSONObject(response.toString());
            //GlobalVariables.getInstance().SetJSONNotifInfo(jsonresult);
            GlobalVariables.getInstance().SetDocID(Integer.parseInt(jsonresult.getString("DocId")));
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    void SetNotificationToPush(final JSONObject ContentToPush)
    {
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    resultArray = GETDOCLIST();

                    GlobalVariables.getInstance().SetJSONADocList(resultArray);
                    for (int i = 0; i < resultArray.length(); i++)
                    {
                        try {
                            Log.d(TAG, resultArray.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    String Disease = null;
                    try {
                        Disease = ContentToPush.getString("Disease");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    switch (GetSpecificDoctor(Disease)) {
                        case OCULIST:
                            DOCTOR = OCULIST;
                            break;
                        case PEDIATRU:
                            DOCTOR = PEDIATRU;
                            break;
                        case TERAPEUT:
                            DOCTOR = TERAPEUT;
                            break;
                        case CHIRURG:
                            DOCTOR = CHIRURG;
                            break;
                        default:
                            DOCTOR = NODOCTOR;
                            break;
                    }
                    Log.d(TAG, "Specific doctor is:" + DOCTOR);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
