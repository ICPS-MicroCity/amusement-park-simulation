plugins {
    id("com.gradle.enterprise") version "3.10.1"
}

rootProject.name = "amusement-park-alchemist-simulation"

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishOnFailure()
    }
}

enableFeaturePreview("VERSION_CATALOGS")
