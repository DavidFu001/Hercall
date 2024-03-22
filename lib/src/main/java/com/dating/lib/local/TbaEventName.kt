package com.dating.lib.local

object TbaEventName {
    const val startApp = "veeh_app_open"
    const val exitApp = "veeh_app_exit"
    const val registerTime = "veeh_app_sign"//账户注册

    const val userListView = "veeh_ac_view"//进入页面上报


    const val clickCallInUserList = "veeh_ac_cal_click"//主播列表拨打按钮点击

    const val profileView = "veeh_pf_view"//进入主播详情页上报
    const val clickProfileCall = "veeh_pf_cal_click"//主播详情拨打按钮点击
    const val clickProfileChat = "veeh_pf_mg_click"//点击发消息按钮上报
    const val clickProfileFollow = "veeh_pf_flw_click"//点击关注按钮上报

    const val callBeAccept = "veeh_cal_acpt"//用户拨打且接通通话时上报
    const val clickCallAnswer = "veeh_cal_asr_click"//用户点击接听按钮上报

    const val callWithin60sShow = "veeh_ved_otp_view"//视频内倒计时1min弹窗展示
    const val callEndShow = "veeh_ved_fns_view"//通话结束数据上报

    const val clickChatCall = "veeh_mg_cal_click"//聊天内电话点击
    const val clickGift = "veeh_gif_sd_click"//点击礼物上报

    const val paySuccess = "veeh_pmet_p_sces"//原生支付且admin校验成功时上报

    const val chargeCoin = "veeh_cin_csm"//用户扣除金币时上报
    const val clickActivityGood = "veeh_dsctet_click"//活动促销购买点击


    //匹配相关
    const val clickStartMatch = "veeh_vid_match_click"//在匹配页面点击开始按钮
    const val matchSuccess = "veeh_vid_match_success"//匹配成功页面进入上报（匹配到主播）
    const val clickCallInMatch = "veeh_vid_match_cal_click"//喜欢弹窗点击打电话
    const val clickChatInMatch = "veeh_vid_match_mg_click" //喜欢弹窗点击发消息

    const val splashAdView = "veeh_oppg_view"//开屏页曝光时上报
    const val splashAdClick = "veeh_oppg_click"//点击开屏页时上报
    const val homeBannerView = "veeh_banner_view"//banner曝光时上报
    const val homeBannerClick = "veeh_banner_click" //点击banner时上报
    const val homeFloatView = "veeh_pdt_view"//挂件曝光时上报
    const val homeFloatClick = "veeh_pdt_click"//点击挂件时上报
    const val searchUserClick = "veeh_search_click"//点击搜索栏中的🔍触发搜索上报

    const val imView = "veeh_mglst_view"//进入消息列表页上报
    const val callFromView= "veeh_cal_view"//进入呼叫页面上报
    const val receiveCallView= "veeh_cal_asr_view"//主播来电（页面+弹窗）展示上报
    const val shopView= "veeh_csp_view"//打开金币商店页面


    object CallEventFromName {
        const val callFromPop = "popup" // 来电弹窗接听
        const val callFromPage = "page" // 来电页面接听
    }

    object ClickGiftFromEventName {
        const val clickGiftInChat = "mg" // im中送礼物
        const val clickGiftInCall = "ved" // 视频通话中送礼物
    }

    object ChargeCoinFromName {
        const val chargeCoinInCall = "ved_view" // 通话消耗
        const val chargeCoinInChatGift = "im_gft_view" // IM送礼物消耗
        const val chargeCoinInCallGift = "ved_gft_view" //视频礼物消耗
    }

    object PaySuccessFromEventName {
        const val payFromMe = "personal" // 用户中心
        const val payFromUserList = "ac_cal_click" // 主播列表拨打欠费
        const val payFromSeeking = "search"//主播搜索列表
        const val payFromProfile = "pf_cal_click" // 详情页拨打欠费
        const val payFromChatCall = "mg_cal_click" // 消息内打电话
        const val payFromFollow = "im_flw" //关注列表拨打欠费
        const val payFromCallGift = "gft_ved" //视频内礼物充值
        const val payFromCallGiftNoCoin = "gft_ved_out" //视频内礼物欠费
        const val payFromChatGift = "gft_mg" // 消息内礼物充值
        const val payFromChatNoCoin = "gft_mg_out" // 消息内礼物欠费
        const val payFromAnswerNoCoin = "cal_asr_view" // 接听欠费

        const val payFromCall = "ved_sp_click" // 视频通话内点击金币商店充值
        const val payFromCallCountDown = "ved_otp_click" // 视频通话内点击倒计时1min金币充值
        const val payFromActivityGood = "dsctevent" // 活动充值
        const val payFromCallBackNoCoin = "calback_click"//点击回拨欠费

        const val payFromMatchCoin = "videomatch"//匹配金币入口
        const val payFromMatchCall = "videomatchcall"//匹配喜欢点击拨打
        const val payFromMatchHistory = "videomatchhis"// 匹配history页面点击拨打

        var currentPayFrom = payFromUserList
    }

    object CallEndFromName {
        const val callFrom_Profile = "pf" // 主播详情页
        const val callFrom_Chat = "mg" // 消息页面
        const val callFrom_Follow = "im_flw" // 关注列表
        const val callFrom_Back = "calback"//回拨
        const val callFrom_Hot = "listhot" // hot列表
        const val callFrom_New = "listnew" // new列表
        const val callFrom_Seeking = "listsearch" //search通过搜索到主播打电话充值
        const val callFrom_Match_OK = "video_match_suc" // 匹配成功页面
        const val callFrom_Match_History = "video_match_his"// 匹配history页面

        var currentCallFromType = callFrom_Profile
    }

    object ViewUserListEventFromName {
        const val viewFrom_Hot = "listhot" // hot列表
        const val viewFrom_New = "listnew" // new列表
    }

    object page2ProfileFrom {
        const val profileFrom_Follow = "im_flw" // 主播详情页 "im_flw"  //关注列表
        const val profileFrom_Conversation = "im_mg"  //消息列表
        const val profileFrom_Chat = "mg" //消息聊天界面
        const val profileFrom_BlockList = "blocklist"  //黑名单
        const val profileFrom_CallList = "calrecd" // 通话列表
        const val profileFrom_Hot = "listhot" //hot列表
        const val profileFrom_New = "listnew" // new列表
        const val profileFrom_MatchHistory = "video_match_his"// 匹配history页面"
    }

    object imViewEventFromName {
        const val viewFromFollow = "im_flw" // 关注列表
        const val viewFromMg = "im_mg" // 消息列表
        const val viewFromCall = "calrecd" // 通话列表
    }

}
