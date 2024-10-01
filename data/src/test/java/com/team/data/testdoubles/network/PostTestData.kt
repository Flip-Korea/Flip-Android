package com.team.data.testdoubles.network

val postsResponseTestData = """
    {
      "post_cnt": 12001,
      "has_next": true,
      "next_cursor": "0000002024030854",
      "posts": [
        {
          "postId": 1,
          "profile": {
            "profile_id": "honggd",
            "nickname": "어스름늑대",
            "photo_url": "https://images.dog.ceo/breeds/bulldog-boston/n02096585_11731.jpg",
            "is_follower": false,
            "is_following": false
          },
          "title": "행정권은 대통령을 수반으로 어쩌고 어쩌고!",
          "content": "행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다. 행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다. 행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다.",
          "liked": true,
          "likeCnt": 4,
          "commentCnt": 31,
          "scrapCnt": 331,
          "scraped": false,
          "categoryId": 5,
          "bgColorType": "DEFAULT",
          "fontStyleType": "NORMAL",
          "tags": [
            "일기",
            "일상"
          ],
          "postAt": "2024-03-04 MON"
        },
        {
          "postId": 2,
          "profile": {
            "profile_id": "honggd",
            "nickname": "어스름늑대",
            "photo_url": "https://images.dog.ceo/breeds/bulldog-boston/n02096585_11731.jpg",
            "is_follower": false,
            "is_following": false
          },
          "title": "행정권은 대통령을 수반으로 어쩌고 어쩌고!",
          "content": "행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다. 행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다. 행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다.",
          "liked": true,
          "likeCnt": 4,
          "commentCnt": 31,
          "scrapCnt": 331,
          "scraped": false,
          "categoryId": 5,
          "bgColorType": "YELLOW",
          "fontStyleType": "NORMAL",
          "tags": [
            "일기",
            "일상"
          ],
          "postAt": "2024-03-04 MON"
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
          "postId": 1,
          "profile": {
            "profile_id": "honggd",
            "nickname": "어스름늑대",
            "photo_url": "https://images.dog.ceo/breeds/bulldog-boston/n02096585_11731.jpg",
            "is_follower": false,
            "is_following": false
          },
          "title": "행정권은 대통령을 수반으로 어쩌고 어쩌고!",
          "content": "행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다. 행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다. 행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다.",
          "liked": true,
          "likeCnt": 4,
          "commentCnt": 31,
          "scrapCnt": 331,
          "scraped": false,
          "categoryId": 5,
          "bgColorType": "DEFAULT",
          "fontStyleType": "NORMAL",
          "tags": [
            "일기",
            "일상"
          ],
          "postAt": "2024-03-04 MON"
        },
        {
          "postId": 2,
          "profile": {
            "profile_id": "honggd",
            "nickname": "어스름늑대",
            "photo_url": "https://images.dog.ceo/breeds/bulldog-boston/n02096585_11731.jpg",
            "is_follower": false,
            "is_following": false
          },
          "title": "행정권은 대통령을 수반으로 어쩌고 어쩌고!",
          "content": "행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다. 행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다. 행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다.",
          "liked": true,
          "likeCnt": 4,
          "commentCnt": 31,
          "scrapCnt": 331,
          "scraped": false,
          "categoryId": 5,
          "bgColorType": "YELLOW",
          "fontStyleType": "NORMAL",
          "tags": [
            "일기",
            "일상"
          ],
          "postAt": "2024-03-04 MON"
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
          "postId": 1,
          "profile": {
            "profile_id": "honggd",
            "nickname": "어스름늑대",
            "photo_url": "https://images.dog.ceo/breeds/bulldog-boston/n02096585_11731.jpg",
            "is_follower": false,
            "is_following": false
          },
          "title": "행정권은 대통령을 수반으로 어쩌고 어쩌고!",
          "content": "행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다. 행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다. 행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다.",
          "liked": true,
          "likeCnt": 4,
          "commentCnt": 31,
          "scrapCnt": 331,
          "scraped": false,
          "scrapComment": "스크랩 코멘트 테스트",
          "categoryId": 5,
          "bgColorType": "DEFAULT",
          "fontStyleType": "NORMAL",
          "tags": [
            "일기",
            "일상"
          ],
          "postAt": "2024-03-04 MON"
        },
        {
          "postId": 2,
          "profile": {
            "profile_id": "honggd",
            "nickname": "어스름늑대",
            "photo_url": "https://images.dog.ceo/breeds/bulldog-boston/n02096585_11731.jpg",
            "is_follower": false,
            "is_following": false
          },
          "title": "행정권은 대통령을 수반으로 어쩌고 어쩌고!",
          "content": "행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다. 행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다. 행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다.",
          "liked": true,
          "likeCnt": 4,
          "commentCnt": 31,
          "scrapCnt": 331,
          "scraped": false,
          "scrapComment": "스크랩 코멘트 테스트",
          "categoryId": 5,
          "bgColorType": "YELLOW",
          "fontStyleType": "NORMAL",
          "tags": [
            "일기",
            "일상"
          ],
          "postAt": "2024-03-04 MON"
        }
      ]
    }
""".trimIndent()

val postResponseTestData = """
    {
      "postId": 2,
      "profile": {
        "profile_id": "honggd",
        "nickname": "어스름늑대",
        "photo_url": "https://images.dog.ceo/breeds/bulldog-boston/n02096585_11731.jpg",
        "is_follower": false,
        "is_following": false
      },
      "title": "행정권은 대통령을 수반으로 어쩌고 어쩌고!",
      "content": "행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다. 행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다. 행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다.",
      "liked": true,
      "likeCnt": 4,
      "commentCnt": 31,
      "scrapCnt": 331,
      "scraped": false,
      "categoryId": 5,
      "bgColorType": "YELLOW",
      "fontStyleType": "NORMAL",
      "tags": [
        "일기",
        "일상"
      ],
      "postAt": "2024-03-04 MON"
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