package com.team.data.testdoubles.network

val postsResponseTestData = """
    {
    	"post_cnt": 12001,
      "has_next": true,
      "next_cursor": "0000002024030854",
      "posts": [
        {
          "post_id": 12345,
          "profile": {
            "profile_id": "honggd",
            "nickname": "홍길동이",
            "photo_url": "https://sample-server.com/65332",
    				"is_follower": false,
    				"is_following": false
          },
          "title": "오늘의 일기!",
          "content": "API 명세서 작성하느라 매우 머리가 어지러웠다.",
          "liked": true,
          "like_cnt": 4,
          "comment_cnt": 31,
          "scrap_cnt": 331,
          "scraped": false,
          "category_id": 5,
          "bg_color_id": 3,
          "font_style_id": 2,
          "tag": ["일기", "일상"],
          "created_at": "2024-03-04 MON"
        },
        {
          "post_id": 12346,
          "profile": {
            "profile_id": "honggd2",
            "nickname": "홍길동이2",
            "photo_url": "https://sample-server.com/24123",
    				"is_follower": false,
    				"is_following": false
          },
          "title": "오늘의 일기2222!",
          "content": "테스틈당",
          "liked": false,
          "like_cnt": 2,
          "comment_cnt": 31,
          "scrap_cnt": 30,
          "scraped": false,
          "category_id": 5,
          "bg_color_id": 2,
          "font_style_id": 2,
          "tag": ["일기", "일상"],
          "created_at": "2024-03-05 TUE"
        }
      ]
    }
""".trimIndent()
val postsResponseTestDataEndOfPage = """
    {
    	"post_cnt": 12001,
      "has_next": false,
      "next_cursor": "0000002024030854",
      "posts": [
        {
          "post_id": 12345,
          "profile": {
            "profile_id": "honggd",
            "nickname": "홍길동이",
            "photo_url": "https://sample-server.com/65332",
    				"is_follower": false,
    				"is_following": false
          },
          "title": "오늘의 일기!",
          "content": "API 명세서 작성하느라 매우 머리가 어지러웠다.",
          "liked": true,
          "like_cnt": 4,
          "comment_cnt": 31,
          "scrap_cnt": 331,
          "scraped": false,
          "category_id": 5,
          "bg_color_id": 3,
          "font_style_id": 2,
          "tag": ["일기", "일상"],
          "created_at": "2024-03-04 MON"
        },
        {
          "post_id": 12346,
          "profile": {
            "profile_id": "honggd2",
            "nickname": "홍길동이2",
            "photo_url": "https://sample-server.com/24123",
    				"is_follower": false,
    				"is_following": false
          },
          "title": "오늘의 일기2222!",
          "content": "테스틈당",
          "liked": false,
          "like_cnt": 2,
          "comment_cnt": 31,
          "scrap_cnt": 30,
          "scraped": false,
          "category_id": 5,
          "bg_color_id": 2,
          "font_style_id": 2,
          "tag": ["일기", "일상"],
          "created_at": "2024-03-05 TUE"
        }
      ]
    }
""".trimIndent()

val postsResponseTestDataWithScrapComment = """
    {
    	"post_cnt": 12001,
      "has_next": true,
      "next_cursor": "0000002024030854",
      "posts": [
        {
          "post_id": 12345,
          "profile": {
            "profile_id": "honggd",
            "nickname": "홍길동이",
            "photo_url": "https://sample-server.com/65332",
    				"is_follower": false,
    				"is_following": false
          },
          "title": "오늘의 일기!",
          "content": "API 명세서 작성하느라 매우 머리가 어지러웠다.",
          "liked": true,
          "like_cnt": 4,
          "comment_cnt": 31,
          "scrap_cnt": 331,
          "scraped": false,
          "scrap_comment": "스크랩 코멘트 텍스트",
          "category_id": 5,
          "bg_color_id": 3,
          "font_style_id": 2,
          "tag": ["일기", "일상"],
          "created_at": "2024-03-04 MON"
        },
        {
          "post_id": 12346,
          "profile": {
            "profile_id": "honggd2",
            "nickname": "홍길동이2",
            "photo_url": "https://sample-server.com/24123",
    				"is_follower": false,
    				"is_following": false
          },
          "title": "오늘의 일기2222!",
          "content": "테스틈당",
          "liked": false,
          "like_cnt": 2,
          "comment_cnt": 31,
          "scrap_cnt": 30,
          "scraped": false,
          "scrap_comment": "스크랩 코멘트 텍스트",
          "category_id": 5,
          "bg_color_id": 2,
          "font_style_id": 2,
          "tag": ["일기", "일상"],
          "created_at": "2024-03-05 TUE"
        }
      ]
    }
""".trimIndent()

