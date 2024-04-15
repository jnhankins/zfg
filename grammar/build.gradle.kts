plugins {
    java
    antlr
}

group = "zfg"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // ANTLR for parsing
    antlr("org.antlr:antlr4:4.13.1")
    implementation("org.antlr:antlr4-runtime:4.13.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}