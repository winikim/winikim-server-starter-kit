dependencies {
    runtimeOnly("com.h2database:h2")
    implementation("io.ktor:ktor-client-apache:1.5.3")
    implementation("io.ktor:ktor-client-json-jvm:1.5.3")
    implementation("io.ktor:ktor-client-jackson:1.5.3")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    api(project(":domain"))
}