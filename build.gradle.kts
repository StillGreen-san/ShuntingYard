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
    implementation("ch.obermuhlner:big-math:2.3.2") //TODO figure out this mess
    implementation("ch.obermuhlner:kotlin-big-math:2.3.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}