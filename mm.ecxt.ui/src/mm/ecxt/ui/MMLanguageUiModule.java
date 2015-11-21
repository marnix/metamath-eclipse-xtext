package mm.ecxt.ui;

import mm.ecxt.ui.highlight.MMHighlightingCalculator;
import mm.ecxt.ui.highlight.MMHighlightingConfiguration;
import mm.ecxt.ui.hyperlinking.MMHyperlinkHelper;
import mm.ecxt.ui.tooltips.MMEObjectDocumentationProvider;
import mm.ecxt.ui.tooltips.MMEObjectHoverProvider;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.documentation.IEObjectDocumentationProvider;
import org.eclipse.xtext.resource.containers.IAllContainersState;
import org.eclipse.xtext.ui.editor.hover.IEObjectHoverProvider;
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkHelper;
import org.eclipse.xtext.ui.editor.model.IResourceForEditorInputFactory;
import org.eclipse.xtext.ui.editor.model.ResourceForIEditorInputFactory;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;
import org.eclipse.xtext.ui.resource.SimpleResourceSetProvider;

import com.google.inject.Provider;

/**
 * Use this class to register components to be used within the IDE.
 */
public class MMLanguageUiModule extends mm.ecxt.ui.AbstractMMLanguageUiModule {
	public MMLanguageUiModule(AbstractUIPlugin plugin) {
		super(plugin);
	}

	@Override
	public Class<? extends IResourceForEditorInputFactory> bindIResourceForEditorInputFactory() {
		return ResourceForIEditorInputFactory.class;
	}

	@Override
	public Class<? extends IResourceSetProvider> bindIResourceSetProvider() {
		return SimpleResourceSetProvider.class;
	}

	@Override
	public Provider<IAllContainersState> provideIAllContainersState() {
		return org.eclipse.xtext.ui.shared.Access.getWorkspaceProjectsState();
	}

	public Class<? extends IHighlightingConfiguration> bindIHighlightingConfiguration() {
		return MMHighlightingConfiguration.class;
	}

	public Class<? extends ISemanticHighlightingCalculator> bindISemanticHighlightingCalculator() {
		return MMHighlightingCalculator.class;
	}

	public Class<? extends IHyperlinkHelper> bindIHyperlinkHelper() {
		return MMHyperlinkHelper.class;
	}

	public Class<? extends IEObjectHoverProvider> bindIEObjectHoverProvider() {
		return MMEObjectHoverProvider.class;
	}

	public Class<? extends IEObjectDocumentationProvider> bindIEObjectDocumentationProviderr() {
		return MMEObjectDocumentationProvider.class;
	}
}
