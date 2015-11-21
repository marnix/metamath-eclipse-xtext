package mm.ecxt.ui.contentassist;

import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.xtext.ui.editor.autoedit.AbstractEditStrategyProvider;
import org.eclipse.xtext.ui.editor.autoedit.CompoundMultiLineTerminalsEditStrategy;
import org.eclipse.xtext.ui.editor.autoedit.MultiLineTerminalsEditStrategy;
import org.eclipse.xtext.ui.editor.autoedit.PartitionDeletionEditStrategy;
import org.eclipse.xtext.ui.editor.autoedit.PartitionEndSkippingEditStrategy;
import org.eclipse.xtext.ui.editor.autoedit.PartitionInsertEditStrategy;
import org.eclipse.xtext.ui.editor.autoedit.SingleLineTerminalsStrategy;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class MMAutoEditStrategyProvider extends AbstractEditStrategyProvider {

	@Inject
	protected Provider<DefaultIndentLineAutoEditStrategy> defaultIndentLineAutoEditStrategy;
	@Inject
	protected Provider<PartitionEndSkippingEditStrategy> partitionEndSkippingEditStrategy;
	@Inject
	protected PartitionInsertEditStrategy.Factory partitionInsert;
	@Inject
	protected PartitionDeletionEditStrategy.Factory partitionDeletion;
	@Inject
	protected SingleLineTerminalsStrategy.Factory singleLineTerminals;
	@Inject
	protected MultiLineTerminalsEditStrategy.Factory multiLineTerminals;
	@Inject
	protected CompoundMultiLineTerminalsEditStrategy.Factory compoundMultiLineTerminals;

	@Override
	protected void configure(IEditStrategyAcceptor acceptor) {
		configureMultilineComments(acceptor);
		configureCompoundBracesBlocks(acceptor);
		configureIncludeStatements(acceptor);
		// See DefaultAutoEditStrategyProvider for more inspiration...
	}

	protected void configureCompoundBracesBlocks(IEditStrategyAcceptor acceptor) {
		acceptor.accept(compoundMultiLineTerminals.newInstanceFor("${", "$}"), IDocument.DEFAULT_CONTENT_TYPE);
	}

	protected void configureIncludeStatements(IEditStrategyAcceptor acceptor) {
		acceptor.accept(singleLineTerminals.newInstance("$[", "$]"), IDocument.DEFAULT_CONTENT_TYPE);
	}

	protected void configureMultilineComments(IEditStrategyAcceptor acceptor) {
		acceptor.accept(singleLineTerminals.newInstance("$(", "$)"), IDocument.DEFAULT_CONTENT_TYPE);
	}
}
