Metamath plugin for Eclipse based on Xtext
==========================================

Travis CI says: [![Build Status](https://travis-ci.org/marnix/metamath-eclipse-xtext.svg?branch=master)](https://travis-ci.org/marnix/metamath-eclipse-xtext)

CloudBees says: [![Build Status](https://buildhive.cloudbees.com/job/marnix/job/metamath-eclipse-xtext/badge/icon)](https://buildhive.cloudbees.com/job/marnix/job/metamath-eclipse-xtext/)

To install, point your Eclipse to the following update site (courtesy of the OSS Artifactory repository from JFrog): `jar:https://oss.jfrog.org/artifactory/libs-snapshot/mm/ecxt/mm.ecxt.updatesite/1.0.0-SNAPSHOT/mm.ecxt.updatesite-1.0.0-SNAPSHOT.zip!/` and select the MMLanguage SDK Feature.  (Note that this update site is large, so it might take a while to download and install: it is a single 70+ MB zip-file, which also contains Xtext and all other dependencies.  This should be replaced by a very small update site which points to the Xtext update site.  I don't yet know how to best fight Tycho on this point.)

Before you try to open `set.mm`, make sure you run your Eclipse with `-Xmx4G`, and it still might hang on you unless you have a fast system.

LICENSE: Eclipse Public License - v 1.0

*Please note that this project is in the very early stages, and doesn't do much yet.*  However, even if I don't find the time to continue on it, hopefully someone else might pick it up as a useful starting point...
