package com.doxa360.android.dutch;

import com.doxa360.android.dutch.model.Ad;
import com.doxa360.android.dutch.model.Category;
import com.doxa360.android.dutch.model.State;
import com.doxa360.android.dutch.model.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

import static android.R.attr.description;

/**
 * Created by Apple on 03/01/2017.
 */

public interface DutchApiInterface {

    @POST(Dutch.SIGN_UP_API)
    Call<User> signUpUser(@Body User user);

    @POST(Dutch.SIGN_IN_API)
    Call<User> signInUser(@Body User user);

    @GET(Dutch.ALL_RECENT_ITEMS)
    Call<List<Ad>> allItems();

    @GET(Dutch.ALL_USER_ADS)
    Call<List<Ad>> userAds(@Query("user_id") int user_id);

    @GET(Dutch.ALL_CATEGORIES)
    Call<List<Category>> allCategories();

    @GET(Dutch.ALL_STATES)
    Call<List<State>> allStates();

    @GET(Dutch.ALL_CATEGORY_ITEMS)
    Call<List<Ad>> allItemsByCategory(@Query("category_id") int category);

//    @POST(Dutch.SIGN_IN_USER_API)
//    Call<User> signInUsername(@Body User user);
//
//    @GET(Dutch.GET_USER_DETAILS_API)
//    Call<User> getUserDetails(@Query(Dutch.TOKEN) String token);
//
//    @POST(Dutch.EDIT_PROFILE_API)
//    Call<User> editUserProfile(@Body User user,
//                               @Query(Dutch.TOKEN) String token);
//
//    @POST(Dutch.EDIT_PHONE_API)
//    Call<User> editUserPhone(@Body User user,
//                             @Query(Dutch.TOKEN) String token);
//
//    @GET(Dutch.SEARCH_PHONE_API)
//    Call<List<User>> searchPhoneAvailability(@Query(Dutch.PHONE) String phone,
//                                             @Query(Dutch.TOKEN) String token);

    @GET(Dutch.SEARCH_API)
    Call<List<Ad>> search(@Query(Dutch.Q) String search);


    @GET(Dutch.GET_USER_BY_INDUSTRY_API)
    Call<List<User>> getUserByIndustry(@Query(Dutch.TOKEN) String token,
                                       @Query (Dutch.INDUSTRY) String industry);

    @Multipart
    @POST(Dutch.UPLOAD_PHOTO_API)
    Call<Ad> uploadProfilePhoto(//@Part("file_name") RequestBody description,
//                                          @Part MultipartBody.Part file,
//                                          @Part("") Ad ad,
//                                @PartMap() Map<String, RequestBody> partMap,
//                                @PartMap() Map<String, Ad> partAd,
                                @PartMap() Map<String, RequestBody> partMap,
                                @Part("file[]") RequestBody fileKey,
                                          @Part List<MultipartBody.Part> files);



//    @POST(Dutch.BACKUP_CONTACTS)
//    Call<CallNote> saveCallNote(@Query("token") String token,
//                                @Body CallNote callNote);
}
