package mm.ecxt.ui.tooltips;

import java.util.Collection;
import java.util.Collections;

import mm.ecxt.mmLanguage.AxiomStatement;
import mm.ecxt.mmLanguage.ConstDecl;
import mm.ecxt.mmLanguage.Decl;
import mm.ecxt.mmLanguage.EssentialHypothesisStatement;
import mm.ecxt.mmLanguage.FloatingHypothesisStatement;
import mm.ecxt.mmLanguage.ProofStatement;
import mm.ecxt.mmLanguage.VarDecl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.hover.html.DefaultEObjectHoverProvider;

import com.google.common.html.HtmlEscapers;

public class MMEObjectHoverProvider extends DefaultEObjectHoverProvider {

	@Override
	protected String getFirstLine(EObject o) {
		if (o instanceof ConstDecl) {
			return "Constant <code>$c " + toMathStringHtml(Collections.singletonList((Decl) o)) + " $.</code>";
		} else if (o instanceof VarDecl) {
			return "Variable <code>$c " + toMathStringHtml(Collections.singletonList((Decl) o)) + " $.</code>";
		} else if (o instanceof AxiomStatement) {
			AxiomStatement a = (AxiomStatement) o;
			return "Axiom <code>" + a.getName() + " $a " + toMathStringHtml(a.getSymbols()).toString() + " $.</code>";
		} else if (o instanceof ProofStatement) {
			ProofStatement p = (ProofStatement) o;
			return "Theorem <code>" + p.getName() + " $p " + toMathStringHtml(p.getSymbols()).toString() + " $= ... $.</code>";
		} else if (o instanceof EssentialHypothesisStatement) {
			EssentialHypothesisStatement e = (EssentialHypothesisStatement) o;
			return "Assumption <code>" + e.getName() + " $e " + toMathStringHtml(e.getSymbols()).toString() + " $.</code>";
		} else if (o instanceof FloatingHypothesisStatement) {
			FloatingHypothesisStatement f = (FloatingHypothesisStatement) o;
			return "Declaration <code>" + f.getName() + " $f " + f.getConstant().getName() + " " + f.getVariable().getName()
					+ " $.</code>";
		}
		return super.getFirstLine(o);
	}

	private static String toMathStringHtml(Collection<Decl> symbols) {
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