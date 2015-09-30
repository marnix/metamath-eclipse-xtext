package mm.ecxt.tests

import com.google.inject.Inject
import mm.ecxt.MMLanguageInjectorProvider
import mm.ecxt.mmLanguage.MMDatabase
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import java.util.Arrays

@RunWith(XtextRunner)
@InjectWith(MMLanguageInjectorProvider)
class SimpleParsingTest {
	
	@Inject extension ParseHelper<MMDatabase>
	@Inject extension ValidationTestHelper
	
	@Test def void testParse() {
		val mmDb = '''
			$c |- $.
			$c wff $.
			$c set class $.
		'''.parse
		assertNotNull(mmDb)
		assertNoErrors(mmDb)
		assertEquals(3, mmDb.statements.size)
		assertEquals(Arrays.asList("|- "), mmDb.statements.get(0).symbols)
		assertEquals(Arrays.asList("set ", "class "), mmDb.statements.get(2).symbols)
	}
	
	@Test def void testParseOneError() {
		val mmDb = '''
		    $c class $.
		    $c $.
		    $c set $.
		    $c xu $.
		'''.parse
		assertNotNull(mmDb)
		assertEquals(1, validate(mmDb).size)
		assertEquals(3, mmDb.statements.size)
	}
}