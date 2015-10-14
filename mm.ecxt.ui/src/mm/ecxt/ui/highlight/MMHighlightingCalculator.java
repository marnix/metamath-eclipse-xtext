package mm.ecxt.ui.highlight;

import static mm.ecxt.ui.highlight.MMHighlightingConfiguration.COMMENT;
import static mm.ecxt.ui.highlight.MMHighlightingConfiguration.KEYWORD;
import static mm.ecxt.ui.highlight.MMHighlightingConfiguration.VARIABLE;

import org.eclipse.xtext.impl.RuleCallImpl;
import org.eclipse.xtext.impl.TerminalRuleImpl;
import org.eclipse.xtext.nodemodel.BidiIterator;
import org.eclipse.xtext.nodemodel.BidiTreeIterator;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.impl.HiddenLeafNode;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;

import mm.ecxt.mmLanguage.VarDecl;

public class MMHighlightingCalculator implements ISemanticHighlightingCalculator {

	public void provideHighlightingFor(XtextResource resource, IHighlightedPositionAcceptor acceptor) {
		if (resource == null || resource.getParseResult() == null)
			return;

		INode root = resource.getParseResult().getRootNode();
		BidiTreeIterator<INode> it = root.getAsTreeIterable().iterator();
		while (it.hasNext()) {
			INode node = it.next();
			// for more highlighting inspiration, see
			// http://www.mo-seph.com/projects/syntaxhighlighting
			if (node.getSemanticElement() instanceof VarDecl) {
				setStyles(acceptor, it, VARIABLE);
			} else if (node instanceof HiddenLeafNode && node.getGrammarElement() instanceof TerminalRuleImpl) {
				TerminalRuleImpl ge = (TerminalRuleImpl) node.getGrammarElement();
				if (ge.getName().equals("COMMENT")) {
					acceptor.addPosition(node.getOffset(), node.getLength(), COMMENT);
				}
			} else if (node.getGrammarElement() instanceof RuleCallImpl) {
				RuleCallImpl rc = (RuleCallImpl) node.getGrammarElement();
				if (rc.getRule() instanceof TerminalRuleImpl) {
					TerminalRuleImpl ge = (TerminalRuleImpl) rc.getRule();
					if (ge.getName().startsWith("DOLLAR_")) {
						acceptor.addPosition(node.getOffset(), node.getLength(), KEYWORD);
					}
				}
			}
			// else
			// System.err.println( "Node: " + node.getClass().getSimpleName() +
			// " " + node.getGrammarElement().getClass().getSimpleName() );
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