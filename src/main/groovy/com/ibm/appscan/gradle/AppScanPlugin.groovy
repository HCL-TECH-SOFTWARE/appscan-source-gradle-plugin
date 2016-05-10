package com.ibm.appscan.gradle;

import org.gradle.api.Project;
import org.gradle.api.Plugin;
import org.gradle.api.tasks.TaskExecutionException;

class AppScanPlugin implements Plugin<Project> {

    void apply(Project project) {
			
		project.extensions.create("appscansettings", AppScanSettingsExtension, project)
	
        	project.task('createProject') << {
			try {
				new ProjectCreator(project).run();
			} catch (AppScanException e) {
				throw new TaskExecutionException(project.createProject, e);
			}
		}
		
		project.task('createCLIScript') << {
			try {
				new ScriptCreator(project).run();
			} catch (AppScanException e) {
				throw new TaskExecutionException(project.createCLIScript, e);
			}
		}
		
		project.task('runScan') << {
			try {
				new ScanRunner(project).run();
			} catch (AppScanException e) {
				throw new TaskExecutionException(project.runScan, e);
			}
		}
    }
}
