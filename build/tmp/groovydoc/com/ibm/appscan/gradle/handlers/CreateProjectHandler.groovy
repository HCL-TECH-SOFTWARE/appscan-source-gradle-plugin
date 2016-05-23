package com.ibm.appscan.gradle.handlers;

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet

import com.ibm.appscan.gradle.error.AppScanException


public class CreateProjectHandler extends AppScanHandler{

	private String m_appdir;
	private String m_appname;
	private String m_projectname;
	private List<String> m_exclusions;

	public CreateProjectHandler(Project project) {
		super(project)
		m_appdir = project.appscansettings.appdir
		m_appname = project.appscansettings.appname
		m_projectname = project.appscansettings.projectname
		m_exclusions = Arrays.asList(project.appscansettings.sourceexcludes)
	}
	
	@Override
	protected void runTask() throws AppScanException{
		//Work around a problem updating existing .ppf files
		File projectfile = new File(m_project.projectDir, "${m_projectname}.ppf")
		if(projectfile.isFile())
			projectfile.delete()
		
		def ant = new AntBuilder()

		ant.taskdef(resource: "com/ouncelabs/ounceant/task/ounce.xml") {
			classpath {
				fileset(dir: m_project.appscansettings.installdir + "/lib") {
					include(name: "*.jar")
				}
			}
		}
		ant.ounceCreateProject(
			name: m_projectname,
			classpath: m_project.sourceSets.main.compileClasspath.asPath,
			workingDir: m_project.projectDir,
			appName: m_appname,
			appDir: m_appdir) {
				for(SourceSet sourceSet : m_project.sourceSets) {
					if(m_exclusions.contains(sourceSet.getName())) {
						ounceSourceRoot(dir: sourceSet.output.classesDir)
					}
				}
				if(m_project.plugins.hasPlugin("org.gradle.war")) {
					ounceWeb (webContextRoot: m_project.webAppDir.getAbsolutePath())
				}
			}
	}
	
	@Override
	protected void verifySettings() throws AppScanException {
		if(!new File(m_appdir).isDirectory())
			throw new AppScanException("The application directory ${m_appdir} does not exist!")
	}
}

