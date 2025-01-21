package com.team.data.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.account.Register
import com.team.domain.model.account.RegisterProfile

// {
//    "provider" : "kakao",
//    "oauthId" : "oauth123",
//    "ads_agree" : true,
//    "profile" : {
//    "userId" : "user123",
//    "nickname" : "nickname123",
//    "photoUrl" : "https://flip-storage-server.com/11"
// }
// }

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    /** google, kakao, naver, apple 중 하나여야 함 */
    val provider: String,
    val oauthId: String,
    @Json(name = "ads_agree") val adsAgree: Boolean,
    val profile: ProfileRequest,
)

@JsonClass(generateAdapter = true)
data class ProfileRequest(
    val userId: String,
    val nickname: String,
    @Json(name = "imageUrl") val photoUrl: String? = null,
)

fun RegisterProfile.toNetwork(): ProfileRequest =
    ProfileRequest(userId = userId, nickname = nickname, photoUrl = photoUrl)

fun Register.toNetwork(): RegisterRequest =
    RegisterRequest(
        provider = socialLoginPlatform.providerName,
        oauthId = oauthId,
        profile = profile.toNetwork(),
        adsAgree = adsAgree,
    )
