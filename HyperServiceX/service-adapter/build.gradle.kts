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
    idea
    java
    application
    id("org.springframework.boot") version "2.2.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
}

group = "com.hsx"
version = "1.0-SNAPSHOT"

repositories {
    maven(
            url = "http://www.gridgainsystems.com/nexus/content/repositories/external/"
    )
    mavenLocal()
    mavenCentral()
}

/*tasks.getByName<BootJar>("bootJar") {
    mainClassName = "com.bcs.xborder.mqadaptor.MQAdapterApplication"
}*/

tasks.getByName<Jar>("jar") {
    enabled = true
}

val jvmArgs = mutableListOf("-Djava.net.preferIPv4Stack=true -Xms512m -Xmx1024m", "-Dspring.profiles.active=$profile");

if(profile == "dev"){
    jvmArgs.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8001");
}

application {
    mainClassName = "com.hsx.sa.ServiceAdapterApplication"
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

tasks.startScripts {
    (unixStartScriptGenerator as TemplateBasedScriptGenerator).template = resources.text.fromFile("$projectDir/MQAdapterUnixScriptTemplate.txt")
    (windowsStartScriptGenerator as TemplateBasedScriptGenerator).template = resources.text.fromFile("$projectDir/MQAdapterWindowsScriptTemplate.txt")
}

dependencies {
    compile(project(":common-utils"))

    //Spring dependencies
    implementation("org.springframework.boot:spring-boot-starter-web")
    compile("org.springframework.boot:spring-boot-starter-security")
    compile("org.springframework.boot", "spring-boot-starter-aop", "2.2.2.RELEASE")
    compile("org.springframework.integration", "spring-integration-core", "5.1.0.RELEASE")
    compile("org.springframework.integration", "spring-integration-stream", "5.1.0.RELEASE")
    compile("net.logstash.logback:logstash-logback-encoder:4.11")
    compile("ch.qos.logback.contrib:logback-json-classic:0.1.5")
    compile("ch.qos.logback.contrib:logback-jackson:0.1.5")

    compile("net.sf.jopt-simple", "jopt-simple", "4.6")
    compile("com.google.guava", "guava", "20.0")


    //Micrometer and metrics dependencies    
    compile("org.springframework.boot", "spring-boot-starter-actuator", "2.2.2.RELEASE")
    compile("io.micrometer", "micrometer-registry-prometheus", "1.1.0")
    compile("com.fasterxml.jackson.core", "jackson-databind", "2.9.4")

    compile("javax.servlet", "javax.servlet-api", "4.0.1")

    //JSON dependencies
    compile("com.fasterxml.jackson.core", "jackson-core", "2.9.7")
    compile("org.apache.camel", "camel-disruptor", "2.22.2")

    //Decrypt the properties
    compile("org.jasypt", "jasypt-spring31", "1.9.2")

    //MQ Libs
    implementation(fileTree(mapOf("dir" to "libs/compile", "include" to listOf("*.jar"))))

    //solace dependencies
    compile("com.solacesystems:sol-jcsmp:10.6.4")
    compile("com.solace.cloud.core:solace-services-info:0.4.1")
    compile("com.solace.cloud.cloudfoundry:solace-spring-cloud-connector:4.1.1")

    compile("org.apache.kafka", "kafka-clients", "2.0.0")
    //Logging
    compile("org.slf4j", "slf4j-api", "1.7.25")
    compile("org.slf4j", "jcl-over-slf4j", "1.7.25")
    compile("ch.qos.logback", "logback-classic", "1.2.3")
    compile("ch.qos.logback", "logback-core", "1.2.3")
    compile("com.github.danielwegener", "logback-kafka-appender", "0.2.0-RC2")

    //compile("com.bcs.xborder", "gateway-xsd-bindings", "0.0.1")
    compile("org.apache.santuario", "xmlsec", "2.0.7")
    testCompile("junit", "junit", "4.12")

    implementation("javax.ws.rs", "javax.ws.rs-api", "2.1")

    compile("io.springfox", "springfox-swagger2", "2.9.2")
    compile("io.springfox", "springfox-swagger-ui", "2.9.2")
    compile("org.springframework.hateoas", "spring-hateoas", "0.18.0.RELEASE")

    //TODO : Added because of error caused in FileUtil. verify
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("org.apache.commons:commons-text:1.8")

    compileOnly("org.projectlombok:lombok:1.18.8")
    testCompileOnly("org.projectlombok:lombok:1.18.8")
    annotationProcessor("org.projectlombok:lombok:1.18.8")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.8")

    implementation("net.lingala.zip4j:zip4j:2.5.1")
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