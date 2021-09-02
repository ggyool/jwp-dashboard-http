## 🚀 1단계 - HTTP 서버 구현하기
### ✏️ GET /index.html 응답하기
- [x] http request 를 파싱하기
  - [x] StringUtils 만들기
- [x] http response 만들기
- [x] 정적 요청 관련 매핑
  - [x] html 요청 관련 매핑
  - [x] css 요청 관련 매핑
  - [x] js 요청 관련 매핑
- [x] forward 요청 관련 매핑
- [x] redirect 요청 관련 매핑
- [x] 매핑이 없으면 404를 응답한다.
- [x] 인증에 실패하면 401을 응답한다.
- [x] POST 구현
  - [x] 회원가입 구현
  - [x] login POST로 리팩터링
  

## 1단계 리팩터링 목록
- HttpParseUtils 라는 이름이 마음이 안 들기도 하고 딴 데서는 안 쓰일 것 같이 변해가서 객체로 분리하도록 함 (HttpTranslator)
