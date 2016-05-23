package com.ibm.appscan.gradle.tasks;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException

import com.ibm.appscan.gradle.error.AppScanException
import com.ibm.appscan.gradle.handlers.RunScriptHandler


class AppScanRunScript extends DefaultTask {

	String scriptDir
	String logDir

	@TaskAction
	def runScript() {
		configureSettings()
		try {
			new RunScriptHandler(project).run()
		} catch(AppScanException e) {
			throw new TaskExecutionException(project.runScript, e)
		}
	}
	
	void configureSettings() {
		project.appscansettings.scriptdir = scriptDir ?: project.appscansettings.scriptdir
		project.appscansettings.logdir = logDir ?: project.appscansettings.logdir
	}
}
