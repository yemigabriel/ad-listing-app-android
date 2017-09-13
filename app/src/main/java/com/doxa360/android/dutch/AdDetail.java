package com.doxa360.android.dutch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doxa360.android.dutch.model.Ad;
import com.doxa360.android.dutch.model.AdImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdDetail extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST = 120;
    private TextView mTitle, mDescription, mCategory, mPrice, mSeller, mViews, mAddress;
    private ImageView mPhoto, mDarken, mFullScreen;
    private Boolean isFabOpen = false;
    public FloatingActionButton fab, fab1, fab2, fab3;
    private TextView fabText1, fabText2, fabText3;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private String phone = "";
    private String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final Ad ad = intent.getParcelableExtra(Dutch.AD);
        Log.e("Adetailerror", ad.toString());
        phone = ad.getUser().getPhone()!=null ? ad.getUser().getPhone():" ";
        email = ad.getUser().getEmail();

//        getSupportActionBar().setTitle(ad.getTitle());

        mTitle = (TextView) findViewById(R.id.title);
        mDescription = (TextView) findViewById(R.id.description);
        mAddress = (TextView) findViewById(R.id.address);
        mCategory = (TextView) findViewById(R.id.category);
        mPrice = (TextView) findViewById(R.id.price);
        mSeller = (TextView) findViewById(R.id.owner);
        mViews = (TextView) findViewById(R.id.views);
        mPhoto = (ImageView) findViewById(R.id.photo);
        mDarken = (ImageView) findViewById(R.id.darken);
        mFullScreen = (ImageView) findViewById(R.id.fullscreen);

        mTitle.setText(ad.getTitle());
        Log.e("ADDETAIL", ad.getDescription());
        mDescription.setText(ad.getDescription());
        mCategory.setText(ad.getCategory().getTitle());
        mPrice.setText(ad.getFormattedPrice());
        mAddress.setText(ad.getAddress3() /*+ (ad.getAddress2()!=null?ad.getAddress2():" ")*/ + " " +ad.getAddress1());
        Log.e("AD_DETAIL", ad.getDescription()+ad.getPrice()+"");
        mSeller.setText("Seller: " + ad.getUser().getName());
//        mViews.setText("Views: " + ad.getViews());

        Picasso.with(this)
                .load(Dutch.PHOTO_URL + ad.getImage())
                .into(mPhoto);

        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AdDetail.this, ImageFullscreenActivity.class);
                intent1.putExtra(Dutch.AD, ad.getImages());
                startActivity(intent1);
            }
        });

        Log.e(TAG, ad.getImages().size()+" size");
        Log.e(TAG, ad.getImages().getClass().getSimpleName()+" size");
        Log.e(TAG, ad.getImages().get(0).getClass().getSimpleName()+" size ");
        Log.e(TAG, ad.getImages().get(0).getImage()+" size ");

        mFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AdDetail.this, ImageFullscreenActivity.class);
                intent1.putExtra(Dutch.AD, ad.getImages());
                startActivity(intent1);
            }
        });


        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fabText1 = (TextView) findViewById(R.id.fab_text1);
        fabText2 = (TextView) findViewById(R.id.fab_text2);
        fabText3 = (TextView) findViewById(R.id.fab_text3);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdDetail.this, "Calling Seller", Toast.LENGTH_SHORT).show();
                makeCall();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(AdDetail.this, "Send Message", Toast.LENGTH_SHORT).show();
//                sendEMAILIntent(email);
                shareIntent(ad);
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(AdDetail.this, "Post Your Ad", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdDetail.this, PostAdActivity.class);
                startActivity(intent);
            }
        });

    }

    private void shareIntent(Ad ad) {
    }

    private void makeCall () {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(AdDetail.this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AdDetail.this,
                    Manifest.permission.CALL_PHONE)) {

            } else {
                requestPermissions(
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST);
            }
        } else {
            startActivity(callIntent);
        }

    }

    private void sendSMSIntent(String number, String msg) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", msg);
        startActivity(it);

    }

    private void sendEMAILIntent(String email) {
        String[] emailAddress = new String[] {email};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
        if (intent.resolveActivity(AdDetail.this.getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    public void animateFAB() {

        if (isFabOpen) {

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fabText1.startAnimation(fab_close);
            fabText2.startAnimation(fab_close);
            fabText3.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fab.setImageResource(R.drawable.ic_more_horiz_white_36dp);
            mDarken.setVisibility(View.INVISIBLE);
            isFabOpen = false;
            Log.e("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fabText1.startAnimation(fab_open);
            fabText2.startAnimation(fab_open);
            fabText3.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab.setImageResource(R.drawable.ic_close_white_36dp);
            mDarken.setVisibility(View.VISIBLE);
            isFabOpen = true;
            Log.e("Raj", "open");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        onBackPressed();
        return true;
    }



}
