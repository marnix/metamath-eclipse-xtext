package mm.ecxt.tests

import com.google.inject.Inject
import java.util.Arrays
import mm.ecxt.MMLanguageInjectorProvider
import mm.ecxt.mmLanguage.Block
import mm.ecxt.mmLanguage.ConstantStatement
import mm.ecxt.mmLanguage.FloatingHypothesisStatement
import mm.ecxt.mmLanguage.MMDatabase
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
}