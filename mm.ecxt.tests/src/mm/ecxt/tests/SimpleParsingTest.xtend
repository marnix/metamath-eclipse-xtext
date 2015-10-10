package mm.ecxt.tests

import com.google.inject.Inject
import java.util.Arrays
import mm.ecxt.MMLanguageInjectorProvider
import mm.ecxt.mmLanguage.Block
import mm.ecxt.mmLanguage.CommentStatement
import mm.ecxt.mmLanguage.FloatingHypothesisStatement
import mm.ecxt.mmLanguage.MMDatabase
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import org.junit.Ignore

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
		assertEquals(Arrays.asList("|-"), (mmDb.statements.get(0) as CommentStatement).symbols)
		assertEquals(Arrays.asList("set", "class"), (mmDb.statements.get(2) as CommentStatement).symbols)
	}
	
	@Test def void testParseOneError() {
		val mmDb = '''
		    $c class $.
		    $c $.
		    $c set $.
		    $c xu $.
		    $v ph b c $.
		    $d a b $.
		    ${
		    	wph $f wff ph $.
		    $}
		'''.parse
		assertNotNull(mmDb)
		val validationErrors = validate(mmDb)
		assertEquals(1, validationErrors.size)
		assertEquals(6, mmDb.statements.size)
		assertEquals("ph", ((mmDb.statements.get(5) as Block).contents.statements.get(0) as FloatingHypothesisStatement).variable.name);
	}
	
	@Ignore
	@Test def void testVarNameWithDots() {
		val mmDb = '''
		    $v .ph $.
		    wph $f wff .ph $.
		'''.parse
		assertNotNull(mmDb)
		assertEquals(2, mmDb.statements.size)
		val validationErrors = validate(mmDb)
		assertEquals(0, validationErrors.size)
		assertEquals(".ph", (mmDb.statements.get(1) as FloatingHypothesisStatement).variable.name);
	}
	
	@Test def void testParseDollarEInBlock() {
		val mmDb = '''
			${ dummylink.1 $e |- ph $. $}
		'''.parse
		assertNotNull(mmDb)
		assertNoErrors(mmDb)
		assertEquals(1, mmDb.statements.size)
	}
}