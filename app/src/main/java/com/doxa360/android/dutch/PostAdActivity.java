package com.doxa360.android.dutch;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.doxa360.android.dutch.helpers.DutchSharedPref;
import com.doxa360.android.dutch.helpers.ImageResizer;
import com.doxa360.android.dutch.helpers.MyToolBox;
import com.doxa360.android.dutch.model.Ad;
import com.doxa360.android.dutch.model.Category;
import com.doxa360.android.dutch.model.State;
import com.doxa360.android.dutch.model.User;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.squareup.picasso.Picasso;
import com.thinkincode.utils.views.HorizontalFlowLayout;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.Part;

import static android.support.v4.content.LocalBroadcastManager.getInstance;
import static com.doxa360.android.dutch.helpers.MyToolBox.isExternalStorageAvailable;

public class PostAdActivity extends AppCompatActivity {

    private static final int REQUEST_PLACE_PICKER = 7;
    private final String TAG = this.getClass().getSimpleName();
    private EditText mTitle, mDescription, mPrice, mCategory, mState, mAddress;
    private TextInputLayout mTitleLayout, mDescriptionLayout, mPriceLayout, mCategoryLayout, mStateLayout, mAddressLayout;
    private ImageView mAddPhoto;
    private HorizontalFlowLayout mPhotoLayout;
    private Button mBtn;
    private ProgressDialog mProgressDialog;
    private DutchApiInterface mDutchApiInterface;
    private String title, description, price, category, state, address;
    private int category_id;
    private Ad ad;

    private DutchSharedPref mSharedPref;

    View.OnClickListener addPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            cameraChoices();
        }
    };
    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int CHOOSE_PHOTO_REQUEST = 1;
    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;
    public  static final int FILE_SIZE_LIMIT = 1024*1024*10; //10MB
    protected Uri mMediaUri;
    private byte[] fileBytes;
    String fileName;
    private File mediaFile = null;
    private ArrayList<File> allPhotos;

    protected DialogInterface.OnClickListener mDialogListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); //cos of the broadcast...
                            if (mMediaUri == null){
                                Toast.makeText(PostAdActivity.this, "There was a problem capturing your photo", Toast.LENGTH_LONG).show();
                            }
                            else{
                                captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                                startActivityForResult(captureImageIntent, TAKE_PHOTO_REQUEST);
                            }
                            break;
                        case 1:
                            Intent chooseImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            chooseImageIntent.setType("image/*");
                            //Toast.makeText(getActivity(), "The size of your video must be less than 10MB", Toast.LENGTH_LONG).show();
                            startActivityForResult(chooseImageIntent, CHOOSE_PHOTO_REQUEST);
                            break;
                    }
                }
                private Uri getOutputMediaFileUri(int mediaType) {
                    if(isExternalStorageAvailable()){
                        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                                getString(R.string.app_name));
                        if(!mediaStorageDir.exists()){
                            if(!mediaStorageDir.mkdirs()){
                                return null;
                            }
                        }
                        Date now = new Date();
                        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(now);

                        String path = mediaStorageDir.getPath() + File.separator;
                        if (mediaType == MEDIA_TYPE_IMAGE){
                            mediaFile = new File(path+"IMG_"+timestamp+".jpg");
                        }
                        else if (mediaType == MEDIA_TYPE_VIDEO){
                            mediaFile = new File(path+"VID_"+timestamp+".mp4");
                        }
                        else{
                            return null;
                        }
                        Toast.makeText(PostAdActivity.this,"File: "+Uri.fromFile(mediaFile),Toast.LENGTH_LONG).show();
                        return Uri.fromFile(mediaFile);
                    }
                    else{
                        return null;
                    }
                }
                private boolean isExternalStorageAvailable(){
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            };

    private BroadcastReceiver mCategoryBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Category category = intent.getParcelableExtra(Dutch.CATEGORY);
//            Log.e("receiver", "Got message: " + category.getTitle());
            mCategory.setText(category.getTitle());
            category_id = category.getId();
        }
    };

    private BroadcastReceiver mStateBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            State state = intent.getParcelableExtra(Dutch.STATE);
