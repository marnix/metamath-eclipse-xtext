package mm.ecxt.core;

import java.util.ArrayList;
import java.util.List;

public class CompressedProofHelper {
	public static List<Integer> decode(String encodedNumberString) {
		List<Integer> result = new ArrayList<>();
		int n = 0;
		for (char c : encodedNumberString.toCharArray()) {
			if ('U' <= c && c <= 'Y') {
				n = n * 5 + (c - 'U' + 1);
			} else if ('A' <= c && c <= 'T') {
				n = n * 20 + (c - 'A' + 1);
				result.add(n);
				n = 0;
			} else if (c == 'Z' && n == 0 && !result.isEmpty()) {
				int i = result.size() - 1;
				result.set(i, -result.get(i));
			} else if (c == '?' && n == 0) {
				result.add(0);
			} else {
				throw new IllegalArgumentException(
						"unexpected character '" + c + "' in encoded string '" + encodedNumberString + "'");
			}
		}
		return result;
	}
}