val postResponseTestData = """
    {
      "post_id": 12346,
      "profile": {
        "profile_id": "honggd2",
        "nickname": "홍길동이2",
        "photo_url": "https://sample-server.com/24123",
    		"is_follower": false,
    		"is_following": false
      },
      "title": "오늘의 일기2222!",
      "content": "테스틈당",
      "liked": false,
      "like_cnt": 2,
      "comment_cnt": 31,
      "scrap_cnt": 30,
      "scraped": false,
      "category_id": 5,
      "bg_color_id": 2,
      "font_style_id": 2,
      "tag": ["일기", "일상"],
      "created_at": "2024-03-05 TUE"
    }
""".trimIndent()

val postRequestTestData = """
    {
      "title" : "title",
      "content" : "content",
      "bgColorType" : "BLUE",
      "fontStyleType" : "NORMAL",
      "tags" : [ "tag1", "tag2" ],
      "categoryId" : 1
    }
""".trimIndent()

val resultIdResponseTestData = """
    {
      "result_id": 123
    }
""".trimIndent()

val commentResponseTestData = """
    {
      "comment_cnt": 2,
      "has_next": true,
      "next_cursor": "0000002024030854",
      "comment": [
        {
    	    "comment_id": 43523,
          "profile_id": "miffy0924",
          "nickname": "miffy",
          "photo_url": "https://sample-server.com/2342",
          "content": "굿!!!",
          "comment_date": "2024-03-05 TUE"
        },
        {
    	    "comment_id": 43523,
          "profile_id": "miffy0924",
          "nickname": "miffy",
          "photo_url": "https://sample-server.com/2342",
          "content": "도움 되는 글입니당!",
          "comment_date": "2024-03-05 TUE"
        }
      ]
    }
""".trimIndent()

val commentRequestTestData = """
    {
      "profile_id": "honggd123",
      "post_id": 4312,
      "comment": "좋은 글이네용~"
    }
""".trimIndent()

val likePostRequestTestData = """
    {
      "profile_id": "honggd",
      "post_id": 12424
    }
""".trimIndent()

val addScrapRequestTestData = """
    {
      "profile_id": "honggd",
      "post_id": 624,
      "message": "어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구"
    }
""".trimIndent()

val tempPostListResponseTestData = """
    {
      "tempPosts": [
        {
          "tempPostId": 1,
          "title": "title",
          "content": "content",
          "bgColorType": "RED",
          "fontStyleType": "BOLD",
          "postAt": "2024-08-27 17:19:17",
          "categoryId": 1,
          "categoryName": "DAILY",
          "tags": []
        },
        {
          "tempPostId": 3,
          "title": "title3",
          "content": "content3",
          "bgColorType": "RED",
          "fontStyleType": "BOLD",
          "postAt": "2024-09-01 17:19:17",
          "categoryId": 3,
          "categoryName": "TIPS",
          "tags": [
            "tag1"
          ]
        },
        {
          "tempPostId": 4,
          "title": "title4",
          "content": "content4",
          "bgColorType": "RED",
          "fontStyleType": "BOLD",
          "postAt": "2024-06-28 17:19:17",
          "categoryId": 2,
          "categoryName": "IT_SCIENCE",
          "tags": [
            "tag1"
          ]
        },
        {
          "tempPostId": 5,
          "title": "title4",
          "content": "content4",
          "bgColorType": "RED",
          "fontStyleType": "BOLD",
          "postAt": "2024-08-20 17:19:17",
          "categoryId": 2,
          "categoryName": "IT_SCIENCE",
          "tags": [
            "tag1"
          ]
        },
        {
          "tempPostId": 6,
          "title": "title4",
          "content": "content4",
          "bgColorType": "RED",
          "fontStyleType": "BOLD",
          "postAt": "2024-08-29 17:19:17",
          "categoryId": 2,
          "categoryName": "IT_SCIENCE",
          "tags": [
            "tag1"
          ]
        }
      ],
      "totalCount": 5
    }
""".trimIndent()