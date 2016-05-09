package com.ibm.appscan.gradle;

import groovy.util.AntBuilder;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

public class ProjectCreator extends AppScanTask{

	public ProjectCreator(Project project) {
		super(project);
	}
	
	public void run() throws AppScanException{
	
		if(!checkInstall())
			throw new AppScanException("AppScan Source install directory does not exist!")
			
		if(m_project.plugins.hasPlugin("org.gradle.java")) {
		
			//Work around a problem updating existing .ppf files
			File projectfile = new File(m_project.projectDir, "${m_project.appscansettings.projectname}.ppf")
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
				name: m_project.appscansettings.projectname,
				classpath: m_project.sourceSets.main.compileClasspath.asPath,
				workingDir: m_project.projectDir,
				appName: m_project.appscansettings.appname,
				appDir: m_project.appscansettings.appdir) {
					for(SourceSet sourceSet : m_project.sourceSets) {
						if(includeSourceSet(sourceSet.getName())) {
							ounceSourceRoot(
								dir: sourceSet.output.classesDir)
						}
					}
					if(m_project.plugins.hasPlugin("org.gradle.war")) {
						ounceWeb (
							webContextRoot: m_project.webAppDir.getAbsolutePath())
					}
			}
		}
	}
	
	private boolean includeSourceSet(String name) {
		List<String> exclusions = Arrays.asList(m_project.appscansettings.sourceexcludes.split(";"))
		return !exclusions.contains(name)
	}
}
