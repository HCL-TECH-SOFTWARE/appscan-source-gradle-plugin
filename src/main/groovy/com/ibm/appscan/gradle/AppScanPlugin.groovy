package com.ibm.appscan.gradle;

import org.gradle.api.Project;
import org.gradle.api.Plugin;
import org.gradle.api.tasks.TaskExecutionException;

class AppScanPlugin implements Plugin<Project> {

    void apply(Project project) {
			
		project.extensions.create("appscansettings", AppScanSettingsExtension, project)
	
        	project.task('createProject', description: "Generates an AppScan Source project for this Gradle project.") << {
			try {
				new ProjectCreator(project).run();
			} catch (AppScanException e) {
				throw new TaskExecutionException(project.createProject, e);
			}
		}
		
		project.task('createCLIScript', description: "Generates an AppScan Source CLI script for executing a scan.") << {
			try {
				new ScriptCreator(project).run();
			} catch (AppScanException e) {
				throw new TaskExecutionException(project.createCLIScript, e);
			}
		}
	
		project.task('runScan', description: "Executes the AppScan Source CLI script located at <scriptdir>/cliscript.txt.") << {
			try {
				new ScanRunner(project).run();
			} catch (AppScanException e) {
				throw new TaskExecutionException(project.runScan, e);
			}
		}
    }
}
