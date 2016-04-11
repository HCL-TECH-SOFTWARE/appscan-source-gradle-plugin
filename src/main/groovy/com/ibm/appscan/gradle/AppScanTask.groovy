package com.ibm.appscan.gradle;

import org.gradle.api.Project;

public abstract class AppScanTask {

	protected Project m_project;
	
	public AppScanTask(Project project) {
		m_project = project;
	}
	
	public abstract void run() throws AppScanException;
	
	protected boolean checkInstall() {
		boolean installExists = new File(m_project.appscansettings.installdir).isDirectory();
		if(!installExists)
			print "Could not find the AppScan Source installation directory at $m_project.appscansettings.installdir \nSet the appscansettings.installdir property to point to your installation directory."
		return installExists;
	}
}