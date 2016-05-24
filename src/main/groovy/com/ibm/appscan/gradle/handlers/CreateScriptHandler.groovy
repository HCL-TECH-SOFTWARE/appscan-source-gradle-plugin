package com.ibm.appscan.gradle.handlers;

import org.gradle.api.Project

import com.ibm.appscan.gradle.error.AppScanException

public class CreateScriptHandler extends AppScanHandler {

	private String m_script
	private String m_application
	private String m_acceptssl
	private boolean m_publishASE

	public CreateScriptHandler(Project project) {
		super(project)
		m_script = "${project.appscansettings.scriptdir}/${project.appscansettings.scriptname}"
		m_application = "${project.appscansettings.appdir}/${project.appscansettings.appname}.paf"
		if(project.appscansettings.acceptssl)
			m_acceptssl = "-acceptssl"
		else
			m_acceptssl = ""
	}

	@Override
	protected void runTask() {
		File cliscript = new File(m_script)
		if(cliscript.isFile())
			cliscript.delete()
		if(!cliscript.getParentFile().isDirectory())
			cliscript.getParentFile().mkdirs()

		String newline = System.getProperty("line.separator")
		cliscript << (createLoginCommand() + newline)
		cliscript << (createOpenAppCommand() + newline)
		cliscript << (createScanCommand() + newline)
		cliscript << (createReportCommand() + newline)
		cliscript << (createPublishCommand() + newline)
		cliscript << ("exit")
	}

	@Override
	protected void verifySettings() throws AppScanException {
		if(m_script == null)
			throw new AppScanException("Unable to create CLI script ${m_script}")
		
		File scriptDir = new File(m_script).getParentFile()
		if(scriptDir != null)
			scriptDir.mkdirs()
			
		if(scriptDir == null || !scriptDir.isDirectory())
			throw new AppScanException("Unable to create CLI script ${m_script}")
	}
	
	private String createLoginCommand() {
		if(m_project.appscansettings.username != "" && m_project.appscansettings.password != "")
			return "login ${m_project.appscansettings.server} ${m_project.appscansettings.username} ${m_project.appscansettings.password} ${m_acceptssl}"
		else
			return "login_file $m_project.appscansettings.server $m_project.appscansettings.tokenfile ${m_acceptssl}"
	}

	private String createOpenAppCommand() {
    	StringBuilder command = new StringBuilder("")
    	File appFile = new File(m_application)

    	if(appFile.isFile()) {
    		command.append("oa ")
    		command.append(appFile.getAbsolutePath())
    	}
    	return command.toString()
	}

	private String createScanCommand() {
		StringBuilder command = new StringBuilder("scan")
		if(m_project.appscansettings.scanconfig != null)
			command.append("-scanconfig ${m_project.appscansettings.scanconfig}")
		return command.toString()
	}

	private String createReportCommand() {
		if(m_project.appscansettings.reporttype != null && m_project.appscansettings.reportformat != null) {
			StringBuilder command = new StringBuilder("")
			command.append("report")
			command.append(" \"${m_project.appscansettings.reporttype}\"")
			command.append(" ${m_project.appscansettings.reportformat}")
			command.append(" ${m_project.appscansettings.reportlocation}")
			return command.toString()
		}
		return ""
	}	

	private String createPublishCommand() {
		StringBuilder command = new StringBuilder("")
		if(m_publishASE) {
			command.append("pase")
			if(m_publishFolder != null)
	            command.append(" -folder ${m_project.appscansettings.publishfolder}")
			if(m_publishApp != null)
				command.append(" -application ${m_project.appscansettings.publishapp}")
			if(m_publishName != null)
				command.append(" -name ${m_project.appscansettings.publishname}")
		}
		return command.toString()
	}
}

