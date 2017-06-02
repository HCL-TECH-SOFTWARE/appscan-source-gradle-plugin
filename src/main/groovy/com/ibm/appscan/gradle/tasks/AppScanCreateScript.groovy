package com.ibm.appscan.gradle.tasks;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException

import com.ibm.appscan.gradle.error.AppScanException
import com.ibm.appscan.gradle.handlers.CreateScriptHandler


class AppScanCreateScript extends DefaultTask {

	String scriptDir

	//login properties
	String server
	String user
	String password
	boolean acceptssl

	//scan properties
	String appDir
	String appName
	String scanConfig
	boolean compileCode
	
	//report properties
	String reportType
	String reportFormat
	String reportLocation
	
	//publish properties
	String publishFolder
	String publishApp
	String publishName
	boolean publishASE
	
	@TaskAction
	def createScript() {
		configureSettings()
		try {
			new CreateScriptHandler(project).run()
		} catch(AppScanException e) {
			throw new TaskExecutionException(project.createCLIScript, e)
		}
	}
	
	void configureSettings() {
		project.appscansettings.scriptdir = scriptDir ?: project.appscansettings.scriptdir
		project.appscansettings.appname = appName ?: project.appscansettings.appname
		project.appscansettings.appdir = appDir ?: project.appscansettings.appdir
		project.appscansettings.server = server ?: project.appscansettings.server	
		project.appscansettings.username = user ?: project.appscansettings.username
		project.appscansettings.password = password ?: project.appscansettings.password
		project.appscansettings.acceptssl = acceptssl ?: project.appscansettings.acceptssl	
		project.appscansettings.scanconfig = scanConfig ?: project.appscansettings.scanconfig
		project.appscansettings.reporttype = reportType ?: project.appscansettings.reporttype
		project.appscansettings.reportformat = reportFormat ?: project.appscansettings.reportformat
		project.appscansettings.reportlocation = reportLocation ?: project.appscansettings.reportlocation
		project.appscansettings.publishfolder = publishFolder ?: project.appscansettings.publishfolder
		project.appscansettings.publishapp = publishApp ?: project.appscansettings.publishapp
		project.appscansettings.publishname = publishName ?: project.appscansettings.publishname
		project.appscansettings.publishASE = publishASE ?: project.appscansettings.publishASE
		project.appscansettings.compilecode = compileCode ?: project.appscansettings.compilecode
	}
}
