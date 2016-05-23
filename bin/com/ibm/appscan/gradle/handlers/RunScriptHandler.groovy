package com.ibm.appscan.gradle.handlers;

import org.gradle.api.Project

import com.ibm.appscan.gradle.error.AppScanException

public class RunScriptHandler extends AppScanHandler {

	private String m_script;
	private String m_log;

	public RunScriptHandler(Project project, String script, String log) {
		super(project);
		m_script = script;
		m_log = log;
	}
	
	public void runTask() throws AppScanException {
		def ant = new AntBuilder()
	
		ant.taskdef(resource: "com/ouncelabs/ounceant/task/ounce.xml") {
			classpath {
				fileset(dir: m_project.appscansettings.installdir + "/lib") {
					include(name: "*.jar")
				}
			}
		}
		
		ant.ounceCli(
			dir: m_project.appscansettings.installdir,
			script: m_script,
			output: m_log)
	}
	
	protected void verifySettings() throws AppScanException {
		if(!new File(m_script).isFile()) 
			throw new AppScanException("The script ${m_script} does not exist!")
			
		if(!new File(m_log).getParentFile().isDirectory())
			throw new AppScanException("The directory for the log file ${m_log} does not exist!")
	}
}

