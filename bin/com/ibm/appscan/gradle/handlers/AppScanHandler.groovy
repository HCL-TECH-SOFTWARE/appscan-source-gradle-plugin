package com.ibm.appscan.gradle.handlers;

import org.gradle.api.Project

import com.ibm.appscan.gradle.error.AppScanException

public abstract class AppScanHandler {

	protected Project m_project;

	public AppScanHandler(Project project) {
		m_project = project;
	}

	public void run() throws AppScanException {
		checkInstall()
		verifySettings()
		runTask()
	}
	
	protected abstract void runTask() throws AppScanException
	
	protected abstract void verifySettings() throws AppScanException;
	
	private void checkInstall() {
		if(!new File(m_project.appscansettings.installdir).isDirectory())
			throw new AppScanException("Could not find the AppScan Source installation directory at ${m_project.appscansettings.installdir} \nSet the appscansettings.installdir property to point to your installation directory.")
	}
}
