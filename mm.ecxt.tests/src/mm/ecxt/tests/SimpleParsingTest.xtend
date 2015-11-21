package mm.ecxt.tests

import com.google.inject.Inject
import java.util.Arrays
import mm.ecxt.MMLanguageInjectorProvider
import mm.ecxt.mmLanguage.Block
import mm.ecxt.mmLanguage.CompressedProof
import mm.ecxt.mmLanguage.ConstantStatement
import mm.ecxt.mmLanguage.FloatingHypothesisStatement
import mm.ecxt.mmLanguage.LabeledStatement
import mm.ecxt.mmLanguage.MMDatabase
import mm.ecxt.mmLanguage.ProofStatement
import mm.ecxt.scoping.MMHelpers
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(MMLanguageInjectorProvider)
class SimpleParsingTest {

	@Inject extension ParseHelper<MMDatabase>
	@Inject extension ValidationTestHelper

	@Test def void testParse() {
		val mmDb = '''
			$c |- $. $( a comment! $)
			$c wff $.
			$c set class $.
		'''.parse
		assertNotNull(mmDb)
		assertNoErrors(mmDb)
		assertEquals(3, mmDb.statements.size)
		assertEquals(Arrays.asList("|-"), (mmDb.statements.get(0) as ConstantStatement).constants.map([c|c.name]))
		assertEquals(Arrays.asList("set", "class"), (mmDb.statements.get(2) as ConstantStatement).constants.map([ c |
			c.name
		]))
	}

	@Test def void testParseOneError() {
		val mmDb = '''
			$c class $.
			$c $.
			$c set $.
			$c wff $.
			$v ph b c $.
			$d b ph $.
			${
				wph $f wff ph $.
			$}
		'''.parse
		assertNotNull(mmDb)
		val validationErrors = validate(mmDb)
		assertEquals(1, validationErrors.size)
		assertEquals(6, mmDb.statements.size)
		assertEquals("ph",
			((mmDb.statements.get(5) as Block).contents.statements.get(0) as FloatingHypothesisStatement).variable.
				name);
			}

			@Test def void testVarNameStartingWithDot() {
				val mmDb = '''
					$c wff $.
					$v .ph $.
					wph $f wff .ph $.
				'''.parse
				assertNotNull(mmDb)
				assertEquals(3, mmDb.statements.size)
				val validationErrors = validate(mmDb)
				assertEquals(0, validationErrors.size)
				assertEquals(".ph", (mmDb.statements.get(2) as FloatingHypothesisStatement).variable.name);
			}

			@Test def void testVarNameEndingWithDot() {
				val mmDb = '''
					$c wff $.
					$v ph $.
					wph $f wff ph. $.
				'''.parse
				assertNotNull(mmDb)
				assertEquals(3, mmDb.statements.size)
				val validationErrors = validate(mmDb)
				assertEquals(1, validationErrors.size)
				assertNull((mmDb.statements.get(2) as FloatingHypothesisStatement).variable.name);
			}

			@Test def void testParseDollarEInBlock() {
				val mmDb = '''
					$c |- $.
					$v ph $.
					${ dummylink.1 $e |- ph $. $}
				'''.parse
				assertNotNull(mmDb)
				assertNoErrors(mmDb)
				assertEquals(3, mmDb.statements.size)
			}

			@Test def void testNoForwardConstantAndVariableReferences() {
				val mmDb = '''
					${ dummylink.1 $e |- ph $. $}
					$c |- $.
					$v ph $.
				'''.parse
				assertNotNull(mmDb)
				val validationErrors = validate(mmDb)
				assertEquals(2, validationErrors.size)
				assertEquals(3, mmDb.statements.size)
			}

			@Test def void testReferenceToAxiomOrProofInNestedScope() {
				val mmDb = '''
					$c wff |- true $.
					$v ph $.
					wph $f wff ph $.
					wtrue $a wff true $.
					${
						axiom $a |- true $.
						${
							theorem.1 $e |- ph $.
							theorem $p |- ph $= theorem.1 $.
						$}
					$}
					x $p |- true $= wtrue axiom theorem $.
				'''.parse
				assertNotNull(mmDb)
				val validationErrors = validate(mmDb)
				assertEquals(0, validationErrors.size)
				assertEquals(6, mmDb.statements.size)
			}

			@Test def void noReferenceToHypothesisInNestedScope() {
				val mmDb = '''
					$c wff |- true $.
					$v ph $.
					wph $f wff ph $.
					wtrue $a wff true $.
					${
						axiom $a |- true $.
						${
							theorem.1 $e |- ph $.
						$}
						${
							theorem $p |- ph $= theorem.1 $.
						$}
					$}
					x $p |- true $= wtrue axiom theorem $.
				'''.parse
				assertNotNull(mmDb)
				val validationErrors = validate(mmDb)
				assertEquals(1, validationErrors.size)
				assertEquals(6, mmDb.statements.size)
			}

			@Test def void testCompressedToNormalProof() {
				val mmDb = '''
					$c wff |- true $.
					$v ph $.
					wph $f wff ph $.
					wtrue $a wff true $.
					${
						axiom $a |- true $.
						${
							theorem.1 $e |- ph $.
							theorem $p |- ph $= theorem.1 $.
						$}
					$}
					x $p |- true $= ( wtrue axiom theorem ) A BC $.
				'''.parse
				assertNotNull(mmDb)
				val validationErrors = validate(mmDb)
				assertEquals(0, validationErrors.size)
				val cp = ((mmDb.statements.get(5) as ProofStatement).proof as CompressedProof)
				val encodedNumberList = cp.parts.join
				assertEquals("ABC", encodedNumberList)
			}

			@Test def void testFindMandatoryHypotheses() {
				val mmDb = '''
					$c wff |- true $.
					$v ph $.
					wph $f wff ph $.
					wtrue $a wff true $.
					${
						axiom $a |- true $.
						${
							$v ps $.
							unused.dummy $e wff ps $.
							theorem.1 $e |- ph $.
							theorem $p |- ph $= theorem.1 $.
						$}
					$}
					x $p |- true $= wph wtrue axiom theorem $.
				'''.parse
				assertNotNull(mmDb)
				val validationErrors = validate(mmDb)
				assertEquals(0, validationErrors.size)

				val wph = mmDb.statements.get(2) as LabeledStatement
				assertEquals("wph", wph.name)
				val unusedDummy = ((mmDb.statements.get(4) as Block).contents.statements.get(1) as Block).contents.
					statements.get(1) as LabeledStatement
				assertEquals("unused.dummy", unusedDummy.name)
				val theorem1 = ((mmDb.statements.get(4) as Block).contents.statements.get(1) as Block).contents.
					statements.get(2) as LabeledStatement
				assertEquals("theorem.1", theorem1.name)
				val theorem = ((mmDb.statements.get(4) as Block).contents.statements.get(1) as Block).contents.
					statements.get(3) as LabeledStatement
				assertEquals("theorem", theorem.name)
				val x = mmDb.statements.get(5) as LabeledStatement
				assertEquals("x", x.name)

				assertEquals(Arrays.asList(), MMHelpers.getHypothesesFor(theorem1))
				assertEquals(Arrays.asList(wph, unusedDummy, theorem1), MMHelpers.getHypothesesFor(theorem))
				assertEquals(Arrays.asList(), MMHelpers.getHypothesesFor(x))
			}

		}