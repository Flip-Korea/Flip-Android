package com.team.data.testdoubles.network

val networkProfileTestData = """
    {
      "profile_id": "honggildong_main",
      "nickname": "홍길동이(메인)",
      "introduce": "홍길동 메인 계정입니당.",
      "post_cnt": 3,
      "following_cnt": 10,
      "follower_cnt": 11,
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
      "post_cnt": 3,
      "following_cnt": 10,
      "follower_cnt": 11,
      "rating": "default",
      "photo_url": "https://maybe-storage-server.com/12",
      "categories": [1, 2, 3]
    }
""".trimIndent()
val makeNetworkMyProfileTestData = { profileId: String ->
    """
        {
          "profile_id": "$profileId",
          "nickname": "홍길동이(메인)",
          "introduce": "makeNetworkMyProfileTestData",
          "photo_url": "https://maybe-storage-server.com/12",
          "post_cnt": 3,
          "following_cnt": 10,
          "follower_cnt": 11,
          "categories": [1, 2, 3],
          "rating": "default"
        }
    """.trimIndent()
}

val networkFollowersTestData = """
    {
      "has_next": true,
      "next_cursor": "0000002024030854",
      "followers": [
        {
          "profile_id": "kakao39393",
          "nickname": "어스름늑대",
          "photo_url": "https://example.com/profile-image.jpg",
          "is_following": false,
          "is_follower": false
        },
        {
          "profile_id": "kakao39391",
          "nickname": "어스름늑대2",
          "photo_url": "https://example.com/profile-image2.jpg",
          "is_following": false,
          "is_follower": false
        }
      ]
    }
""".trimIndent()
val networkFollowersTestDataEndOfPage = """
    {
      "has_next": false,
      "next_cursor": "0000002024030854",
      "followers": [
        {
          "profile_id": "kakao39393",
          "nickname": "어스름늑대",
          "photo_url": "https://example.com/profile-image.jpg",
          "is_following": false,
          "is_follower": false
        },
        {
          "profile_id": "kakao39391",
          "nickname": "어스름늑대2",
          "photo_url": "https://example.com/profile-image2.jpg",
          "is_following": false,
          "is_follower": false
        }
      ]
    }
""".trimIndent()

val networkFollowingsTestData = """
    {
      "has_next": true,
      "next_cursor": "0000002024030854",
      "followings": [
        {
          "profile_id": "kakao39393",
          "nickname": "어스름늑대",
          "photo_url": "https://example.com/profile-image.jpg",
          "is_following": false,
          "is_follower": false
        },
        {
          "profile_id": "kakao39391",
          "nickname": "어스름늑대2",
          "photo_url": "https://example.com/profile-image2.jpg",
          "is_following": false,
          "is_follower": false
        }
      ]
    }
""".trimIndent()
val networkFollowingsTestDataEndOfPage = """
    {
      "has_next": false,
      "next_cursor": "0000002024030854",
      "followings": [
        {
          "profile_id": "kakao39393",
          "nickname": "어스름늑대",
          "photo_url": "https://example.com/profile-image.jpg",
          "is_following": false,
          "is_follower": false
        },
        {
          "profile_id": "kakao39391",
          "nickname": "어스름늑대2",
          "photo_url": "https://example.com/profile-image2.jpg",
          "is_following": false,
          "is_follower": false
        }
      ]
    }
""".trimIndent()

val networkBlocksTestData = """
    {
      "has_next": true,
      "next_cursor": "0000002024030854",
      "block_list": [
        {
          "block_id": 123,
          "blocked_id": "honggd",
          "nickname": "어스름늑대",
          "photo_url": "https://example.com/profile-image.jpg"
        }
      ]
    }
""".trimIndent()
val networkBlocksTestDataEndOfPage = """
    {
      "has_next": false,
      "next_cursor": "",
      "block_list": [
        {
          "block_id": 123,
          "blocked_id": "honggd",
          "nickname": "어스름늑대",
          "photo_url": "https://example.com/profile-image.jpg"
        }
      ]
    }
""".trimIndent()

val networkMyCommentsTestData = """
    {
      "has_next": true,
      "next_cursor": "0000002024030854",
      "posts": [
        {
          "post_id": 41231,
          "title": "숙면의 중요성",
          "nickname": "돌거북",
          "my_comment": "덕분에 위로받고 갑니다~",
          "created_at": "2024-02-15"
        },
        {
          "post_id": 2343,
          "title": "숙면의 중요성 2",
          "nickname": "돌거북",
          "my_comment": "덕분에 위로받고 갑니다~ 2",
          "created_at": "2024-02-16"
        }
      ]
    }
""".trimIndent()
val networkMyCommentsTestDataEndOfPage = """
    {
      "has_next": false,
      "next_cursor": "",
      "posts": [
        {
          "post_id": 41231,
          "title": "숙면의 중요성",
          "nickname": "돌거북",
          "my_comment": "덕분에 위로받고 갑니다~",
          "created_at": "2024-02-15"
        },
        {
          "post_id": 2343,
          "title": "숙면의 중요성 2",
          "nickname": "돌거북",
          "my_comment": "덕분에 위로받고 갑니다~ 2",
          "created_at": "2024-02-16"
        }
      ]
    }
""".trimIndent()