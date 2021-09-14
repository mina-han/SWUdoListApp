 package com.example.swudolistapp

import retrofit2.Call
import retrofit2.http.*
import java.util.*

 interface MyAPI {

    @FormUrlEncoded
    @POST("/app_login/")
    fun requestLogin(
            @Field("user_id") user_id: String,
            @Field("user_pw") user_pw: String,
    ): Call<PostItem>

    @FormUrlEncoded
    @POST("/app_register/")
    fun requestRegister(
        @Field("user_r_id") user_r_id: String,
        @Field("user_r_pw") user_r_pw: String,
        @Field("user_r_email") user_r_email: String,
        @Field("user_tot_subject") user_tot_subject: String,
    ): Call<PostItem>

    @FormUrlEncoded
    @POST("/app_logout/")
    fun requestLogout(
        @Field("user_lo_id") user_lo_id: String,
    ): Call<PostItem>

    @FormUrlEncoded
    @PUT("/app_update/")
    fun requestChange(
        @Field("user_update_id") user_update_id: String,
        @Field("user_update_subject") user_update_subject: String,
    ): Call<PostItem>

    @GET("/app_get_user/")
    fun getUser(
        @Query("user_id") user_id: String,
    ): Call<GetInfo>

    @FormUrlEncoded
    @POST("/app_add_todolist/")
    fun addTodolist(
        @Field("subject_code") subject_code: String,
        @Field("context") context: String,
    ): Call<PostItem>

    @GET("/app_get_todolist/")
    fun getTodolist(
        @Query("subject_code") subject_code: String,
    ): Call<List<ToDoListData>>

    @FormUrlEncoded
    @POST("/app_edit_post/")
    fun editPost(
        @Field("author") author: String,
        @Field("title") title: String,
        @Field("context") context: String,
        @Field("subject") subject: String,
    ): Call<EditItem>

    @GET("/app_get_posts/")
    fun getPost(
        @Query("subject") subject: String,
    ): Call<List<BoardData>>

}