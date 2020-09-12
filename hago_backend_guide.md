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

1. User (사용자)
    
2. Board (게시판)    
    
3. Post (고민글)
    
4. Comment (댓글)

5. HashTag (해시태그)
    
    
    
* Role (관리자/일반사용자)
* BaseTimeEntity (생성날짜/수정날짜)

```



<hr/>
# 4. 개발

## feat.01 Kakao Login

<center><image src="./images/login_step.PNG"></image></center>
### 카카오 로그인 진행 과정

<br/>

사용자 토큰은 매번 인증을 거치지 않고도 일정 기간 카카오 API를 사용할 수 있도록 하는 권한 증명입니다. Kakao SDK는 사용자 토큰 관리 기능을 갖고 있습니다. REST API 사용 시에는 필요에 따라 토큰 정보 확인이나 갱신을 위한 요청을 해야 합니다.

사용자 토큰은 두 가지입니다. 보안상의 이유로 권한 증명 역할을 하는 **액세스 토큰(Access Token)**은 비교적 짧은 유효기간을 가지며, 일정 기간 동안 인증을 거치지 않고 액세스 토큰을 갱신할 수 있게 해주는 역할을 하는 **리프레시 토큰(Refresh Token)**은 보다 긴 유효기간을 가집니다.

사용자 토큰 유효기간은 플랫폼마다 다릅니다. 사용자 토큰의 역할과 유효기간을 표로 정리하면 다음과 같습니다.

<br/>

```java
/* 
 1. 카카오 로그인 창에서 ID, PWD 입력 하면
 2. 카카오 API 서버에서 code를 준다. callback 주소로
 3. code가 정상이면, 카카오 API 서버에 token(Access Token) 요청
 4. 카카오 API 서버로부터 token(Access Token)을 발급받는다.
 5. token으로 카카오 자원 서버에 자원을 요청
 6. 카카오 자원 서버로부터 사용자 정보를 받는다.
*/
```

<br/>

| Token Type    | Role                                                         | Valid                                                     |
| :------------ | :----------------------------------------------------------- | :-------------------------------------------------------- |
| Access Token  | 사용자를 인증합니다.                                         | Android, iOS : 12시간 JavaScript: 2 시간 REST API : 6시간 |
| Refresh Token | 일정 기간 동안 다시 인증 절차를 거치지 않고도 액세스 토큰 발급을 받을 수 있게 해 줍니다. | 2달 유효기간 1달 남은 시점부터 갱신 가능                  |

<br/>

### JWT(Json Web Token)



<center><image src="./images/jwt_exam.PNG"></image></center>
<br/>

#### when should you use



<center><image src="./images/jwt_use.PNG"></image></center>

• 하나의 End Point가 아닌 Mobile/Web 등의 multiple endpoint 환경이라면 통합적인 인증/인가 환경을 제공하기위해선 반드시 JWT를 사용합니다.

• 보편적인 예로, React로 client를 구성하고 REST API 서버를 따로 두는 경우에는 JWT를 사용합니다.

• 3rd party에게 public 하게 open 한 REST Endpoint가 존재할 경우 해당 3rd party의 인증/인가를 관리하기 위해 JWT를 사용합니다.

<br/>

<center><image src="./images/jwt.PNG"></image></center>

> #### 이때 각각의 part는 다음과 같은 정보를 가집니다.

• **Header** : token의 type과 JWT를 digitally sign할때 사용한 algorithm을 정의합니다.

• **Payload** : JWT에 담아서 전달할 data를 정의합니다.

• **Signature** : 위의 Header와 Payload 값을 base64로 encode한 값을 JWT secret key 값으로 encrypt한 값을 명시합니다.

<br/>

# feat.02 Naver Login

네이버 아이디로 로그인 (이하 네아로) 서비스는 네이버가 아닌 다른 서비스에서 네이버의 사용자 인증 기능을 이용할 수 있게 하는 서비스입니다.

별도의 아이디나 비밀번호를 기억할 필요 없이 네이버 아이디로 간편하고 안전하게 서비스에 로그인할 수 있어, 가입이 귀찮거나 가입한 계정이 생각나지 않아 서비스를 이탈하는 사용자를 잡고 싶다면 네아로를 사용해 보세요.

<br/>

### 네아로 회원의 프로필 정보 

네아로 연동을 성공적으로 완료하면 연동 결과로 접근 토큰을 얻을 수 있습니다.
접근 토큰을 이용하여 사용자 프로필 정보를 조회할 수 있습니다.

<br/>

### 제공가능한 프로필 정보

- 이용자 고유 식별자 (네이버 ID가 아닌 고유 식별자)
- 이름
- 닉네임 (네이버 별명)
- 프로필 이미지 (네이버 내정보 프로필 이미지 URL)
- 이메일 (네이버 내정보 이메일)
- 생일
- 연령대
- 성별

**각각의 프로필 정보의 규격은 다음과 같습니다.**

- 이용자 고유 식별자 : INT64 규격의 숫자
- 이름 : 10자 이내로 구성된 문자열
- 닉네임 : 20자 이내로 구성된 문자열
- 프로필 이미지 : 255자 이내로 구성된 URL 형태의 문자열
- 이메일 : 이메일 규격의 문자열
- 생일 : 월-일 (MM-DD) 형태의 문자열
- 연령대 : 연령 구간에 따라 0-9 / 10-19 / 20-29 / 30-39 / 40-49 / 50-59 / 60- 으로 표현된 문자열
- 성별 : M/F (남성/여성) 으로 표된 문자

**사용할 프로필 정보 예시**

```java
- 이용자 고유 식별자 : 120381022
- 닉네임 : 네이버닉네임
- 프로필 이미지 : https://phinf.pstatic.net/.../image.jpg
- 연령대 : 20-29
```

<hr/>

<center><image src="./images/naver_login.PNG"></image></center>

<br/>

<hr/>

# AWS S3_ 정적 파일 업로더

> SpringBoot로 서비스를 구축하다보면 꼭 만들어야할 것이 **정적 파일 업로더**입니다.
> 이미지나 HTML과 같은 정적 파일을 S3에 제공해서 이를 원하는 곳에서 URL만으로 호출할 수 있게 하는걸 말합니다.

<br/>

### 로컬 환경 구성

### build.gradle

```groovy
repositories {
    mavenCentral()
    maven { url 'https://repo.spring.io/libs-milestone'}
}


