package com.ibm.appscan.gradle.handlers;

import org.gradle.api.Project

import com.ibm.appscan.gradle.error.AppScanException

public class CreateScriptHandler extends AppScanHandler {

	private String m_script;
	private String m_application;

	private String m_server;
	private String m_user;
	private String m_password;
	private String m_acceptssl;

	private String m_scanConfig;

	private String m_reportType;
	private String m_reportFormat;
	private String m_reportLocation;

	private String m_publishFolder;
	private String m_publishApp;
	private String m_publishName;
	private boolean m_publishASE;

	public CreateScriptHandler(Project project, String script, String application, String server, String user, String password, boolean acceptssl, String scanConfig, String reportType,
		String reportFormat, String reportLocation, String publishFolder, String publishApp, String publishName, boolean publishASE) {
		super(project)
		m_script = script
		m_application = application
		m_server = server
		m_user = user
		m_password = password
		m_scanConfig = scanConfig
		m_reportType = reportType
		m_reportFormat = reportFormat
		m_reportLocation = reportLocation
		m_publishFolder = publishFolder
		m_publishApp = publishApp
		m_publishName = publishName
		m_publishASE = publishASE
		if(acceptssl)
			m_acceptssl = "-acceptssl"
		else
			m_acceptssl = ""
	}

	public void runTask() {
		File cliscript = new File(m_script)
		if(cliscript.isFile())
			cliscript.delete()

		def newline = System.getProperty("line.separator")
		cliscript << (createLoginCommand() + newline)
		cliscript << (createOpenAppCommand() + newline)
		cliscript << (createScanCommand() + newline)
		cliscript << (createReportCommand() + newline)
		cliscript << (createPublishCommand() + newline)
		cliscript << ("exit")
	}

	private String createLoginCommand() {
    	return "login ${m_server} ${m_user} ${m_password} ${m_acceptssl}"
	}

	private String createOpenAppCommand() {
    	StringBuilder command = new StringBuilder("")
    	File appFile = new File(m_application);

    	if(appFile.isFile()) {
        		command.append("oa ")
        		command.append(appFile.getAbsolutePath())
    	}
    	return command.toString()
	}

	private String createScanCommand() {
		StringBuilder command = new StringBuilder("scan")
		if(m_scanConfig != null)
			command.append("-scanconfig ${m_scanConfig}")
		return command.toString()
	}

	private String createReportCommand() {
		if(m_reportType != null && m_reportFormat != null) {
			StringBuilder command = new StringBuilder("")
			command.append("report")
			command.append(" \"${m_reportType}\"")
			command.append(" ${m_reportFormat}")
			command.append(" ${m_reportLocation}")
			return command.toString()
		}
		return ""
	}	

	private String createPublishCommand() {
		StringBuilder command = new StringBuilder("")
		if(m_publishASE) {
			command.append("pase")
			if(m_publishFolder != null)
	            		command.append(" -folder ${m_publishFolder}")
			if(m_publishApp != null)
				command.append(" -application ${m_publishApp}")
			if(m_publishName != null)
				command.append(" -name ${m_publishName}")
		}
		return command.toString()
	}

	protected void verifySettings() throws AppScanException {
		if(m_script == null || !(new File(m_script).getParentFile().isDirectory()))
			throw new AppScanException("Unable to create CLI script ${script}")
	}
}

