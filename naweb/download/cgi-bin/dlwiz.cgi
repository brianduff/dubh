#!/usr/bin/tclsh

# NewsAgent Download Wizard
# Main CGI Module
# Version 1.0

package require cgi

proc dorequirederror {first} {

	cgi_title "Error!"
	h1 "There was a problem"
	p {
		You probably didn't fill in all the fields that were marked
		as required (by two stars). Please use the back button on
		your browser to go back to the form and make sure you fill
		in all required entries.
	}
	p "The first missing field was $first"
	exit

}

# check a variable to see if it exists Return -1 if not
proc checkvar {sVarName} {
	set lVars [cgi_import_list]
	if {[lsearch $lVars $sVarName]==-1} {
		return 0
	} else {
		return 1
	}
}

cgi_eval {
	cgi_debug -on
	cgi_admin_mail_addr bd@dcs.st-and.ac.uk
	cgi_name "NewsAgent Download Wizard"
	cgi_input "FullName=Brian&Country=UK&OperatingSystem=solaris&JDK=Checked"
	if {[checkvar FullName]} {
		cgi_import FullName
		if {[string trim $FullName] == ""} {	
			dorequirederror "full name"
		}
	} else {
		dorequirederror "full name"
	}
	if {[checkvar Email]} {
		cgi_import Email
	} else {
		set Email "NotSpecified@nowhere.com"
	}
	if {[checkvar URL]} {
		cgi_import URL
	} else {
		set URL "not specified"
	}
	if {[checkvar Country]} {
		cgi_import Country
		if {[string trim $Country]==""} {
			dorequirederror "country"
		}
	} else {
		dorequirederror "country"
	}
	if {[checkvar Occupation]} {
		cgi_import Occupation
	} else {
		set Occupation "not specified"
	}
	if {[checkvar SendEmail]} {
		cgi_import SendEmail
	} else {
		set SendEmail "no"
	}
	if {[checkvar OperatingSystem]} {
		cgi_import OperatingSystem
	} else {
		dorequirederror "operating system"
	}
	if {[checkvar OsOther]} {
		cgi_import OsOther
	} else {
		set OsOther ""
	}
	if {[checkvar JDK]} {
		cgi_import JDK
	} else {
		set JDK ""
	} 
	if {[checkvar Swing]} {
		cgi_import Swing
	} else {
		set Swing ""
	}

#	cgi_mail_addr $Email
#	cgi_mail_start briand@st-and.compsoc.org.uk
#	cgi_mail_add "Subject: $FullName downloaded NewsAgent"
#	cgi_mail_add
#	cgi_mail_add "Details as follows:"
#	cgi_mail_add "Full Name: $FullName"
#	cgi_mail_add "Email: $Email"
#	cgi_mail_add "URL: $URL"
#	cgi_mail_add "Occupation: $Occupation"
#	cgi_mail_add "Country: $Country"
#	cgi_mail_add "SendEmail? $SendEmail"
#	cgi_mail_add "OS: $OperatingSystem"
#	cgi_mail_add "Other: $OsOther"
#	cgi_mail_add "JDK: $JDK"
#	cgi_mail_add "Swing: $Swing"
#	cgi_mail_end	

	# Redirect

	if {$OperatingSystem=="windows32"} {
		if {$JDK=="Checked" && $Swing=="Checked"} {
			cgi_redirect http://st-and.compsoc.org.uk/~briand/newsagent/download/step2_windows_jre_swing.html
			exit
		}
                if {$Swing!="Checked" && $JDK=="Checked"} {
			cgi_redirect http://st-and.compsoc.org.uk/~briand/newsagent/download/step2_windows_jre.html
			exit
		}
		cgi_redirect http://st-and.compsoc.org.uk/~briand/newsagent/download/step2_windows.html
		exit
	} 
	if {$OperatingSystem=="solaris" || $OperatingSystem=="linux" || $OperatingSystem=="digitalunix"} {
		if {$JDK=="Checked" && $Swing=="Checked"} {
			cgi_redirect http://st-and.compsoc.org.uk/~briand/newsagent/download/step2_unix_jre_swing.html
			exit
		}
		if {$Swing!="Checked" && $JDK=="Checked"} {
			cgi_redirect http://st-and.compsoc.org.uk/~briand/newsagent/download/step2_unix_jre.html
			exit
		}
		cgi_redirect http://st-and.compsoc.org.uk/~briand/newsagent/download/step2_unix.html
		exit
	}
	cgi_redirect http://st-and.compsoc.org.uk/~briand/newsagent/download/step2_other.html
	exit
}
