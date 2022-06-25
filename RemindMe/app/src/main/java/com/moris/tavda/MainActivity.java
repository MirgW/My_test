package com.moris.tavda;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.moris.tavda.adapter.TabsPagerFragmentAdapter;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.ViewPager;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;


public class MainActivity extends AppCompatActivity implements LifecycleOwner {
    private static final int LAYOUT = R.layout.activity_maun;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String UserID;
    private SharedPreferences preferences;
    private static final int REQUEST_CODE_PERMISSION_INTERNET = 123;
//    private LifecycleRegistry lifecycleRegistry;

    final String LOG_TAG = "myLog";

    boolean bound = false;
    //    ServiceConnection sConn;
    Intent intent;

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

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

//        lifecycleRegistry = new LifecycleRegistry(this);
//        lifecycleRegistry.markState(Lifecycle.State.CREATED);
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            //readContacts();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, REQUEST_CODE_PERMISSION_INTERNET);
        }
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
//        if (year < 2021) {
        setContentView(LAYOUT);
//        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Сообшить о проблеме", Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.CYAN)
                        .setAction("Начать", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent startIntent = new Intent(getApplicationContext(), CamActivity.class);
                                        startActivity(startIntent);
//                                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                        startActivityForResult(cameraIntent,2);

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
        if (isOnline(this)) {
            initTabs();
        } else {
            intent = new Intent(this, internet.class);
            startActivity(intent);
        }

        //     ((TextView) findViewById(R.id.textView)).setText("sssss");
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
                    .Builder(MyWorker.class, 15, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .addTag("TavadSet")
                    .build();
            // TODO: 7/3/2019  getInstance(this) getInstance() - проверить уведомления
            WorkManager.getInstance(this).enqueueUniquePeriodicWork("TavadSet", ExistingPeriodicWorkPolicy.KEEP,
                    myWorkRequest);
        } else {
            PeriodicWorkRequest myWorkRequest = new PeriodicWorkRequest
                    .Builder(MyWorker.class, 2, TimeUnit.HOURS)
                    .setConstraints(constraints)
                    .addTag("TavadSet")
                    .build();
            WorkManager.getInstance(this).enqueueUniquePeriodicWork("TavadSet", ExistingPeriodicWorkPolicy.KEEP,
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
            Context context = getApplicationContext(); // or activity.getApplicationContext()
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();

            String myVersionName = "not available"; // initialize String

            try {
                myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
                ((TextView) findViewById(R.id.textView)).setText(myVersionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
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
                //       Intent intent = new Intent(getApplicationContext(), Authentication.class);
//            EditText editText = (EditText) findViewById(R.id.editText);
//            String message = editText.getText().toString();
//            intent.putExtra(EXTRA_MESSAGE, message);
                startActivityForResult(intent, 1);
                return false;
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        DrawerLayout mDrawerLayout;
//        NavigationView navigationView = findViewById(R.id.navigation);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerdayout);
        if (mDrawerLayout.isOpen()) {
            mDrawerLayout.closeDrawers();
        } else {
            if (isOnline(this)) {
                FragmentManager fragmentManager;
                int pos;
                boolean flag;
                pos = tabLayout.getSelectedTabPosition();
                List<Fragment> list = getSupportFragmentManager().getFragments();
//            FragmentManager.BackStackEntry fragment = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);
                Fragment fragment=null;
//                list.get(1).isMenuVisible()
//                list.get(0).getClass().getSimpleName()
                for(Fragment f : list){
                    if(f != null && f.isMenuVisible())
                        fragment=f;
                }

                if (!(fragment instanceof IOnBackPressed)) {
                    //     super.onBackPressed();
                    if (pos == 0) {
                        super.onBackPressed();
                    } else {
                        tabLayout.selectTab(tabLayout.getTabAt(0), true);
//                getSupportFragmentManager().popBackStack();
                    }
                } else {
                    flag = ((IOnBackPressed) fragment).onBackPressed();
                    if (flag) {
                        tabLayout.selectTab(tabLayout.getTabAt(0), true);
                    } else {
                        //               super.onBackPressed();
                    }
                }
//        int count = getSupportFragmentManager().getBackStackEntryCount();
//        String tmpStr10 = String.valueOf(count);
//        Toast toast = Toast.makeText(this, tmpStr10,Toast.LENGTH_LONG);
//        toast.show();
                //else tabLayout.selectTab(tabLayout.getTabAt(0),true);
/*//
        if (getFragmentManager().getBackStackEntryCount() == 0) {
       //     this.finish();
        } else {
            getFragmentManager().popBackStack();
        }*/
            } else {
                super.onBackPressed();
            }
        }
    }

    private void initTabs() {
        viewPager = findViewById(R.id.viewPager);
        TabsPagerFragmentAdapter adapter = new TabsPagerFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_blue_grey_900_24dp);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_format_list_bulleted_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.baseline_navigation_grey_900_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.baseline_error_outline_grey_900_24dp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.deep_orange_500, null), PorterDuff.Mode.SRC_IN);
//        tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.grey_60, null), PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.grey_60, null), PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.grey_60, null), PorterDuff.Mode.SRC_IN);
        }
//        tabLayout.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.grey_60, null), PorterDuff.Mode.SRC_IN);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tab.getIcon().setColorFilter(getResources().getColor(R.color.deep_orange_500, null), PorterDuff.Mode.SRC_IN);
                }
