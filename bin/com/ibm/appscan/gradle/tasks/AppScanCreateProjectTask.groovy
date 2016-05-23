package com.ibm.appscan.gradle.tasks;

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException

import com.ibm.appscan.gradle.error.AppScanException
import com.ibm.appscan.gradle.handlers.CreateProjectHandler



class AppScanCreateProjectTask extends DefaultTask {
	
	String appdir = project.appscansettings.appdir
	String appname = project.appscansettings.appname
	String projectname = project.appscansettings.projectname
	String exclusions = project.appscansettings.sourceexcludes;	

	@InputFiles
	FileCollection classfiles
	
	@OutputFile
	File projectFile = new File(project.projectDir, "${projectname}.ppf")
	
	@TaskAction
	def createProject() {
		try {
			new CreateProjectHandler(project, appdir, appname, projectname, exclusions).run()
		} catch(AppScanException e) {
			throw new TaskExecutionException(project.createProject, e)
		}
	}
}