dependencies {
    compile('org.springframework.boot:spring-boot-starter-web')
    compile('org.springframework.cloud:spring-cloud-starter-aws')

    // handlebars
    compile 'pl.allegro.tech.boot:handlebars-spring-boot-starter:0.3.0'

    compileOnly('org.projectlombok:lombok')
    testCompile('org.springframework.boot:spring-boot-starter-test')
}


dependencyManagement {
    imports {
        mavenBom 'org.springframework.cloud:spring-cloud-aws:2.0.0.RC2'
    }
}
```

>  Spring Cloud AWS가 현재 (2018.06.03) 2.0.0.RC2가 최신이라 `maven { url 'https://repo.spring.io/libs-milestone'}`가 필요합니다.

<br/>

### S3Uploader.java

다음으로 S3에 정적 파일을 올리는 기능을 하는 `S3Uploader.java` 파일을 생성합니다.

> 여기선 Lombok을 사용한 코드가 있으니 참고하세요.

```java
@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}
```

> 롬복의 `@RequiredArgsConstructor`는 `final` 멤버변수가 있으면 생성자 항목에 포함시킵니다.
> 여기선 `AmazonS3Client amazonS3Client`만 `final`에 있으니 생성자에 `AmazonS3Client amazonS3Client`만 포함됩니다.
> 반대로 `String bucket`는 생성자 항목에 포함되지 않습니다.
> 생성자에 포함된 `amazonS3Client`는 DI를 받게되며, `bucket`는 평범한 멤버변수로 남습니다.

<br/>

코드의 순서는 간단합니다.

1. MultipartFile을 전달 받고

2. S3에 전달할 수 있도록 MultiPartFile을 File로 전환

3. S3에 Multipartfile 타입은 전송이 안됩니다. 전환된 File을 S3에 public 읽기 권한으로 put

   외부에서 정적 파일을 읽을 수 있도록 하기 위함입니다.

4. 로컬에 생성된 File 삭제

   Multipartfile -> File로 전환되면서 로컬에 파일 생성된것을 삭제합니다.

5. 업로드된 파일의 S3 URL 주소를 반환



여기서 재밌는 것은 별다른 Configuration 코드 없이 `AmazonS3Client` 를 DI 받은것인데요.
Spring Boot Cloud AWS를 사용하게 되면 **S3 관련 Bean을 자동 생성**해줍니다.
그래서 아래 3개는 **직접 설정할 필요가 없습니다**.

- AmazonS3
- AmazonS3Client
- ResourceLoader

참고로 위의 코드 중, `dirName`은 **S3에 생성된 디렉토리**를 나타냅니다.



<hr/>



