# 웹 애플리케이션 서버
## 진행 방법
* 웹 애플리케이션 서버 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)


## 기능 목록
1. HTTP GET 요청을 통해 정적 리소스를 반환한다.
   - HTTP 헤더를 파싱한다.
   - path에 해당하는 리소스를 반환한다. 
2. CSS 요청을 처리한다.
   - Accept 필드로 요청을 구분한다.
3. Query String 파싱하고 처리한다.
4. HTTP POST로 회원가입할 수 있다.
    - RFC2616에 의거 content-type을 헤더에 담는 것이 표준임으로 없는 요청은 무시한다.
    - Body는 key,value 타입의 형식만 받는다.
    - 현재는 application/x-www-form-urlencoded만 지원
5. 회원가입 이후 Redirect할 수 있다.
