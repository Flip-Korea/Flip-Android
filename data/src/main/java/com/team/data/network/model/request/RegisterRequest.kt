package com.team.data.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.account.Register
import com.team.domain.model.account.RegisterProfile
import com.team.domain.type.SocialLoginPlatform

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
    val provider: SocialLoginPlatform,
    val oauthId: String,
    @Json(name = "ads_agree") val adsAgree: Boolean,
    val profile: ProfileRequest,
)

@JsonClass(generateAdapter = true)
data class ProfileRequest(
    val userId: String,
    val nickname: String,
    val photoUrl: String,
)

fun RegisterProfile.toNetwork(): ProfileRequest =
    ProfileRequest(userId = userId, nickname = nickname, photoUrl = photoUrl)

fun Register.toNetwork(): RegisterRequest =
    RegisterRequest(
        provider = socialLoginPlatform,
        oauthId = oauthId,
        profile = profile.toNetwork(),
        adsAgree = adsAgree,
    )
