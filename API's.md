
### <span style="color: red;">**GET**</span> 커뮤니티 조회
```
/api/posts
```
**Response**
```
{
    "statusCode": 200,
    "data": [
        {
            "title": "제목",
            "content": "내용",
            "authorName": "작성자"
            "postImage": "/image_storage/photo.jpg",
            "likeCount": "1",
            "viewCount": "2",
            "commentCount": "3",
            "createdAt": "2025-03-30 00:00:00",
            "updatedAt": "2025-03-30 00:00:00",
        }
    ]
}
```

### <span style="color: red;">POST</span> 커뮤니티 글 작성
```
/api/posts
```
**Body** <span> form-data</span>
|KEY|VALUE|
|:--:|:--:|
|title|테스트 제목입니다.|
|content|테스트 내용입니다.|
|file|\<file>|

**Response**
```
{
    "statusCode": 200,
    "responseMessage": "커뮤니티 작성 성공",
    "data": null
}
```
