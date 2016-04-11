# IBM Security AppScan Source Gradle Plugin

The AppScan Source Gradle plugin is used to automate the scanning of Java and Java web projects in Gradle.  It generates AppScan Source project files for Gradle projects that have the "java" plugin and/or "war" plugins applied.  It can also generate and run a CLI script for executing a scan.

# Prerequisites:

- A local installation of IBM Security AppScan Source.

-  Enable AppScan Source to scan .class files.  To do so, open AppScan Source For Analysis and go to Edit -> Preferences -> Project File Extensions.  On the Java tab, double-click the .class extension and choose "Scan files with this extension".

-  You must compile any Java projects prior to running a scan.  A simple way to do this is to use task dependencies.
	
# Recommendations:

Create a cli.token to persist the user credentials, so they do not need to be included in the generated CLI script.  To generate the .token, execute a "login" command using the interactive AppScan Source CLI.  For example, at a command prompt run:
	AppScanSrcCli.exe (Windows)
	appscansrccli (Linux)
Then execute a login command using the "-persist" flag.  For example:
	login <server> <username> <password> -persist

# Tasks:

createProject:
	Creates an AppScan Source project file (.ppf) for the given project and adds it to an AppScan Source application file (.paf).

createCLIScript:
	Generates an AppScan Source project file (.ppf) for each subproject and creates a CLI script that can be run with the "runScan" task.

runScan:
	Generates a CLI script using the createCLIScript task and runs a scan using the script $scriptdir/cliscript.txt".

# Configurable Options:

	OPTION:						DEFAULT VALUE									DESCRIPTION
	projectname					project.name									The name of the generated .ppf file.
	appname						rootProject.name								The name of the generated .paf file.
	appdir						rootProject.projectDir							The location of the generated .paf file.
	scriptdir					"$appdir/appscan"								The location of the generated CLI script.
	installdir					"C:/Program Files (x86)/IBM/AppScanSource"		The AppScan Source installation directory.
	logdir						"$appdir/appscan/"								The location of the generated scan log.
	username					""												The username for authenticating with ASE. Leave blank if using a cli.token (recommended).
	password					""												The password for authenticating with ASE. Leave blank if using a cli.token (recommended).
	server						"localhost"										The ASE server to authenticate with.
	scanoptions					""												Options passed to the CLI "scan" command. See the AppScan Source Utilities Guide for valid options.

To set options, add an "appscansettings" block to your build script specifying the settings you want to change and the desired value.  For example, to change the directory for the generated CLI script and the generated scan log to "/myApp/temp_files", add the following:

appscansettings {
	scriptdir = "/myApp/temp_files"
	log = "/myApp/temp_files"
}

# Example Usage:

Below is an example of applying the AppScan plugin to a build.gradle for the root project of a multi-project build.  In this example, the "buildApplication" task builds the project.  Executing the "runAppScan" task will build the application and execute a scan.

	buildscript {
		dependencies {
			classpath files("C:/gradle/plugins/appscan_gradle_plugin.jar")
		}
	}

	allprojects {
		apply plugin: 'com.ibm.appscan.gradle.appscan_plugin'

		appscansettings {
			server = "myASEserver.sample.com"
		}
	}
	...
	<other tasks>
	...
	task runAppScan(dependsOn : [buildApplication, runScan]) << {
	}
	runScan.mustRunAfter buildApplication

# License

All files found in this project are licensed under the [Apache License 2.0](LICENSE).

