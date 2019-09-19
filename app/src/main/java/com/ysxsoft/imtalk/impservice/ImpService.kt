package com.ysxsoft.imtalk.impservice

import com.ysxsoft.imtalk.bean.*
import com.ysxsoft.imtalk.com.RoomStarBean
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*
import rx.Observable


/**
 *Create By 胡
 *on 2019/7/22 0022
 */

interface ImpService {
    //注册
    @POST("login/register")
    fun Register(@Query("mobile") mobile: String,
                 @Query("password") password: String,
                 @Query("pwd") pwd: String,
                 @Query("code") code: String,
                 @Query("invitation_code") invitation_code: String): Observable<RegisterBean>


    //完善资料
    @POST("login/perfection")
    fun ImprovingData(
            @Query("uid") uid: String,
            @Query("sex") sex: String,
            @Query("nickname") nickname: String,
            @Query("date_birth") date_birth: String): Observable<CommonBean>


    //登录
    @POST("login/login")
    fun Login(@Query("mobile") mobile: String, @Query("password") password: String): Observable<LoginBean>

    //短信验证码
    @POST("Login/tel_code")
    fun telCode(@Query("phone") phone: String): Observable<CommonBean>

    //忘记密码
    @POST("login/forget_pwd")
    fun ForgetPwd(@Query("mobile") mobile: String,
                  @Query("code") code: String,
                  @Query("password") password: String,
                  @Query("pwd") pwd: String): Observable<CommonBean>


    //头饰- 座驾-礼物列表
    @POST("Goods/fill_store_list")
    fun DressMall(@Query("type") type: String): Observable<DressMallBean>

    //表情列表
    @POST("Goods/face_list")
    fun FaceList(): Observable<FaceListBean>

    //金币充值列表
    @POST("Goods/gold_list")
    fun GoldList(): Observable<GoldListBean>

    //标签列表
    @POST("Goods/label_list")
    fun TabList(): Observable<TabListBean>

    //轮播图
    @POST("index/banner")
    fun HomeBanner(@Query("type") type: String): Observable<BannerBean>


    //轮播图详情
    @POST("index/banner_detail")
    fun HomeBannerDetail(@Query("banner_id") banner_id: String): Observable<BannerDetailBean>

    //签到列表
    @POST("Sign/sign_list")
    fun SignList(@Query("uid") uid: String): Observable<QdSignListBean>

    //签到规则
    @POST("Sign/sign_rule")
    fun SignRule(): Observable<SignRuleBean>

    //上传图片
    @Multipart
    @POST("upload/upload")
    fun UploadFile(@Part file: MultipartBody.Part): Observable<UploadFileBean>

    //获取个人信息
    @POST("login/getUserInfo")
    fun GetUserInfo(@Query("uid") uid: String): Observable<UserInfoBean>

    //修改个人资料
    @POST("login/setUserInfo")
    fun Head(@Query("uid") uid: String,
             @Query("icon") icon: String): Observable<CommonBean>

    //修改个人资料
    @POST("login/setUserInfo")
    fun NikeName(@Query("uid") uid: String,
                 @Query("nickname") nickname: String): Observable<CommonBean>

    //修改个人资料
    @POST("login/setUserInfo")
    fun Brith(@Query("uid") uid: String,
              @Query("date_birth") date_birth: String): Observable<CommonBean>


    //修改个人相册图片
    @POST("login/setUserInfo")
    fun Picture(@Query("uid") uid: String,
                @Query("user_picture_id") user_picture_id: String): Observable<CommonBean>


    //修改个人相册图片
    @POST("login/setUserInfo")
    fun UserDesc(@Query("uid") uid: String,
                 @Query("user_desc") user_desc: String): Observable<CommonBean>


    //我的 - 设置 - 模块接口   2 社区规范；3 官方联系 ；4 帮助；5 关于平台 （根据不同的类型取对应的字段值）
    @POST("index/baseSet")
    fun BaseSetCommunity(@Query("type") type: String): Observable<CommunityBean>

    //我的 - 设置 - 模块接口   2 社区规范；3 官方联系 ；4 帮助；5 关于平台 （根据不同的类型取对应的字段值）
    @POST("index/baseSet")
    fun BaseSetContact(@Query("type") type: String): Observable<ContactBean>

    //我的 - 设置 - 模块接口   2 社区规范；3 官方联系 ；4 帮助；5 关于平台 （根据不同的类型取对应的字段值）
    @POST("index/baseSet")
    fun BaseSetHelp(@Query("type") type: String): Observable<HelpBean>

