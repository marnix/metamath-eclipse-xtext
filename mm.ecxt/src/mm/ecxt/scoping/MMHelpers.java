package mm.ecxt.scoping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mm.ecxt.mmLanguage.AxiomStatement;
import mm.ecxt.mmLanguage.ConstDecl;
import mm.ecxt.mmLanguage.Decl;
import mm.ecxt.mmLanguage.EssentialHypothesisStatement;
import mm.ecxt.mmLanguage.FloatingHypothesisStatement;
import mm.ecxt.mmLanguage.HypothesisStatement;
import mm.ecxt.mmLanguage.LabeledStatement;
import mm.ecxt.mmLanguage.MMDatabase;
import mm.ecxt.mmLanguage.ProofStatement;
import mm.ecxt.mmLanguage.VarDecl;
import mm.ecxt.mmLanguage.util.MmLanguageSwitch;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

public class MMHelpers {

	public static List<HypothesisStatement> getHypothesesFor(LabeledStatement stat) {
		if (!(stat instanceof AxiomStatement || stat instanceof ProofStatement)) {
			return Collections.emptyList();
		}
		List<Decl> statSymbols = getSymbols(stat);
		if (statSymbols == null) {
			return Collections.emptyList();
		}

		Map<String, FloatingHypothesisStatement> fHyps = new HashMap<>();

		List<HypothesisStatement> result = new ArrayList<>();
		for (TreeIterator<EObject> allContents = EcoreUtil.getRootContainer(stat).eAllContents(); allContents.hasNext();) {
			EObject object = allContents.next();
			if (!textuallyOccursBefore(object, stat)) {
				break;
			}
			if (!HypothesisStatement.class.isAssignableFrom(object.getClass())) {
				continue;
			}
			HypothesisStatement hyp = (HypothesisStatement) object;
			if (!isValidReferenceAccordingToScopingRules(stat, hyp)) {
				continue;
			}
			if (hyp instanceof FloatingHypothesisStatement) {
				FloatingHypothesisStatement fHyp = (FloatingHypothesisStatement) hyp;
				fHyps.put(fHyp.getVariable().getName(), fHyp);
			} else {
				result.add(hyp);
				addFHypsForVars(result, hyp, fHyps);
			}
		}

		Collections.sort(result, new Comparator<EObject>() {
			@Override
			public int compare(EObject h1, EObject h2) {
				if (h1.equals(h2)) {
					return 0;
				} else if (MMHelpers.textuallyOccursBefore(h1, h2)) {
					return -1;
				} else {
					return 1;
				}
			}
		});

		return result;
	}

	private static void addFHypsForVars(List<HypothesisStatement> result, HypothesisStatement hyp,
			Map<String, FloatingHypothesisStatement> fHyps) {
		for (Decl x : getSymbols(hyp)) {
			if (x instanceof VarDecl) {
				VarDecl v = (VarDecl) x;
				FloatingHypothesisStatement fHypForV = fHyps.get(v.getName());
				if (fHypForV != null && !result.contains(fHypForV)) {
					result.add(fHypForV);
				}
			}
		}
	}

	private static List<Decl> getSymbols(LabeledStatement stat) {
		return new MmLanguageSwitch<List<Decl>>() {
			@Override
			public List<Decl> caseAxiomStatement(AxiomStatement object) {
				return object.getSymbols();
			};

			@Override
			public List<Decl> caseProofStatement(ProofStatement object) {
				return object.getSymbols();
			};

			@Override
			public List<Decl> caseEssentialHypothesisStatement(EssentialHypothesisStatement object) {
				return object.getSymbols();
			};

			@Override
			public List<Decl> defaultCase(EObject object) {
				return null;
			};
		}.doSwitch(stat);
	}

	public static boolean isValidReferenceAccordingToScopingRules(EObject source, EObject target) {
		if (target.eContainer() == null) {
			// TODO: Implement references to $[ ... $] included file
			return false;
		}
		boolean targetVisibleAfterBlock = target instanceof ConstDecl || target instanceof AxiomStatement
				|| target instanceof ProofStatement;
		if (!targetVisibleAfterBlock) {
			// First we make sure that the source is in the target's block
			EObject targetBlock = target instanceof VarDecl ? target.eContainer().eContainer() : target.eContainer();
			if (!(targetBlock instanceof MMDatabase)) {
				// this should not occur, but for robustness we'll stop here
				return false;
			}
			if (!contains(targetBlock, source)) {
				return false;
			}
		}
		return textuallyOccursBefore(target, source);
	}

	public static boolean textuallyOccursBefore(EObject x, EObject y) {
		return startOffsetOf(x) < startOffsetOf(y);
	}

	private static int startOffsetOf(EObject x) {
		return NodeModelUtils.getNode(x).getTotalOffset();
	}

	private static boolean contains(EObject container, EObject obj) {
		return obj != null && (obj.equals(container) || contains(container, obj.eContainer()));
	}
}
