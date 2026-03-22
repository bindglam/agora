plugins {
    java
    war
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.bindglam"
version = "0.0.1-SNAPSHOT"
description = "agora"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat-runtime")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.xerial:sqlite-jdbc:3.41.2.1")
    implementation("org.hibernate.orm:hibernate-community-dialects")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.2")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.61")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