//            Log.e("state receiver", "Got message: " + state.getStateName());
            mState.setText(state.getStateName());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSharedPref = new DutchSharedPref(this);
        if (mSharedPref.getCurrentUser() == null) {
            //alert stuff first
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String mTitle = "Account required";

            builder.setTitle(mTitle)
                    .setMessage("Please create an account or log in to your existing account to post an ad")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(PostAdActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });


            AlertDialog dialog = builder.create();
            dialog.show();
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
        }

        allPhotos = new ArrayList<File>();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("just a minute...");
        mTitleLayout = (TextInputLayout) findViewById(R.id.layout_title);
        mDescriptionLayout = (TextInputLayout) findViewById(R.id.layout_description);
        mPriceLayout = (TextInputLayout) findViewById(R.id.layout_price);
        mCategoryLayout = (TextInputLayout) findViewById(R.id.layout_category);
        mStateLayout = (TextInputLayout) findViewById(R.id.layout_state);
        mAddressLayout = (TextInputLayout) findViewById(R.id.layout_address);

        mTitle = (EditText) findViewById(R.id.ad_title);
        mDescription = (EditText) findViewById(R.id.description);
        mPrice = (EditText) findViewById(R.id.price);
        mCategory = (EditText) findViewById(R.id.category);
        mState = (EditText) findViewById(R.id.state);
        mAddress = (EditText) findViewById(R.id.address);

        mAddPhoto = (ImageView) findViewById(R.id.add_ad_image);
        mPhotoLayout = (HorizontalFlowLayout) findViewById(R.id.photo_layout);

        mAddPhoto.setOnClickListener(addPhoto);

        mCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryBottomSheet();
            }
        });

        mState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStateBottomSheet();
            }
        });

        mAddress.setEnabled(true);
