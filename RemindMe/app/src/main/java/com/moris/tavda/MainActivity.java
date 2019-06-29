package com.moris.tavda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.viewpager.widget.ViewPager;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.moris.tavda.adapter.TabsPagerFragmentAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements LifecycleOwner {
    private static final int LAYOUT = R.layout.activity_maun;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String UserID;
    private SharedPreferences preferences;
    private LifecycleRegistry lifecycleRegistry;

    final String LOG_TAG = "myLog";

    boolean bound = false;
//    ServiceConnection sConn;
    Intent intent;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case 1:
                UserID = data.getStringExtra("name");
                invalidateOptionsMenu();
                break;
            case 2:
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);

        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        Intent intentWeb = getIntent();
        String fileName = intentWeb.getStringExtra("url_DTO");
        if (!TextUtils.isEmpty(fileName)) {
            String str;
            str = "http://www.adm-tavda.ru" + fileName;
            Intent startIntent = new Intent(this, ActivityWebview.class);
            startIntent.putExtra("INTENT_EXTRA_URL", str);
            startActivity(startIntent);
        }
        preferences = getSharedPreferences(getApplicationContext().getPackageName() + "_preferences", Context.MODE_PRIVATE);
        UserID = preferences.getString("User", "");
        if (UserID.equals("")) UserID = null;
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (year < 2021) {
            setContentView(LAYOUT);
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Сообшить о проблеме", Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.CYAN)
                        .setAction("Начать", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(cameraIntent, 2);

//                                        Intent intent = new Intent();
//                                        intent.setAction(Intent.ACTION_CAMERA_BUTTON);
//                                        intent.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN,
//                                                KeyEvent.KEYCODE_CAMERA));
//                                        startActivityForResult(intent,2);
                                        //sendOrderedBroadcast(intent, null);

                                        // Intent intent = new Intent(getApplicationContext(), CamActivity.class);
                                        // startActivityForResult(intent, 3);
                                    }
                                }
                        ).show();
            }
        });
        initToolbar();
        initNavigationView();
        initTabs();
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
//        OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
//                .setConstraints(constraints)
//                .addTag("TavadSet")
//                .build();
//        WorkManager.getInstance().enqueueUniqueWork("TavadSet", ExistingWorkPolicy.REPLACE,   myWorkRequest);

        if (BuildConfig.DEBUG) {
            PeriodicWorkRequest myWorkRequest = new PeriodicWorkRequest
                    .Builder(MyWorker.class, 1, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .addTag("TavadSet")
                    .build();
            WorkManager.getInstance().enqueueUniquePeriodicWork("TavadSet", ExistingPeriodicWorkPolicy.KEEP,
                    myWorkRequest);
        } else
            {
                PeriodicWorkRequest myWorkRequest = new PeriodicWorkRequest
                        .Builder(MyWorker.class, 2, TimeUnit.HOURS)
                        .setConstraints(constraints)
                        .addTag("TavadSet")
                        .build();
                WorkManager.getInstance().enqueueUniquePeriodicWork("TavadSet", ExistingPeriodicWorkPolicy.KEEP,
                        myWorkRequest);
        }


//        WorkManager.getInstance().getWorkInfoByIdLiveData(myWorkRequest.getId())
//                .observe(this, info -> {
//                    if (info != null && info.getState().isFinished()) {
//                        Log.d("myLog", "doWork: OK!");
//                    }
//                });

//        WorkManager.getInstance().getWorkInfoByIdLiveData(myWorkRequest.getId())
//                .observe( this, new Observer<WorkInfo>() {
//                    @Override
//                    public void onChanged(@Nullable WorkInfo workStatus) {
//                        Log.d(LOG_TAG, "onChanged: " + workStatus.getState()+" - "+workStatus.getTags().toString());
//                        Log.d(LOG_TAG, "onChanged: " + workStatus.getOutputData().getString("keyA"));
//                        Log.d(LOG_TAG, "onChanged: " + workStatus.getOutputData().getKeyValueMap());
//                    }
//        });
////////////////////////////////////////////////////////////////////////////////////////
//        intent = new Intent(this, TavdaService.class);
//        sConn = new ServiceConnection() {
//            public void onServiceConnected(ComponentName name, IBinder binder) {
//                Log.d(LOG_TAG, "MainActivity onServiceConnected");
//                bound = true;
//            }
//
//            public void onServiceDisconnected(ComponentName name) {
//                Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
//                bound = false;
//            }
//        };
//        if (preferences.getBoolean("notifications_new_message", true))
//            startService(new Intent(this, TavdaService.class));
//        if (BuildConfig.DEBUG) {
//            ActivityManager am = (ActivityManager) this
//                    .getSystemService(ACTIVITY_SERVICE);
//            List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(3);
//
//            for (int i = 0; i < rs.size(); i++) {
//                ActivityManager.RunningServiceInfo rsi = rs.get(i);
//                Log.d(LOG_TAG, "Process " + rsi.process + " with component "
//                        + rsi.service.getClassName());
//            }
//        }
/////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
 //       WorkManager.getInstance().cancelAllWorkByTag("TavadSet");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (bound) {
//            unbindService(sConn);
//        }
        bound = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (UserID != null) {
            menu.findItem(R.id.search).setIcon(R.drawable.ic_account_check);
            ((TextView) findViewById(R.id.textView)).setText(UserID);
        } else {
            menu.findItem(R.id.search).setIcon(R.drawable.ic_account);
            ((TextView) findViewById(R.id.textView)).setText("");
        }

//            menu.findItem(R.id.search).setIcon(getResources().getDrawable(R.drawable.ic_account_check, getTheme()));
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.search || super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                Log.d("LOG", "ssssssssssssssss");
                Intent intent = new Intent(getApplicationContext(), Authentication.class);
//            EditText editText = (EditText) findViewById(R.id.editText);
//            String message = editText.getText().toString();
//            intent.putExtra(EXTRA_MESSAGE, message);
                startActivityForResult(intent, 1);
                return false;
            }
        });
    }


    private void initTabs() {
        viewPager = findViewById(R.id.viewPager);
        TabsPagerFragmentAdapter adapter = new TabsPagerFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleRegistry.markState(Lifecycle.State.STARTED);
/*        if (preferences.getBoolean("notifications_new_message", true))
            bindService(intent, sConn, Context.BIND_AUTO_CREATE);
*/
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    private void initNavigationView() {
        drawerLayout = findViewById(R.id.drawerdayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.vew_navigation_open, R.string.view_navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                int niter = item.getItemId();
                switch (niter) {
                    case R.id.actionNotificationItem:
//                        showNatificationTab(niter);
                        viewPager.setCurrentItem(constants.TAB_ONE);
                        break;
                    case R.id.nav_gallery:
                        viewPager.setCurrentItem(constants.TAB_TWO);
                        break;
                    case R.id.nav_slideshow:
                        viewPager.setCurrentItem(constants.TAB_THREE);
                        break;
                    case R.id.nav_nastr:
                        Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_manage:
                        viewPager.setCurrentItem(constants.TAB_FOUR);
                        break;
                    case R.id.nav_exit:
                        finish();
                        break;
                    case R.id.nav_share:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Отправлено из мобильного приложения Живая Тавда: " + "Установи приложение ...");
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Отправлено из мобильного приложения Живая Тавда");
//                    sendIntent.putExtra(Intent.EXTRA_EMAIL  , new String[] { "ssss@where.com" });
//                    sendIntent.putExtra(Intent.EXTRA_CONTENT_ANNOTATIONS, "EXTRA_CONTENT_ANNOTATIONS");
//                    sendIntent.putExtra(Intent.EXTRA_SPLIT_NAME,"dddddd");
                        sendIntent.putExtra(Intent.EXTRA_HTML_TEXT, "<html><body><h1>Отправлено из мобильного приложения Живая Тавда.</h1></html></body>");
                        sendIntent.setType("text/plan");
                        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.app_name)));
                }
                return false;
            }
        });

    }


//    private void showNatificationTab(int niter) {
//        viewPager.setCurrentItem(constants.TAB_ONE);
//    }

}
