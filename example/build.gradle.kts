plugins {
    id("de.lancom.genesis.kotlin") version "1.1.3"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

genesisKotlin {
    withJavadocJar()
    withSourcesJar()
    withDetekt()
    withKtlint()
}
