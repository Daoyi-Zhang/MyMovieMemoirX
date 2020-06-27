package com.monash.mymoviememoirx.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.monash.mymoviememoirx.R;
import com.monash.mymoviememoirx.fragment.HomeFragment;
import com.monash.mymoviememoirx.fragment.MemoirFragment;
import com.monash.mymoviememoirx.fragment.MovieSearchFragment;
import com.monash.mymoviememoirx.fragment.ReportFragment;
import com.monash.mymoviememoirx.fragment.WatchlistFragment;
import com.monash.mymoviememoirx.pojo.Credential;
import com.monash.mymoviememoirx.pojo.User;
import com.monash.mymoviememoirx.util.WatchlistAlarm;
import com.monash.mymoviememoirx.util.WatchlistMovie;
import com.monash.mymoviememoirx.viewmodel.WatchListMovieViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";
    private static final String MOVIE_ADD_TIME_OVER_WEEK = "com.monash.mymoviememorix.MOVIE_ADD_TIME_OVER_WEEK";
    private static final int LOGIN_REQUEST_CODE = 1;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private static Credential currentCredential;
    private WatchlistAlarm alarm;
    private WatchListMovieViewModel vm;
    private Dialog dialog;

//    private void createNotificationChannel(){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationChannel notificationChannel =
//                    new NotificationChannel("mymoviememoir", "My Movie Memoir", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationChannel.setDescription("My Movie Memoir");
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//    }
//
//    private void showNotification(){
//        createNotificationChannel();
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "mymoviememoir");
//        builder.setContentTitle("You have one or more movies in watchlist over a week")
//                .setSmallIcon(R.drawable.ic_date_range_black_24dp)
//                .setContentText("Delete them?")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//        notificationManagerCompat.notify(100, builder.build());
//    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(){
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, WatchlistAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 11, intent, 0);
        manager.setExact(AlarmManager.RTC_WAKEUP, new Date().getTime() + 1000, pendingIntent);
    }

    private void initViewModel(){
        Log.d(TAG, "initViewModel: called");
        if (currentCredential != null) {
            vm = new ViewModelProvider(this).get(WatchListMovieViewModel.class);
            vm.initVars(this.getApplication());
            LiveData<List<WatchlistMovie>> allMovies = vm.getAllMovies(currentCredential.getUserId().getUserId().toString());
            allMovies.observe(this, new Observer<List<WatchlistMovie>>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onChanged(List<WatchlistMovie> watchListMovies) {
                    Log.d(TAG, "onChanged: called");
                    int count = 0;
                    for (WatchlistMovie movie :
                            watchListMovies) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String addDateStr = movie.getAddDate();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.DAY_OF_MONTH, -7);
                        Date lastWeek = calendar.getTime();
                        try {
                            Date addDate = sdf.parse(addDateStr);
                            if (lastWeek.getTime() > addDate.getTime()) {
                                count++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if (count > 0) {
                        Log.d(TAG, "onChanged: alarm started");
                        startAlarm();
                    }
                }
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        //start LoginSignUpActivity
        Intent start = new Intent(this, LoginSignUpActivity.class);
        startActivityForResult(start, LOGIN_REQUEST_CODE);
        initLogout();
    }

    private void initNavigationDrawer() {
        //adding the toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //these two lines of code show the nav_icon drawer icon top left hand side
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        //drawerLayout.openDrawer(GravityCompat.START);

        String welcome = "Welcome! " + currentCredential.getUserId().getName() + "!    Today is "
                + new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        //set welcome
        toolbar.setSubtitle(welcome);
        //set nav e-mail and full name
        TextView headerName = navigationView.getHeaderView(0).findViewById(R.id.username_text_view);
        TextView herderEmail = navigationView.getHeaderView(0).findViewById(R.id.email_text_view);
        String fullName = currentCredential.getUserId().getName() + " " + currentCredential.getUserId().getSurname();
        headerName.setText(fullName);
        herderEmail.setText(currentCredential.getUsername());
        //TODO set user image
    }

    public void replaceFragment(Fragment nextFragment) {
        Log.d(TAG, "replaceFragment: Last fragment: " + nextFragment.getClass().getName());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nextFragment);
        fragmentTransaction.addToBackStack("nav");
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home_page:
                replaceFragment(HomeFragment.newInstance(currentCredential.getUserId()));
                break;
            case R.id.movie_search:
                replaceFragment(MovieSearchFragment.newInstance(currentCredential.getUserId()));
                break;
            case R.id.movie_memoir:
                replaceFragment(MemoirFragment.newInstance(currentCredential.getUserId()));
                break;
            case R.id.watchlist:
                replaceFragment(WatchlistFragment.newInstance(currentCredential.getUserId()));
                break;
            case R.id.reports:
                replaceFragment(ReportFragment.newInstance(currentCredential.getUserId()));
                break;
            case R.id.maps:
                Bundle bundle = new Bundle();
                bundle.putParcelable("currentUser", currentCredential.getUserId());
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.logout:
                dialog.show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        currentCredential = (Credential) bundle.get("currentCredential");
        User currentUser = (User) bundle.get("currentUser");
        currentCredential.setUserId(currentUser);
        //init Navigation Drawer
        initNavigationDrawer();
        replaceFragment(HomeFragment.newInstance(currentCredential.getUserId()));
        //initViewModel();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    public static User findTheUser(){
        return currentCredential.getUserId();
    }
    public void initLogout(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.user_logout);
        Button confirm = dialog.findViewById(R.id.logout_confirm);
        Button cancel = dialog.findViewById(R.id.logout_cancel);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent start = new Intent(getApplicationContext(), LoginSignUpActivity.class);
                startActivityForResult(start, LOGIN_REQUEST_CODE);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}