    //我的 - 设置 - 模块接口   2 社区规范；3 官方联系 ；4 帮助；5 关于平台 （根据不同的类型取对应的字段值）
    @POST("index/baseSet")
    fun BaseSetAbout(@Query("type") type: String): Observable<CommunityBean>

    @POST("index/help_detail")
    fun HelpDetail(@Query("id") id: String): Observable<CommunityBean>

    @POST("index/bank_list")
    fun BankList(): Observable<BankListBean>

    @POST("index/user_bank_list")
    fun UserBankList(@Query("uid") uid: String, @Query("page") page: String): Observable<UserBankListBean>

    //添加银行卡信息
    @POST("index/addBankInfo")
    fun addBankInfo(@Query("uid") uid: String,
                    @Query("bank_id") bank_id: String,
                    @Query("real_name") real_name: String,
                    @Query("bank_number") bank_number: String,
                    @Query("bank_address") bank_address: String): Observable<AddBankInfoBean>

    //获取银行卡信息
    @POST("index/getBankInfo")
    fun getBankInfo(@Query("uid") uid: String,
                    @Query("user_bank_id") user_bank_id: String): Observable<GetBankInfoBean>

    //编辑银行卡信息
    @POST("index/editBankInfo")
    fun editBankInfo(@Query("uid") uid: String,
                     @Query("user_bank_id") user_bank_id: String,
                     @Query("bank_id") bank_id: String,
                     @Query("real_name") real_name: String,
                     @Query("bank_number") bank_number: String,
                     @Query("bank_address") bank_address: String): Observable<AddBankInfoBean>

    //删除银行卡信息
    @POST("index/delBankInfo")
    fun DelBankInfo(@Query("uid") uid: String,
                    @Query("user_bank_id") user_bank_id: String): Observable<CommonBean>

    //管理我的相册 - 删除
    @POST("login/my_photo_delete")
    fun Delphoto(@Query("uid") uid: String,
                 @Query("photo_id") photo_id: String): Observable<CommonBean>


    //我的邀请
    @POST("users/my_qrcode")
    fun myQrCode(@Query("uid") uid: String): Observable<MyQrCodeBean>


    //我的邀请记录
    @POST("users/my_log")
    fun myInvitationRecode(@Query("uid") uid: String,
                           @Query("page") page: String): Observable<MyInvitationRecodeBean>


    //实名认证
    @POST("users/real_name")
    fun realName(@Query("uid") uid: String,
                 @Query("username") username: String,
                 @Query("number_id") number_id: String): Observable<CommonBean>

    //获取实名认证信息
    @POST("users/get_real_info")
    fun getRealInfo(@Query("uid") uid: String): Observable<RealInfoBean>

    //本期奖池
    @POST("Share/award_list")
    fun awardList(@Query("uid") uid: String): Observable<AwardListDataBean>

    //本期奖池
    @POST("Share/award_log")
    fun awardRecode(@Query("uid") uid: String, @Query("page") page: String): Observable<AwardRecodeBean>


    //开始砸金蛋
    @POST("Share/beginAward")
    fun beginAward(@Query("uid") uid: String,
                   @Query("type") type: String): Observable<EggBean>


    //音乐（共享）列表 - 后台设置
    @POST("Goods/music_list")
    fun musicList(): Observable<MusicListBean>

    //房间音乐列表（我的曲库）
    @POST("Room/RoomMusicList")
    fun RoomMusicList(@Query("room_id") room_id: String): Observable<RoomMusicListBean>

    //创建房间
    @POST("Room/create_room")
    fun CreateRoom(@Query("uid") uid: String): Observable<CreateRoomBean>

    //房间黑名单/管理员 - 列表
    /**
     * type	：1 黑名单；2 管理员
     */
    @POST("Room/blackList")
    fun blackList(@Query("uid") uid: String,
                  @Query("room_id") room_id: String,
                  @Query("type") type: String): Observable<BlackListBean>

    //房间黑名单/管理员 - 添加
    /**
     * type	：1 黑名单；2 管理员
     */
    @POST("Room/add_black")
    fun addBlack(@Query("uid") uid: String,
                 @Query("room_id") room_id: String,
                 @Query("type") type: String): Observable<CommonBean>

    //房间黑名单/管理员 - 移除
    /**
     * type	：1 黑名单；2 管理员
     */
    @POST("Room/delab")
    fun DelBlack(@Query("uid") uid: String,
                 @Query("room_id") room_id: String,
                 @Query("type") type: String,
                 @Query("black_id") black_id: String): Observable<CommonBean>

