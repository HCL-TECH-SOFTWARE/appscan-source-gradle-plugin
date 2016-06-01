package com.ibm.appscan.gradle.tasks;

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException

import com.ibm.appscan.gradle.error.AppScanException
import com.ibm.appscan.gradle.handlers.CreateProjectHandler



class AppScanCreateProject extends DefaultTask {
	
	String applicationDir
	String applicationName
	String projectName
	String exclusions

	@InputFiles
	def classfiles
	
	@TaskAction
	def createAppScanProject() {
		if(project.plugins.hasPlugin("org.gradle.java")) {
			configureSettings()
			try {
				new CreateProjectHandler(project).run()
			} catch(AppScanException e) {
				throw new TaskExecutionException(project.createProject, e)
			}
		}
	}
	
	void configureSettings() {
		project.appscansettings.appdir = applicationDir ?: project.appscansettings.appdir
		project.appscansettings.appname = applicationName ?: project.appscansettings.appname
		project.appscansettings.projectname = projectName ?: project.name
		project.appscansettings.sourceexcludes = exclusions ?: project.appscansettings.sourceexcludes
	}
}
