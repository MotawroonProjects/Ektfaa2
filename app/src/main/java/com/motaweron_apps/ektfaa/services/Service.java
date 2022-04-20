package com.motaweron_apps.ektfaa.services;


import com.motaweron_apps.ektfaa.model.AllAreaModel;
import com.motaweron_apps.ektfaa.model.AllCategoryModel;
import com.motaweron_apps.ektfaa.model.AllDriverModel;
import com.motaweron_apps.ektfaa.model.CarTypeDataModel;
import com.motaweron_apps.ektfaa.model.DepartmentDataModel;
import com.motaweron_apps.ektfaa.model.DriverFilterDataModel;
import com.motaweron_apps.ektfaa.model.FilterDataModel;
import com.motaweron_apps.ektfaa.model.NotificationDataModel;
import com.motaweron_apps.ektfaa.model.OrderDataModel;
import com.motaweron_apps.ektfaa.model.PackageDataModel;
import com.motaweron_apps.ektfaa.model.PlaceGeocodeData;
import com.motaweron_apps.ektfaa.model.PlaceMapDetailsData;
import com.motaweron_apps.ektfaa.model.PlacesDataModel;
import com.motaweron_apps.ektfaa.model.ProductDataModel;
import com.motaweron_apps.ektfaa.model.ReasonDataModel;
import com.motaweron_apps.ektfaa.model.SendOrderModel;
import com.motaweron_apps.ektfaa.model.SettingModel;
import com.motaweron_apps.ektfaa.model.SingleChaletsModel;
import com.motaweron_apps.ektfaa.model.SingleDepartmentModel;
import com.motaweron_apps.ektfaa.model.SingleFamilyModel;
import com.motaweron_apps.ektfaa.model.SingleMessageModel;
import com.motaweron_apps.ektfaa.model.SingleOrderModel;
import com.motaweron_apps.ektfaa.model.SingleProductDataModel;
import com.motaweron_apps.ektfaa.model.SingleProductModel;
import com.motaweron_apps.ektfaa.model.Slider_Model;
import com.motaweron_apps.ektfaa.model.StatusResponse;
import com.motaweron_apps.ektfaa.model.UserModel;
import com.motaweron_apps.ektfaa.model.UsersDataModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface Service {


    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);


    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(@Field("phone_code") String phone_code,
                          @Field("phone") String phone
    );

    @Multipart
    @POST("api/client-register")
    Call<UserModel> clientSignUp(@Part("name") RequestBody name,
                                 @Part("phone_code") RequestBody phone_code,
                                 @Part("phone") RequestBody phone,
                                 @Part("software_type") RequestBody software_type,
                                 @Part MultipartBody.Part image
    );

    @Multipart
    @POST("api/client-update-register")
    Call<UserModel> updateClientProfile(@Header("Authorization") String bearer_token,
                                        @Part("user_id") RequestBody user_id,
                                        @Part("name") RequestBody name,
                                        @Part("phone_code") RequestBody phone_code,
                                        @Part("phone") RequestBody phone,
                                        @Part("software_type") RequestBody software_type,
                                        @Part MultipartBody.Part image
    );

    @Multipart
    @POST("api/driver-register")
    Call<UserModel> driverSignUp(@Part("name") RequestBody name,
                                 @Part("car_type_id") RequestBody car_type_id,
                                 @Part("latitude") RequestBody latitude,
                                 @Part("longitude") RequestBody longitude,
                                 @Part("address") RequestBody address,
                                 @Part("area_id") RequestBody area_id,
                                 @Part("phone_code") RequestBody phone_code,
                                 @Part("phone") RequestBody phone,
                                 @Part("software_type") RequestBody software_type,
                                 @Part MultipartBody.Part image
    );

    @Multipart
    @POST("api/driver-update-register")
    Call<UserModel> updateDriverProfile(@Header("Authorization") String bearer_token,
                                        @Part("user_id") RequestBody user_id,
                                        @Part("name") RequestBody name,
                                        @Part("car_type_id") RequestBody car_type_id,
                                        @Part("area_id") RequestBody area_id,
                                        @Part("latitude") RequestBody latitude,
                                        @Part("longitude") RequestBody longitude,
                                        @Part("address") RequestBody address,
                                        @Part("phone_code") RequestBody phone_code,
                                        @Part("phone") RequestBody phone,
                                        @Part("software_type") RequestBody software_type,
                                        @Part MultipartBody.Part image
    );


    @Multipart
    @POST("api/chalet-register")
    Call<UserModel> chaletSignUp(@Part("name") RequestBody name,
                                 @Part("area_id") RequestBody area_id,
                                 @Part("phone_code") RequestBody phone_code,
                                 @Part("phone") RequestBody phone,
                                 @Part("software_type") RequestBody software_type,
                                 @Part MultipartBody.Part image
    );

    @Multipart
    @POST("api/chalet-update-register")
    Call<UserModel> updateProfileChalet(@Header("Authorization") String bearer_token,
                                        @Part("user_id") RequestBody user_id,
                                        @Part("name") RequestBody name,
                                        @Part("area_id") RequestBody area_id,
                                        @Part("phone_code") RequestBody phone_code,
                                        @Part("phone") RequestBody phone,
                                        @Part("software_type") RequestBody software_type,
                                        @Part MultipartBody.Part image
    );


    @Multipart
    @POST("api/family-register")
    Call<UserModel> familySignUp(@Part("name") RequestBody name,
                                 @Part("area_id") RequestBody area_id,
                                 @Part("phone_code") RequestBody phone_code,
                                 @Part("phone") RequestBody phone,
                                 @Part("latitude") RequestBody latitude,
                                 @Part("longitude") RequestBody longitude,
                                 @Part("address") RequestBody address,
                                 @Part("basic_department_id") RequestBody basic_department_id,
                                 @Part("sub_department_id") RequestBody sub_department_id,
                                 @Part("software_type") RequestBody software_type,
                                 @Part MultipartBody.Part image
    );

    @Multipart
    @POST("api/family-update-register")
    Call<UserModel> updateProfileFamily(@Header("Authorization") String bearer_token,
                                        @Part("user_id") RequestBody user_id,
                                        @Part("name") RequestBody name,
                                        @Part("area_id") RequestBody area_id,
                                        @Part("phone_code") RequestBody phone_code,
                                        @Part("phone") RequestBody phone,
                                        @Part("latitude") RequestBody latitude,
                                        @Part("longitude") RequestBody longitude,
                                        @Part("address") RequestBody address,
                                        @Part("basic_department_id") RequestBody basic_department_id,
                                        @Part("sub_department_id") RequestBody sub_department_id,
                                        @Part("software_type") RequestBody software_type,
                                        @Part MultipartBody.Part image
    );


    @GET("api/get-departments-with-sub-departments")
    Call<DepartmentDataModel> getFamilyDepartmentWithSubDepartment();

    @GET("api/get-cars-type")
    Call<CarTypeDataModel> getCarType();

    @GET("api/setting")
    Call<SettingModel> getSetting();

    @GET("api/areas")
    Call<AllAreaModel> getArea();

    @GET("api/slider")
    Call<Slider_Model> get_slider(@Query("area_id") int area_id);

    @GET("api/places")
    Call<PlacesDataModel> get_chalets(@Query("area_id") String area_id,
                                      @Query("search_name") String search_name,
                                      @Query("latitude") double latitude,
                                      @Query("longitude") double longitude,
                                      @Query("order_by_type") String order_by_type,
                                      @Query("order_by") String order_by


    );

    @GET("api/one-place")
    Call<SingleChaletsModel> getChaletById(@Query("place_id") String place_id

    );

    @GET("api/drivers")
    Call<AllDriverModel> get_drivers(@Query("driver_id") String driver_id,
                                     @Query("area_id") int area_id,
                                     @Query("search_name") String search_name,
                                     @Query("latitude") double latitude,
                                     @Query("longitude") double longitude,
                                     @Query("order_by_type") String order_by_type,
                                     @Query("car_type_id") String car_type_id

    );

    @GET("api/basic-departments")
    Call<AllCategoryModel> getCategories();


    @GET("api/one-family")
    Call<SingleFamilyModel> get_oneFamily(@Query("family_id") String family_id

    );

    @FormUrlEncoded
    @POST("api/contact-us")
    Call<StatusResponse> contactUs(@Field("name") String name,
                                   @Field("email") String email,
                                   @Field("subject") String subject,
                                   @Field("message") String message


    );

    @FormUrlEncoded
    @POST("api/firebase-tokens")
    Call<StatusResponse> updateFirebaseToken(@Header("Authorization") String bearer_token,
                                             @Field("user_id") String user_id,
                                             @Field("phone_token") String phone_token,
                                             @Field("software_type") String software_type


    );

    @FormUrlEncoded
    @POST("api/logout")
    Call<StatusResponse> logout(@Header("Authorization") String bearer_token,
                                @Field("user_id") String user_id,
                                @Field("phone_token") String phone_token


    );

    @GET("api/families")
    Call<UsersDataModel> searchFamily(@Query("area_id") String area_id,
                                      @Query("basic_department_id") String basic_department_id,
                                      @Query("latitude") String latitude,
                                      @Query("longitude") String longitude,
                                      @Query("search_name") String search_name,
                                      @Query("sub_department_id[]") List<String> subDeptList


    );

    @GET("api/filter-result")
    Call<FilterDataModel> getSubDepartment(@Query("basic_department_id") String basic_department_id

    );

    @GET("api/food-details")
    Call<SingleProductDataModel> getProductById(@Query("food_id") String food_id,
                                                @Query("user_id") String user_id

    );

    @FormUrlEncoded
    @POST("api/add-department")
    Call<SingleDepartmentModel> addCategory(@Header("Authorization") String bearer_token,
                                            @Field("user_id") String user_id,
                                            @Field("title") String title


    );

    @FormUrlEncoded
    @POST("api/edit-department")
    Call<SingleDepartmentModel> editCategory(@Header("Authorization") String bearer_token,
                                             @Field("user_id") String user_id,
                                             @Field("department_id") String department_id,
                                             @Field("title") String title


    );

    @FormUrlEncoded
    @POST("api/delete-department")
    Call<StatusResponse> deleteCategory(@Header("Authorization") String bearer_token,
                                        @Field("user_id") String user_id,
                                        @Field("department_id") String department_id


    );

    @GET("api/departments")
    Call<DepartmentDataModel> getFamilyCategory(@Header("Authorization") String bearer_token,
                                                @Query("user_id") String user_id);

    @GET("api/show-foods")
    Call<ProductDataModel> getFamilyProductByCategoryId(@Header("Authorization") String bearer_token,
                                                        @Query("user_id") String user_id,
                                                        @Query("department_id") String department_id
    );

    @FormUrlEncoded
    @POST("api/delete-food")
    Call<StatusResponse> deleteFamilyProduct(@Header("Authorization") String bearer_token,
                                             @Field("user_id") String user_id,
                                             @Field("food_id") String food_id);

    @GET("api/food-details")
    Call<SingleProductModel> getProductDataById(@Query("food_id") String product_id

    );

    @FormUrlEncoded
    @POST("api/delete-food-image")
    Call<StatusResponse> deleteProductImage(@Header("Authorization") String bearer_token,
                                            @Field("food_image_id") String food_image_id
    );

    @Multipart
    @POST("api/add-food")
    Call<StatusResponse> addProduct(@Header("Authorization") String bearer_token,
                                    @Part("user_id") RequestBody user_id,
                                    @Part("title") RequestBody title,
                                    @Part("department_id") RequestBody department_id,
                                    @Part("details") RequestBody details,
                                    @Part("area_id") RequestBody area_id,
                                    @Part("price") RequestBody price,
                                    @Part("have_offer") RequestBody have_offer,
                                    @Part("offer_type") RequestBody offer_type,
                                    @Part("offer_value") RequestBody offer_value,
                                    @Part List<MultipartBody.Part> attributes,
                                    @Part List<MultipartBody.Part> images


    );

    @Multipart
    @POST("api/edit-food")
    Call<StatusResponse> updateProduct(@Header("Authorization") String bearer_token,
                                       @Part("food_id") RequestBody food_id,
                                       @Part("user_id") RequestBody user_id,
                                       @Part("title") RequestBody title,
                                       @Part("department_id") RequestBody department_id,
                                       @Part("details") RequestBody details,
                                       @Part("area_id") RequestBody area_id,
                                       @Part("price") RequestBody price,
                                       @Part("have_offer") RequestBody have_offer,
                                       @Part("offer_type") RequestBody offer_type,
                                       @Part("offer_value") RequestBody offer_value,
                                       @Part List<MultipartBody.Part> images


    );


    @Multipart
    @POST("api/add-place")
    Call<StatusResponse> chaletAddAds(@Header("Authorization") String bearer_token,
                                      @Part("user_id") RequestBody user_id,
                                      @Part("title") RequestBody title,
                                      @Part("details") RequestBody details,
                                      @Part("area_id") RequestBody area_id,
                                      @Part("price") RequestBody price,
                                      @Part("address") RequestBody address,
                                      @Part("latitude") RequestBody latitude,
                                      @Part("longitude") RequestBody longitude,
                                      @Part List<MultipartBody.Part> attributes,
                                      @Part List<MultipartBody.Part> images


    );

    @Multipart
    @POST("api/edit-place")
    Call<StatusResponse> chaletUpdateAds(@Header("Authorization") String bearer_token,
                                         @Part("user_id") RequestBody user_id,
                                         @Part("place_id") RequestBody place_id,
                                         @Part("title") RequestBody title,
                                         @Part("details") RequestBody details,
                                         @Part("area_id") RequestBody area_id,
                                         @Part("price") RequestBody price,
                                         @Part("address") RequestBody address,
                                         @Part("latitude") RequestBody latitude,
                                         @Part("longitude") RequestBody longitude,
                                         @Part List<MultipartBody.Part> attributes,
                                         @Part List<MultipartBody.Part> images


    );


    @FormUrlEncoded
    @POST("api/family-update-status")
    Call<UserModel> updateFamilyStatus(@Header("Authorization") String bearer_token,
                                       @Field("user_id") String user_id,
                                       @Field("status") String status
    );

    @FormUrlEncoded
    @POST("api/driver-update-status")
    Call<UserModel> updateDriverStatus(@Header("Authorization") String bearer_token,
                                       @Field("user_id") String user_id,
                                       @Field("status") String status
    );

    @GET("api/{endPoint}")
    Call<PackageDataModel> getPackages(@Path("endPoint") String endPoint,
                                       @QueryMap Map<String, String> queriesMap
    );

    @GET("api/online-families")
    Call<UsersDataModel> getAvailableFamily(@Header("Authorization") String bearer_token,
                                            @Query("area_id") String area_id


    );

    @POST("api/add-order-delivery")
    Call<StatusResponse> sendOrder(@Header("Authorization") String bearer_token,
                                   @Body SendOrderModel model
    );


    @GET("api/new-orders")
    Call<OrderDataModel> getOrder(@Header("Authorization") String bearer_token,
                                  @Query("driver_id") String driver_id


    );

    @GET("api/new-client-orders")
    Call<OrderDataModel> getClientNewOrder(@Header("Authorization") String bearer_token,
                                           @Query("user_id") String user_id


    );

    @FormUrlEncoded
    @POST("api/update-location")
    Call<StatusResponse> updateLocation(@Header("Authorization") String bearer_token,
                                        @Field("user_id") String user_id,
                                        @Field("latitude") String latitude,
                                        @Field("longitude") String longitude

    );

    @GET("api/show-order_chat")
    Call<SingleOrderModel> getOrderById(@Header("Authorization") String bearer_token,
                                        @Query("order_id") String order_id


    );

    @FormUrlEncoded
    @POST("api/driver-change-offer-for-client")
    Call<StatusResponse> sendAnotherOffer(@Header("Authorization") String bearer_token,
                                          @Field("user_id") String user_id,
                                          @Field("driver_id") String driver_id,
                                          @Field("order_id") String order_id,
                                          @Field("delivery_value") String delivery_value


    );

    @FormUrlEncoded
    @POST("api/driver-refuse-or-accept-and-make-offer")
    Call<StatusResponse> sendOffer(@Header("Authorization") String bearer_token,
                                   @Field("user_id") String user_id,
                                   @Field("driver_id") String driver_id,
                                   @Field("order_id") String order_id,
                                   @Field("status") String status,
                                   @Field("delivery_value") String delivery_value,
                                   @Field("refuse_reason") String refuse_reason


    );

    @FormUrlEncoded
    @POST("api/delete-order")
    Call<StatusResponse> cancelOrder(@Header("Authorization") String bearer_token,
                                     @Field("user_id") String user_id,
                                     @Field("order_id") String order_id


    );


    @FormUrlEncoded
    @POST("api/driver-refuse-or-accept-and-make-offer")
    Call<StatusResponse> refuseOrder(@Header("Authorization") String bearer_token,
                                     @Field("user_id") String user_id,
                                     @Field("driver_id") String driver_id,
                                     @Field("order_id") String order_id,
                                     @Field("status") String status,
                                     @Field("refuse_reason") String refuse_reason


    );

    @FormUrlEncoded
    @POST("api/client-change-driver")
    Call<StatusResponse> chooseAnotherDriver(@Header("Authorization") String bearer_token,
                                             @Field("user_id") String user_id,
                                             @Field("driver_id") String driver_id,
                                             @Field("order_id") String order_id

    );

    @FormUrlEncoded
    @POST("api/accept-or-refuse-driver-offer")
    Call<StatusResponse> acceptOffer(@Header("Authorization") String bearer_token,
                                     @Field("user_id") String user_id,
                                     @Field("driver_id") String driver_id,
                                     @Field("order_id") String order_id,
                                     @Field("status") String status,
                                     @Field("refuse_reason") String refuse_reason


    );

    @GET("api/{endPoint}")
    Call<OrderDataModel> getOrders(@Header("Authorization") String bearer_token,
                                   @Path("endPoint") String endPoint,
                                   @Query("user_id") String user_id


    );

    @GET("api/current-driver-orders")
    Call<OrderDataModel> getDriverCurrentOrders(@Header("Authorization") String bearer_token,
                                                @Query("driver_id") String user_id


    );

    @GET("api/old-driver-orders")
    Call<OrderDataModel> getDriverPreviousOrders(@Header("Authorization") String bearer_token,
                                                 @Query("driver_id") String user_id
    );

    @GET("api/filter-driver-result")
    Call<DriverFilterDataModel> getDriverAreaCarType();

    @GET("api/notifications")
    Call<NotificationDataModel> getNotification(@Header("Authorization") String bearer_token,
                                                @Query("user_id") String user_id);

    @Multipart
    @POST("api/create-slider")
    Call<StatusResponse> addAds(@Header("Authorization") String bearer_token,
                                @Part("user_id") RequestBody user_id,
                                @Part("link") RequestBody link,
                                @Part("num_days") RequestBody num_days,
                                @Part("total_cost") RequestBody total_cost,
                                @Part MultipartBody.Part image


    );

    @FormUrlEncoded
    @POST("api/add-message")
    Call<SingleMessageModel> sendChatMessage(@Header("Authorization") String bearer_token,
                                             @Field("order_chat_id") String order_chat_id,
                                             @Field("from_user_id") String from_user_id,
                                             @Field("to_user_id") String to_user_id,
                                             @Field("type") String type,
                                             @Field("message") String message

    );

    @FormUrlEncoded
    @POST("api/add-message")
    Call<SingleMessageModel> sendLocationChatMessage(@Header("Authorization") String bearer_token,
                                                     @Field("order_chat_id") String order_chat_id,
                                                     @Field("from_user_id") String from_user_id,
                                                     @Field("to_user_id") String to_user_id,
                                                     @Field("type") String type,
                                                     @Field("message") String message,
                                                     @Field("latitude") String latitude,
                                                     @Field("longitude") String longitude


    );

    @Multipart
    @POST("api/add-message")
    Call<SingleMessageModel> sendChatImage(@Header("Authorization") String bearer_token,
                                           @Part("order_chat_id") RequestBody order_chat_id,
                                           @Part("from_user_id") RequestBody from_user_id,
                                           @Part("to_user_id") RequestBody to_user_id,
                                           @Part("type") RequestBody type,
                                           @Part MultipartBody.Part image


    );

    @GET("api/order-client-refuses")
    Call<ReasonDataModel> getClientRefuseReasons(@Header("Authorization") String bearer_token

    );

    @GET("api/order-driver-r    efuses")
    Call<ReasonDataModel> getDriverRefuseReasons(@Header("Authorization") String bearer_token

    );

    @FormUrlEncoded
    @POST("api/driver-delivered-order-to-client")
    Call<StatusResponse> deliveredOrder(@Header("Authorization") String bearer_token,
                                        @Field("user_id") String user_id,
                                        @Field("driver_id") String driver_id,
                                        @Field("order_id") String order_id


    );

    @GET("api/get-places")
    Call<PlacesDataModel> get_MyChalets(@Header("Authorization") String bearer_token,
                                        @Query("user_id") String user_id


    );

    @FormUrlEncoded
    @POST("api/delete-place")
    Call<StatusResponse> deleteMyChalet(@Header("Authorization") String bearer_token,
                                        @Field("user_id") String user_id,
                                        @Field("place_id") String place_id
    );


    @FormUrlEncoded
    @POST("api/delete-place-image")
    Call<StatusResponse> deleteChaletImage(@Header("Authorization") String bearer_token,
                                           @Field("place_image_id") String food_image_id
    );

}