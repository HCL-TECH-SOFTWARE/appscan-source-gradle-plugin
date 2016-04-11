package com.ibm.appscan.gradle;

import groovy.util.AntBuilder;
import org.gradle.api.Project;

public class ScanRunner extends AppScanTask {

	public ScanRunner(Project project) {
		super(project);
	}
	
	public void run() throws AppScanException {
		verifySettings()
		
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
			script: "$m_project.appscansettings.scriptdir/$m_project.appscansettings.scriptname",
			output: "$m_project.appscansettings.logdir/$m_project.appscansettings.logname")
	}
	
	private void verifySettings() throws AppScanException {
		if(!checkInstall())
			throw new AppScanException("AppScan Source install directory does not exist!")
		
		File cliscript = new File(m_project.appscansettings.scriptdir, m_project.appscansettings.scriptname)
		if(!cliscript.isFile())
			throw new AppScanException("Scan failed.\nThe script " + cliscript.getAbsolutePath() + " does not exist!")

		//Prevents a scan of all applications from being run if the application file does not exist.
		File applicationfile = new File(m_project.appscansettings.appdir, "${m_project.appscansettings.appname}.paf")
		if(!applicationfile.isFile())
			throw new AppScanException("Scan failed.\nThe application " + applicationfile.getAbsolutePath() + " does not exist!")
			
		File log_dir = new File(m_project.appscansettings.logdir)
		if(!log_dir.isDirectory())
			log_dir.mkdirs()
		if(!log_dir.isDirectory())
			throw new AppScanException("Scan failed.  Could not create log directory " + log_dir.getAbsolutePath())
	}
}