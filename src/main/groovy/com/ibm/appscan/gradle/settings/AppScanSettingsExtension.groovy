package com.ibm.appscan.gradle.settings;

import org.gradle.api.Project

import com.ibm.appscan.gradle.AppScanConstants

public class AppScanSettingsExtension implements AppScanConstants {

	//Don't allow changing the script name since we need to delete the file for each run.
	final String scriptname = "cliscript.txt"
	final String logname = "appscanlog.txt"
	
	String projectname
	String appname
	String appdir
	String projectdir
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
		appdir = System.getProperty(APP_DIR) == null ? project.rootDir.getAbsolutePath() : System.getProperty(APP_DIR)
		projectdir = System.getProperty(PROJECT_DIR)
		logdir = new File(project.rootDir, "appscan").getAbsolutePath()
		scriptdir = new File(project.rootDir, "appscan").getAbsolutePath()
		setAppScanDirs()
		def usertoken = new File(System.getProperty("user.home"), ".ounce/ouncecli.token")
		tokenfile = usertoken.exists() ? usertoken.getAbsolutePath() : "${configdir}/config/ounceautod.token"
	}

	private void setAppScanDirs() {
		String osName = System.getProperty("os.name").toLowerCase()
		if(osName.contains("windows")) {
			installdir = System.getProperty(INSTALL) ?: new File("C:/Program Files/HCL/AppScanSource").exists() ? "C:/Program Files/HCL/AppScanSource" : "C:/Program Files (x86)/IBM/AppScanSource"
			configdir = new File("C:/ProgramData/HCL/AppScanSource").exists() ? "C:/ProgramData/HCL/AppScanSource" : "C:/ProgramData/IBM/AppScanSource"
		}
		else if(osName.contains("mac")) {
			installdir = System.getProperty(INSTALL) ?: "/Applications/AppScanSource.app"
			configdir = "/Users/Shared/AppScanSource"	
		}
		else {
			installdir = System.getProperty(INSTALL) ?: new File("/opt/hcl/appscansource").exists() ? "/opt/hcl/appscansource" : "/opt/ibm/appscansource"
			configdir = new File("/var/opt/hcl/appscansource").exists() ? "/var/opt/hcl/appscansource" : "/var/opt/ibm/appscansource"
		}
	}
}
