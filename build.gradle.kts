plugins {
  java
  application
  antlr
}

repositories {
  mavenCentral()
}

dependencies {
  // ANTLR for parsing
  antlr("org.antlr:antlr4:4.13.1")
  implementation("org.antlr:antlr4-runtime:4.13.1")

  // ASM for generating bytecode
  implementation("org.ow2.asm:asm:9.7")

  // Junit 5 (Jupiter) for testing
  testImplementation(platform("org.junit:junit-bom:5.10.2"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

application {
  mainClass = "zfg.Main"
}

tasks.wrapper {
  gradleVersion = "8.7"
  distributionType = Wrapper.DistributionType.BIN
}

tasks.generateGrammarSource {
  maxHeapSize = "64m"
  arguments = arguments + listOf("-visitor", "-long-messages", "-package", "zfg.antlr")
  outputDirectory = outputDirectory.resolve("zfg/antlr");
}

tasks.register<Delete>("cleanAntlrDebug") {
  delete("src/main/antlr/.antlr")
}

tasks.named("clean") {
  dependsOn("cleanAntlrDebug")
}



