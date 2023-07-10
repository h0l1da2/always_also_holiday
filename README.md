# 프로젝트 소개
<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fpdyvo%2FbtshHMQ3AGA%2FjyJlOfloclRuI5ZtAi7qRK%2Fimg.png">
세상에서 제일 멋진 휴일이의 팬들을 위한<br>
휴일이 팬 사이트
<br>


<h2>주소</h2>
오늘도 휴일 (http://alwaysalsoholiday.com)<br><br>
*혹시 해당 주소가 접속 불가일 경우,<br>티스토리(https://hyuil.tistory.com/199) 에서 페이지를 확인해주세요!<br><br>
<br>
프로젝트 상세 소개 (https://hyuil.notion.site/hyuil/cf625f8928c84daf96a7f2a8cf6104c1?p=8bc1b63fc2264d6f8cdaecb412394b9c&pm=c)



<h2>작업자</h2>
강휴일 1인 개발

<h2>작업 기간</h2>
2023.03.07 ~ 2023.05.15


<h2>기술 스택</h2>
JAVA , SpringBoot , JPA , Gradle, AWS<br>
thymeleaf, HTML/CSS, BootStrap


<h2>주요 기능</h2>

<h3>로그인 회원가입</h3>

```swift
회원가입
- 기본 정보 (아이디 등) 중복 확인 후 가입
- 회원가입 시 이메일 인증 코드 받아야 가입 가능
- OAuth2 회원가입 구현 (SNS 간편 회원 가입)

로그인
- SpringSecurity Filter 를 이용해 JWT 로그인 인증
- 로그인 시 전에 있던 페이지로 리디렉션 하는 기능 추가
- OAuth2 로그인 시
 * 리디렉션 정보를 주기 위해 OAuth2AuthorizationRequestResolver 구현
 * 해당 클라이언트(SNS)에게 유저 정보 받기 위해 OAuth2AuthorizationCodeGrantFilter 구현
```


<h3>게시판</h3>



```swift
팬레터
- 다중 이미지 업로드 가능
- 업로드 이미지는 AWS S3 서버에 저장

댓글
- ajax 를 이용한 비동기 통신
- 작성 후 댓글 창 부분만 리로드
- 대댓글 작성 가능
```

<h3>기타</h3>



```swift
- 여러 컨트롤러에서 반복되는 중복 코드 줄이기 위해 WebService 구현
 * Session 에서 id 를 가져오는 코드
 * ResponseEntity 를 보내는 코드
 * 특정 Dto 를 Null Check 하는 코드 등 ...

- 런타임 예외 컨트롤을 편하게 하기 위해 사용자 정의 예외 구현
 * MemberNotFoundException - 회원을 찾지 못했을 때
 * ThisEntityIsNull - Entity 가 Null 일 때
 
- JPA 페이징 기능(Pageable)을 이용하기 위해 JPARepository 이용

- AWS RDS 이용

- DB 에 정보를 남겨두기 위해 delete 문 사용 대신 remove_date 등을 이용
 * remove_date 가 Null이 아닐 경우 삭제 된 데이터로 간주, 서버에 보여주지 않음
 
- Thymeleaf 레이아웃 기능 이용으로 중복되는 Header, Footer 등을 따로 관리

- Postman 을 이용하여 프론트 적용 전 임의의 데이터를 보내 테스트
```
