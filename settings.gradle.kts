plugins {
    id("com.gradle.enterprise") version "3.11"
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
