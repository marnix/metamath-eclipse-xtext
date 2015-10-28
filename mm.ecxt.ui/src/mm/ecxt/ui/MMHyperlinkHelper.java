package mm.ecxt.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mm.ecxt.mmLanguage.LabeledStatement;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.Region;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.TerminalRule;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.scoping.IScopeProvider;
import org.eclipse.xtext.ui.editor.hyperlinking.HyperlinkHelper;
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkAcceptor;

import com.google.inject.Inject;

public class MMHyperlinkHelper extends HyperlinkHelper {

	private static final Pattern PATTERN_INSIDE_TILDE_LINK = Pattern.compile("(.*\\s+\\~\\s+)[-A-Za-z0-9_.]*", Pattern.DOTALL);
	private static final Pattern PATTERN_INITIAL_MATHSYMBOL = Pattern.compile("([-A-Za-z0-9_.]+).*", Pattern.DOTALL);

	@Inject
	private IScopeProvider scopeProvider;

	@Override
	public void createHyperlinksByOffset(XtextResource resource, int offset, IHyperlinkAcceptor acceptor) {
		super.createHyperlinksByOffset(resource, offset, acceptor);

		ILeafNode leaf = NodeModelUtils.findLeafNodeAtOffset(resource.getParseResult().getRootNode(), offset);
		if (!(leaf.getGrammarElement() instanceof TerminalRule)
				|| !"COMMENT".equals(((TerminalRule) leaf.getGrammarElement()).getName())) {
			return;
		}
		// we are in a comment

		String commentText = leaf.getText();
		int offsetInComment = offset - leaf.getTotalOffset();
		Matcher m = PATTERN_INSIDE_TILDE_LINK.matcher(commentText.substring(0, offsetInComment));
		if (!m.matches()) {
			return;
		}
		// we are at a 'tilde' link

		offsetInComment = m.group(1).length();
		String commentPartAfterTilde = commentText.substring(offsetInComment);
		m = PATTERN_INITIAL_MATHSYMBOL.matcher(commentPartAfterTilde);
		if (!m.matches()) {
			return;
		}
		String mathSymbol = m.group(1);
		// we found a math symbol after the tilde

		// TODO: Replace the following label search by a lookup into some kind of index
		// I have no idea yet how to do such index creation/update for an Ecore or an Xtext-based Ecore model.
		EObject target = null;
		for (LabeledStatement ls : EcoreUtil2.getAllContentsOfType(resource.getParseResult().getRootASTElement(),
				LabeledStatement.class)) {
			if (mathSymbol.equals(ls.getName())) {
				target = ls;
				break;
			}
		}
		if (target == null) {
			return;
		}
		// we found a statement with the desired label

		Region region = new Region(leaf.getOffset() + offsetInComment, mathSymbol.length());
		createHyperlinksTo(resource, region, target, acceptor);
	}
}
