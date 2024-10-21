package com.team.data.network.testdoubles

val displayProfileListResponseTestData = """
    {
        "has_next": true,
        "next_cursor": "0000002024030854",
    	"profile_list" : [
    		{
    		  "profile_id": "honggildong_main",
    		  "nickname": "홍길동이(메인)",
    		  "photo_url": "https://maybe-storage-server.com/12",
              "is_follower": false,
              "is_following": false,
    		  "introduce": "홍길동 메인 계정입니당.",
    		  "rating": "default"
    		},
    		{
    		  "profile_id": "honggildong_main",
    		  "nickname": "홍길동이(메인)",
    		  "photo_url": "https://maybe-storage-server.com/12",
              "is_follower": false,
              "is_following": false,
    		  "introduce": "홍길동 메인 계정입니당.",
    		  "rating": "default"
    		}
    	]
    }
""".trimIndent()
val displayProfileListResponseTestDataEndOfPage = """
    {
        "has_next": false,
        "next_cursor": "",
    	"profile_list" : [
    		{
    		  "profile_id": "honggildong_main",
    		  "nickname": "홍길동이(메인)",
    		  "photo_url": "https://maybe-storage-server.com/12",
              "is_follower": false,
              "is_following": false,
    		  "introduce": "홍길동 메인 계정입니당.",
    		  "rating": "default"
    		},
    		{
    		  "profile_id": "honggildong_main",
    		  "nickname": "홍길동이(메인)",
    		  "photo_url": "https://maybe-storage-server.com/12",
              "is_follower": false,
              "is_following": false,
    		  "introduce": "홍길동 메인 계정입니당.",
    		  "rating": "default"
    		}
    	]
    }
""".trimIndent()

val tagListResponseTestData = """
    {
      "search_tag": "design",
      "next_cursor": "0000002024030854",
      "has_next": true,
      "tags": [
        {
          "tag_name": "design-practice",
          "post_cnt": 334
        },
        {
          "tag_name": "design-skills",
          "post_cnt": 1334
        }
      ]
    }
""".trimIndent()
val tagListResponseTestDataEndOfPage = """
    {
      "search_tag": "design",
      "next_cursor": "",
      "has_next": false,
      "tags": [
        {
          "tag_name": "design-practice",
          "post_cnt": 334
        },
        {
          "tag_name": "design-skills",
          "post_cnt": 1334
        }
      ]
    }
""".trimIndent()