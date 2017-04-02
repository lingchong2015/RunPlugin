package com.curry.stephen;

import org.gradle.api.Plugin
import org.gradle.api.Project

class Run implements Plugin<Project> {
    @Override
    void apply(Project target) {
        target.android.applicationVariants.all { variant ->
            if (variant.install) {
                target.tasks.create(name: "run${variant.name.capitalize()}", dependsOn: variant.install) {
                    description "Installs the ${variant.descri  ption} and runs the main launcher activity."
                    println "ApplicationId of ${variant.name}: ${variant.applicationId}"
                    doFirst {
                        println 'Running the Activity.'

                        def xmlFile = target.file('\\src\\main\\AndroidManifest.xml')
                        def androidManifest = new XmlSlurper().parse(xmlFile)
                        def packageName = androidManifest['@package']
//                    def classpath = android.defaultConfig.applicationId
//                    if (variant.buildType.applicationIdSuffix) {
//                        classpath -= "${variant.buildType.applicationIdSuffix}"
//                    }
                        def launchClass = "${variant.applicationId}/${packageName}.MainActivity"
                        project.exec {
                            executable = 'adb'
                            args = ['shell', 'am', 'start', '-n', launchClass]
                        }
                    }
                }
            }
        }
    }
}