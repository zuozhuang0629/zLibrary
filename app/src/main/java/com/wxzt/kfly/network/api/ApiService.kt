package com.wxzt.kfly.network.api


import com.wxzt.kfly.entitys.TaskDataEntity
import com.wxzt.kfly.entitys.UserInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 *  author : zuoz
 *  date : 2021/3/31 10:23
 *  description :
 */
interface ApiService {
    /**
     * 上传预览图
     *
     * @param access_token
     * @return
     */
    @Multipart
    @POST("prodfm/api/uavShotRealTimeDataTransfer")
    fun uploadPreview(
            @Part("userName") userName: RequestBody?,
            @Part("taskId") taskId: RequestBody?,
            @Part("altitude") altitude: Float,
            @Part("latitude") latitude: Double,
            @Part("longitude") longitude: Double,
            @Part("pitchDegrees") pitchDegrees: Float,
            @Part("aircraftHeadDirection") aircraftHeadDirection: Int,
            @Part("taskType") taskType: String?,
            @Part part: MultipartBody.Part?,
            @Header("Authorization") authorization: String): Call<ResponseBody?>?

    /**
     * 上传原始图
     * @param file
     * @param userName
     * @param token
     * @return
     */
    //    @POST("msmedia/api/singleImageUpload")
    @Multipart
    @POST("prodpc/api/imageUploadForApp")
    fun postOriginalImage(
            @Part("userName") userName: RequestBody,
            @Part file: MultipartBody.Part?,
            @Query("Authorization") token: String?): Call<ResponseBody?>?

    @POST("/prodfm/api/uavVideoRealTimeDataTransfer")
    fun uploadLiveInfo(@Body data: RequestBody?): Call<ResponseBody?>?


    /**
     * 获取任务
     *
     * @param status
     * @param taskType
     * @return
     */
    @GET("prodpc/api/mission/{status}/{taskType}")
//    @GET("api/mission/{status}/{taskType}")
    suspend fun getMissionInfo(@Path("taskType") taskType: String,
                               @Path("status") status: String,
                               @Header("Authorization") authorization: String): TaskDataEntity

    @GET("/prodmd/api/panpoint/panoramaPoint")
    fun panoramaPoint(): Call<Result<ResponseBody?>?>?

    //region mission
    /**
     * 增加项目
     */
    @POST("prodmd/api/projects")
    fun projects(@Body requestBody: RequestBody?): Call<Result<ResponseBody?>?>?

    /**
     * 增加街区
     */
    @POST("prodmd/api/blocks")
    fun blocks(@Body requestBody: RequestBody?): Call<Result<ResponseBody?>?>?

    /**
     * 增加飞行航线
     */
    @POST("prodmd/api/flightRoute")
    fun flightRoute(@Body requestBody: RequestBody?): Call<Result<ResponseBody?>?>?

    /**
     * 获取项目
     */
    @GET("prodmd/api/project")
    fun project(): Call<Result<ResponseBody?>?>?

    //endregion

    /**
     * 删除任务
     *
     * @param taskId
     * @return
     */
    @FormUrlEncoded
    @POST("mission/api/deleteFlyTask")
    suspend fun deleteMission(@Field("flyId") taskId: String): ResponseBody

    /**
     * 判断任务是否存在
     *
     * @param taskId
     * @return
     */
    //@FormUrlEncoded
    //@Headers("Authorization: Bearer d80ee24d-68c5-484b-b560-ddc4aff389fe")
    @GET("prodpc/api/missionIsExit/{taskId}")
    fun isMissionExit(@Path("taskId") taskId: String?,
                      @Query("access_token") access_token: String?,
                      @Header("Authorization") authorization: String?): Call<ResponseBody?>?

    /**
     * 创建任务
     */
    @POST("prodmd/api/mvls/t")
    fun createMission(@Body info: RequestBody?,
                      @Query("access_token") access_token: String?): Call<ResponseBody?>?

    @POST("prodfm/api/updateMissionState")
    suspend fun updateMissionState(@Body info: RequestBody,
                                   @Header("Authorization") access_token: String): Call<ResponseBody>

    /**
     * 上传无人机实时信息
     *
     * @param requestBody
     * @param access_token
     * @return
     */
    @POST("prodfm/api/uavRealTimeDataTransfer")
    fun updateRealUAVData(@Body requestBody: RequestBody,
                          @Query("access_token") access_token: String?): Call<ResponseBody?>?


    /**
     * 获取用户Token
     *
     * @param userName
     * @param password
     * @param grant_type 固定参数 password
     * @return
     */
    @FormUrlEncoded
    @Headers("Authorization: Basic RDgyRjgxMzRFMDFEMTFFOUE0RjM1MDQ2NUQ1NjAxQ0U6OTUyNjNFQTBFMDFFMTFFOUE0RjM1MDQ2NUQ1NjAxQ0U=")
    @POST("msauthserver/oauth/token")
    suspend fun getUserInfo(@Field("username") userName: String,
                            @Field("password") password: String,
                            @Field("grant_type") grant_type: String): UserInfo

}