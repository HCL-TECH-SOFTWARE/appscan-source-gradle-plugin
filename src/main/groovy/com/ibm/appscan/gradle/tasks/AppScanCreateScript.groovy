package com.ibm.appscan.gradle.tasks;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException

import com.ibm.appscan.gradle.error.AppScanException
import com.ibm.appscan.gradle.handlers.CreateScriptHandler


class AppScanCreateScript extends DefaultTask {

	@Internal
	String scriptDir

	//login properties
	@Internal
	String server
	@Internal
	String user
	@Internal
	String password
	boolean acceptssl
	@Internal
	boolean getAcceptssl() {
		return acceptssl
	}

	//scan properties
	@Internal
	String appDir
	@Internal
	String appName
	@Internal
	String scanConfig

	//report properties
	@Internal
	String reportType
	@Internal
	String reportFormat
	@Internal
	String reportLocation
	
	//publish properties
	@Internal
	String publishFolder
	@Internal
	String publishApp
	@Internal
	String publishName
	boolean publishASE
	@Internal
	boolean getPublishASE() {
		return publishASE
	}
	
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
	}
}
