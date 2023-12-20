# 게시판 API (Spring Board Server)

<p align="center">
    <img src="https://github.com/maruhxn/spring-board-server/assets/127298684/324b04be-637d-4087-914e-33a6f4e41ea4" />
</p>

![Generic badge](https://img.shields.io/github/license/maruhxn/spring-board-server)
![Generic badge](https://img.shields.io/badge/version-1.0.0-brightgreen.svg)
![Generic badge](https://img.shields.io/badge/java-17-red.svg)
![Generic badge](https://img.shields.io/badge/spring_framework-6-yellowgreen.svg)
![Generic badge](https://img.shields.io/badge/spring_boot-3.1.5-darkgreen.svg)
![Generic badge](https://img.shields.io/badge/test-61_passed-blue.svg)

> 스프링을 공부하면서 만들어보는 첫 연습용 게시판 프로젝트

## Getting Started

### Prerequisites

- Docker, Docker Compose for Production
- OpenJDK 17
- IntelliJ IDEA 2023.2.2

### Development

개발 환경(Dev profile)의 경우 메모리 H2 DB를 사용하며, redis session store를 사용하지 않으므로 특별한 설정 없이 빌드:

```shell
./gradlew clean build
```

이후 생성된 jar 파일을 dev 환경에서 실행하기 위해 다음을 실행:

```shell
java -jar -Dspring.profiles.active=dev build/libs/board-server-1.0.0.jar
```

### Production

배포 환경에서 사용하기 위해서는 Docker 및 Docker Compose의 사전 설치가 필요하다. 개발 환경에서는 Redis를 사용하지 않으며 메모리 DB를 사용하지만, 배포환경에서는 docker 컨테이너로
실행되고 있는 mysql 및 redis를 사용하므로 <code>docker-compose.yml</code>을 확인하고 <code>application-prod.yml</code>에 datasource 및 redis
설정을 마친다.

위 과정을 마친 후 다음을 수행한다 :

혹시라도 실행되고 있을 이미지 및 컨테이너가 있다면 이를 종료 및 삭제한다.

```shell
docker-compose down --rmi all
```

초기화가 되었다면 다시 이미지를 받아오고 컨테이너화 한다.

```shell
docker-compose up -d
```

3306, 6379, 8080 포트에서 각각 mysql, redis, spring-board-server가 구동되고 있는 것을 확인한다.

## Running the tests

다음의 스크립트를 통해 test를 진행할 수 있다.

```shell
./gradlew test
```

## Authors

* **maruhxn** - *Initial work* - [maruhxn_](https://github.com/maruhxn)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

