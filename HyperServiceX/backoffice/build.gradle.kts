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
    war
    maven
    application
    distribution
    id("org.springframework.boot") version "2.2.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
}

group = "com.hsx"
version = "1.0-SNAPSHOT"

/*
tasks.getByName<BootJar>("bootJar") {
    mainClassName = "com.bcs.ngs.rt.bo.BackOfficeApplication"
}*/

tasks.getByName<Jar>("jar") {
    enabled = true
}


val jvmArgs = mutableListOf("-Djava.net.preferIPv4Stack=true -Xms512m -Xmx1024m", "-Dspring.profiles.active=$profile");

if (profile == "dev") {
    jvmArgs.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8004")
}


application {
    mainClassName = "com.hsx.bo.BackOfficeApplication"
    applicationDefaultJvmArgs = jvmArgs
}
//Remote Debug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8001

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


distributions {
    main {
        contents {
            from(createLogs) {
                into("logs")
            }
            from("${projectDir}/src/main/resources") {
                into("config")
            }
            from("${projectDir}/src/main/webapp") {
                into("config")
            }
        }
    }
}

tasks.startScripts {
    (unixStartScriptGenerator as TemplateBasedScriptGenerator).template = resources.text.fromFile("$projectDir/BackOfficeUnixScriptTemplate.txt")
    (windowsStartScriptGenerator as TemplateBasedScriptGenerator).template = resources.text.fromFile("$projectDir/BackOfficeWindowsScriptTemplate.txt")
}

repositories {
    mavenCentral()
    maven(
            url = "http://www.gridgainsystems.com/nexus/content/repositories/external/"
    )
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs/compile", "include" to listOf("*.jar"))))
    compile(project(":common-utils"))
    compile(project(":common-models"))

    implementation("org.springframework.boot", "spring-boot-starter-data-jpa");
    implementation("org.springframework.boot", "spring-boot-starter-security");
    implementation("org.springframework.boot", "spring-boot-starter-web");
    compile("org.springframework.boot", "spring-boot-starter-aop")
    compile("org.springframework.boot", "spring-boot-starter-data-mongodb")
    compile("org.springframework.integration", "spring-integration-core", "5.1.0.RELEASE")
    compile("org.springframework.integration", "spring-integration-stream", "5.1.0.RELEASE")
    compile("org.springframework.hateoas", "spring-hateoas", "0.18.0.RELEASE")
    compile("io.springfox", "springfox-swagger2", "2.9.2")
    compile("io.springfox", "springfox-swagger-ui", "2.9.2")

    compileOnly("org.projectlombok:lombok:1.18.8")
    testCompileOnly("org.projectlombok:lombok:1.18.8")
    annotationProcessor("org.projectlombok:lombok:1.18.8")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.8")

    compile("org.slf4j", "slf4j-api", "1.7.25")

    // Validation
    compile("javax.validation", "validation-api", "2.0.1.Final")

    testCompile("org.springframework", "spring-test", "5.1.1.RELEASE")
    testCompileOnly("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")

//    //solace dependencies
    compile("com.solacesystems:sol-jcsmp:10.6.4")
    compile("com.solace.cloud.core:solace-services-info:0.4.1")
    compile("com.solace.cloud.cloudfoundry:solace-spring-cloud-connector:4.1.1")
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