package com.ibm.appscan.gradle.tasks;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException

import com.ibm.appscan.gradle.error.AppScanException
import com.ibm.appscan.gradle.handlers.RunScriptHandler


class AppScanRunScriptTask extends DefaultTask {

	String script = "$project.appscansettings.scriptdir/$project.appscansettings.scriptname"
	String log = "$project.appscansettings.logdir/$project.appscansettings.logname"

	@TaskAction
	def runScript() {
		try {
			new RunScriptHandler(m_project, script, log).run()
		} catch(AppScanException e) {
			throw new TaskExecutionException(project.runScript, e)
		}
	}
}