    @POST("Room/delab")
    fun DelBlack1(@Query("uid") uid: String,
                  @Query("room_id") room_id: String,
                  @Query("type") type: String): Observable<CommonBean>


    //房间背景列表 - 后台设置
    @POST("Goods/bg_list")
    fun RoomBg(): Observable<RoomBgBean>

    //举报列表 - 后台设置
    @POST("Room/informList")
    fun ReportList(): Observable<ReportListBean>

    //举报房间
    @POST("Room/reportRoom")
    fun reportRoom(@Query("room_id") room_id: String,
                   @Query("uid") uid: String,
                   @Query("info_id") info_id: String,
                   @Query("pic_id") pic_id: String): Observable<CommonBean>

    //进入房间（后台对接了融云）
    @POST("Room/inRoom")
    fun inRoom(@Query("room_id") room_id: String,
               @Query("uid") uid: String): Observable<CommonBean>

    //获取房间信息
    @POST("Room/get_room_info")
    fun RoomInfo(@Query("room_id") room_id: String,
                 @Query("uid") uid: String): Observable<RoomInfoBean>

    //房间的麦位列表
    @POST("Room/mwList")
    fun RoomMicList(@Query("room_id") room_id: String,
                    @Query("uid") uid: String): Observable<RoomMicListBean>

    //设置房间信息
    @POST("Room/setRoomInfo")
    fun setRoomInfo(@Body body: RequestBody): Observable<CommonBean>

    //房内榜
    @POST("Room/room_star")
    fun roomStar(@Query("type") type: String, @Query("room_id") room_id: String): Observable<RoomStarBean>

    //房间 - 我的背包列表
    @POST("Room/knapsack")
    fun BageList(@Query("uid") uid: String): Observable<BageListBean>

    //礼物数量 用于送礼物-我的背包选择数量用到
    @POST("Room/gift_times")
    fun giftTimes(): Observable<GiftTimesBean>

    //购买座驾 - 购买头饰（自己使用）
    @POST("Room/buy_dress_up")
    fun buyDressUp(@Query("type") type: String,
                   @Query("auto_id") auto_id: String,
                   @Query("uid") uid: String): Observable<CommonBean>

    //好友-粉丝-关注列表
    @POST("users/fans_list")
    fun fansList(@QueryMap map: Map<String, String>): Observable<FansListBean>

    //关注好友
    @POST("users/fans")
    fun fans(@QueryMap map: Map<String, String>): Observable<CommonBean>

    //家族长信息
    @FormUrlEncoded
    @POST("family/fm_list")
    fun fm_list(@Field("uid") uid: String,
                @Field("fm_id") fm_id: String,
                @Field("type") type: String): Observable<FMListBean>

    //家族长信息
    @FormUrlEncoded
    @POST("group/groupList")
    fun groupList(@Field("uid") uid: String,
                  @Field("fm_id") fm_id: String,
                  @Field("type") type: String): Observable<GroupListBean>


    //家族成员列表
    @FormUrlEncoded
    @POST("family/fm_list")
    fun fm_list1(@Field("uid") uid: String,
                 @Field("fm_id") fm_id: String,
                 @Field("type") type: String,
                 @Field("page") page: String): Observable<JZListBean>


    //家族广场
    @FormUrlEncoded
    @POST("family/family_list")
    fun familyList(@Field("uid") uid: String): Observable<FamilyListBean>

    //家族指南
    @POST("family/jzzn")
    fun FamilyGuide(): Observable<AwardBean>

    //我的家族
    @FormUrlEncoded
    @POST("family/my_jz")
    fun mFamily(@Field("uid") uid: String): Observable<MFamilyBean>

    //我的家族信息
    @FormUrlEncoded
    @POST("family/my_fm")
    fun myfm(@Field("uid") uid: String, @Field("fm_id") fm_id: String): Observable<MyFmBean>

    //踢出 - 退出家族
    @FormUrlEncoded
    @POST("family/tc_fm")
    fun Tcfm(@Field("m_id") m_id: String, @Field("fm_id") fm_id: String): Observable<CommonBean>

    //加入家族
    @FormUrlEncoded
    @POST("family/join_family")
    fun Joinfm(@Field("uid") uid: String, @Field("fm_id") fm_id: String): Observable<CommonBean>

    //加入群（后台对接了融云）
    @FormUrlEncoded
    @POST("group/join_group")
    fun JoinGroup(@Field("uid") uid: String,
                  @Field("fm_id") fm_id: String,
                  @Field("group_id") group_id: String): Observable<CommonBean>

