/*
 * generated by Xtext
 */
package mm.ecxt;

import mm.ecxt.scoping.SingleElementQualifiedNameConverter;

import org.eclipse.xtext.naming.IQualifiedNameConverter;

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
public class MMLanguageRuntimeModule extends mm.ecxt.AbstractMMLanguageRuntimeModule {

	public Class<? extends IQualifiedNameConverter> bindIQualifiedNameConverter() {
		return SingleElementQualifiedNameConverter.class;
	}
}
