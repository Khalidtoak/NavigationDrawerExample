package com.example.user.navigationdrawerexample;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.user.navigationdrawerexample.fragment.HomeFragment;
import com.example.user.navigationdrawerexample.fragment.MoviesFragment;
import com.example.user.navigationdrawerexample.fragment.NotificationsFragment;
import com.example.user.navigationdrawerexample.fragment.SettingsFragment;
import com.example.user.navigationdrawerexample.fragment.photoFragment;
import com.example.user.navigationdrawerexample.other.CircleTransformation;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener,
        MoviesFragment.OnFragmentInteractionListener,
        NotificationsFragment.OnFragmentInteractionListener,
        photoFragment.OnFragmentInteractionListener,SettingsFragment.OnFragmentInteractionListener
{
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View navHeader;
    private ImageView imgNavHeaderbg,imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    //urls to load Image
    private static final String urlNavHeaderBg = "https://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImage
            = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";
    //index to identify nav_menu_item
    public static int nav_int_index =0;
    //tags for attaching the fragment
    private static final String TAG_HOME = "home";
    public static final String TAG_PHOTOS = "photos";
    public static final String TAG_MOVIES = "movies";
    public static final String TAG_NOTIFICATIONS = "notifications";
    public static final String TAG_SETTNGS = "settings";
    public static  String CURRENT_TAG = TAG_HOME;
    private String [] activityTitles;

    //tool bar titles respected to select nav menu item
    private boolean shouldLoadFromBackPress = true;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler();
        drawerLayout  = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        floatingActionButton = findViewById(R.id.fab);

        //Nav View header
        navHeader = navigationView.getHeaderView(0);
        txtName = navHeader.findViewById(R.id.name);
        txtWebsite = navHeader.findViewById(R.id.website);
        imgNavHeaderbg = navHeader.findViewById(R.id.img_header_bg);
        imgProfile = navHeader.findViewById(R.id.img_profile);

        //load toolBar titles from String Resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with an action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        //load nav menu header data
        loadNavHeader();

        // initialize nav menu
        setUpNavigationView();
        if (savedInstanceState == null){
            nav_int_index = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }
    //load navigation menu header
    private void loadNavHeader(){
        //name and webSite
        txtName.setText("khalid toak");
        txtWebsite.setText("www.androidhive.com");

        //loading header Image
        Glide.with(this).load(urlNavHeaderBg).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderbg);
        //load profileImage
        Glide.with(this).load(urlProfileImage).crossFade().thumbnail(0.5f).bitmapTransform(new
                CircleTransformation(this)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgProfile);
        //showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }
    //this will return the fragment of what ever the user clicks
    private void loadHomeFragment(){
        //select the nav menu item that was clicked
        selectNavMenu();
        //set toolbar title
        setToolBarTitle();
        //if use selects the same nav menu being displayed do nothing..close the nav drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG)!=null){
            drawerLayout.closeDrawers();
            //show or hide fab button
            togglefab();
            return;
        }
        //use runnable to create crossFade effect
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // update main content by updating the fragment
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();

            }
        };
        //if pendingRunnable is not null, add to the message queue
        if (runnable!= null){
            mHandler.post(runnable);
        }
        //show or hide the fab button
        togglefab();
        ///close drawer when an item is clicked
        drawerLayout.closeDrawers();
        //refresh tool bar menu
        invalidateOptionsMenu();
    }
    private Fragment getHomeFragment(){
        switch (nav_int_index){
            case 0:
                //home
                HomeFragment homeFragment = new HomeFragment();
                return  homeFragment;
            case 1:
                //photos
                photoFragment photoFragment = new photoFragment();
                return photoFragment;
            case 2:
                //Movies
                MoviesFragment moviesFragment = new MoviesFragment();
            case 3:
                //Notification
                NotificationsFragment notificationsFragment = new NotificationsFragment();
                return notificationsFragment;

            case 4:
                //settings
                SettingsFragment settingsFragment = new SettingsFragment();
                return  settingsFragment;
            default:
                    return new HomeFragment();

        }
    }
    private void setToolBarTitle(){
        getSupportActionBar().setTitle(activityTitles[nav_int_index]);
    }
    private void selectNavMenu(){
        navigationView.getMenu().getItem(nav_int_index).setChecked(true);
    }
    private void setUpNavigationView(){
        //handle the item Click of the Nav menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //check to see which item was clicked
                switch (item.getItemId()){
                    case R.id.nav_home:
                        nav_int_index = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_photos:
                        nav_int_index = 1;
                        CURRENT_TAG = TAG_PHOTOS;
                    case R.id.nav_movies:
                            nav_int_index = 2;
                            CURRENT_TAG = TAG_MOVIES;
                            break;
                    case R.id.nav_settings:
                        nav_int_index = 4;
                        CURRENT_TAG = TAG_SETTNGS;
                        break;
                    case R.id.nav_notifications:
                        nav_int_index = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    case R.id.nav_about_us:
                        //launch new activity not fragment
                        Intent intent = new Intent(MainActivity.this,AboutUsActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        return true;
                    default:nav_int_index =0;

                }
                if (item.isChecked()){
                    item.setChecked(false);
                }
                else{
                    item.setChecked(true);
                }
                loadHomeFragment();
                return true;
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.openDrawer, R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                //when Drawer is Closed since we are not doing anything we leave it empty
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //when drawer is opened, we are not doing anything so we leave it empty
                super.onDrawerOpened(drawerView);
            }
        };
        //set the listener
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //call sync state so that icons will show
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
            return;
        }
        //load home fragment when back key is pressed and when user is in another fragment other than
        // the home fragment
        if (shouldLoadFromBackPress){
            if (nav_int_index!=0){
                nav_int_index =0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu and show only when home fragment is selected
        if (nav_int_index ==0){
            getMenuInflater().inflate(R.menu.main, menu);
        }
        //when fragment is notifications, load menu for the notifications
        if (nav_int_index == 3){
            getMenuInflater().inflate(R.menu.notification, menu);

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle actionBar item clicks
        int id = item.getItemId();
        if (id == R.id.action_logout){
            Toast.makeText(getApplicationContext(), "Logged out!", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.action_mark_all_read){
            Toast.makeText(getApplicationContext(),"marked all as read", Toast.LENGTH_LONG).show();
        }
        if (id== R.id.action_clear_notifications){
            Toast.makeText(getApplicationContext(), "Cleared Notifications", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
    //show or hide fab
    private void togglefab(){
        if (nav_int_index==0){
            floatingActionButton.show();
        }
        else
            floatingActionButton.hide();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
