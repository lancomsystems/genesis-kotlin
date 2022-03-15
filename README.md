# Genesis Kotlin Gradle Plugin

Basic configuration of Kotlin based Gradle projects. 

[Changelog](CHANGELOG.md)

## Activation
```kotlin 
plugins {
    id("de.lancom.genesis.kotlin") version "<version>"
}
```

## Configuration
```kotlin 
genesisKotlin {

    // enable code documentation 
    withDokka()

    // register additional artifacts
    withJavadocJar() 
    withSourcesJar()
    
    // enable code quality checks
    withDetekt(
        detektVersion = "0.15.0" // optional
    )
    withKtlint(
        ktlintVersion = "0.40.0" // optional
    )
    
    // enable additional kotlin compiler plugins 
    withSpringSupport() // org.jetbrains.kotlin.plugin.spring
    withJpaSupport() // org.jetbrains.kotlin.plugin.jpa
}
```

#### Detekt
Detekt can be configured by creating a Detekt config file `config/detekt/detekt.yml` like
the [example file](example/config/detekt/detekt.yml) or by running the `detektGenerateConfig` task.
For additional information see the [Detekt documentation](https://arturbosch.github.io/detekt/configurations.html).

#### KtLint
KtLinkt can be configured using the `.editorconfig` file like the [example file](example/.editorconfig).
For additional information see the [KtLint Plugin](https://github.com/pinterest/ktlint#editorconfig).

### Example

An example project can be found in the `examples/project`. 

Execute `./gradlew -p example <tasks>` to run tasks of the [example project](./example).

## Plugins

- [Kotlin Plugin](https://kotlinlang.org/docs/reference/using-gradle.html)
- [Kotlin Spring Plugin](https://kotlinlang.org/docs/reference/compiler-plugins.html#spring-support)
- [Kotlin JPA Plugin](https://kotlinlang.org/docs/reference/compiler-plugins.html#jpa-support)
- [Dokka Plugin](https://github.com/Kotlin/dokka)
- [Detekt Plugin](https://detekt.github.io/detekt/)
- [Ktlint Plugin](https://github.com/JLLeitschuh/ktlint-gradle)


## Maintainers
- [Artur Taube](https://github.com/Adduh)
- [Dennis Rieks](https://github.com/drieks)
- [Jonas von Andrian](https://github.com/johnny)
- [Maya Naydenova](https://github.com/mnaydeno)
