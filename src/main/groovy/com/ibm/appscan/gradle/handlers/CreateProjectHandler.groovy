package com.ibm.appscan.gradle.handlers;

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet

import com.ibm.appscan.gradle.error.AppScanException


public class CreateProjectHandler extends AppScanHandler{

	private String m_appdir;
	private String m_projectdir;
	private String m_appname;
	private String m_projectname;
	private List<String> m_exclusions;

	public CreateProjectHandler(Project project) {
		super(project)
		m_appdir = project.appscansettings.appdir
		m_projectdir = project.appscansettings.projectdir ?: project.projectDir
		m_appname = project.appscansettings.appname
		m_projectname = project.appscansettings.projectname
		m_exclusions = Arrays.asList(project.appscansettings.sourceexcludes)
	}
	
	@Override
	protected void runTask() throws AppScanException{
		//Work around a problem updating existing .ppf files
		File projectfile = new File(m_projectdir, "${m_projectname}.ppf")
		if(projectfile.isFile())
			projectfile.delete()
		
		//Set the ounce.application_dir property, so ant uses the same value
		System.setProperty("ounce.application_dir", m_appdir)
		
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
			classpath: m_project.sourceSets.main.compileClasspath.asPath + File.pathSeparator + getClassesDirs(),
			workingDir: m_projectdir,
			appName: m_appname,
			appDir: m_appdir) {
				for(SourceSet sourceSet : m_project.sourceSets) {
					if(!m_exclusions.contains(sourceSet.getName())) {
						if(!m_project.appscansettings.compilecode) {
							for (File file : sourceSet.output.classesDirs) {
								ounceSourceRoot(dir: file.getAbsolutePath())
							}
						}
						else {
						    for(File file : sourceSet.java.getSrcDirs()) {
							    ounceSourceRoot(dir: m_project.relativePath(file))
						    }
						}
					}
				}
			    if(m_project.plugins.hasPlugin("org.gradle.war") || m_project.plugins.hasPlugin("war")) {

					ounceWeb (webContextRoot: m_project.webAppDir.getAbsolutePath())
				}
			}
	}
	
	@Override
	protected void verifySettings() throws AppScanException {
		//Ensure the app and project dirs exist
		new File(m_appdir).mkdirs()
		new File(m_projectdir).mkdirs()
	
		if(!new File(m_appdir).isDirectory())
			throw new AppScanException("The application directory ${m_appdir} does not exist!")
		if(!new File(m_projectdir).isDirectory())
			throw new AppScanException("The project directory ${m_projectdir} does not exist!")
	}
	
	private String getClassesDirs() {
        String ret = "";
        for (String str : m_project.sourceSets.main.output.classesDirs) {
            ret += str + File.pathSeparator
        }
        return ret
    }
}

