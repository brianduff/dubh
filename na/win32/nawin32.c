/*   NewsAgent: A Java USENET Newsreader
 *   Copyright (C) 1997-8  Brian Duff
 *   Email: bduff@uk.oracle.com
 *   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * Windows 32 launcher App
 * Author: Brian Duff
 * Version : 0.2 [08/06/98]: Added userdoc to classpath
 *           0.3 [05/11/98]: Added support for newsagent and DJU .jar files
 */
#include "windows.h"
#include "resource.h"
#include <stdio.h>

#define MAX_PATHSIZE 255
#define MAX_COMMANDLINE 512

#define APP_NAME "NewsAgent"
#define APP_VERSION "1.0.2"

/** Version of Dubh Java Utils this version of NewsAgent builds against */
#define DJU_VERSION "1.0.1"

/** SWING_RELEASE is defined if this executable is to be bundled with Swing */

#define SWING_CLASSPATH "\\lib\\swingall.jar"

#define NEWSAGENT_CLASSPATH "\\lib\\dubhna." APP_VERSION ".jar"

#define DJU_CLASSPATH "\\lib\\dubhju." DJU_VERSION ".jar"

#define JRE_CLASSPATH "\\lib\\rt.jar"
#define RT_EXE "\\bin\\jrew.exe"
#define JDK_CLASSPATH "\\lib\\classes.zip"
#define CLASSPATH_ARG " -classpath \"%s;%s;%s;%s;%s\""

#define USERDOC_CLASSPATH "\\userdoc"

/* The class in NewsAgent that contains the main() method that starts the app */
#define NEWSAGENT_RUNNABLE  " dubh.apps.newsagent.NewsAgent "

// JDK Version Minimum Requirements. 1.1.4 will work, but is unstable.
#define MIN_MAJOR		1
#define MIN_MINOR		1
#define MIN_MICRO		4

#define RUNTIME_NONE		0
#define RUNTIME_JRE			1
#define RUNTIME_JDK			2	

#define STRING_STORAGE 6

/*
 * We are interested in the following registry keys under HKEY_LOCAL_MACHINE:
 */

// An installation key for NewsAgent. Should be created by the installer
#define REG_NA "Software\\Dubh\\NewsAgent"
// The version specific key for NewsAgent.
#define REG_NA_THISVERSION "Software\\Dubh\\NewsAgent\\%s"
// The CurrentVersion is a string value in the REG_NA key.
#define REG_NA_SUB_VERSION "CurrentVersion"
// The Home is a string value in the REG_NA\\x.x key
#define REG_NA_SUB_HOME	   "Home"
// The Swing is a string value in the REG_NA key
#define REG_NA_SUB_SWING   "SwingAll"


// JDK and JRE installation keys
#define REG_JDK "Software\\JavaSoft\\Java Development Kit"
#define REG_JRE "Software\\JavaSoft\\Java Runtime Environment"
#define REG_JDK_VER "Software\\JavaSoft\\Java Development Kit\\%i.%i"
#define REG_JRE_VER "Software\\JavaSoft\\Java Runtime Environment\\%i.%i"
#define REG_JAVAVERSION "%i.%i"
#define REG_JAVAHOME "JavaHome"
#define REG_JAVA_MICRO "MicroVersion"
#define REG_JAVA_CURRENT "CurrentVersion"

HINSTANCE appInstance;

/**
 * Call this for any other error. It uses a string defined in the resource
 * file for this application, displays a message box and terminates.
 *	Parameters:
 *		nID - The id of the resource string to use
 */
void fatalAppError(LONG nID) {
	// Replace this!!!
	LPTSTR lpBuffer;
	DWORD  nSize = 255;

	lpBuffer = LocalAlloc(LPTR, nSize);
	LoadString(appInstance, nID, lpBuffer, nSize);
	
	MessageBox( NULL, lpBuffer, APP_NAME, MB_OK | MB_ICONEXCLAMATION );
	LocalFree(lpBuffer);
	exit(1);
}

/**
 * Check that JDK or JRE is available.
 * Returns: RUNTIME_JDK or RUNTIME_JRE if a runtime is present or
 *	RUNTIME_NONE. If both are installed, RUNTIME_JDK is the default.
 *  The latest version number is returned in the three parameters.
 */
