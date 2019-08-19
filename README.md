# URL단축 프로젝트

## 1. 프로젝트 소개
### 1) 웹으로 부터 입력받은 Full Url을 해당 웹서비스의 단축서버의 URL로 생성
### 2) 단축URL은 단축서버 주소뒤에 8자의 대소문자로 발생시킴
### 3) 이미 생성된 Fully Url로 재호출시 동일한 단축URL 결과를 발생시킴
### 4) 단축URL호출시 Fully Url로 Redirect 처리

## 2. 문제해결 전략
### 1) 단축주소 생성 : 랜덤숫자를 기준으로 Char로 캐스팅하여 8자리의 랜덤문자 생성
### 2) 동일주소 확인 : File시스템을 통해서 [ 원주소 + "/t" + 단축주소 ] 정보를 등록 저장
                   동일한 원주소 호출시 해당행의 단축주소 리턴
### 3) URL유효성 확인 : org.apache.commons.validator 이용하여 유효하지 않은 URL Validation체크

## 3. 사양
### 1) Language : JDK 1.8
### 2) Framework : Spring 3.2.x
### 3) Server : Tomcat 9.0
### 4) Javascript : JQuery

## 4. 빌드방법
### 1) Git Repositoris에 등록
#### : Eclipse에서 Git repository에서 Clone a Git Repository 선택후 URI에 하기의 주소등록
####   (URI : https://github.com/KoSeungHan/SHORTEN_URL.git )
### 2) Project Import
#### : Directory등록후(ex: C:\project\git\shorten_url) 생성된 Repository에서 WorkingTree의
####   SHORTEN_URL을 Import project    
### 3) 빌드확인
#### : Eclipse버전에 따라 Properties > Java Build Path 에서 Java1.8 이 unbound되어 있을수
####   있으므로 확인
### 4) 서버설정
#### : Eclipse의 Servers에서 Apache > Tomcat9.0 으로 생성 후 SHORTEN_URL을 ADD하고 Finish
####   설치 후 포트변경 (80) , Modules에서 Path를 "/" 으로 Edit
### 5) 기타
#### : DB에서가 아닌 Filesystem으로 데이타를 저장하과 확인하는 구조이므로 File저장위치에
####   대한 읽기/쓰기 권한이 있는지 반드시 설정해 주어야 합니다.
####   ※ File생성 위치 (C:/Temp/ShortenUrlList.txt)

## 5. 실행방법
### 1) 주소창에서 http://localhost 호출
### 2) 주소입력란에 URL입력 후 [URL 단축] 버튼 클릭
### 3) 우측에 표시되는 단축주소 확인 및 [이동하기] 버튼 클릭하여 해당주소 redirect 확인

## 6. JUnit 테스트
### 1) 테스트 클래스 위치 : test/shorten/util/ShortenUtilTest.java
### 2) 테스트 내용
#### -  단축URL생성여부
#### -  동일URL등록시 동일한 단축URL 처리여부
#### -  단축URL 호출시 원래URL로 Redirect할 주소 리턴하는지 여부
