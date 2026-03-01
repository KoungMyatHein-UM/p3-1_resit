plugins {
    java
    id("org.springframework.boot") version "3.5.11"
    id("io.spring.dependency-management") version "1.1.7"
    id("io.freefair.lombok") version "8.6"
}

group = "lol.khakikukhi"
version = "0.0.1-SNAPSHOT"
description = "p3-1_resit"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webflux")
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        // Matches Spring Boot 3.5.x
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.0.0")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