//        mAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onChooseAddress();
//            }
//        });


    }

    private void showCategoryBottomSheet() {
        FragmentManager fm = getSupportFragmentManager();
        CategoryBottomSheetDialogFragment categoryBottomSheetDialogFragment = new CategoryBottomSheetDialogFragment();
        categoryBottomSheetDialogFragment.show(fm, "CATEGORY_BOTTOM_SHEET");
    }

    private void showStateBottomSheet() {
        FragmentManager fm = getSupportFragmentManager();
        StateBottomSheetDialogFragment stateBottomSheetDialogFragment = new StateBottomSheetDialogFragment();
        stateBottomSheetDialogFragment.show(fm, "STATE_BOTTOM_SHEET");
    }

    private void onChooseAddress() {
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, REQUEST_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this, e.getMessage()+"", Toast.LENGTH_LONG).show();
        }
    }


    private void validateAd() {
        title = mTitle.getText().toString().trim();
        description = mDescription.getText().toString().trim();
        price = mPrice.getText().toString().trim();
        category = mCategory.getText().toString().trim();
        state = mState.getText().toString().trim();
        address = mAddress.getText().toString().trim();

        if (!MyToolBox.isMinimumCharacters(title, 10)) {
            mTitleLayout.setError("Please type in a title with at least 10 characters");
            //disable submit
        }
        else if (!MyToolBox.isMinimumCharacters(description, 10)) {
            mDescriptionLayout.setError("Please type in a clear description with at least 10 characters");
            //disable submit
        }
        else if (!MyToolBox.isMinimumCharacters(price, 3)) {
            mPriceLayout.setError("Are you sure this price is correct?");
        }
        else if (!MyToolBox.isMinimumCharacters(category, 1)) {
            mCategoryLayout.setError("Please choose a category");
            //disable submit
        }
        else if (!MyToolBox.isMinimumCharacters(state, 1)) {
            mStateLayout.setError("Please choose a state");
            //disable submit
        }
        else if (!MyToolBox.isMinimumCharacters(address, 3)) {
            mAddressLayout.setError("Please add an address");
            //disable submit
        }
        else if (allPhotos.size() == 0) {
            Toast.makeText(this, "Please add a photo to your ad", Toast.LENGTH_LONG).show();
        }
        else {
            //enable submit
            if (MyToolBox.isNetworkAvailable(this)) {
//                uploadphotos first
                User currentUser = mSharedPref.getCurrentUser();
                int userId = currentUser.getId();
                String phone = currentUser.getPhone()!=null ? currentUser.getPhone():" ";
                ad = new Ad(category_id,title,description,Float.parseFloat(price),phone,state,null,address,1,userId,null);

                postAd(ad);

                Toast.makeText(this, "uploading...", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Network error. Please check your connection", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void postAd(Ad ad) {
        mProgressDialog.show();

        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < allPhotos.size(); i++) {
            parts.add(prepareFilePart(allPhotos.get(i)));
        }
        RequestBody description = createPartFromString("file[]");

        RequestBody titleBody = RequestBody.create(
                okhttp3.MultipartBody.FORM, ad.getTitle());
        RequestBody categoryIdBody = RequestBody.create(
                okhttp3.MultipartBody.FORM, ad.getCategoryId()+"");
        RequestBody userIdBody = RequestBody.create(
                okhttp3.MultipartBody.FORM, ad.getUserId()+"");
        RequestBody descriptionBody = RequestBody.create(
                okhttp3.MultipartBody.FORM, ad.getDescription());
        RequestBody priceBody = RequestBody.create(
                okhttp3.MultipartBody.FORM, ad.getPrice()+"");
        RequestBody address1Body = RequestBody.create(
                okhttp3.MultipartBody.FORM, ad.getAddress1());
        RequestBody address3Body = RequestBody.create(
                okhttp3.MultipartBody.FORM, ad.getAddress3());
        RequestBody publishedBody = RequestBody.create(
                okhttp3.MultipartBody.FORM, ad.getPublished()+"");
        RequestBody phoneBody = RequestBody.create(
                okhttp3.MultipartBody.FORM, ad.getPhone());


        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("title", titleBody);
        map.put("category_id", categoryIdBody);
        map.put("user_id", userIdBody);
        map.put("published", publishedBody);
        map.put("description", descriptionBody);
        map.put("price", priceBody);
        map.put("phone", phoneBody);
        map.put("address1", address1Body);
        map.put("address3", address3Body);

        mDutchApiInterface = DutchApiClient.getClient().create(DutchApiInterface.class);
        Call<Ad> call = mDutchApiInterface.uploadProfilePhoto(map, description, parts);

        call.enqueue(new Callback<Ad>() {
            @Override
            public void onResponse(Call<Ad> call, Response<Ad> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PostAdActivity.this, "Ad Successfully Posted!", Toast.LENGTH_LONG).show();
                    Log.e(TAG, response.body().getTitle());
                    onBackPressed();

                }
                else {
                    try {
                        Log.e(TAG, response.errorBody().string()+"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Ad> call, Throwable t) {
                Log.e(TAG, "failed" + t.getMessage());

                mProgressDialog.dismiss();
            }
        });

    }

    private void cameraChoices() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.camera_choices, mDialogListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private byte[] getByteArrayFromFile(Context context, Uri uri) {
        byte[] fileBytes = null;
        InputStream inStream = null;
        ByteArrayOutputStream outStream = null;
        Log.e(TAG, uri.getScheme());

        if (uri.getScheme().equals("http")) {
//            new urlToBytes();
        }
        else if (uri.getScheme().equals("content")) {
            try {
                inStream = context.getContentResolver().openInputStream(uri);
                outStream = new ByteArrayOutputStream();

                byte[] bytesFromFile = new byte[1024 * 1024]; // buffer size (1 MB)
                assert inStream != null;
                int bytesRead = inStream.read(bytesFromFile);
                while (bytesRead != -1) {
                    outStream.write(bytesFromFile, 0, bytesRead);
                    bytesRead = inStream.read(bytesFromFile);
                }

                fileBytes = outStream.toByteArray();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                try {
                    assert inStream != null;
                    inStream.close();
                    assert outStream != null;
                    outStream.close();
                } catch (IOException e) { /*( Intentionally blank */ }
            }
        }
        else {
            try {
                File file = new File(uri.getPath());
                FileInputStream fileInput = new FileInputStream(file);
                fileBytes = IOUtils.toByteArray(fileInput);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        Random r = new Random();
        int shortSide = r.nextInt(880 - 500) + 500;
        return reduceImageForUpload(fileBytes, shortSide);
    }

    public static byte[] reduceImageForUpload(byte[] imageData, int shortSide) {
        Bitmap bitmap = ImageResizer.resizeImageMaintainAspectRatio(imageData, shortSide);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
        byte[] reducedData = outputStream.toByteArray();
        try {
            outputStream.close();
        }
        catch (IOException e) {
            // Intentionally blank
        }

        return reducedData;
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(File photo) {
        String partName = "file[]";
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),photo);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, photo.getName(), requestFile);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_ad, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_finish) { //change to finish

            validateAd();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_PLACE_PICKER) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlacePicker.getPlace(this, data);
//                String toastMsg = String.format("Place: %s", place.getName());
////                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
//                mAddress.setText(place.getName()+" "+place.getAddress());
//            }
//        }
        if (resultCode == RESULT_OK){
//            mProgressDialog.show();
            if (requestCode == REQUEST_PLACE_PICKER) {
                Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                mAddress.setText(place.getName()+" "+place.getAddress());
            }
            if (requestCode == CHOOSE_PHOTO_REQUEST){
                if (data == null){
                    Toast.makeText(this,"there was an error selecting your photo",Toast.LENGTH_LONG).show();
                }
                else{
                    mMediaUri = data.getData();
                }
                int fileSize = 0;
                InputStream inputStream = null;
                try{
                    inputStream = getContentResolver().openInputStream(mMediaUri);
                    assert inputStream != null;
                    fileSize = inputStream.available();
                } catch (IOException e){
                    Toast.makeText(PostAdActivity.this,"Error opening image. Please try again.",Toast.LENGTH_LONG).show();
                    return;
                }
                finally {
                    try{
                        assert inputStream != null;
                        inputStream.close();
                    } catch (IOException e){/*Intentionally blank*/ }
                }
                if (fileSize >= FILE_SIZE_LIMIT){
                    Toast.makeText(PostAdActivity.this,"The selected image is too large. Please choose another photo.",Toast.LENGTH_LONG).show();
                    return;
                }
                fileBytes = getByteArrayFromFile(PostAdActivity.this, mMediaUri);
//                if(mediaFile==null) {
                    //create mediaFile
                    Log.e(TAG, "creating media file to write chosen image into");
                    mediaFile = createFile(MEDIA_TYPE_IMAGE);
                    Log.e(TAG, "media file succesfully written");
//                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mediaFile);
                    fos.write(fileBytes);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (MyToolBox.isNetworkAvailable(this)) {
                        //TODO: do both here - upload file using mediaFile global variable ?
//                        uploadFile(mediaFile);
                        allPhotos.add(mediaFile);
                        if (allPhotos.size()>3) {
                            Log.e(TAG, allPhotos.size() + " photos in list " + allPhotos.get(0).getName() + allPhotos.get(1).getName() + allPhotos.get(2).getName());
                        }
//                        for (File photo:allPhotos) {
//                            ImageView adPhoto = new ImageView(PostAdActivity.this);
//                            adPhoto.setPadding(4,4,4,4);
//                            Picasso.with(PostAdActivity.this).load(photo).resize(64,64).centerCrop().into(adPhoto);
//                            mPhotoLayout.addView(adPhoto);
//                        }

//                        allPhotos.add(mediaFile);
                        ImageView photo = new ImageView(PostAdActivity.this);
                        photo.setPadding(4,4,4,4);
                        Picasso.with(PostAdActivity.this).load(mediaFile).resize(64,64).centerCrop().into(photo);
                        mPhotoLayout.addView(photo);

                    } else {
                        MyToolBox.AlertMessage(this, "Oops", "Network Error. Please check your connection");
                    }
                }
            }
            else if(requestCode == TAKE_PHOTO_REQUEST){
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                sendBroadcast(mediaScanIntent);
                try {
                    fileBytes = getByteArrayFromFile(this, mMediaUri);
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeByteArray(fileBytes, 0, fileBytes.length);
                    } catch (OutOfMemoryError memoryError) {
                        Toast.makeText(PostAdActivity.this, memoryError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    if (bitmap != null) {
                        try {
                            bitmap = rotateImageIfRequired(bitmap, mMediaUri);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                            byte[] bitmapdata = bos.toByteArray();
                            FileOutputStream fos = new FileOutputStream(mediaFile);
                            fos.write(bitmapdata);
                            fos.flush();
                            fos.close();
                            Log.e(TAG, "media file succesfully written on Captured...");
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(PostAdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } finally {
                    if (MyToolBox.isNetworkAvailable(this)) {
                        //TODO: do both here - upload file using mediaFile global variable ?
//                        uploadFile(mediaFile);
                        allPhotos.add(mediaFile);
                        ImageView photo = new ImageView(PostAdActivity.this);
                        photo.setPadding(4,4,4,4);
                        Picasso.with(PostAdActivity.this).load(mediaFile).resize(64,64).centerCrop().into(photo);
                        mPhotoLayout.addView(photo);
                    } else {
                        MyToolBox.AlertMessage(this, "Oops", "Network Error. Please check your connection");
                    }
                }
            }
        }
        else if (resultCode != RESULT_CANCELED){
            Toast.makeText(this,"There was an error saving your photo",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onResume() {
        getInstance(this)
                .registerReceiver(mCategoryBroadcastReceiver, new IntentFilter(getString(R.string.pick_category)));

        getInstance(this)
                .registerReceiver(mStateBroadcastReceiver, new IntentFilter(getString(R.string.pick_state)));

        super.onResume();

    }

    @Override
    public void onPause() {
        getInstance(this).unregisterReceiver(
                mCategoryBroadcastReceiver);
        getInstance(this).unregisterReceiver(
                mStateBroadcastReceiver);

        super.onPause();
    }


    private File createFile(int mediaType) {
        if(isExternalStorageAvailable()){
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    getString(R.string.app_name));
            if(!mediaStorageDir.exists()){
                if(!mediaStorageDir.mkdirs()){
                    return null;
                }
            }
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(now);
            String path = mediaStorageDir.getPath() + File.separator;
            if (mediaType == MEDIA_TYPE_IMAGE){
                mediaFile = new File(path+"IMG_"+timestamp+".jpg");
            }
            else if (mediaType == MEDIA_TYPE_VIDEO){
                mediaFile = new File(path+"VID_"+timestamp+".mp4");
            }
            else{
                return null;
            }
            Toast.makeText(PostAdActivity.this,"File: "+Uri.fromFile(mediaFile),Toast.LENGTH_LONG).show();
            return mediaFile;
        }
        else{
            return null;
        }
    }

}
