package com.ibm.appscan.gradle;

import org.gradle.api.Project;

public class ScriptCreator extends AppScanTask {

	public ScriptCreator(Project project) {
		super(project);
	}
	
	public void run() throws AppScanException {
		def newline = System.getProperty("line.separator")
		
		File appscanDir = new File(m_project.appscansettings.scriptdir)
		File cliscript = new File(appscanDir, m_project.appscansettings.scriptname)
		File applicationfile = new File(m_project.appscansettings.appdir, "${m_project.appscansettings.appname}.paf")
		
		if(!appscanDir.isDirectory())
			appscanDir.mkdirs()
		if(cliscript.isFile())
			cliscript.delete()
				
		//Create the new script
		if(m_project.appscansettings.username != "" && m_project.appscansettings.password != "")
			cliscript << ("login $m_project.appscansettings.server $m_project.appscansettings.username $m_project.appscansettings.password -acceptssl" + newline)
		else
			cliscript << ("login_file $m_project.appscansettings.server $m_project.appscansettings.tokenfile -acceptssl" + newline)
		cliscript << ("oa " + applicationfile.getAbsolutePath() + newline)
		cliscript << ("scan $m_project.appscansettings.scanoptions" + newline)
		if(m_project.appscansettings.reportoptions != "")
			cliscript << ("report $m_project.appscansettings.reportoptions" + newline)
		cliscript << ("exit")
	}
}
