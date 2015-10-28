package mm.ecxt.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mm.ecxt.mmLanguage.LabeledStatement;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.Region;
import org.eclipse.xtext.TerminalRule;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.hyperlinking.HyperlinkHelper;
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkAcceptor;
import org.eclipse.xtext.ui.search.IXtextEObjectSearch;

import com.google.inject.Inject;

public class MMHyperlinkHelper extends HyperlinkHelper {

	private static final Pattern PATTERN_INSIDE_TILDE_LINK = Pattern.compile("(.*\\s+\\~\\s+)[-A-Za-z0-9_.]*", Pattern.DOTALL);
	private static final Pattern PATTERN_INITIAL_MATHSYMBOL = Pattern.compile("([-A-Za-z0-9_.]+).*", Pattern.DOTALL);

	@Inject
	private IXtextEObjectSearch search;

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

		EObject target = null;
		for (IEObjectDescription eObjDesc : search.findMatches(mathSymbol, "")) {
			EObject obj = eObjDesc.getEObjectOrProxy();
			if (!mathSymbol.equals(eObjDesc.getName().toString())) {
				// findMatches found a substring, but not the exact mathSymbol we're looking for
				continue;
			}
			if (!(obj instanceof LabeledStatement)) {
				// ~ mathSymbol referred to something, but not a statement label: skip
				continue;
			}
			target = obj;
		}
		if (target == null) {
			return;
		}
		// we found a statement with the desired label

		Region region = new Region(leaf.getOffset() + offsetInComment, mathSymbol.length());
		createHyperlinksTo(resource, region, target, acceptor);
	}
}