int checkJava(int *ver_major, int *ver_minor, int *ver_micro) {
	int biggestMajor = -1;
	int biggestMinor = -1;
	int nMicro = -1;
	int jdkOrJre;
	LONG lnErrors;
	HKEY hkeyOpen;
	HKEY hkeyVersion;
	int nCurrentSubkey = 0;
	DWORD nSubkeyNameSize = STRING_STORAGE;
	char *psSubkeyName;
	FILETIME ftTemp;
	char *pszDelimiter = ".";
	int nMajor, nMinor;
	int bJDK12Found = 0;
	
	/* Find JDK or JRE */

	// REGDB_E_KEYMISSING is returned by RegOpenKeyEx if the key is missing.

	lnErrors = RegOpenKeyEx(
			HKEY_LOCAL_MACHINE, 
			REG_JDK,
			0, 
			KEY_READ,
			&hkeyOpen
	);


	if (lnErrors == ERROR_SUCCESS) {
		jdkOrJre =  RUNTIME_JDK;
	} else {
		// If something went wrong, try to find the JRE
		lnErrors = RegOpenKeyEx(
				HKEY_LOCAL_MACHINE, 
				REG_JRE,
				0, 
				KEY_READ,
				&hkeyOpen
		);
		if (lnErrors == ERROR_SUCCESS) {
			jdkOrJre = RUNTIME_JRE;
		} else {
			return RUNTIME_NONE;
		}
	}
	/* If we get to here, we have an open key in hkeyOpen that is either the
	 * JDK or the JRE. jdkOrJre is set depending on which. */

	/* Alloc heap space for the subkey name */
	psSubkeyName = (char *)malloc(nSubkeyNameSize);

	/* Enumerate the reg keys and find the biggest Major version */

	do {
		lnErrors = RegEnumKeyEx(
			hkeyOpen,
			nCurrentSubkey++,
			psSubkeyName,
			&nSubkeyNameSize,
			NULL, NULL, NULL,
			&ftTemp
		);
		if (lnErrors == ERROR_SUCCESS) {
			// The major version is the bit before the dot
			psSubkeyName = strtok(psSubkeyName, pszDelimiter);
			if (psSubkeyName != NULL) {
				nMajor = atoi(psSubkeyName);
				if (nMajor > biggestMajor) biggestMajor = nMajor;
			}

		}
		nSubkeyNameSize = STRING_STORAGE;
	} while (lnErrors == ERROR_SUCCESS);

	/** Terminate if a proper error occurred */
	if (lnErrors != ERROR_NO_MORE_ITEMS) {
		RegCloseKey(hkeyOpen);
	//	free(psSubkeyName);
		fatalAppError(NA_ERR_REGISTRY);
	}

	/* Enumerate the reg keys again to find the biggest Minor version */
	/* BD-1998-11-05: We don't currently support JDK/RE 1.2.* (tested with the
	 * new version, and the 1.2 VM seems to have trouble with some of our
	 * class files if (and only if, oddly) they are compiled with JDK 1.1.*
	 * Is Java 1.2 backwards compatible???!! (tested this on JDK1.2 RC1 (Nov 98))
	 */

	nCurrentSubkey = 0;

	do {
		lnErrors = RegEnumKeyEx(
			hkeyOpen,
			nCurrentSubkey++,
			psSubkeyName,
			&nSubkeyNameSize,
			NULL, NULL, NULL,
			&ftTemp
		);
		if (lnErrors == ERROR_SUCCESS) {
			// The major version is the bit before the dot
			psSubkeyName = strtok(psSubkeyName, pszDelimiter);
			if (psSubkeyName != NULL) {
				nMajor = atoi(psSubkeyName);
				// Only check the minor if the major is the biggest 
				if (nMajor == biggestMajor) {
					psSubkeyName = strtok(NULL, pszDelimiter);
					if (psSubkeyName != NULL) {
						nMinor = atoi(psSubkeyName);
						//
						// JDK 1.2 Hack starts
						//
						if (nMinor >= 2)
						{
							bJDK12Found = 1;
							// should really let the user know we are going
							// to fall back to 1.1.

						} else 
						//
						// JDK 1.2 Hack Ends
						//
							if (nMinor > biggestMinor) biggestMinor = nMinor;
					}
				}
			}

		}
		nSubkeyNameSize = STRING_STORAGE;
	} while (lnErrors == ERROR_SUCCESS);

	/** Terminate if a proper error occurred */
	if (lnErrors != ERROR_NO_MORE_ITEMS) {
		RegCloseKey(hkeyOpen);
	//	free(psSubkeyName);
		fatalAppError(NA_ERR_REGISTRY);
	}

	if (biggestMinor == -1)
	{
		if (bJDK12Found == 1)
			fatalAppError(NA_ERR_JDK12);
		else
			fatalAppError(NA_ERR_JDKVERS);
	}


	/* Now we have a runtime, major and minor versions. Just need to look up
	 * The micro version */
	
	sprintf(psSubkeyName, REG_JAVAVERSION, biggestMajor, biggestMinor);

	lnErrors = RegOpenKeyEx(
			hkeyOpen, 
			psSubkeyName,
			0, 
			KEY_READ,
			&hkeyVersion
	);
	/** Can now close hkeyOpen. */
	RegCloseKey(hkeyOpen);


	if (lnErrors != ERROR_SUCCESS) {
	//	free(psSubkeyName);
		fatalAppError(NA_ERR_REGISTRY);
	}

	nSubkeyNameSize = STRING_STORAGE;
	/** Look up the MicroVersion subkey */
	lnErrors = RegQueryValueEx(
			hkeyVersion,
			REG_JAVA_MICRO,
			NULL,
			NULL,
			psSubkeyName,
			&nSubkeyNameSize
	);

	RegCloseKey(hkeyVersion);

	if (lnErrors != ERROR_SUCCESS) {
	//	free(psSubkeyName);
		fatalAppError(NA_ERR_REGISTRY);
	}

	// Convert the string
	nMicro = atoi(psSubkeyName);

	// Copy the three version numbers to the passed parameters
	*ver_major = biggestMajor;
	*ver_minor = biggestMinor;
	*ver_micro = nMicro;

	// Free storage
//	free(psSubkeyName);

	// Return the JDK type
	return jdkOrJre;
}

