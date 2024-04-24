package com.team.data.testdoubles.network.model

import com.team.data.network.model.request.NetworkProfile
import com.team.data.network.model.request.NetworkRegister

val networkRegisterTestData = com.team.data.network.model.request.NetworkRegister(
    accountId = "kakao123test",
    name = "testName",
    categories = listOf(1, 2, 3),
    profile = com.team.data.network.model.request.NetworkProfile(
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
                  "post_total": 3,
                  "following_total": 10,
                  "follower_total": 11,
                  "rating": "default",
                  "photo_url": "https://maybe-storage-server.com/12",
                  "categories": [1, 3, 7]
                },
                {
                  "profile_id": "honggildong_sub",
                  "nickname": "홍길동이(서브)",
                  "introduce": "홍길동 서브 계정입니당.",
                  "post_total": 1,
                  "following_total": 5,
                  "follower_total": 6,
                  "rating": "default",
                  "photo_url": "https://maybe-storage-server.com/12",
                  "categories": [1, 3, 4]
                }
              ]
            }
        """.trimIndent()