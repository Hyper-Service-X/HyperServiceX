plugins {
    idea
    java
    eclipse

}

group = "com.hsx"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    maven(
            url = "http://www.gridgainsystems.com/nexus/content/repositories/external/"
    )
    mavenCentral()
}


dependencies {

    compile("org.springframework", "spring-web", "5.2.2.RELEASE")
    compile("org.springframework.integration", "spring-integration-core", "5.1.0.RELEASE")
    compile("org.springframework.integration", "spring-integration-stream", "5.1.0.RELEASE")
    compile("org.springframework.boot", "spring-boot-starter-aop", "2.2.2.RELEASE")

    implementation(fileTree(mapOf("dir" to "libs/compile", "include" to listOf("*.jar"))))
    compile("javax.servlet", "javax.servlet-api", "3.1.0")

    compile(project(":common-utils"))

    //Micrometer and metrics dependencies
    compile("org.springframework.boot", "spring-boot-starter-actuator", "2.2.2.RELEASE")
    compile("io.micrometer", "micrometer-registry-prometheus", "1.1.0")
    compile("com.fasterxml.jackson.core", "jackson-databind", "2.9.4")


    compileOnly("org.projectlombok:lombok:1.18.8")
    testCompileOnly("org.projectlombok:lombok:1.18.8")
    annotationProcessor("org.projectlombok:lombok:1.18.8")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.8")

    //Messaging dependencies
    //compile("com.bcs.messaging", "messaging-api", "1.0")


    //solace dependencies
    compile("com.solacesystems:sol-jcsmp:10.6.4")
    compile("com.solace.cloud.core:solace-services-info:0.4.1")
    compile("com.solace.cloud.cloudfoundry:solace-spring-cloud-connector:4.1.1")


    testCompile ("org.mockito","mockito-junit-jupiter", "2.23.0")
    testCompile("org.springframework", "spring-test", "5.1.1.RELEASE")
    testCompileOnly ("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.3.1")
    testCompile("org.apache.maven.plugins","maven-surefire-plugin", "2.22.2")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<JavaCompile> {
    options.isFork = true
    options.encoding = "UTF-8"
    options.forkOptions.executable = "javac"
    options.compilerArgs = listOf("-XDignore.symbol.file", "-Xlint:deprecation")
}