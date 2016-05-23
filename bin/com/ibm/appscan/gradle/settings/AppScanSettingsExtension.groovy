package com.ibm.appscan.gradle.settings;

import org.gradle.api.Project

public class AppScanSettingsExtension {

	//Don't allow changing the script name since we need to delete the file for each run.
	final String scriptname = "cliscript.txt"
	final String logname = "appscanlog.txt"
	
	String projectname
	String appname
	String appdir
	String scriptdir
	String installdir
	String logdir
	String username = ""
	String password = ""
	String server = "localhost"
	String sourceexcludes = "test"
	String scanconfig
	String reporttype
	String reportformat
	String reportlocation
	String publishfolder
	String publishapp
	String publishname
	boolean publishASE = false
	boolean acceptssl = true
	
	public AppScanSettingsExtension(Project project) {
		projectname = project.name
		appname = project.getRootProject().name
		appdir = project.getRootProject().getRootDir().getAbsolutePath()
		logdir = new File(project.getRootProject().getRootDir(), "appscan").getAbsolutePath()
		scriptdir = new File(project.getRootProject().getRootDir(), "appscan").getAbsolutePath()
		setInstallDir()
	}

	private String setInstallDir() {
		String osName = System.getProperty("os.name").toLowerCase()
		if(osName.contains("windows"))
			installdir = "C:/Program Files (x86)/IBM/AppScanSource"
		else if(osName.contains("mac"))
			installdir = "/Applications/AppScanSource.app"
		else
			installdir = "/opt/ibm/appscansource"
	}
}
