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
        jvmTarget = "11"
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
            kotlinOptions.jvmTarget = "11"
            kotlinOptions.freeCompilerArgs =
                listOf(
                    "-Xopt-in=kotlin.RequiresOptIn",
                    "-XXLanguage:+InlineClasses"
                )
        }

        compileTestKotlin {
            kotlinOptions.jvmTarget = "11"
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
        implementation("org.springframework.boot:spring-boot-starter-log4j2")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}
