package com.ibm.appscan.gradle.tasks;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException

import com.ibm.appscan.gradle.error.AppScanException
import com.ibm.appscan.gradle.handlers.CreateScriptHandler


class AppScanCreateScriptTask extends DefaultTask {

	String script = "${project.appscansettings.scriptdir}/${project.appscansettings.scriptname}"
	String application = "${project.appscansettings.appdir}/${project.appscansettings.appname}.paf"

	String server = project.appscansettings.server
	String user = project.appscansettings.username
	String password = project.appscansettings.password
	boolean acceptssl = project.appscansettings.acceptssl	

	String scanConfig = project.appscansettings.scanconfig
	
	String reportType = project.appscansettings.reporttype
	String reportFormat = project.appscansettings.reportformat
	String reportLocation = project.appscansettings.reportlocation
	
	String publishFolder = project.appscansettings.publishfolder
	String publishApp = project.appscansettings.publishapp
	String publishName = project.appscansettings.publishname
	boolean publishASE = project.appscansettings.publishASE

	@TaskAction
	def createScript() {		
		try {
			new CreateScriptHandler(project, script, application, server, user, password, acceptssl, scanConfig, reportType, reportFormat, reportLocation, publishFolder, publishApp, publishName, publishASE).run()
		} catch(AppScanException e) {
			throw new TaskExecutionException(project.createScript, e)
		}
	}
}
