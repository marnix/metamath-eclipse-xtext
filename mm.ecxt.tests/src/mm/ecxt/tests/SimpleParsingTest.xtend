package mm.ecxt.tests

import com.google.inject.Inject
import mm.ecxt.MMLanguageInjectorProvider
import mm.ecxt.mmLanguage.MetamathDatabase
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(MMLanguageInjectorProvider)
class SimpleParsingTest {
	
	@Inject extension ParseHelper<MetamathDatabase>
	
	@Test def void testParse() {
		val mmDb = '''
			$c |- $.
			$c wff $.
		'''.parse
		assertEquals(2, mmDb.statements.size)
	}
}