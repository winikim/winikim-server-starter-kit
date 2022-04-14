dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation ("io.jsonwebtoken:jjwt:0.9.1")
    api(project(":domain"))
    api(project(":infrastructure"))
}