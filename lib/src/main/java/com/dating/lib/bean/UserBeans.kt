package com.dating.lib.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "userBean")
open class UserBean(
    // 用户id
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "uid")
    @SerializedName("galasis")
    var uid: Int = 0,
    // 用户昵称
    @ColumnInfo(name = "nickname")
    @SerializedName("retruise")
    var nickname: String = "",
    // 用户状态：0: 空闲 1：繁忙 2：离线 3：未审核(主播注册后未审核通过)
    @ColumnInfo(name = "accStatus")
    @SerializedName("sneiceships")
    var accStatus: Int = 4,
    // 国籍
    @ColumnInfo(name = "userCountry")
    @SerializedName("pyawer")
    var userCountry: String? = "",
    //匹配用的字段
    @SerializedName("daills")
    val matchUploadId: Int = 0,
    //匹配用的字段
    @SerializedName("pest")
    val anchorCountry: String? = "",
    // 性别：0：保密 1：男 2：女
    @ColumnInfo(name = "sex")
    @SerializedName("roceam")
    var sex: Int = 0,
    // 年龄，默认18
    @ColumnInfo(name = "age")
    @SerializedName("beke")
    var age: Int = 18,
    // 头像
    @ColumnInfo(name = "avatar")
    @SerializedName("superinrvals")
    var avatar: String? = "",
    // 个人简介
    @ColumnInfo(name = "introduce")
    @SerializedName("sueges")
    var introduce: String? = "",
    // 剩余金币数
    @SerializedName("cgue")
    var coin: String? = "",
    // 融云IMToken
    @SerializedName("saoast")
    val imToken: String? = "",
    // 用户会话Token，放至header（注册、登录和刷新token无此参数，不传），用于身份验证
    @SerializedName("disvices")
    val token: String? = "",
    //设备是否已经注册
    @SerializedName("has_register")
    val hasRegister: Boolean = false,
    //是否开启勿扰模式，默认未开启
    @SerializedName("shids")
    val notDisturb: Boolean = false,
    @ColumnInfo(name = "userLevel")
    @SerializedName("unctories")
    var userLevel: Int = 0, //用户等级
    @SerializedName("sioliters")
    var leftTime: Int = 0, //用户免费通话次数

    //************主播列表新增字段***********/
    @SerializedName("sotal")
    val price: Int = 0,
    @SerializedName("commases")
    val recommend: Boolean = false,
    @ColumnInfo(name = "inBlack")
    @SerializedName("in_black")
    var inBlack: Boolean = false, //是否拉黑
    @ColumnInfo(name = "isLike")
    @SerializedName("slainders")
    var isLike: Boolean = false, //是否关注
    @ColumnInfo(name = "images")
    @SerializedName("images")
    val images: List<FileBean>? = emptyList(),
    @ColumnInfo(name = "videos")
    @SerializedName("callitter")
    val videos: List<FileBean>? = emptyList(),
    @ColumnInfo(name = "impressions")
    @SerializedName("impression")
    val impressions: List<String>? = emptyList(),
    @SerializedName("distance")
    val distance: Int = 0,
) : Serializable {
    //是否存在四级尺度
    fun hasAdultLevel(): Boolean {
        return videos?.any { "ADULT" == it.fileLevel } ?: false
    }

    fun isOffLine(): Boolean {
        return accStatus >= 2
    }

    fun isMale() = sex == 1

    /**
     * 获取主播的等级Str
     */
    fun getLevelStr(): String {
        return when (userLevel) {
            0 -> "S"
            1 -> "A"
            2 -> "B"
            3 -> "C"
            4 -> "D"
            else -> ""
        }
    }

    //主页小视频播放顺序，1级->4级的顺序，且需要过滤掉四级未解锁的
    fun getProfileVideo(): List<FileBean> {
        return if (videos.isNullOrEmpty()) {
            emptyList()
        } else {
            videos.filter { it.fileType == 4 }
        }

    }

}

data class FileBean(
    @SerializedName("hnsfers")
    val file: String, //地址
    @SerializedName("whiley")
    val cover: String, //视频类型的封面图
    @SerializedName("ought")
    val fileType: Int = 0, //资源类型
    @SerializedName("file_level")
    val fileLevel: String = "",//尺度 "NORMAL": 0,"SEXY": 1,"HOT": 2,"ADULT": 3,
    @SerializedName("active")
    var active: Boolean = false, //是开启四级付费解锁
    @SerializedName("unlock")
    var unlock: Boolean = false, //是否解锁
) : Serializable {
    fun getXBannerUrl(): String {
        return file
    }

    val fileLevelValue: Int
        get() {
            return when (fileLevel.uppercase()) {
                "Normal" -> 0
                "SEXY" -> 1
                "HOT" -> 2
                "ADULT" -> 3
                else -> 0
            }
        }
}

data class AllUser(
    @SerializedName("doelt") val total: Int = 0,
    @SerializedName("tuies") val data: List<UserBean> = emptyList()
)

data class UserStateRequestBody(
    @SerializedName("uid") val list: List<Int>
)

data class UIDRequestBody(
    var pexis:Int//uid
)
