val profile = project.findProperty("profile") ?: "dev"

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.2.2.RELEASE")
    }
}

plugins {
    java
    eclipse
    application
    id("org.springframework.boot") version "2.2.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
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


/*tasks.getByName<BootJar>("bootJar") {
    mainClassName = "com.bcs.xborder.processor.ProcessorApplication"
}*/

val jvmArgs = mutableListOf("-Djava.net.preferIPv4Stack=true -Xms512m -Xmx1024m", "-Dspring.profiles.active=$profile");

if (profile == "dev") {
    jvmArgs.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8002");
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

application {
    mainClassName = "com.hsx.processor.ProcessorApplication"
    applicationDefaultJvmArgs = jvmArgs
}

val createLogs by tasks.registering {
    val logs = file("$buildDir/logs")
    outputs.dir(logs)
    doLast {
        logs.mkdirs()
        File(logs, "wrapper.log").writeText("")
    }
}


sourceSets {
    main {
        resources {
            setSrcDirs(listOf("src/main/resources"))
            exclude("*/**")
        }
    }
}

tasks.startScripts {
    (unixStartScriptGenerator as TemplateBasedScriptGenerator).template = resources.text.fromFile("$projectDir/ProcessorUnixScriptTemplate.txt")
    (windowsStartScriptGenerator as TemplateBasedScriptGenerator).template = resources.text.fromFile("$projectDir/ProcessorWindowsScriptTemplate.txt")
}


distributions {
    main {
        contents {
            from(createLogs) {
                into("logs")
            }
            from("${projectDir}/src/main/resources") {
                into("config")
            }
        }
    }
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-web")
    //Spring dependencies
    compile("org.springframework.integration", "spring-integration-core", "5.1.0.RELEASE")
    compile("org.springframework.integration", "spring-integration-stream", "5.1.0.RELEASE")
    compile("org.springframework.boot", "spring-boot-starter-aop", "2.2.2.RELEASE")


    implementation(fileTree(mapOf("dir" to "libs/compile", "include" to listOf("*.jar"))))
    compile("javax.servlet", "javax.servlet-api", "3.1.0")

    //Logging
    compile(project(":common-utils"))
    compile(project(":common-processor-utils"))


    //Micrometer and metrics dependencies
    compile("org.springframework.boot", "spring-boot-starter-actuator", "2.2.2.RELEASE")
    compile("io.micrometer", "micrometer-registry-prometheus", "1.1.0")
    compile("com.fasterxml.jackson.core", "jackson-databind", "2.9.4")


    compileOnly("org.projectlombok:lombok:1.18.8")
    testCompileOnly("org.projectlombok:lombok:1.18.8")
    annotationProcessor("org.projectlombok:lombok:1.18.8")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.8")

    //solace dependencies
    compile("com.solacesystems:sol-jcsmp:10.6.4")
    compile("com.solace.cloud.core:solace-services-info:0.4.1")
    compile("com.solace.cloud.cloudfoundry:solace-spring-cloud-connector:4.1.1")

    // Annotation Processor
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    compile("org.springframework", "spring-jdbc", "3.0.3.RELEASE")
    testCompile("org.springframework.boot:spring-boot-starter-test")
    testCompile("org.junit.jupiter:junit-jupiter-engine:5.2.0")
    testCompile("org.mockito:mockito-junit-jupiter:2.23.0")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        // Make sure output from
        // standard out or error is shown
        // in Gradle output.
        testLogging.events = setOf(
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT)
        showStandardStreams = true
    }
}
configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
