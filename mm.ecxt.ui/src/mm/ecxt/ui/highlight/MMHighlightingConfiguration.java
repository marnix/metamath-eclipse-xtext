package mm.ecxt.ui.highlight;

import static org.eclipse.swt.SWT.*;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.editor.utils.TextStyle;

public class MMHighlightingConfiguration implements IHighlightingConfiguration {
	// provide an id string for the highlighting calculator
	public static final String VARIABLE = "Variable";
	public static final String KEYWORD = "Keyword";
	public static final String COMMENT = "Comment";
	public static final String[] ALL_STRINGS = { VARIABLE, KEYWORD, COMMENT };

	// configure the acceptor providing the id, the description string
	// that will appear in the preference page and the initial text style
	@Override
	public void configure(IHighlightingConfigurationAcceptor acceptor) {
		addType(acceptor, VARIABLE, 139, 105, 20, ITALIC);
		addType(acceptor, KEYWORD, 128, 0, 128, NORMAL);
		addType(acceptor, COMMENT, 150, 200, 200, NORMAL);
	}

	public void addType(IHighlightingConfigurationAcceptor acceptor, String s, int r, int g, int b, int style) {
		TextStyle textStyle = new TextStyle();
		textStyle.setBackgroundColor(new RGB(255, 255, 255));
		textStyle.setColor(new RGB(r, g, b));
		textStyle.setStyle(style);
		acceptor.acceptDefaultHighlighting(s, s, textStyle);
	}
}