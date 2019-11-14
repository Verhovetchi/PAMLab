package com.example.dumitru.lab5;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private String[] sDocName, sDocSpecs, sDocAddress;
    private float[] fDocStars;
    private Bitmap[] bmpDocPhoto;

    CustomListAdapter(Activity context, String[] sDocName,String[] sDocSpecs,String[] sDocAddress,
            float[] fDocStars, Bitmap[] bmpDocPhoto) {
        super(context, R.layout.doc_list_pattern, sDocName);

        this.context=context;
        this.sDocName=sDocName;
        this.sDocSpecs=sDocSpecs;
        this.sDocAddress=sDocAddress;
        this.fDocStars=fDocStars;
        this.bmpDocPhoto=bmpDocPhoto;
    }

    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"})
        View rowView = inflater.inflate(R.layout.doc_list_pattern, null,true);





        TextView doctorname = rowView.findViewById(R.id.Itemname);
        ImageView imageView = rowView.findViewById(R.id.icon);
        TextView profess = rowView.findViewById(R.id.textView);
        TextView addr = rowView.findViewById(R.id.textView2);
        TextView rat = rowView.findViewById(R.id.textView3);

        doctorname.setText(sDocName[position]);
        imageView.setImageBitmap(bmpDocPhoto[position]);
        //imageView.setImageResource(imgid[position]);
        profess.setText(sDocSpecs[position]);
        addr.setText(sDocAddress[position]);
        String temp = fDocStars[position]+"";
        rat.setText(temp);
        return rowView;

    }
}
