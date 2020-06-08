plugins {
    java
    maven
    idea
}

group = "com.hsx.messaging"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {

    testCompile("junit", "junit", "4.12")
    implementation("org.springframework.boot:spring-boot-starter-web:2.2.2.RELEASE")

    //Solace Dependencies
    compile("com.solacesystems:sol-jcsmp:10.6.4")
    compile("com.solace.cloud.core:solace-services-info:0.4.1")
    compile("com.solace.cloud.cloudfoundry:solace-spring-cloud-connector:4.1.1")

    compileOnly("org.projectlombok:lombok:1.18.8")
    testCompileOnly("org.projectlombok:lombok:1.18.8")
    annotationProcessor("org.projectlombok:lombok:1.18.8")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.8")

    compile("com.fasterxml.jackson.core", "jackson-databind", "2.9.4")
    implementation("org.apache.commons:commons-text:1.8")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}