import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	java
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.airlock.iam"
version = "0.0.1-SNAPSHOT"
description = "SCIM Service für Airlock IAM"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven {
		url = uri("https://artifacts.ergon.ch/artifactory/proxy-maven-central/")
	}
	maven {
		name = "artifactory"
		url = project.uri("https://artifacts.ergon.ch/artifactory/proj-airlock-pd-iam-intermediates/")
		credentials {
			username = findProperty("artifactoryUser") as String? ?: ""
			password = findProperty("artifactoryPassword") as String? ?: ""
		}
	}
	maven {
		url = uri("https://artifacts.ergon.ch/artifactory/proxy-jooq/")
		content {
			includeGroup("org.jooq.pro")
		}
		credentials {
			username = findProperty("artifactoryUser") as String? ?: ""
			password = findProperty("artifactoryPassword") as String? ?: ""
		}
	}
	maven {
		url = uri("https://artifacts.ergon.ch/artifactory/ergon-public-releases/")
		credentials {
			username = findProperty("artifactoryUser") as String? ?: ""
			password = findProperty("artifactoryPassword") as String? ?: ""
		}
	}
}


tasks.named<BootJar>("bootJar") {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


dependencies {
	val scimSdkVersion = "1.28.0"
	val mapStructVersion = "1.5.5.Final"
//	val iamVersion = "8.6.0-956.8ce52ae"
	val iamVersion = "8.6.0-36.2a39067"

	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("de.captaingoldfish:scim-sdk-server:${scimSdkVersion}")
	implementation("de.captaingoldfish:scim-sdk-client:${scimSdkVersion}")
	implementation("com.airlock.iam:core:${iamVersion}") { // FIXME only need ConfigActivator without all the other dependencies
		exclude("com.aspose")
		exclude("com.onespan")
		exclude("cronto")
		exclude(group = "com.airlock.iam", module = "platform")
	}
	implementation("com.airlock.iam:plugin-framework:${iamVersion}") { // FIXME: we only need the plugin framework
		exclude(group = "com.airlock.iam", module = "platform")
		exclude(group = "com.airlock.iam", module = "base")
	}

	implementation("com.airlock.iam:login-app:${iamVersion}") { // FIXME: do not use the loginapp
		exclude("com.aspose")
		exclude("com.onespan")
		exclude("cronto")
		exclude("vasco")
	}


	implementation("org.mapstruct:mapstruct:${mapStructVersion}")
	annotationProcessor("org.mapstruct:mapstruct-processor:${mapStructVersion}")

	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.h2database:h2")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.tngtech.archunit:archunit-junit5:1.1.1")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
