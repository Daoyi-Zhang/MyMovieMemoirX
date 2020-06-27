package com.monash.mymoviememoirx.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.monash.mymoviememoirx.R;
import com.monash.mymoviememoirx.fragment.MovieViewFragment;
import com.monash.mymoviememoirx.pojo.DetailMovie;
import com.monash.mymoviememoirx.pojo.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MovieViewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public static final String TAG = "MovieViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.main_background));
        replaceFragment(MovieViewFragment.newInstance((DetailMovie) getIntent().getExtras().get("selected movie"),
                (User) getIntent().getExtras().get("currentUser")));
    }
    public void replaceFragment(Fragment nextFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.movie_view_container, nextFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        TextView dateTV = findViewById(R.id.memoir_date);
        dateTV.setText(date);
    }
}
