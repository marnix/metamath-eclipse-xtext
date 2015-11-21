package mm.ecxt.ui.tooltips;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mm.ecxt.mmLanguage.EssentialHypothesisStatement;
import mm.ecxt.mmLanguage.FloatingHypothesisStatement;
import mm.ecxt.mmLanguage.HypothesisStatement;
import mm.ecxt.mmLanguage.LabeledStatement;
import mm.ecxt.scoping.MMHelpers;
import mm.ecxt.ui.MMUiHelpers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.documentation.IEObjectDocumentationProvider;

import com.google.common.html.HtmlEscapers;

public class MMEObjectDocumentationProvider implements IEObjectDocumentationProvider {

	@Override
	public String getDocumentation(EObject o) {
		// TODO: for $a/$p, show its $e hypotheses
		if (o instanceof LabeledStatement) {
			List<HypothesisStatement> l = MMHelpers.getHypothesesFor((LabeledStatement) o);
			if (l == null || l.isEmpty()) {
				return null;
			}

			Collections.sort(l, new Comparator<HypothesisStatement>() {
				@Override
				public int compare(HypothesisStatement h1, HypothesisStatement h2) {
					if (h1 instanceof EssentialHypothesisStatement && h2 instanceof FloatingHypothesisStatement) {
						return -1;
					} else if (h1 instanceof FloatingHypothesisStatement && h2 instanceof EssentialHypothesisStatement) {
						return 1;
					}
					return 0;
				}
			});

			StringBuffer result = new StringBuffer();
			result.append("<ul>");
			for (HypothesisStatement h : l) {
				result.append("<li>");
				String hypDesc = MMUiHelpers.getDescriptionAsHtmlOrNull(h);
				result.append(hypDesc != null ? hypDesc : HtmlEscapers.htmlEscaper().escape(h.toString()));
				result.append("</li>");
			}
			result.append("</ul>");
			return result.toString();
		}
		return null;
	}

}