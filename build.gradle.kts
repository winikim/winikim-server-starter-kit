import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugin.springBoot) version Version.springBoot
    id(Plugin.springDependencyManagement) version Version.springDependencyManagement
    id(Plugin.kotlinJpa) version Version.kotlin
    idea
    kotlin("jvm") version Version.kotlin
    kotlin("plugin.spring") version Version.kotlin
}

group = "me.winikim"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}



tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

allprojects {
    group = "me.winikim"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
        mavenLocal()
        jcenter()
        maven("https://repo.spring.io/release")
        maven("http://repo.spring.io/milestone/")
    }

}


subprojects {
    apply(plugin = Plugin.kotlin)
    apply(plugin = Plugin.kapt)
    apply(plugin = Plugin.kotlinJpa)
    apply(plugin = Plugin.kotlinSpring)
    apply(plugin = Plugin.springBoot)
    apply(plugin = Plugin.springDependencyManagement)

    configurations.forEach { it.exclude("org.springframework.boot", "spring-boot-starter-logging") }

    tasks {
        val bootJar by getting(org.springframework.boot.gradle.tasks.bundling.BootJar::class) {
            enabled = false
        }

        val jar by getting(Jar::class) {
            enabled = true
        }
        compileKotlin {
            kotlinOptions.jvmTarget = "1.8"
            kotlinOptions.freeCompilerArgs =
                listOf(
                    "-Xopt-in=kotlin.RequiresOptIn",
                    "-XXLanguage:+InlineClasses"
                )
        }

        compileTestKotlin {
            kotlinOptions.jvmTarget = "1.8"
        }
    }


    noArg {
        annotation("javax.persistence.Entity")
    }

    allOpen {
        annotations(
            "javax.persistence.Entity"
        )
    }

    dependencies {
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.apache.logging.log4j:log4j-api:2.17.1")
        implementation("org.apache.logging.log4j:log4j-core:2.17.1")
        implementation("org.apache.logging.log4j:log4j-jul:2.17.1")
        implementation("org.apache.logging.log4j:log4j-sl4j-impl:2.17.1")
        implementation("org.slf4j:slf4j-api:1.7.32")
        implementation("org.slf4j:jul-to-slf4j:1.7.32")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

project(":server-boot") {
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        api(project(":presentation"))
    }
}

project(":presentation") {
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        api("org.springframework.boot:spring-boot-starter-security")
        implementation ("io.jsonwebtoken:jjwt:0.9.1")
        api(project(":application"))
    }
}

project(":application") {
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation ("io.jsonwebtoken:jjwt:0.9.1")
        api(project(":domain"))
        api(project(":infrastructure"))
    }
}

project(":domain") {
    dependencies {
        api("org.springframework.boot:spring-boot-starter-data-jpa")
    }
}

project(":infrastructure") {
    dependencies {
        runtimeOnly("com.h2database:h2")
        implementation("io.ktor:ktor-client-apache:1.5.3")
        implementation("io.ktor:ktor-client-json-jvm:1.5.3")
        implementation("io.ktor:ktor-client-jackson:1.5.3")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        api(project(":domain"))

    }
}

