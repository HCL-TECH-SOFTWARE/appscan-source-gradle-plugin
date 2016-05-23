package com.ibm.appscan.gradle;

import org.gradle.api.Plugin
import org.gradle.api.Project

import com.ibm.appscan.gradle.settings.AppScanSettingsExtension

class AppScanPlugin implements Plugin<Project> {

    void apply(Project project) {
			
		project.subprojects*.apply (plugin: 'com.ibm.appscan')
		
		project.extensions.create("appscansettings", AppScanSettingsExtension, project)

        project.task('createProject',
				description: "Generates an AppScan Source project for this Gradle project.",
				type: com.ibm.appscan.gradle.tasks.AppScanCreateProjectTask) << {
			if(project.plugins.hasPlugin("org.gradle.java"))
				classfiles = compileJava.outputs.files
		}
		
		project.task('createCLIScript',
				description: "Generates an AppScan Source CLI script for executing a scan.",
				type: com.ibm.appscan.gradle.tasks.AppScanCreateScriptTask
		) << {}
		
		project.task('runScan',
				description: "Executes a security scan of this project and all subprojects.",
				type: com.ibm.appscan.gradle.tasks.AppScanRunScriptTask,
				dependsOn: [project.subprojects.createProject,project.createCLIScript]
		) << {}
    }
}