/*
 * Inform the user that the version of JDK or JRE is too low.
 */
void versionTooLow(int major, int minor, int micro) {
	LPTSTR lpBuffer;
	DWORD  nSize = 255;
	LPTSTR lpFinalBuffer;

	lpFinalBuffer = LocalAlloc(LPTR, nSize);
	lpBuffer = LocalAlloc(LPTR, nSize);
	LoadString(appInstance, NA_ERR_JAVAVERSION, lpBuffer, nSize);
	sprintf(lpFinalBuffer, lpBuffer, major, minor, micro, MIN_MAJOR, MIN_MINOR, MIN_MICRO);
	
	MessageBox( NULL, lpFinalBuffer, APP_NAME, MB_OK | MB_ICONEXCLAMATION );
	LocalFree(lpBuffer);
	LocalFree(lpFinalBuffer);
	exit(1);
}

/*
 * Determine the directory in which NewsAgent is installed. Terminates
 * the app with an error dialogue if something goes wrong.
 * Parameters:
 *	pszPath - A preallocated string on the heap
 *	nSize	- The size of the preallocated string buffer you passed
 */
void getNewsAgentHome(LPTSTR pszPath, int nSize) {
	LONG lnErrors;
	HKEY hkeyNewsAgent, hkeyThisVersion;
	LPTSTR pszTemp;
	LONG nTempSize;

	/*
	 * Open the root NewsAgent application registration database key
	 */

	lnErrors = RegOpenKeyEx(
		HKEY_LOCAL_MACHINE,
		REG_NA,
		0,
		KEY_READ,
		&hkeyNewsAgent
	);


	if (lnErrors != ERROR_SUCCESS)
		fatalAppError(NA_ERR_NAINSTALL);

	// Temp storage for the version number
	pszTemp = LocalAlloc(LPTR, MAX_PATHSIZE);
	nTempSize = MAX_PATHSIZE;

	/*
	 * Get the current version number
	 */

	lnErrors = RegQueryValueEx(
		hkeyNewsAgent,
		REG_NA_SUB_VERSION,
		NULL, 
		NULL, 
		pszTemp,
		&nTempSize
	);

	if (lnErrors != ERROR_SUCCESS) {
		LocalFree(pszTemp);
		RegCloseKey(hkeyNewsAgent);
		fatalAppError(NA_ERR_NAINSTALL);
	}

	/*
	 * Open the version specific registration key
	 */

	lnErrors = RegOpenKeyEx(
		hkeyNewsAgent,
		pszTemp,
		0,
		KEY_READ,
		&hkeyThisVersion
	);

	RegCloseKey(hkeyNewsAgent);
	LocalFree(pszTemp);

	if (lnErrors != ERROR_SUCCESS) 
		fatalAppError(NA_ERR_NAINSTALL);

	/*
	 * And read the home directory into the return string
	 */
	lnErrors = RegQueryValueEx(
		hkeyThisVersion,
		REG_NA_SUB_HOME,
		NULL,
		NULL,
		pszPath,
		&nSize
	);

	RegCloseKey(hkeyThisVersion);

	if (lnErrors != ERROR_SUCCESS)
		fatalAppError(NA_ERR_NAINSTALL);
}

