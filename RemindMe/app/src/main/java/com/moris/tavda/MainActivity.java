package com.moris.tavda;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.moris.tavda.adapter.TabsPagerFragmentAdapter;
import com.moris.tavda.servic.TavdaService;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final int LAYOUT = R.layout.activity_maun;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String UserID;
    private SharedPreferences preferences;

    final String LOG_TAG = "myLog";

    boolean bound = false;
    ServiceConnection sConn;
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
            case 2: break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("TAVDA_PREFERENCES", Context.MODE_PRIVATE);
        UserID = preferences.getString("User", "");
        if (UserID.equals("")) UserID = null;
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if ((month < 9) & (year < 2019)) {
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
        intent = new Intent(this, TavdaService.class);
        sConn = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.d(LOG_TAG, "MainActivity onServiceConnected");
                bound = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
                bound = false;
            }
        };
//        startService(new Intent(this, TavdaService.class));
        bindService(intent, sConn, Context.BIND_AUTO_CREATE);
        if (BuildConfig.DEBUG) {
            ActivityManager am = (ActivityManager) this
                    .getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(3);

            for (int i = 0; i < rs.size(); i++) {
                ActivityManager.RunningServiceInfo rsi = rs.get(i);
                Log.d(LOG_TAG, "Process " + rsi.process + " with component "
                        + rsi.service.getClassName());
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            unbindService(sConn);
        }
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
