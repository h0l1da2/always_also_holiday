# 프로젝트 소개
<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fpdyvo%2FbtshHMQ3AGA%2FjyJlOfloclRuI5ZtAi7qRK%2Fimg.png">
세상에서 제일 멋진 휴일이의 팬들을 위한<br>
휴일이 팬 사이트
<br><br>

<img src="src/main/resources/readme/readmeMain.png">

<aside>
🤩 1인으로 진행한 토이 프로젝트입니다. 오늘도 휴일은 휴일이의 팬들을 위한 서비스입니다. 휴일이의 지난 업적을 살펴보거나 팬레터를 작성할 수 있고, 굿즈 중고 장터에서 물건을 사고 팔 수 있습니다.

</aside>

<h2>작업자</h2>
강휴일 1인 개발

<h2>작업 기간</h2>
2023.03.07 ~ 2023.05.15

## 🕹️ 기술 스택

- Java , Spring Boot, Spring Security
- JWT, OAuth2
- MySQL , JPA
- Thymeleaf

## 🤗 역할

- **1인 개발**
- 기획부터 DB 설계, 기타 모든 작업 1인 진행
  - JWT 인증 로그인 및 회원가입(OAuth2) , 비밀번호 변경
  - 이미지 포함 게시판 CRUD 와 List , 댓글 작성 및 삭제
  - AWS EC2 배포와 Route53 도메인 연결, 기타 S3 및 RDS 사용

## 주소
*(https://alwaysalsoholiday.com/) -> 현재 서버 OFF

*페이지 확인(https://hyuil.tistory.com/199)


## 🐳 Backend

- [JWT 를 이용한 로그인 회원 가입 구현](https://hyuil.tistory.com/188)
  - JWT 를 제대로 이해하지 못하고 클라이언트와 통신하지 않는 서비스인데도 불구하고 JWT 를 구현
  - 해당 서비스에는 굳이 JWT 가 아니라 스프링 세션 로그인을 하는 것이 나았을 것 같다는 아쉬움
  - 다음부터는 필요한 상황에 맞게 인증 방법을 다르게 하자.
- [SNS 로그인 + JWT + Redirect 기능 구현](https://hyuil.tistory.com/193)
  - Redirect 기능 추가를 위해 OAuth2AuthorizationRequestResolver 구현
- [Pageable 객체를 이용한 게시판 페이징](https://hyuil.tistory.com/192)
- [AWS를 이용하여 오늘도 휴일 배포 완료.](https://alwaysalsoholiday.com/) (현재 Server OFF)
- Soft Delete → remove_date 추가
  - remove_date == null ? 삭제 데이터 : 실제 데이터 → 필터링 작업을 거쳐서 데이터 보여줌.
- Thymeleaf layout 기능으로 중복 header, footer 따로 관리
- 다중 이미지 업로드 → *`MULTIPART_FORM_DATA_VALUE`*
  - AWS S3 에 이미지 파일 저장


## 주요 기능

### 로그인 회원가입

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


### 게시판



```swift
팬레터
- 다중 이미지 업로드 가능
- 업로드 이미지는 AWS S3 서버에 저장

댓글
- ajax 를 이용한 비동기 통신
- 작성 후 댓글 창 부분만 리로드
- 대댓글 작성 가능
```

### 기타



```swift
- 여러 컨트롤러에서 반복되는 중복 코드 줄이기 위해 WebService 구현
 * Session 에서 id 를 가져오는 코드
 * ResponseEntity 를 보내는 코드
 * 특정 Dto 를 Null Check 하는 코드 등 ...

- 예외 컨트롤을 편하게 하기 위해 사용자 정의 예외 구현
 * BadRequestException
 
- JPA 페이징 기능(Pageable)을 이용하기 위해 JPARepository 이용

- AWS RDS 이용

- Postman Test
```


### 😵 힘들었던 점

- **JWT 이해가 힘들어서 개발 진행이 막혔다.**
  - “스프링 시큐리티 인 액션” 서적 구매 후 학습하며 개발
- **OAuth2 를 구현할 때 Redirect 기능을 추가하고 싶다.**
  - ChatGPT 활용.
    - ChatGPT 의 말은 **무조건 걸러들어야 한다**. 확인하는 작업을 거쳤다.
  - 기존에 구현했던 JWT 구현을 응용해 OAuth2 에 맞게 적용했다.
  - 각 Client Server 에 있는 가이드를 활용해 클라이언트마다 어떤 방식으로 인증을 하는지 보고 각 클라이언트만의 방식으로 구현했다.

### 👏 이렇게 개선했어요!

- [JPA 관련 코드 개선](https://github.com/h0l1da2/always_also_holiday/commit/322a11df4ad4602d890f04930a96c8390d886913)
- [객체지향에 맞도록 MailService 코드 개선](https://hyuil.tistory.com/204)
- 3.0 마이그레이션 후, Junit5 를 이용한 [Service Test](https://hyuil.tistory.com/212) 와 [Controller Test](https://hyuil.tistory.com/213)
  - [실제로 Test 후, 오류를 잡고 수정.](https://hyuil.tistory.com/214)