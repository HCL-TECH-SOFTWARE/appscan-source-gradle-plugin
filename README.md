# IBM Security AppScan Source Gradle Plugin

The AppScan Source Gradle plugin is used to automate the scanning of Java and Java web projects in Gradle.  It generates AppScan Source project files for Gradle projects that have the "java" plugin and/or "war" plugins applied.  It can also generate and run a CLI script for executing a scan.

# Usage:

To use the AppScan Source plugin, add the following lines to build.gradle:

	plugins {
		id "com.ibm.appscan" version "1.1.13"
	}
	
	apply plugin: 'com.ibm.appscan'

Or use the following:

	buildscript {
		repositories {
	    		maven { url "https://plugins.gradle.org/m2/" }
	  	}
		dependencies { classpath "gradle.plugin.com.ibm.security:appscan-source-gradle-plugin:1.1.13" }
	}

	apply plugin: 'com.ibm.appscan'

# Prerequisites:

- A local installation of IBM Security AppScan Source.

- As of version 10.0.7, AppScan Source has an updated installation path, replacing IBM with HCL. To accommodate this change, data directory has been updated accordingly in 1.1.13 version of the AppScan Source Gradle plugin. It first looks for the install path with HCL in it, if the path does not exist it maps to the old path which is IBM.

-  Enable AppScan Source to scan .class files.  To do so:

	- In AppScan Source For Analysis go to Edit -> Preferences -> Project File Extensions.  On the Java tab, double-click the .class extension and choose "Scan files with this extension".
	
	Or:

	- Open <data_dir>/file_extensions.xml in a text editor and locate the FileExtensionSet named "java".  Edit the "class" extension and set the "assess" attribute to a value of "true".
	<data_dir> maps to the following directories, by default:
	
		Windows - C:\ProgramData\HCL\AppScanSource\ltd
	
		Linux - /var/opt/hcl/appscansource/ltd
	
		Mac - /Users/Shared/AppScanSource/ltd

	
# Recommendations:

Create a cli.token to persist the user credentials, so they do not need to be included in the generated CLI script.  To generate the .token, execute a "login" command using the interactive AppScan Source CLI.  For example, at a command prompt run:


	AppScanSrcCli.exe (Windows)

	appscansrccli (Linux or Mac)


Then execute a login command using the "-persist" flag.  For example:

	login <server> <username> <password> -persist

# Tasks:

- createProject:
	Creates an AppScan Source project file (.ppf) for the given project and adds it to an AppScan Source application file (.paf).

- createCLIScript:
	Generates an AppScan Source project file (.ppf) for each subproject and creates a CLI script that can be run with the "runScan" task.

- runScan:
	Generates a CLI script using the createCLIScript task and runs a scan using the script $scriptdir/cliscript.txt".

# Configurable Options:

	OPTION:						DEFAULT VALUE									DESCRIPTION
	appname						rootProject.name								The name of the generated .paf file.
	appdir						rootProject.projectDir							The location of the generated .paf file.
	projectdir					project.projectDir							The location of the generated .ppf file.
	scriptdir					"$appdir/appscan"								The location of the generated CLI script.
	installdir					Varies by platform								The AppScan Source installation directory.
	configdir					Varies by platform								The AppScan Source shared data directory.
	logdir						"$appdir/appscan/"								The location of the generated scan log.
	tokenfile					"$user.home/.ounce/ouncecli.token				The token file to use for login.
	username					""												The username for authenticating with ASE. Leave blank if using a cli.token (recommended).
	password					""												The password for authenticating with ASE. Leave blank if using a cli.token (recommended).
	acceptssl					true											Whether or not to accept untrusted certificates.
	server						"localhost"										The ASE server to authenticate with.
	scanoptions					""												Options passed to the CLI "scan" command. See the AppScan Source Utilities Guide for valid options.
	sourceexcludes					"test"											A ; delimited list of source set names that should be excluded from scanning.
	scanconfig					null											The scan configuration to use for the scan.
	reporttype					null											The report type to generate.
	reportformat					null											The format of the generated report.
	reportlocation					null											The location of the generated report.
	publishfolder					null											The AppScan Enterprise folder where results will be published.
	publishapp					null											The AppScan Enterprise application where results will be published.
	publishname					null											The name of the published assessment in AppScan Enterprise.
	publishASE					false											Whether or not the results should be published to AppScan Entrerprise (requires additional configruation).
    compilecode          false                               If set to true, it will compile code prior to scan. If set to false, it will reuse the bytecodes already generated.

To set options, add an "appscansettings" block to your build script specifying the settings you want to change and the desired value.  For example, to change the directory for the generated CLI script and the generated scan log to "/myApp/temp_files", add the following:

appscansettings {
	scriptdir = "/myApp/temp_files"
	logdir = "/myApp/temp_files"
}

<b>NOTE:</b> The installdir, appdir, and projectdir options can now be specified using Java properties on the command line using the following syntax:
	-Dappscan.installdir=<install directory>
	-Dappscan.appdir=<directory to store .paf>
	-Dappscan.projectdir=<directory to store .ppf>

# Example Usage:

Below is an example of applying the AppScan plugin to a build.gradle for the root project of a multi-project build.  Executing the ":runscan" task will compile all java projects and execute a scan.

NOTE: It's recommended to execute the runscan task as ":runscan" so the task will only be executed once for the root project.  Executing "runscan" will result in the runscan task executing for each project in the build, which is generally not the desired behavior.

	apply plugin: 'com.ibm.appscan'

	appscansettings {
		server = "myASEserver.sample.com"
	}

# License

All files found in this project are licensed under the [Apache License 2.0](LICENSE).

