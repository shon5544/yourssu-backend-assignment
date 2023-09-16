## 시작하기 전에...
- 프로젝트에 스프링 시큐리티를 이용한 인증 필터가 들어가있습니다.
- 요구사항을 보니 토큰이 필요없는데, 코프링 처음이라고 신나서 먼저 막 코딩했더니 들어가게 되었네요.
- #feature/10-token-free-version 브랜치에 매 요청에 토큰이 없어도 작동하게끔 넣었으니 참고해주세요..!

## apis
> 인수 테스트 통과

### 회원 가입
> 💡 POST /user/signup

### 회원 탈퇴
> 💡 DELETE /user/withdraw

### 게시글 작성
> 💡 POST /article/write

### 게시글 수정
> 💡 PUT /article/edit/{articleId}

### 게시글 삭제
> 💡 DELETE /article/delete/{articleId}

### 댓글 작성
> 💡 POST /comment/write/{articleId}

### 댓글 수정
> 💡 PUT /comment/edit?article={articleId}&comment={commentId}

### 댓글 삭제
> 💡 DELETE /comment/delete/article={articleId}&comment={commentId}

### ++ 로그인
> 💡 POST /user/login

request form
```json
{
    "email": "email@urssu.com",
    "password": "password"
}
```

response form
```json
// success
{
    "success": true,
    "message": "로그인 성공.",
    "accessToken": "accessToken",
    "refreshToken": "refreshToken"
}
```
```json
// failure: 회원 가입되지 않은 유저가 로그인 하려할 때
{
    "success": false,
    "message": "로그인 실패: 인증 객체 생성 실패: 해당 email을 가진 유저가 없습니다."
}

// failure: 비밀번호가 틀렸을 때
{
    "success": false,
    "message": "로그인 실패: 자격 증명에 실패하였습니다."
}
```