/*
 * Determine the precise location of swingall.jar. Terminates the program
 * with an alert if something goes wrong.
 * Parameters:
 *	pszPath - A preallocated string on the heap.
 *  nSize	- The size of the buffer you passed.
 */
void getSwingClassPath(LPTSTR pszPath, int nSize) {
#ifdef SWING_RELEASE
	// Version is released bundled with Swing
	// Swing is in NAHOME\swingall.jar
	getNewsAgentHome(pszPath, nSize);
	strcat(pszPath, SWING_CLASSPATH);
#else
	/* Version is not released bundled with Swing
	 * The location of swing has been set by the installation program
	 * in the registry.
	 */
	HKEY hkeyNewsAgent;
	LONG lnErrors;
	int nTempSize;
	//REG_NA_SUB_SWING

	/*
	 * Open the root NewsAgent registry key
	 */
	lnErrors = RegOpenKeyEx(
		HKEY_LOCAL_MACHINE,
		REG_NA,
		0,
		KEY_READ,
		&hkeyNewsAgent
	);


	if (lnErrors != ERROR_SUCCESS)
		fatalAppError(NA_ERR_NAINSTALL);

	/*
	 * Query the REG_NA_SUB_SWING subkey
	 */
	nTempSize = nSize;
	lnErrors = RegQueryValueEx(
		hkeyNewsAgent,
		REG_NA_SUB_SWING,
		NULL,
		NULL,
		pszPath,
		&nTempSize
	);
	
	RegCloseKey(hkeyNewsAgent);

	if (lnErrors != ERROR_SUCCESS)
		fatalAppError(NA_ERR_SWINGINSTALL);

	strcat(pszPath, SWING_CLASSPATH);

#endif
}

/*
 * Retrieve the NewsAgent classpath. Terminates with an alert if something
 * goes wrong.
 * Parameters:
 *	pszPath - A preallocated buffer
 *	nSize   - The size of the buffer in bytes
 */
void getNewsAgentClassPath(LPTSTR pszPath, int nSize) {
	getNewsAgentHome(pszPath, nSize);
	strcat(pszPath, NEWSAGENT_CLASSPATH);
}

/*
 * Retrieve the NewsAgent documentation classpath. 
 * Parameters:
 *	pszPath - Prealloced buffer
 *	nSize   - Size of buffer
 */
void getDocClassPath(LPTSTR pszPath, int nSize) {	
	getNewsAgentHome(pszPath, nSize);
	strcat(pszPath, USERDOC_CLASSPATH);
}

/*
 * Retrieve the Dubh Java Utilities classpath
 * Parameters:
 *   pszPath - Preallocated buffer
 *   nSize   - Size of buffer
 */