//                ViewAnimation.fadeOutIn(nested_scroll_view);
                int pos;
                pos = tabLayout.getSelectedTabPosition();
                FloatingActionButton fab = findViewById(R.id.fab);
                toolbar = findViewById(R.id.toolbar);
//                setSupportActionBar(toolbar);
                switch (pos) {
                    case 0:
//                        fab.show();
                        toolbar.setTitle("Тавда");
                        break;
                    case 1:
                        fab.hide();
                        toolbar.setTitle("ЕДДС");
                        break;
                    case 2:
                        fab.hide();
                        toolbar.setTitle("Карта");
                        break;
                    case 3:
                        fab.hide();
                        toolbar.setTitle("COVID-19");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tab.getIcon().setColorFilter(getResources().getColor(R.color.grey_60, null), PorterDuff.Mode.SRC_IN);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        lifecycleRegistry.markState(Lifecycle.State.STARTED);
/*        if (preferences.getBoolean("notifications_new_message", true))
            bindService(intent, sConn, Context.BIND_AUTO_CREATE);
*/
    }

    //    @NonNull
//    @Override
//    public Lifecycle getLifecycle() {
//        return lifecycleRegistry;
//    }
    private Uri imageUri;

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
                    case R.id.nav_policy:
                        Uri address = Uri.parse("https://sites.google.com/view/tavdanews/Privacy");
                        Intent openlink = new Intent(Intent.ACTION_VIEW, address);
                        try {
                            startActivity(Intent.createChooser(openlink, ""));
                        } catch (ActivityNotFoundException e) {
                        }
                        break;
                    case R.id.nav_news:
                        address = Uri.parse("https://sites.google.com/view/tavdanews/news");
                        openlink = new Intent(Intent.ACTION_VIEW, address);
                        try {
                            startActivity(Intent.createChooser(openlink, ""));
                        } catch (ActivityNotFoundException e) {
                        }
                        break;
                    case R.id.nav_contact:
                        address = Uri.parse("https://sites.google.com/view/tavdanews/contacts");
                        openlink = new Intent(Intent.ACTION_VIEW, address);
                        try {
                            startActivity(Intent.createChooser(openlink, ""));
                        } catch (ActivityNotFoundException e) {
                        }
                        break;
                    case R.id.nav_exit:
                        finish();
                        break;
                    case R.id.nav_share:
                        int sharableImage = R.drawable.tavda1024;
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), sharableImage);
                        String path = getExternalCacheDir() + "/tavda.jpg";
                        java.io.OutputStream out;
                        java.io.File file = new java.io.File(path);
                        if (!file.exists()) {
                            try {
                                out = new java.io.FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                out.flush();
                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        path = file.getPath();
//                        imageUri = Uri.parse("file://" + path);
                        imageUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(path));
                        ;
                        Intent sendIntent = new Intent();
                        sendIntent.setType("text/plain");
                        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        imageUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/" + "tavda1024.png");
                        sendIntent.setAction(android.content.Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "" +
                                "Новости города Тавда в мобильном приложении\n\n" +
                                "С помощью нашего приложения Вы сможете:\n" +
                                "- Своевременно ознакомится с новостями города Тавды и Тавдинского района\n" +
                                "- Смотреть новостные программы местного телевидения\n" +
                                "- Получать уведомления от городских служб\n" +
                                "- Быть в курсе предстоящих городских мероприятиях\n" +
                                "- Делится новостями в социальных сетях.\n" +
                                " https://play.google.com/store/apps/details?id=com.moris.tavda.free");
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Новости города Тавда в мобильном приложении");
                        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                        sendIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
/*                        PackageManager pm = getApplicationContext().getPackageManager();
                        @SuppressLint("QueryPermissionsNeeded") List<ResolveInfo> activityList = pm.queryIntentActivities(sendIntent, 0);
                        for (final ResolveInfo app : activityList) {
                            if ((app.activityInfo.name).contains("ru.ok.android")) {
                                final ActivityInfo activity = app.activityInfo;
                                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                                sendIntent.setPackage("ru.ok.android");
                                sendIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                sendIntent.setComponent(name);
//                                startActivity(sendIntent);
                                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.app_name)));
                                break;
                            }
                        }*/
//                    sendIntent.putExtra(Intent.EXTRA_EMAIL  , new String[] { "ssss@where.com" });
//                    sendIntent.putExtra(Intent.EXTRA_CONTENT_ANNOTATIONS, "EXTRA_CONTENT_ANNOTATIONS");
//                    sendIntent.putExtra(Intent.EXTRA_SPLIT_NAME,"dddddd");
//                        sendIntent.putExtra(Intent.EXTRA_HTML_TEXT, "<html><body><h1>Отправлено из мобильного приложения Живая Тавда. #живаятавда</h1></html></body>");
//                        sendIntent.setType("text/plan");
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
