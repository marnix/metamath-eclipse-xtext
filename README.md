Metamath plugin for Eclipse based on Xtext
==========================================

# Goal

My goal is to be able to edit Metamath `.mm` files in an Eclipse-based editor,
with proof assistance and everything one needs to create Metamath proofs.
Hopefully most of the work can be done in the editor, in keeping with
Metamath's philosophy of treating the .mm file as source code.

*Please note that this project is in the very early stages, and doesn't do much yet.*  However, even if I don't find the time to continue on it, hopefully someone else might pick it up as a useful starting point...

# Feedback

Feel very free to create a GitHub issue, create a pull request, or drop me a line, if you
have any opinions, bug reports, requests, or whatever about this project.  Thanks!

# Build Status

Travis CI says: [![Build Status](https://travis-ci.org/marnix/metamath-eclipse-xtext.svg?branch=master)](https://travis-ci.org/marnix/metamath-eclipse-xtext)

CloudBees says: [![Build Status](https://buildhive.cloudbees.com/job/marnix/job/metamath-eclipse-xtext/badge/icon)](https://buildhive.cloudbees.com/job/marnix/job/metamath-eclipse-xtext/)

# License

The code in this repository is licensed under the Eclipse Public License - v 1.0; see LICENSE.txt for details.

# Installation instructions

To install, point your Eclipse to the following update site (courtesy of the OSS Artifactory repository from JFrog): `jar:https://oss.jfrog.org/artifactory/libs-snapshot/mm/ecxt/mm.ecxt.updatesite/1.0.0-SNAPSHOT/mm.ecxt.updatesite-1.0.0-SNAPSHOT.zip!/` and select the MMLanguage SDK Feature.  (Note that this update site is large, so it might take a while to download and install: it is a single 70+ MB zip-file, which also contains Xtext and all other dependencies.  This should be replaced by a very small update site which points to the Xtext update site.  I don't yet know how to best fight Tycho on this point.)

I have only tested so far with Eclipse Mars R.

*Note about large files.* Before you try to open `set.mm` (which is 20+MB these days), make sure you run your Eclipse with `-Xmx4G` in the proper place in your `eclipse.ini`.  And then it still might hang on you unless you have a fast system.  You also probably want to close the Outline view.

# Acknowledgements

This project would not have been possible without [Xtext](https://www.eclipse.org/Xtext/), which forms the core and basis
of this plug-in.

Support and useful notes came from
[the Xtext documentation](https://www.eclipse.org/Xtext/documentation/),
[Ray Wu's blog posts on Xtext](http://rayjcwu.logdown.com/tags/xtext),
Dave Murray-Rust's blog post [Creating a Syntax Highlighting, Outlining editor with Eclipse and XText](http://www.mo-seph.com/projects/syntaxhighlighting),
[Sebastian Zarnekow's blog posts on Xtext](https://zarnekow.blogspot.com/search/label/Xtext),
[the Xtext community forum](https://www.eclipse.org/forums/index.php/f/27/),
and [Stack Overflow](https://stackoverflow.com/questions/tagged/xtext).

And of course thanks to the Eclipse project for building a great IDE framework!

The starting point was the very helpful Maven-Tycho-Xtext-Xtend project https://github.com/xtext/maven-xtext-example.

The latest builds are always available within minutes of pushing a commit, thanks to GitHub and their free source code storage, TravisCI and their free continuous builds, and JFrog and their free Bintray/Artifactory deployment.

# TODO list

I'd like to extend this project in several areas: here is a somewhat structured and prioritized to-do list.

## Viewing correct and complete files

 - Add syntax highlighting/coloring for variables/constants.
 - Add syntax highlighting/coloring for `` math symbol sequence `` in comments.
 - Support `$[ ... $]` include statements, including references.
   Note that it will be very difficult to allow this in arbitrary places:
   I will only allow it on the 'statement' level, so in any place where a $c/v/f/e/d/a/p statement is allowed.
   Question: How to treat a file that is imported in two otherwise related .mm files,
   possibly in a different 'context'?
 - When showing a tooltip for a label, show the (formatted-as-on-website) comment
   that precedes that label (for $a/$p/$e/$f) or that follows the declaration (for $c/$v).
 - In non-compressed proof formats, highlight the non-syntax steps.
 - Try and render math symbols as Unicode characters, based on set.mm's `$t` info.

## Basic editing

 - Add a 'Toggle Comment' UI action, which replaces `$p` etc. by `@p` etc., or vice versa.
 - Correct auto-closing of `$(` and `${` brackets.

## Proof assistant

 - Create 'refactoring' action to reformat a proof in a 'calculational' format,
   where every 'non-syntax' step is on its own line, the indentation of that
   step is based on the number non-syntax expressions that are left on the
   stack after the step, and the line below the step shows the resulting
   expression in `$( `` ... `` $)` format.
 - Auto-complete a `?` proof step with the applicable steps.
 - When auto-completing `?`, prefer 'windowing' steps which allow 'zooming in'
   on a subexpression, or which allow transitive steps.
 - Add additional proof refactorings, such as changing the order of the hypotheses
   of a statement.
 - For completeness: 
    - Create 'refactoring' action to reformat in compressed format.
    - Create 'refactoring' action to reformat in normal format.
    - Create 'refactoring' action to reformat in 'lemmon' format.
    - Create 'refactoring' action to reformat in 'indented-as-on-website' format.
   
## Detecting errors

 - Bugfix: Don't treat a `?` in a proof as a parse error, and instead generate
   a warning.
 - Properly validate use of whitespace, either in the parser/grammar or separately.
 - Additional validations, like "no `$c` in a nested scope" etc.
 - Validate proofs.  If necessary for performance: only the 'current' proof,
   or only when modifying one.  Or probably better: treat this like unit tests,
   with a separate 'Verify Proofs' command complete with green/red(/yellow) progress indicator.
 - Syntax validation, to make sure that every statement's math string can be
   parsed in exactly one way.
 
## Performance

 - As a performance stop-gap, perhaps we can automatically switch to the normal
   text editor for files that are too large to handle?
 - Make the performance acceptable for `set.mm`, e.g., by using the tips from
   https://www.eclipsecon.org/na2015/sites/default/files/slides/Scaling%20Xtext.pdf
   and http://www.sigasi.com/content/view-complexity-your-xtext-ecore-model
 - Add a refactoring to split a large .mm file using `$[` statements, just like
   https://github.com/sorear/set.mm-history does.
 - Efficient Outline view which is helpful for large files (like `set.mm`).
   (Stop-gap: create a 'not yet implemented' empty outline tree.)
 - Create a small update site which points to the Xtext update site, instead
   of including all of Xtext in our own update site resulting in 70+MB.
 - Support multiple versions of Eclipse.
 
## Clean-up

 - Remove leftovers from the sample project, visible in Preferences (mwe2,
   'compilation' setting).
 - Don't show Xtext-related labels (e.g., in the generated markers).
   Try to replace Xtext nature by a custom Metamath nature.
 - Try to better follow the Maven conventions like using src/main/java, etc.
 - Try to get rid of Xtend, if possible.  (I prefer a pure Java approach, to make
   this project more 'hackable' for people who don't know Xtend, and don't want
   to install the Xtend IDE for developing it.)
 
## Extensibility

 - Define an Eclipse plug-in extension point for injecting custom proof
   engines, like Mario Carneiro's calculator for automatically prove basic
   facts about numbers.
   