void getDJUClassPath(LPTSTR pszPath, int nSize) {
	getNewsAgentHome(pszPath, nSize);
	strcat(pszPath, DJU_CLASSPATH);
}

/*
 * Retrieve the JDK or JRE home directory. Terminate with an alert if something goes wrong
 * Parameters:
 *	pszPath - A preallocated buffer
 *	nSize	- The size of the buffer
 *	runtime
 *	major
 *	minor
 */
void getRuntimeDirectory(LPTSTR pszPath, LONG nSize, int runtime, int major, int minor) {
	int nTempSize;
	LPTSTR pszRuntimeKey;
	HKEY hkeyRuntimeKey;
	LONG lnErrors;

	nTempSize = nSize;
	pszRuntimeKey = LocalAlloc(LPTR, MAX_PATHSIZE);

	if (runtime == RUNTIME_JRE)
		sprintf(pszRuntimeKey, REG_JRE_VER, major, minor);
	else if (runtime == RUNTIME_JDK)
		sprintf(pszRuntimeKey, REG_JDK_VER, major, minor);
	/*
	 * Get the installation version home key
	 */
	lnErrors = RegOpenKeyEx(
		HKEY_LOCAL_MACHINE,
		pszRuntimeKey,
		0,
		KEY_READ,
		&hkeyRuntimeKey
	);

	if (lnErrors != ERROR_SUCCESS) {
		LocalFree(pszRuntimeKey);
		fatalAppError(NA_ERR_REGISTRY);
	}

	/*
	 * Query the JavaHome value (REG_JAVAHOME)
	 */
	lnErrors = RegQueryValueEx(
		hkeyRuntimeKey,
		REG_JAVAHOME,
		NULL, NULL,
		pszPath, 
		&nTempSize
	);
	
	RegCloseKey(hkeyRuntimeKey);
	if (lnErrors != ERROR_SUCCESS) {
		LocalFree(pszRuntimeKey);
		fatalAppError(NA_ERR_REGISTRY);
	}
	LocalFree(pszRuntimeKey);
}

/*
 * Retrieve the JDK classpath. Terminate with an alert if something goes wrong
 * Parameters:
 *	pszPath - A preallocated buffer
 *	nSize	- The size of the buffer in bytes
 *  runtime - The runtime type: RUNTIME_JDK or RUNTIME_JRE
 *	major	- The major version of the runtime
 *	minor	- The minor version of the runtime
 */
void getRuntimeClassPath(LPTSTR pszPath, LONG nSize, int runtime, int major, int minor) {
	
	getRuntimeDirectory(pszPath, nSize, runtime, major, minor);
	/*
	 * And concatenate the correct runtime library
	 */
	strcat(pszPath, ((runtime == RUNTIME_JRE) ? JRE_CLASSPATH : JDK_CLASSPATH));

}

/*
 * Construct the CLASSPATH that will be used with Java. This depends wholly on
 * whether JRE or JDK is being used and whether Swing is included or not.
 * If Swing isn't included with the distribution, the installation program
 * should have created a registry key under HKEY_LOCAL_MACHINE that has the
 * path to swingall.jar.
 * Parameters:
 *	pszBuffer	-	A preallocated buffer
 *	nSize		-	The size of the buffer
 *	runtime		-	The Java Runtime system (JDK or JRE)
 *	major, minor -	Java Version
 */
void constructClassPath(PTSTR pszBuffer, LONG nSize, int runtime, int major, int minor) {
	/*
	 * For JDK, the CLASSPATH consists of:
	 *		o getRuntimeClassPath()
	 *		o getNewsAgentClassPath()
	 *		o getSwingClassPath()
	 *		o getDocClassPath()
	 */
	LPTSTR pszRuntime, pszSwing, pszNewsAgent, pszDoc, pszDJU;
	nSize = MAX_PATHSIZE;

	pszRuntime		= LocalAlloc(LPTR, MAX_PATHSIZE);
	pszSwing		= LocalAlloc(LPTR, MAX_PATHSIZE);
	pszNewsAgent	= LocalAlloc(LPTR, MAX_PATHSIZE);
	pszDoc			= LocalAlloc(LPTR, MAX_PATHSIZE);
	pszDJU          = LocalAlloc(LPTR, MAX_PATHSIZE);

	getRuntimeClassPath(pszRuntime, nSize, runtime, major, minor);
	getSwingClassPath(pszSwing, nSize);
	getNewsAgentClassPath(pszNewsAgent, nSize);
	getDocClassPath(pszDoc, nSize);
	getDJUClassPath(pszDJU, nSize);

	sprintf(pszBuffer, CLASSPATH_ARG, pszRuntime, pszSwing, pszNewsAgent, pszDoc, pszDJU);

	LocalFree(pszRuntime);
	LocalFree(pszSwing);
	LocalFree(pszNewsAgent);
	LocalFree(pszDoc);
	LocalFree(pszDJU);
}

