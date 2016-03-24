package niteshParser;

import niteshParser.creategrammar.ExtendItem;
import niteshParser.creategrammar.ImplementInterfaceItem;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class UMLClass {
	static void classorinstance(creategrammar parser1) {
		if (parser1.isInterfaceClassVisitor) {
		    parser1.GN.add(parser1.nameClassVisitor);
		} 
		else {
		    parser1.CN.add(parser1.nameClassVisitor);
		}

		if (parser1.extendClassVisitor != null) {
		    for (ClassOrInterfaceType item : parser1.extendClassVisitor) {
		        creategrammar.ExtendItem extendItem = parser1.new ExtendItem();
		        extendItem.subClassName = parser1.nameClassVisitor;
		        extendItem.superClassName = item.getName();
		        parser1.extlist.add(extendItem);
		    }
		}

		if (parser1.implementClassVisitor != null) {
		    for (ClassOrInterfaceType item : parser1.implementClassVisitor) {
		        creategrammar.ImplementInterfaceItem implementInterfaceItem = parser1.new ImplementInterfaceItem();
		        implementInterfaceItem.implementName = parser1.nameClassVisitor;
		        implementInterfaceItem.interfaceName = item.getName();
		        parser1.implementInterfaceList.add(implementInterfaceItem);
		    }
		}
		 
 
	}
	
	 
	 
}
