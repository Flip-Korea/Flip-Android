package com.team.data.testdoubles.network.model

val networkProfileTestData = """
    {
      "profile_id": "honggildong_main",
      "nickname": "홍길동이(메인)",
      "introduce": "홍길동 메인 계정입니당.",
      "post_total": 3,
      "following_total": 10,
      "follower_total": 11,
      "rating": "default",
      "photo_url": "https://maybe-storage-server.com/12",
      "is_following": false,
      "is_follower": false
    }
""".trimIndent()

val networkMyProfileTestData = """
    {
      "profile_id": "honggildong_main",
      "nickname": "홍길동이(메인)",
      "introduce": "홍길동 메인 계정입니당.",
      "post_total": 3,
      "following_total": 10,
      "follower_total": 11,
      "rating": "default",
      "photo_url": "https://maybe-storage-server.com/12",
      "categories": [1, 2, 3]
    }
""".trimIndent()