/*
 * Construct the command line that will run Java.
 * Parameters:
 *	pszBuffer	-	A preallocated buffer for the command line
 *	nSize		-   The size of the buffer
 *	runtime		-	The Java Runtime (RUNTIME_JRE or RUNTIME_JDK)
 *	major		-	The major java version
 *	minor		-	The minor java version
 *  args		-	Arguments to the program (passed through to the runnable Java class)
 */
void constructCommand(LPTSTR pszBuffer, LONG nSize, int runtime, int major, int minor, LPSTR args) {
	/* Command line consists of:
	 * getRuntimeDirectory() + RT_EXE + constructClasspath() + NEWSAGENT_RUNNABLE + PROG_ARGS
	 */
	LPTSTR pszRuntimeDir, pszClassPathArg;
	pszRuntimeDir = LocalAlloc(LPTR, MAX_PATHSIZE);
	pszClassPathArg = LocalAlloc(LPTR, MAX_PATHSIZE);

	getRuntimeDirectory(pszRuntimeDir, 255, runtime, major, minor);
	constructClassPath(pszClassPathArg, 255, runtime, major, minor);

	pszBuffer[0] = '\0';
	strcat(pszBuffer, pszRuntimeDir);
	strcat(pszBuffer, RT_EXE);
	strcat(pszBuffer, pszClassPathArg);
	strcat(pszBuffer, NEWSAGENT_RUNNABLE);
	strcat(pszBuffer, args);

	LocalFree(pszRuntimeDir);
	LocalFree(pszClassPathArg);
}


/*
 * A dummy dialogue call back routine
 */
BOOL CALLBACK myDlgProc( HWND hwndDlg, UINT uMsg, WPARAM wParam, LPARAM lParam) {
	return TRUE;
}
 



int WINAPI WinMain( HINSTANCE hInstance, 
	HINSTANCE hPrevInstance, 
	LPSTR lpCmdLine, 
	int nCmdShow) {
	
	int major, minor, micro, runtime, nSize;
	LPTSTR pszJavaCommand;

#ifdef _DEBUG
	FILE *f;
#endif

	appInstance = hInstance;

	runtime = checkJava(&major, &minor, &micro);

	if (runtime == RUNTIME_NONE)
		fatalAppError(NA_ERR_NOJDKORJRE);

	if (major < MIN_MAJOR)
		versionTooLow(major, minor, micro);
	if (major == MIN_MAJOR && minor < MIN_MINOR)
		versionTooLow(major, minor, micro);
	if (major == MIN_MAJOR && minor == MIN_MINOR && micro < MIN_MICRO)
		versionTooLow(major, minor, micro);

	pszJavaCommand = LocalAlloc(LPTR, MAX_COMMANDLINE);
	nSize = MAX_COMMANDLINE;

	constructCommand(pszJavaCommand, nSize, runtime, major, minor, lpCmdLine);

#ifdef _DEBUG
	
	//
	// Store a record of our executed command in the nawin32.log file in
	// windows dir.
	//
	f = fopen("\\nawin32.log", "a");
	if (f != NULL)
	{
		fprintf(f, pszJavaCommand);
		fflush(f);
		fclose(f);
	}

#endif

	if (WinExec(pszJavaCommand, SW_SHOWNORMAL) < 32) {
		fatalAppError(NA_ERR_RUN);
	}
	LocalFree(pszJavaCommand);


	return (0);

}


		