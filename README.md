# 2-ethan-park-community-be

A community project built with Java and MySQL.

## 기술 스택

- **Java:** 21.0.5
- **Database:** MySQL 9.2.0
- **툴:** MySQL Workbench

[POST]   /api/users                          # 회원가입
[POST]   /api/auth/login                     # 로그인
[PUT]    /api/users/me/profile               # 프로필 수정
[PUT]    /api/users/me/password              # 비밀번호 수정

[GET]    /api/posts                          # 게시글 목록 조회
[GET]    /api/posts/{postId}                 # 게시글 상세 조회
[POST]   /api/posts                          # 게시글 작성
[PUT]    /api/posts/{postId}                 # 게시글 수정
[DELETE] /api/posts/{postId}                 # 게시글 삭제

[POST]   /api/posts/{postId}/comments        # 댓글 작성
[PUT]    /api/posts/{postId}/comments/{id}   # 댓글 수정
[DELETE] /api/posts/{postId}/comments/{id}   # 댓글 삭제

[POST]   /api/posts/{postId}/likes           # 좋아요
[DELETE] /api/posts/{postId}/likes           # 좋아요 취소