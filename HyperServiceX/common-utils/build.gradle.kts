plugins {
    idea
    java
    eclipse
}


group = "com.hsx"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    // mavenCentral()
    /// name:*.jar,dirs: libs
    /// flatdir { dir 'libs'}

    maven(
            url = "http://www.gridgainsystems.com/nexus/content/repositories/external/"
    )
    mavenCentral()
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs/compile", "include" to listOf("*.jar"))))
    //Spring dependencies

    compile("org.springframework", "spring-core", "5.1.1.RELEASE")
    compile("org.springframework", "spring-context", "5.1.1.RELEASE")
    compile("org.springframework", "spring-web", "5.1.1.RELEASE")
    compileOnly("org.projectlombok:lombok:1.18.8")
    testCompileOnly("org.projectlombok:lombok:1.18.8")
    annotationProcessor("org.projectlombok:lombok:1.18.8")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.8")
    compile("com.fasterxml.jackson.datatype", "jackson-datatype-jsr310", "2.9.7")
    //JSON dependencies
    compile("com.fasterxml.jackson.core", "jackson-core", "2.9.7")
    compile("com.fasterxml.jackson.core", "jackson-databind", "2.9.7")
    compile("org.apache.camel", "camel-disruptor", "2.22.2")
    compile("org.snmp4j", "snmp4j", "1.10.1") {
        exclude("log4j", "log4j")
    }
    //Decrypt the properties
    compile("org.jasypt", "jasypt-spring31", "1.9.2")

    compile(project(":common-models"))

    compile("com.google.guava", "guava", "27.1-jre")
    //Logging
    compile("org.slf4j", "slf4j-api", "1.7.15")
    compile("javax.xml.bind", "jaxb-api", "2.3.0")
    compile("org.slf4j", "jcl-over-slf4j", "1.7.15")
    compile("ch.qos.logback", "logback-classic", "1.2.3")
    compile("ch.qos.logback", "logback-core", "1.2.3")
    compile("ch.qos.logback.contrib:logback-json-classic:0.1.5")
    compile("ch.qos.logback.contrib:logback-jackson:0.1.5")

    testCompile("junit", "junit", "4.12")
    implementation("org.springframework.boot:spring-boot:2.2.4.RELEASE")
    //compile(project(":solace-api"))
    //compile("com.bcs.messaging", "messaging-api", "1.0")
    implementation("org.apache.commons:commons-text:1.8")
    compile("org.apache.commons", "commons-lang3", "3.1")

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