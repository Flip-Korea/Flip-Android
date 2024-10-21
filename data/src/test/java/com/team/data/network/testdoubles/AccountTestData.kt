package com.team.data.network.testdoubles

import com.team.data.network.model.request.ProfileRequest
import com.team.data.network.model.request.RegisterRequest
import com.team.domain.model.account.Register
import com.team.domain.model.account.RegisterProfile

fun ProfileRequest.toExternal(): RegisterProfile =
    RegisterProfile(profileId, nickname, photoUrl)

fun RegisterRequest.toExternal(): Register =
    Register(
        accountId = accountId,
        categories = categories,
        name = name,
        profile = profile.toExternal()
    )

val networkRegisterTestData = RegisterRequest(
    accountId = "kakao123test",
    name = "testName",
    categories = listOf(1, 2, 3),
    profile = ProfileRequest(
        profileId = "honggd",
        nickname = "testNickName",
        photoUrl = "test.com"
    )
)

val networkAccountJsonTestData = """
            {
              "account_id": "kakao12s3sd4aadv5",
              "name": "홍길동",
              "email": "user@example.com",
              "phone_num": "010-1111-2222",
              "profile": [
                {
                  "profile_id": "honggildong_main",
                  "nickname": "홍길동이(메인)",
                  "introduce": "홍길동 메인 계정입니당.",
                  "post_cnt": 3,
                  "following_cnt": 10,
                  "follower_cnt": 11,
                  "rating": "default",
                  "photo_url": "https://maybe-storage-server.com/12",
                  "categories": [1, 3, 7]
                },
                {
                  "profile_id": "honggildong_sub",
                  "nickname": "홍길동이(서브)",
                  "introduce": "홍길동 서브 계정입니당.",
                  "post_cnt": 1,
                  "following_cnt": 5,
                  "follower_cnt": 6,
                  "rating": "default",
                  "photo_url": "https://maybe-storage-server.com/12",
                  "categories": [1, 3, 4]
                }
              ]
            }
        """.trimIndent()