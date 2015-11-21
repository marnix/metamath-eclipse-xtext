package mm.ecxt.ui;

import java.util.Collections;

import mm.ecxt.mmLanguage.AxiomStatement;
import mm.ecxt.mmLanguage.ConstDecl;
import mm.ecxt.mmLanguage.Decl;
import mm.ecxt.mmLanguage.EssentialHypothesisStatement;
import mm.ecxt.mmLanguage.FloatingHypothesisStatement;
import mm.ecxt.mmLanguage.ProofStatement;
import mm.ecxt.mmLanguage.VarDecl;
import mm.ecxt.ui.tooltips.MMEObjectHoverProvider;

import org.eclipse.emf.ecore.EObject;

public class MMUiHelpers {

	public static String getDescriptionAsHtmlOrNull(EObject o) {
		if (o instanceof ConstDecl) {
			return "Constant <code>$c " + MMEObjectHoverProvider.toMathStringHtml(Collections.singletonList((Decl) o)) + " $.</code>";
		} else if (o instanceof VarDecl) {
			return "Variable <code>$c " + MMEObjectHoverProvider.toMathStringHtml(Collections.singletonList((Decl) o)) + " $.</code>";
		} else if (o instanceof AxiomStatement) {
			AxiomStatement a = (AxiomStatement) o;
			return "Axiom <code>" + a.getName() + " $a " + MMEObjectHoverProvider.toMathStringHtml(a.getSymbols()).toString() + " $.</code>";
		} else if (o instanceof ProofStatement) {
			ProofStatement p = (ProofStatement) o;
			return "Theorem <code>" + p.getName() + " $p " + MMEObjectHoverProvider.toMathStringHtml(p.getSymbols()).toString() + " $= ... $.</code>";
		} else if (o instanceof EssentialHypothesisStatement) {
			EssentialHypothesisStatement e = (EssentialHypothesisStatement) o;
			return "Assumption <code>" + e.getName() + " $e " + MMEObjectHoverProvider.toMathStringHtml(e.getSymbols()).toString() + " $.</code>";
		} else if (o instanceof FloatingHypothesisStatement) {
			FloatingHypothesisStatement f = (FloatingHypothesisStatement) o;
			return "Declaration <code>" + f.getName() + " $f " + f.getConstant().getName() + " " + f.getVariable().getName()
					+ " $.</code>";
		}
		return null;
	}

}
