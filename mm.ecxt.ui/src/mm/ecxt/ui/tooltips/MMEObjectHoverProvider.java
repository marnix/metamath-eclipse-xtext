package mm.ecxt.ui.tooltips;

import java.util.Collection;

import mm.ecxt.mmLanguage.Decl;
import mm.ecxt.mmLanguage.VarDecl;
import mm.ecxt.ui.MMUiHelpers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.hover.html.DefaultEObjectHoverProvider;

import com.google.common.html.HtmlEscapers;

public class MMEObjectHoverProvider extends DefaultEObjectHoverProvider {

	@Override
	protected String getFirstLine(EObject o) {
		String r = MMUiHelpers.getDescriptionAsHtmlOrNull(o);
		return r != null ? r : super.getFirstLine(o);
	}

	public static String toMathStringHtml(Collection<Decl> symbols) {
		StringBuffer result = new StringBuffer();
		String separator = "";
		for (Decl d : symbols) {
			result.append(separator);
			if (d instanceof VarDecl) {
				result.append("<i>");
			}
			result.append(HtmlEscapers.htmlEscaper().escape(d.getName()));
			if (d instanceof VarDecl) {
				result.append("</i>");
			}
			separator = " ";
		}
		return result.toString();
	}
}