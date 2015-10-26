package mm.ecxt.ui.highlight;

import static mm.ecxt.ui.highlight.MMHighlightingConfiguration.*;

import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.TerminalRule;
import org.eclipse.xtext.impl.TerminalRuleImpl;
import org.eclipse.xtext.nodemodel.BidiIterator;
import org.eclipse.xtext.nodemodel.BidiTreeIterator;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.impl.HiddenLeafNode;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultSemanticHighlightingCalculator;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;

public class MMHighlightingCalculator extends DefaultSemanticHighlightingCalculator {

	@Override
	public void provideHighlightingFor(XtextResource resource, IHighlightedPositionAcceptor acceptor) {
		if (resource == null || resource.getParseResult() == null)
			return;

		INode root = resource.getParseResult().getRootNode();
		BidiTreeIterator<INode> it = root.getAsTreeIterable().iterator();
		while (it.hasNext()) {
			INode node = it.next();
			// for more highlighting inspiration, see
			// http://www.mo-seph.com/projects/syntaxhighlighting
			boolean highlighted = false;
			if (node instanceof HiddenLeafNode && node.getGrammarElement() instanceof TerminalRule) {
				TerminalRule tr = (TerminalRule) node.getGrammarElement();
				if ("COMMENT".equals(tr.getName())) {
					acceptor.addPosition(node.getOffset(), node.getLength(), COMMENT);
					highlighted = true;
				}
			} else if (node.getGrammarElement() instanceof RuleCall) {
				RuleCall rc = (RuleCall) node.getGrammarElement();
				if (rc.getRule() instanceof TerminalRule && rc.getRule().getName().startsWith("DOLLAR_")) {
					acceptor.addPosition(node.getOffset(), node.getLength(), KEYWORD);
					highlighted = true;
					// } else {
					// System.err.println("RULE: " + rc.getRule());
				}
				// } else if (node.getSemanticElement() instanceof LabeledStatement) {
				// LabeledStatement ls = (LabeledStatement) node.getSemanticElement();
			}
			if (!highlighted) {
				// System.err.println("Node not highlighted: " + node.getClass().getSimpleName() + " "
				// + node.getGrammarElement().getClass().getSimpleName() + ": '" + node.getText() + "'");
			}
		}
	}

	void setStyles(IHighlightedPositionAcceptor acceptor, BidiIterator<INode> it, String... styles) {
		for (String s : styles) {
			if (!it.hasNext())
				return;
			INode n = skipWhiteSpace(acceptor, it);
			if (n != null && s != null)
				acceptor.addPosition(n.getOffset(), n.getLength(), s);
		}
	}

	INode skipWhiteSpace(IHighlightedPositionAcceptor acceptor, BidiIterator<INode> it) {
		INode n = null;
		while (it.hasNext() && (n = it.next()).getClass() == HiddenLeafNode.class)
			processHiddenNode(acceptor, (HiddenLeafNode) n);
		return n;
	}

	INode skipWhiteSpaceBackwards(IHighlightedPositionAcceptor acceptor, BidiIterator<INode> it) {
		INode n = null;
		while (it.hasPrevious() && (n = it.previous()).getClass() == HiddenLeafNode.class)
			processHiddenNode(acceptor, (HiddenLeafNode) n);
		return n;
	}

	void processHiddenNode(IHighlightedPositionAcceptor acceptor, HiddenLeafNode node) {
		if (node.getGrammarElement() instanceof TerminalRuleImpl) {
			TerminalRuleImpl ge = (TerminalRuleImpl) node.getGrammarElement();
			if (ge.getName().equals("COMMENT"))
				acceptor.addPosition(node.getOffset(), node.getLength(), COMMENT);
		}

	}
}