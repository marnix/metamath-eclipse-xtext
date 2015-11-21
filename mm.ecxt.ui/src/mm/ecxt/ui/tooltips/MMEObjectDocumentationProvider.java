package mm.ecxt.ui.tooltips;

import mm.ecxt.mmLanguage.AxiomStatement;
import mm.ecxt.mmLanguage.ProofStatement;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.documentation.IEObjectDocumentationProvider;

public class MMEObjectDocumentationProvider implements IEObjectDocumentationProvider {

	@Override
	public String getDocumentation(EObject o) {
		// TODO: for $a/$p, show its $e hypotheses
		if (o instanceof AxiomStatement || o instanceof ProofStatement) {
			return "TODO: show mandatory hypotheses here, if any...";
		}
		return null;
	}

}