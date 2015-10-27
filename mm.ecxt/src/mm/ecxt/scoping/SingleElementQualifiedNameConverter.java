package mm.ecxt.scoping;

import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;

import com.google.inject.Singleton;

@Singleton
public class SingleElementQualifiedNameConverter implements IQualifiedNameConverter {

	@Override
	public String toString(QualifiedName name) {
		return name.toString("-UNUSED-DELIMITER-");
	}

	@Override
	public QualifiedName toQualifiedName(String qualifiedNameAsText) {
		return QualifiedName.create(qualifiedNameAsText);
	}

}