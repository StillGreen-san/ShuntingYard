plugins {
    kotlin("jvm") version "1.9.23"
}

group = "moe.sgs.kt.shuntingyard"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    implementation("ch.obermuhlner:big-math:2.3.2")
    implementation("ch.obermuhlner:kotlin-big-math:2.3.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}