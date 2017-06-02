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
	String configdir
	String logdir
	String tokenfile
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
	boolean compilecode = false
	
	public AppScanSettingsExtension(Project project) {
		projectname = project.name
		appname = project.rootProject.name
		appdir = project.rootDir.getAbsolutePath()
		logdir = new File(project.rootDir, "appscan").getAbsolutePath()
		scriptdir = new File(project.rootDir, "appscan").getAbsolutePath()
		setAppScanDirs()
		def usertoken = new File(System.getProperty("user.home"), ".ounce/ouncecli.token")
		tokenfile = usertoken.exists() ? usertoken.getAbsolutePath() : "${configdir}/config/ounceautod.token"
	}

	private void setAppScanDirs() {
		String osName = System.getProperty("os.name").toLowerCase()
		if(osName.contains("windows")) {
			installdir = "C:/Program Files (x86)/IBM/AppScanSource"
			configdir = "C:/ProgramData/IBM/AppScanSource"	
		}
		else if(osName.contains("mac")) {
			installdir = "/Applications/AppScanSource.app"
			configdir = "/Users/Shared/AppScanSource"	
		}
		else {
			installdir = "/opt/ibm/appscansource"
			configdir = "/var/opt/ibm/appscansource"	
		}
	}
}
