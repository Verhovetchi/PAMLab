package com.example.dumitru.lab5;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    String TAG = "MYERRORS";
    HomeFragment homeFragment;
    NotificationFragment notificationFragment;
    AddFragment addFragment;
    ScheduleFragment scheduleFragment;
    ProfileFragment profileFragment;
    DocListFragment docListFragment;
    FragmentTransaction fragmentTransaction;
    Button homebtn, notifbtn, schedulebtn, profilebtn;
    int id;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.actionbar);

        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(
                Color.parseColor("#099900")));


        homeFragment = new HomeFragment();
        notificationFragment = new NotificationFragment();
        addFragment = new AddFragment();
        scheduleFragment = new ScheduleFragment();
        profileFragment = new ProfileFragment();
        docListFragment = new DocListFragment();


        homebtn = findViewById(R.id.button3);
        notifbtn = findViewById(R.id.button2);
        schedulebtn = findViewById(R.id.button4);
        profilebtn = findViewById(R.id.button5);

        homebtn.setBackgroundResource(R.drawable.ic_homeefect_image);
        id = 1;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, homeFragment, "HomeFragment");
        fragmentTransaction.commit();

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setTitle("Home");
                homebtn.setBackgroundResource(R.drawable.ic_homeefect_image);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, homeFragment, "HomeFragment");
                fragmentTransaction.commit();
                if(id != 1)
                    ChangeBtnIcon(id);
                id = 1;
            }
        });
        notifbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Notification");
                notifbtn.setBackgroundResource(R.drawable.notifefect_ic_iamge);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, notificationFragment, "NotificationFragment");
                if(!GlobalVariables.getInstance().CheckNotif())
                {
                    fragmentTransaction.hide(notificationFragment);
                }
                fragmentTransaction.commit();
                if(id != 2)
                    ChangeBtnIcon(id);
                id = 2;
            }
        });
        schedulebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Doctor List");
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, docListFragment, "DocListFragment");
                fragmentTransaction.commit();
                if(id != 3)
                    ChangeBtnIcon(id);
                    id = 3;
            }
        });
        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Profile");
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, profileFragment, "ProfileFragment");
                fragmentTransaction.commit();
                if(id != 4)
                    ChangeBtnIcon(id);
                id = 4;
            }
        });
    }

    private void ChangeBtnIcon(int IDChanged)
    {
        if (IDChanged == 0)
            return;
        switch (IDChanged) {
            case 1:
                homebtn.setBackgroundResource(R.drawable.ic_homeicon_ready);
                break;
            case 2:
                notifbtn.setBackgroundResource(R.drawable.ic_notif_image);
                break;
            case 3:
                schedulebtn.setBackgroundResource(R.drawable.ic_scheduleicon_ready);
            case 4:
                profilebtn.setBackgroundResource(R.drawable.ic_prfileicon_ready);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onSupportNavigateUp(){
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Home");
        homebtn.setBackgroundResource(R.drawable.ic_homeefect_image);
/*        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)
                homebtn.getLayoutParams();
        lp.height = 30;
        lp.width = 30;
        homebtn.setLayoutParams(lp);*/
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, homeFragment, "HomeFragment");
        fragmentTransaction.commit();
        if(id != 1)
            ChangeBtnIcon(id);
        id = 1;
        return true;
    }

}

// TODO : Put home fragment onCreate
// TODO : Set padding on EditText
