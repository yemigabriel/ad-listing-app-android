package com.doxa360.android.dutch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doxa360.android.dutch.helpers.DutchSharedPref;
import com.doxa360.android.dutch.model.User;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = this.getClass().getSimpleName();
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView toolbarLabel;

    private DutchSharedPref mSharedPref;
    private String name,avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

//        toolbarLabel = (TextView) toolbar.findViewById(R.id.toolbar_title);
//        toolbarLabel.setTypeface(utf);

        setSupportActionBar(toolbar);


//        setSupportActionBar(toolbar);
//        toolbar.setTitle("");

//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        mSharedPref = new DutchSharedPref(this);
        User currentUser = mSharedPref.getCurrentUser();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            View headerView = navigationView.getHeaderView(0);
            ImageView profilePhoto = (ImageView) headerView.findViewById(R.id.profilePhoto);
            TextView profileName = (TextView) headerView.findViewById(R.id.profileName);
            if (currentUser!= null) {
                name = currentUser.getName();
                avatar = currentUser.getAvatar();
            } else {
                name = "Create an account";
                profilePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
            if (avatar!=null) {
                if(!avatar.isEmpty()) {
                    if (!avatar.startsWith("http")) {
                        avatar = Dutch.PHOTO_URL + avatar;
                    }
                    Picasso.with(this).load(avatar).into(profilePhoto);
                } else {
                    Picasso.with(this).load(R.drawable.wil_profile).into(profilePhoto);
                }
            } else {
                Picasso.with(this).load(R.drawable.wil_profile).into(profilePhoto);
            }
            profileName.setText(name);
            profilePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSharedPref.getCurrentUser() == null) {
                        //alert stuff first
                        alertAccount("profile");
                    }
                    else {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ProfileFragment fragment = new ProfileFragment();
                        ft.replace(R.id.container, fragment).commit();
                        toolbar.setTitle("Profile");
                    }
                }
            });

            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_home);
            toolbar.setTitle("Home");
        }

        FragmentManager fm = getSupportFragmentManager();
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();
                //additional code
            } else {
                Log.e(TAG, "count is "+ count);
                getFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            onSearchRequested();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = null;
        FragmentTransaction ft = fm.beginTransaction();

        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            toolbar.setTitle(item.getTitle());

        }
        else if (id == R.id.nav_categories) {
            fragment = new CategoryFragment();
            toolbar.setTitle(item.getTitle());

        }
        else if (id == R.id.nav_profile) {
            if (mSharedPref.getCurrentUser() == null) {
                //alert stuff first
                alertAccount("your profile");
            }
            else {
                fragment = new ProfileFragment();
                toolbar.setTitle(item.getTitle());
            }
        }
        else if (id == R.id.nav_my_ads) {
            if (mSharedPref.getCurrentUser() == null) {
                //alert stuff first
                alertAccount("your ads");
            }
            else {
                fragment = new MyAdsFragment();
                toolbar.setTitle(item.getTitle());
            }
        }
//        else if (id == R.id.nav_share) {
//
//        }
        else if (id == R.id.nav_feedback) {
            sendEMAILIntent("feedback@dutch.ng");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (fragment!=null) {
            ft.replace(R.id.container, fragment).commit();
        }
        if (navigationView != null) {
            navigationView.setCheckedItem(item.getItemId());
        }


        return true;
    }

    private void alertAccount(String feature) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String mTitle = "Account required";

        builder.setTitle(mTitle)
                .setMessage("Please create an account or log in to your existing account to access " + feature)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sendEMAILIntent(String email) {
        String[] emailAddress = new String[] {email};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
        if (intent.resolveActivity(HomeActivity.this.getPackageManager()) != null) {
            startActivity(intent);
        }

    }

}