    //立即充值
//    @FormUrlEncoded
    @POST("Order/recharge")
    fun recharge(@Body body: RequestBody): Observable<OrderBean>

    //土豪榜
    @FormUrlEncoded
    @POST("level/local_tyrant")
    fun tyrantList(@Field("type") uid: Int): Observable<TyrantListBean>
    //巨星榜
    @FormUrlEncoded
    @POST("level/giant_star")
    fun supperStarList(@Field("type") uid: Int): Observable<SupperStarBean>
    // 周星榜
//    @FormUrlEncoded
    @POST("level/week_star")
    fun weekStarList(): Observable<WeekStarBean>
    // 周星榜（本周明星）
    @FormUrlEncoded
    @POST("level/week_gift_star")
    fun weekStar(@Field("gift_zl_id") uid: Int): Observable<StarBean>

    @FormUrlEncoded
    @POST("Refund/zs_tx")
    fun zsTxAli(@Field("uid") uid: String,
                @Field("type") type: String): Observable<ZSAliBean>

    @FormUrlEncoded
    @POST("Refund/zs_tx")
    fun zsTxBank(@Field("uid") uid: String,
                 @Field("type") type: String): Observable<ZSBankBean>


//   @FormUrlEncoded
//    @POST("Refund/zs_tx")
//    fun zsTxBank(@Field("uid") uid: String,
//                 @Field("type") type: String,
//                 @Field("user_bank_id") user_bank_id: String): Observable<ZSBankBean>


    @FormUrlEncoded
    @POST("Refund/tx")
    fun TxAli(@Field("uid") uid: String,
              @Field("account") account: String,
              @Field("type") type: String): Observable<OrderBean>

    @FormUrlEncoded
    @POST("Refund/tx")
    fun TxBank(@Field("uid") uid: String,
               @Field("account") account: String,
               @Field("type") type: String,
               @Field("user_bank_id") user_bank_id: String): Observable<OrderBean>


    @POST("category/homeCateList")
    fun HomeCateList(): Observable<HomeCateListBean>

    @FormUrlEncoded
    @POST("category/homeRoomList")
    fun homeRoomList(@Field("pid") pid: String): Observable<HomeRoomListBean>

    @FormUrlEncoded
    @POST("Refund/tx_log")
    fun TxRecord(@Field("uid") uid: String, @Field("page") page: String): Observable<RefundListBean>

    @POST("users/record_detail")
    fun record_detail(@Body body: RequestBody): Observable<RecordDetailBean>

    @POST("users/record_detail")
    fun record_detail1(@Body body: RequestBody): Observable<JbRecordDetailBean>

    @POST("users/diamond_dhym")
    fun diamond_dhym(@Body body: RequestBody): Observable<DiamondBean>

    @POST("users/sub_diamond")
    fun sub_diamond(@Body body: RequestBody): Observable<CommonBean>

    @POST("users/set_default")
    fun set_default(@Body body: RequestBody): Observable<CommonBean>

    @POST("Index/bind_mobile")
    fun bind_mobile(@Body body: RequestBody): Observable<CommonBean>


    @POST("Index/bind_zfb")
    fun bind_zfb(@Body body: RequestBody): Observable<CommonBean>

    @FormUrlEncoded
    @POST("users/my_gift")
    fun my_gift(@Field("uid") uid: String,
                @Field("type") type: String): Observable<SGiftBean>

    @FormUrlEncoded
    @POST("users/my_gift")
    fun my_gift1(@Field("uid") uid: String,
                 @Field("type") type: String): Observable<SFGiftBean>


    @POST("category/roomCateList")
    fun homeTable(): Observable<HomeTableBean>

    @POST("category/roomId")
    fun homeroomId(): Observable<HomeHLBean>

    @FormUrlEncoded
    @POST("category/RoomList")
    fun RoomList(@Field("pid") pid: String): Observable<HomeRoomBean>

    @FormUrlEncoded
    @POST("category/RoomList")
    fun RoomList1(@Field("pid") pid: String): Observable<HomeFRoomBean>

    @FormUrlEncoded
    @POST("users/fans_status")
    fun fans_status(@Field("uid") uid: String, @Field("fs_id") fs_id: String): Observable<FouceOnBean>

    @FormUrlEncoded
    @POST("level/user_level")
    fun user_level(@Field("uid") uid: String, @Field("type") type: String): Observable<UserLevelBean>

