package com.ibm.appscan.gradle;

import org.gradle.api.Project;

public class AppScanSettingsExtension {

	//Don't allow changing the script name since we need to delete the file for each run.
	final String scriptname = "cliscript.txt"
	final String logname = "appscanlog.txt"
	
	String projectname
	String appname
	String appdir
	String scriptdir
	String installdir = "C:/Program Files (x86)/IBM/AppScanSource"
	String logdir
	String username = ""
	String password = ""
	String server = "localhost"
	String scanoptions = ""

	public AppScanSettingsExtension(Project project) {
		projectname = project.name
		appname = project.getRootProject().name
		appdir = project.getRootProject().getRootDir().getAbsolutePath()
		logdir = new File(project.getRootProject().getRootDir(), "appscan").getAbsolutePath()
		scriptdir = new File(project.getRootProject().getRootDir(), "appscan").getAbsolutePath()
	}	
}