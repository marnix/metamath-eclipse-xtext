package mm.ecxt.tests

import java.util.Arrays
import mm.ecxt.core.CompressedProofHelper
import org.junit.Test

import static org.junit.Assert.*

class CompressedProofTest {

	@Test def void testDecode() {
		assertEquals(Arrays.asList(1, 2, 3), CompressedProofHelper.decode("ABC"))
		assertEquals(Arrays.asList(1, -2, 3), CompressedProofHelper.decode("ABZC"))
		assertEquals(Arrays.asList(1, 2, 20, 21, 22, -40, 41, 0, 42, 120, -121, 620, 621),
			CompressedProofHelper.decode("ABTUAUBUTZVA?VBYTUUAZYYTUUUA"))
	}

}