    @POST("Wxlogin/wx_login")
    fun ThirdLogin(@Body body: RequestBody): Observable<ThirdLoginBean>

    @POST("category/sys_list")
    fun sys_list(@Body body: RequestBody): Observable<SysBean>

    @POST("category/sys_detail")
    fun sys_detail(@Body body: RequestBody): Observable<SysDetialBean>


    @FormUrlEncoded
    @POST("pay/app_pay")
    fun WxPay(@Field("order_id") order_id: String, @Field("uid") uid: String): Observable<WxPayBean>

    @FormUrlEncoded
    @POST("Alipay/payOrder")
    fun AliPay(@Field("order_id") order_id: String, @Field("uid") uid: String): Observable<AliPayBean>

    @FormUrlEncoded
    @POST("category/roomSearch")
    fun roomSearch(@Field("keywords") keywords: String): Observable<SearchBean>

    //非法文字库（发消息时用到）
    @POST("index/ille_keywords")
    fun ille_keywords(): Observable<KeywordsBean>

    @POST("Roomnew/getRoomInfo")
    fun Roomnew(@Query("room_id") room_id: String): Observable<RoomnewBean>

    @FormUrlEncoded
    @POST("Room/RoomUserList")
    fun RoomUserList(@Field("room_id") room_id: String): Observable<RoomMemListBean>

    @FormUrlEncoded
    @POST("users/userBlackList")
    fun userBlackList(@Field("uid") uid: String): Observable<UserBlackListBean>

    @FormUrlEncoded
    @POST("users/userRemoveBlack")
    fun userRemoveBlack(@Field("uid") uid: String,
                        @Field("black_uid") black_uid: String,
                        @Field("black_id") black_id: String): Observable<CommonBean>

    @FormUrlEncoded
    @POST("Room/tCRoom")
    fun tCRoom(@Field("uid") uid: String,
               @Field("type") type: String,//类型： 1 离开房间；2 踢出房间
               @Field("room_id") room_id: String): Observable<CommonBean>

    @FormUrlEncoded
    @POST("Room/upDownWheat")
    fun upDownWheat(@Field("uid") uid: String,
                    @Field("room_id") room_id: String,
                    @Field("sort_id") sort_id: String,
                    @Field("type") type: String): Observable<CommonBean>


    @FormUrlEncoded
    @POST("index/couple_back")
    fun couple_back(@Field("uid") uid: String,
                    @Field("desc") desc: String,
                    @Field("wx_qq") wx_qq: String): Observable<CommonBean>

    @FormUrlEncoded
    @POST("room/roomMwUser")
    fun roomMwUser(@Field("uid") uid: String,
                   @Field("room_id") room_id: String): Observable<RoomMwUserBean>

    @FormUrlEncoded
    @POST("index/message")
    fun sysmessage(@Field("uid") uid: String): Observable<SysMessageBean>

    @POST("Room/setRoomMusic")
    fun setRoomMusic(@Body body: RequestBody): Observable<CommonBean>

    @POST("Room/RoomMusicDel")
    fun RoomMusicDel(@Body body: RequestBody): Observable<CommonBean>

    @POST("Room/send_gift")
    fun send_gift(@Body body: RequestBody): Observable<CommonBean>

    @POST("Sign/sign_day")
    fun sign_day(@Body body: RequestBody): Observable<SignDayBean>

    @POST("Sign/help_sign")
    fun help_sign(@Body body: RequestBody): Observable<SignDayBean>

    //锁麦 解麦
    @POST("Room/lock_wheat")
    fun lock_wheat(@Body body: RequestBody): Observable<CommonBean>

    //闭麦 - 开麦
    @POST("Room/oc_wheat")
    fun oc_wheat(@Body body: RequestBody): Observable<CommonBean>

    //判断房间是否上锁
    @POST("Roomnew/room_lock")
    fun room_lock(@Body body: RequestBody): Observable<RoomLockBean>

    //房间分享
    @POST("share/share_user")
    fun share_user(@Body body: RequestBody): Observable<ShareUserBean>

    @POST("Index/getBindInfo")
    fun getBindInfo(@Body body: RequestBody): Observable<BindInfoBean>


    @POST("users/get_real_info")
    fun get_real_info(@Body body: RequestBody): Observable<GetRealInfoBean>

    @POST("Room/isAdmin")
    fun isAdmin(@Body body: RequestBody): Observable<IsAdminBean>

    @POST("Room/remove_user")
    fun remove_user(@Body body: RequestBody): Observable<CommonBean>


}
