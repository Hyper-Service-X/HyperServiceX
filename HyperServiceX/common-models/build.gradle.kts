plugins {
    java
    eclipse
    maven
}

group = "com.hsx"
version = "1.0-SNAPSHOT"


repositories {

    mavenCentral()
}

dependencies {
    compile("javax.persistence", "persistence-api", "1.0")
    compile("com.fasterxml.jackson.core", "jackson-core", "2.9.7")
    compile("org.springframework.hateoas", "spring-hateoas", "0.18.0.RELEASE")
    compile("org.springframework", "spring-core", "5.1.1.RELEASE")
    compile("com.fasterxml.jackson.core", "jackson-databind", "2.9.7")
    compile("org.springframework", "spring-webmvc", "5.2.2.RELEASE")
    compile("org.springframework.boot:spring-boot-starter-data-jpa:2.3.0.RELEASE")

    // Validation
    compile("javax.validation","validation-api", "2.0.1.Final")

    //lombok
    compileOnly("org.projectlombok:lombok:1.18.8")
    testCompileOnly("org.projectlombok:lombok:1.18.8")
    annotationProcessor("org.projectlombok:lombok:1.18.8")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.8")

}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
