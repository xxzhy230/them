package com.yijian.them.api;


import com.yijian.them.ui.home.GroupMoudle;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.ui.login.DataMoudle;
import com.yijian.them.ui.team.TeamMoudle;
import com.yijian.them.ui.team.moudle.TeamInfoMoudle;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface AuthApi {
    //"查看某个用户动态"

    String USERINFO = Http.baseUrl + "dynamic/user/";
    String STATISTICS = Http.baseUrl + "user/statistics";
    String USER = Http.baseUrl + "user/";
    String FOLLOW = Http.baseUrl + "follow/";
    String FOLLOWER = Http.baseUrl + "follow/follower/";
    String DYNAMICTAG = Http.baseUrl + "dynamic/tag/";
    String TAG = Http.baseUrl + "tag/";

    /**
     * 登录
     *
     * @param params
     * @return
     */
    @POST("app/login")
    Observable<JsonResult<DataMoudle.DataBean>> login(@Body Map<String, String> params);


    /**
     * 获取验证码
     *
     * @param phone
     * @return
     */
    @GET("app/sendMsgCode")
    Call<DataMoudle> getCode(@Query("phone") String phone);

    /**
     * 登录后发送验证码到绑定的手机号码
     *
     * @return
     */
    @GET("app/sendVerifyCode")
    Observable<JsonResult<DataMoudle>> sendVerifyCode();


    /**
     * 校验验证码
     *
     * @param phone
     * @param code
     * @return
     */
    @GET("app/verifyMsgCode")
    Observable<JsonResult<Boolean>> verifyCode(@Query("phone") String phone, @Query("code") String code);


    /**
     * 注册
     *
     * @return
     */
    @Multipart
    @POST("app/register")
    Observable<JsonResult<DataMoudle.DataBean>> register(@Part MultipartBody.Part avatar,
                                                         @Query("nickName") String nickName,
                                                         @Query("birthday") String birthday,
                                                         @Query("sex") String sex,
                                                         @Query("phone") String phone,
                                                         @Query("password") String password);

    /**
     * 获取用户信息
     *
     * @return
     */
    @GET("user/")
    Observable<JsonResult<DataMoudle.DataBean>> getUser();

    /**
     * 修改用户信息
     *
     * @return
     */
    @POST("user/")
    Observable<JsonResult<DataMoudle.DataBean>> setUserNickName(
            @Query("nickName") String nickName);

    @POST("user/")
    Observable<JsonResult<DataMoudle.DataBean>> setUserBirthday(
            @Query("birthday") String nickName);

    @Multipart
    @POST("user/")
    Observable<JsonResult<DataMoudle.DataBean>> setUserheadImg(
            @Part MultipartBody.Part avatar);

    @POST("user/")
    Observable<JsonResult<DataMoudle.DataBean>> setUserSign(
            @Query("sign") String sign);

    @POST("user/")
    Observable<JsonResult<DataMoudle.DataBean>> setUserSex(
            @Query("sex") String sex);

    /**
     * 获取统计数据,包括发布动态数，获赞数，关注数，粉丝数
     *
     * @return
     */
    @GET("user/statistics")
    Observable<JsonResult<DataMoudle.DataBean>> getStatistics(@Query("othersId") String othersId);


    /**
     * 设置密码
     *
     * @param type 0:通过旧密码修改 1:通过手机号发送验证码修改
     * @return
     */
    @POST("user/password")
    Observable<JsonResult<Object>> settingPwd(@Body Map<String, String> params,
                                              @Query("type") String type);

    /**
     * 重置密码
     *
     * @return
     */
    @POST("user/password/forgot")
    Observable<JsonResult<Object>> forgot(@Body Map<String, String> params);

    /**
     * 忘记密码
     *
     * @param pwd
     * @return
     */
    @FormUrlEncoded
    @POST("user/resetpassword")
    Observable<JsonResult<Object>> forgetPwd(@Field("phone") String phone,
                                             @Field("password") String pwd,
                                             @Field("smscode") String smscode);

    /**
     * 获取小队信息
     */
    @GET("v2/team/nearby")
    Observable<JsonResult<List<TeamMoudle.DataBean>>> getTeamList(@Query("cityCode") String cityCode,
                                                                  @Query("latitude") String latitude,
                                                                  @Query("longitude") String longitude,
                                                                  @Query("page") String page,
                                                                  @Query("radius") String radius);

    /**
     * 反馈信息
     *
     * @param contact
     * @param content
     * @param userId
     * @return
     */
    @Multipart
    @POST("user/feedback")
    Observable<JsonResult<String>> feedback(@Query("contact") String contact,
                                            @Query("content") String content,
                                            @Part MultipartBody.Part avatar,
                                            @Query("userId") String userId);

    /**
     * 反馈信息
     *
     * @param contact
     * @param content
     * @param userId
     * @return
     */
    @POST("user/feedback")
    Observable<JsonResult<String>> feedback(@Query("contact") String contact,
                                            @Query("content") String content,
                                            @Query("userId") String userId);

    /**
     * 获取推荐动态
     */
    @GET("dynamic/recommended")
    Observable<JsonResult<List<HomeMoudle.DataBean>>> recommended(@Query("page") int page);

    /**
     * 获取推荐动态
     */
    @GET("dynamic/newest")
    Observable<JsonResult<List<HomeMoudle.DataBean>>> dynamicNew(@Query("page") int page);


    /**
     * 获取推荐动态
     */
    @GET("dynamic/nearby")
    Observable<JsonResult<List<HomeMoudle.DataBean>>> nearby(@Query("cityCode") String cityCode,
                                                             @Query("latitude") String latitude,
                                                             @Query("longitude") String longitude,
                                                             @Query("page") int page,
                                                             @Query("radius") String radius);

    /**
     * 用户协议
     */
    @GET("app/userAgreement")
    Observable<JsonResult<String>> userAgreement();

    /**
     * 点赞
     */
    @POST("dynamic/like")
    Observable<JsonResult<DataMoudle.DataBean>> like(@Body Map<String, String> params);

    /**
     * 删除动态
     */
    @DELETE("dynamic/{dynamicId}")
    Observable<JsonResult<String>> delDynamic(@Path("dynamicId") String dynamicId);


    /**
     * 发送动态有图片
     *
     * @return
     */
    @Multipart
    @POST("dynamic")
    Observable<JsonResult<String>> sendDynamic(@Part MultipartBody.Part[] images,
                                               @Query("cityCode") String cityCode,
                                               @Query("content") String content,
                                               @Query("groupId") String groupId,
                                               @Query("groupName") String groupName,
                                               @Query("latitude") String latitude,
                                               @Query("longitude") String longitude,
                                               @Query("localName") String localName,
                                               @Query("tagId") String tagId,
                                               @Query("tagName") String tagName);
    /**
     * 发送动态无图片
     *
     * @return
     */
    @POST("dynamic")
    Observable<JsonResult<String>> sendDynamic(@Query("cityCode") String cityCode,
                                               @Query("content") String content,
                                               @Query("groupId") String groupId,
                                               @Query("groupName") String groupName,
                                               @Query("latitude") String latitude,
                                               @Query("longitude") String longitude,
                                               @Query("localName") String localName,
                                               @Query("tagId") String tagId,
                                               @Query("tagName") String tagName);

    /**
     * 发送动态有视频
     *
     * @return
     */
    @Multipart
    @POST("dynamic")
    Observable<JsonResult<String>> sendDynamicVideo(@Part MultipartBody.Part video,
                                                    @Query("cityCode") String cityCode,
                                                    @Query("content") String content,
                                                    @Query("groupId") String groupId,
                                                    @Query("groupName") String groupName,
                                                    @Query("latitude") String latitude,
                                                    @Query("longitude") String longitude,
                                                    @Query("localName") String localName,
                                                    @Query("tagId") String tagId,
                                                    @Query("tagName") String tagName);

    /**
     * 获取小队列表
     */
    @POST("team/v2")
    Observable<JsonResult<String>> creatTeam(@Query("cityCode") String cityCode,
                                             @Query("teamName") String content,
                                             @Query("teamDesc") String groupId,
                                             @Query("latitude") String latitude,
                                             @Query("longitude") String longitude,
                                             @Query("localName") String localName);

    /**
     * 获取话题列表
     */
    @GET("topic")
    Observable<JsonResult<List<GroupMoudle.DataBean>>> topic();

    /**
     * 获取话题列表
     */
    @GET("tag")
    Observable<JsonResult<List<GroupMoudle.DataBean>>> tag(@Query("page") String page,
                                                           @Query("topicId") String topicId);

    /**
     * 查看单条动态
     */
    @GET("dynamic/{dynamicId}")
    Observable<JsonResult<HomeMoudle.DataBean>> dynamic(@Path("dynamicId") String dynamicId);

    /**
     * 获取评论
     */
    @GET("v2/comment")
    Observable<JsonResult<List<GroupMoudle.DataBean>>> comment(@Query("dynamicId") String dynamicId);

    /**
     * 发送评论
     */
    @POST("v2/comment")
    Observable<JsonResult<String>> sendComment(@Body Map<String, String> params);

    /**
     * 回复评论
     */
    @POST("reply")
    Observable<JsonResult<String>> sendReply(@Body Map<String, String> params);

    /**
     * 屏蔽动态
     */
    @POST("blacklist/dynamic/{dynamicId}")
    Observable<JsonResult<String>> blackDynamic(@Path("dynamicId") String dynamicId);

    /**
     * 加入黑名单
     */
    @POST("blacklist/user/{userId}")
    Observable<JsonResult<String>> blackUser(@Path("userId") String userId);

    /**
     * 举报动态
     */
    @POST("dynamicReport")
    Observable<JsonResult<String>> dynamicReport(@Query("dynamicId") String dynamicId,
                                                 @Query("reason") String reason,
                                                 @Query("reporterId") String reporterId);

    /**
     * 获取回复列表
     */
    @GET("v2/comment/{commentId}/reply")
    Observable<JsonResult<List<GroupMoudle.DataBean>>> reportList(@Path("commentId") String commentId);

    /**
     * 删除评论
     */
    @DELETE("v2/comment/{commentId}")
    Observable<JsonResult<String>> delComment(@Path("commentId") String commentId);

    /**
     * 举报评论
     */
    @POST("commentReport/")
    Observable<JsonResult<String>> commentReport(@Body Map<String, String> params);

    /**
     * 获取小队信息
     */
    @GET("v2/team/{teamId}")
    Observable<JsonResult<TeamInfoMoudle.DataBean>> teamInfo(@Path("teamId") String teamId);

    /**
     * 加入或退出小队
     * 0表示退出小队，1表示加入小队
     */
    @PUT("v2/team/{teamId}")
    Observable<JsonResult<String>> teamOutOrAdd(@Path("teamId") String teamId,
                                                @Query("type") String type);

    /**
     * 我参与的小队
     */
    @GET("v2/team/")
    Observable<JsonResult<List<TeamMoudle.DataBean>>> teamList();

    /**
     * 我参与的小队
     */
    @Multipart
    @POST("v2/team/")
    Observable<JsonResult<Object>> creatTeam(@Query("cityCode") String cityCode,
                                             @Query("latitude") String latitude,
                                             @Query("longitude") String longitude,
                                             @Query("localName") String localName,
                                             @Query("teamDesc") String teamDesc,
                                             @Query("teamName") String teamName,
                                             @Query("teamImgHeight") int teamImgHeight,
                                             @Query("teamImgWidth") int teamImgWidth,
                                             @Part MultipartBody.Part[] parts);

    /**
     * 获取随机话题
     */
    @GET("tag/random")
    Observable<JsonResult<List<GroupMoudle.DataBean>>> random();

    /**
     * 获取随机话题
     */
    @GET("search/hot")
    Observable<JsonResult<List<String>>> hot();

    /**
     * 获取关注人动态
     *
     * @param page
     * @return
     */
    @GET("dynamic/following")
    Observable<JsonResult<List<HomeMoudle.DataBean>>> following(@Query("page") String page);

    /**
     * 查询单条标签
     *
     * @param tagId
     * @return
     */
    @GET("tag/{tagId}")
    Observable<JsonResult<GroupMoudle.DataBean>> tagInfo(@Path("tagId") String tagId);

    /**
     * 查询单条标签
     *
     * @param tagId
     * @param page
     * @param type
     * @return
     */
    @GET("dynamic/tag/{tagId}")
    Observable<JsonResult<List<HomeMoudle.DataBean>>> dynamicTag(@Path("tagId") String tagId,
                                                                 @Query("page") String page,
                                                                 @Query("type") String type);

    /**
     * 获取标签下的群聊
     *
     * @param tagId
     * @return
     */
    @GET("tag/{tagId}/groups")
    Observable<JsonResult<List<String>>> tagGroups(@Path("tagId") String tagId);

    /**
     * 获取标签下的群聊
     *
     * @param tagId
     * @return
     */
    @POST("tag/{tagId}")
    Observable<JsonResult<GroupMoudle.DataBean>> followedTag(@Path("tagId") String tagId);

    /**
     * 举报标签
     *
     * @return
     */
    @POST("tagReport")
    Observable<JsonResult<String>> tagReport(@Body Map<String, String> params);

    /**
     * 创建标签
     *
     * @return
     */
    @Multipart
    @POST("tag")
    Observable<JsonResult<String>> tag(@Part MultipartBody.Part[] parts,
                                       @Query("tagName") String tagName,
                                       @Query("tagDesc") String tagDesc,
                                       @Query("topicId") String topicId);

    /**
     * 获取话题列表
     */
    @GET("tag/following")
    Observable<JsonResult<List<GroupMoudle.DataBean>>> followList(@Query("page") String page);

    /**
     * 关键字搜索
     */
    @POST("search")
    Observable<JsonResult<List<GroupMoudle.DataBean>>> search(@Body Map<String, String> params);

}
