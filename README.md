Metamath plugin for Eclipse based on Xtext
==========================================

# Goal

My goal is to be able to edit Metamath `.mm` files in an Eclipse-based editor,
with proof assistance and everything one needs to create Metamath proofs.
Hopefully most of the work can be done in the editor, in keeping with
Metamath's philosophy of treating the .mm file as source code.

# Build Status

Travis CI says: [![Build Status](https://travis-ci.org/marnix/metamath-eclipse-xtext.svg?branch=master)](https://travis-ci.org/marnix/metamath-eclipse-xtext)

CloudBees says: [![Build Status](https://buildhive.cloudbees.com/job/marnix/job/metamath-eclipse-xtext/badge/icon)](https://buildhive.cloudbees.com/job/marnix/job/metamath-eclipse-xtext/)

# LICENSE

The code in this repository is licensed under the Eclipse Public License - v 1.0; see LICENSE.md for details.

# Installation instructions

*Please note that this project is in the very early stages, and doesn't do much yet.*  However, even if I don't find the time to continue on it, hopefully someone else might pick it up as a useful starting point...

To install, point your Eclipse to the following update site (courtesy of the OSS Artifactory repository from JFrog): `jar:https://oss.jfrog.org/artifactory/libs-snapshot/mm/ecxt/mm.ecxt.updatesite/1.0.0-SNAPSHOT/mm.ecxt.updatesite-1.0.0-SNAPSHOT.zip!/` and select the MMLanguage SDK Feature.  (Note that this update site is large, so it might take a while to download and install: it is a single 70+ MB zip-file, which also contains Xtext and all other dependencies.  This should be replaced by a very small update site which points to the Xtext update site.  I don't yet know how to best fight Tycho on this point.)

I have only tested so far with Eclipse Mars R.

*Note about large files.* Before you try to open `set.mm` (which is 20+MB these days), make sure you run your Eclipse with `-Xmx4G` in the proper place in your `eclipse.ini`.  And then it still might hang on you unless you have a fast system.  You also probably want to close the Outline view.

# TODO list

For now in random order.

 - Add syntax highlighting/coloring for keywords.
 - Add syntax highlighting/coloring for `` math symbol sequence `` in comments.
 - Create 'refactoring' action to reformat a proof in a 'calculational' format,
   where every 'non-syntax' step is on its own line, the indentation of that
   step is based on the number non-syntax expressions that are left on the
   stack after the step, and the line below the step shows the resulting
   expression in `$( `` ... `` $)` format.
 - Create 'refactoring' action to reformat in compressed format.
 - Create 'refactoring' action to reformat in normal format.
 - Create 'refactoring' action to reformat in 'lemmon' format.
 - Create 'refactoring' action to reformat in 'indented-as-on-website' format.
 - Auto-complete a `?` proof step with the applicable steps.
 - When auto-completing `?`, prefer 'windowing' steps which allow 'zooming in'
   on a subexpression, or which allow transitive steps.
 - Make everything clickable / reference.
 - Bugfix: allow only backwards references.
 - Support `$[ ... $]` include statements, including references.
 - Create a 'rename' refactoring.
 - Add additional proof refactorings, such as changing the order of the hypotheses
   of a statement.
 - Fix the names-with-dots bug (e.g., a variable names `./\` should be allowed).
 - Properly validate use of whitespace, either in the parser/grammar or separately.
 - Additional validations, like "no `$c` in a nested scope" etc.
 - Validate proofs.  (If necessary for performance: only the 'current' proof,
   or only when modifying one.)
 - Try and render math symbols as Unicode characters, based on set.mm's `$t` info.
 - Define an Eclipse plug-in extension point for injecting custom proof
   engines, like Mario Carneiro's calculator for automatically prove basic
   facts about numbers.
 - Efficient Outline view which is helpful for large files (like `set.mm`).
 - Syntax validation, to make sure that every statement's math string can be
   parsed in exactly one way.
 - Create a small update site which points to the Xtext update site, instead
   of including all of Xtext in our own update site resulting in 70+MB.
 - _Undoubtedly there is more..._
