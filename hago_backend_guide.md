# 1. DB Server 세팅

#### 	1-1. AWS EC2 (ubuntu 18.04) 만들기

 - Elastic IP 설정 이후 ppk 제작 후 putty를 사용하여 접속

   

   #### 1-2. MySQL 8.0 다운로드

- 참고 : https://www.tecmint.com/install-mysql-8-in-ubuntu/

- 설치 이후 계정 설정, 권한부여 후 workbench로 접속해보기

  <br/>

[HaruGomin-Database]: MySQL

<br/>

<br/>

# 2. Project 생성

<center><image src="./images/spring_initializr.PNG"></image></center>
<br/>

<center><image src="./images/project_settings.PNG"></image></center>
<br/>

<center><image src="./images/project_dependencies.PNG"></image></center>
<br/>

**핵심 라이브러리**

- 스프링 MVC
- 스프링 ORM
- JPA, 하이버네이트
- 스프링 데이터 JPA

**기타 라이브러리**

- MySQL Connector
- 커넥션 풀: 부트 기본은 HikariCP
- WEB(thymeleaf)
- 로깅 SLF4J & LogBack

<br/>

<hr/>
# 3. 설계

### 3 Layer Architecture

#### controller

- URL과 실행 함수를 매핑
- 비즈니스 로직이 있는 service를 호출하여 비즈니스 로직 처리

#### service

- 비즈니스 로직 구현
- 데이터 처리(모델)를 담당하는 repository에서 데이터를 받아와 controller에 넘겨주거나, 비즈니스 로직을 처리함

#### entity

- DB 테이블과 매핑되는 객체를 정의
- JPA에서는 Entity를 통해 데이터를 조작함

#### repository

- 데이터를 가져오거나 조작하는 함수를 정의
- Interface를 implements하여 미리 만들어진 함수를 사용할 수 있으며, 또한 직접 구현이 가능

#### dto

- controller와 service 간에 주고 받을 객체를 정의하며, service로의 요청이나 view에 뿌려줄 객체

<br/>

다음으로 resources 디렉터리의 역할은 다음과 같습니다.

- #### static

- - css, js, img 등의 정적 자원들을 모아놓은 디렉터리입니다.

- #### templates

- - 템플릿을 모아놓은 디렉터리입니다.
  - Thymeleaf는 HTML을 사용합니다.

- <br/>

#### 실제 코드에서는 DB에 소문자 + _ (언더스코어) 스타일을 사용

- Member 는 member, postHistory는 post_history로 사용

<hr/>
## Git-flow 전략 간단하게 살펴보기

Git-flow를 사용했을 때 작업을 어떻게 하는지 살펴보기 전에 먼저 Git-flow에 대해서 간단히 살펴보겠습니다.
Git-flow에는 5가지 종류의 브랜치가 존재합니다. 항상 유지되는 메인 브랜치들(master, develop)과 일정 기간 동안만 유지되는 보조 브랜치들(feature, release, hotfix)이 있습니다.

- master : 제품으로 출시될 수 있는 브랜치
- develop : 다음 출시 버전을 개발하는 브랜치
- feature : 기능을 개발하는 브랜치
- release : 이번 출시 버전을 준비하는 브랜치
- hotfix : 출시 버전에서 발생한 버그를 수정 하는 브랜치


Git-flow를 설명하는 그림 중 이만한 그림은 없는 것 같습니다.

<center><image src="./images/git_flow.PNG"></image></center>
<br/>

git flow가 사용하는 branch는 크게 두가지로 나뉜다.

프로젝트가 끝날 때까지 항상 유지되는 **main branch**와 필요할 때만 사용하고 소멸되는 **sub branch**이다.

메인 브랜치에는 master와 develop가 있고 서브 브랜치에는 feature, release와 hotfix 이렇게 3가지가 있다.

- **master** : 제품으로 출시(Launching)할 수 있는 브랜치
- **develop** : 개발 브랜치로 여기서 **feature, release**가 분기되고 병합된다.
- **feature** : **develop**로 부터 분기되어 개별 기능을 개발하는 브랜치. 기능개발이 완료되면 다시 **develop**로 병합된다.
- **release** : 기능 개발이 완료되어 출시 버전을 준비하는 브랜치.
  - 주로 **주석을 정리**하거나 개발에 참고했던 자료를 **.gitignore**에 등록하고, **readme**를 정리하는 작업
- **hotfix** : 출시 버전에서 발생한 버그를 수정하는 브랜치로 유일하게 **master**에서 분기.

[Git Kraken을 사용한 Git flow 전략]: https://www.youtube.com/watch?v=t6f7ZUG5vAU

<br/>



### Start GitKraken & Update Git-flow

<center><image src="./images/start_gitkraken.PNG"></image></center>

<hr/>

# Entity Modeling



<center><image src="./images/entity_modeling.PNG"></image></center>

<br/>


```java
// Entity Modeling

1. User (사용자 계정)
    
    
2. Post (고민글)
    
    
3. Comment (댓글)
    
    
    
    
* Role (관리자/일반사용자)
* BaseTimeEntity (생성날짜/수정날짜)

```



<hr/>
# 4. 개발

## feat.01 Kakao Login

<center><image src="./images/login_step.PNG"></image></center>

카카오 로그인 진행 과정

<br/>

사용자 토큰은 매번 인증을 거치지 않고도 일정 기간 카카오 API를 사용할 수 있도록 하는 권한 증명입니다. Kakao SDK는 사용자 토큰 관리 기능을 갖고 있습니다. REST API 사용 시에는 필요에 따라 토큰 정보 확인이나 갱신을 위한 요청을 해야 합니다.

사용자 토큰은 두 가지입니다. 보안상의 이유로 권한 증명 역할을 하는 **액세스 토큰(Access Token)**은 비교적 짧은 유효기간을 가지며, 일정 기간 동안 인증을 거치지 않고 액세스 토큰을 갱신할 수 있게 해주는 역할을 하는 **리프레시 토큰(Refresh Token)**은 보다 긴 유효기간을 가집니다.

사용자 토큰 유효기간은 플랫폼마다 다릅니다. 사용자 토큰의 역할과 유효기간을 표로 정리하면 다음과 같습니다.

<br/>

| Token Type    | Role                                                         | Valid                                                     |
| :------------ | :----------------------------------------------------------- | :-------------------------------------------------------- |
| Access Token  | 사용자를 인증합니다.                                         | Android, iOS : 12시간 JavaScript: 2 시간 REST API : 6시간 |
| Refresh Token | 일정 기간 동안 다시 인증 절차를 거치지 않고도 액세스 토큰 발급을 받을 수 있게 해 줍니다. | 2달 유효기간 1달 남은 시점부터 갱신 가능                  |

<br/>



