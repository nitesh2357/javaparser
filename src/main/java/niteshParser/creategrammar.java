package niteshParser;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.lang.Math;

import java.util.*;

public class creategrammar {

    static List<String> CN;
    static List<String> GN;
    static List<AssociationItem> assolist;  
    static List<String> csU;  
    static List<String> asU; 
    static List<String> esU; 
    static List<String> isU; 
    static List<ExtendItem> extlist; 
    static Set<UseInterfaceItem> useInterfaceList; 
    static List<ImplementInterfaceItem> implementInterfaceList;

   

    
    class ExtendItem {
        String superClassName;
        String subClassName;
    }

    class UseInterfaceItem {
        String interfaceName;
        String useName;

        @Override
        public int hashCode() {
            int hashcode = 0;
            hashcode = interfaceName.hashCode() * 20;
            hashcode += useName.hashCode();
            return hashcode;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof UseInterfaceItem) {
                UseInterfaceItem item = (UseInterfaceItem) obj;
                return (item.interfaceName.equals(this.interfaceName) && item.useName.equals(this.useName));
            } else {
                return false;
            }
        }
    }

    class ImplementInterfaceItem {
        String interfaceName;
        String implementName;
    }
    class AssociationItem {
        String startName;
        String endName;
        String attributeName;
        boolean ifMultiple;
    }


    
    static String nameClassVisitor;
    static boolean isInterfaceClassVisitor;
    static int modifierClassVisitor;
    static List<ClassOrInterfaceType> extendClassVisitor;
    static List<ClassOrInterfaceType> implementClassVisitor;

    
    static List<String> nameVisitor;
    static List<Integer> modifierVisitor;
    static List<String> typeVisitor;
    static List<List<Parameter>> parameterListMethodVisitor;
    
    static List<String> getSetAttribute = new ArrayList<String>();   // Used to set get set attribute

    
    static List<String> nameFieldVisitor;
    static List<Integer> modifierFieldVistor;
    static List<String> typeFieldVisitor;

    
    static List<String> nameConstructorVisitor;
    static List<Integer> modifierConstructorVisitor;
    static List<List<Parameter>> parameterListConstructorVisitor;

   
    static ArrayList<String> innerAttributeTypes = new ArrayList<String>();

    creategrammar() {
        CN = new ArrayList<String>();
        GN = new ArrayList<String>();

        assolist = new ArrayList<AssociationItem>();
        extlist = new ArrayList<ExtendItem>();
        useInterfaceList = new LinkedHashSet<UseInterfaceItem>();
        implementInterfaceList = new ArrayList<ImplementInterfaceItem>();

        csU = new ArrayList<String>();
        asU = new ArrayList<String>();
        esU = new ArrayList<String>();
        isU = new ArrayList<String>();

        extendClassVisitor = new ArrayList<ClassOrInterfaceType>();
        implementClassVisitor = new ArrayList<ClassOrInterfaceType>();

        nameVisitor = new ArrayList<String>();
        modifierVisitor = new ArrayList<Integer>();
        typeVisitor = new ArrayList<String>();
        parameterListMethodVisitor = new ArrayList<List<Parameter>>();

        nameFieldVisitor = new ArrayList<String>();
        modifierFieldVistor = new ArrayList<Integer>();
        typeFieldVisitor = new ArrayList<String>();

        nameConstructorVisitor = new ArrayList<String>();
        modifierConstructorVisitor = new ArrayList<Integer>();
        parameterListConstructorVisitor = new ArrayList<List<Parameter>>();

        innerAttributeTypes = new ArrayList<String>();
    }

    
    public static class ClassVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, Object arg) {

            nameClassVisitor = n.getName();
            isInterfaceClassVisitor = n.isInterface();
            extendClassVisitor = n.getExtends();
            implementClassVisitor = n.getImplements();
            modifierClassVisitor = n.getModifiers();
          
        }

    }

    
    public static class MethodVisitor extends VoidVisitorAdapter{

        @Override
        public void visit(MethodDeclaration n, Object arg) {
            
        	if(!(n.getName().startsWith("get") || n.getName().startsWith("set")))
        	{
        		modifierVisitor.add(n.getModifiers());
                nameVisitor.add(n.getName());            
                typeVisitor.add(n.getType().toString());
                parameterListMethodVisitor.add(n.getParameters());
              
        	}    
        	else
        	{
        		String methodName = n.getName();
        	  
        	    methodName = methodName.substring(3);
        	    methodName = methodName.toLowerCase();
        		
        		getSetAttribute.add(methodName);
        		
        		
        	}
        }
    }

    
    public static class FieldVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(FieldDeclaration n, Object arg) {
        	
        	
        	for(int i=0;i<getSetAttribute.size();i++){
        	  
        	} 
        	
        	if(!(getSetAttribute.contains(n.getVariables().get(0).toString())))
        	{
        		
        		
        		typeFieldVisitor.add(n.getType().toString());
                nameFieldVisitor.add(n.getVariables().get(0).toString());
                modifierFieldVistor.add(n.getModifiers());
               
        	}
        	else
        	{
        		
        		typeFieldVisitor.add(n.getType().toString());
                nameFieldVisitor.add(n.getVariables().get(0).toString());
                modifierFieldVistor.add(ModifierSet.PUBLIC);
            
        	}
            
        }
    }

    //4. visit constructor
    public static class ConstructorVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(ConstructorDeclaration n, Object arg) {
            modifierConstructorVisitor.add(n.getModifiers());
            nameConstructorVisitor.add(n.getName());
            parameterListConstructorVisitor.add(n.getParameters());
        }
    }

    //5. visit inner attributes in methods
    public static class VariableDecVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(VariableDeclarationExpr n, Object arg) {
            innerAttributeTypes.add(n.getType().toString());
        }
    }

    //1. create class UML & save use of interfaces & save association
    public void createcsU() {
        String source = "";
        if (isInterfaceClassVisitor) {
            source += "interface " + nameClassVisitor + " {\n";
        } else {
            if (ModifierSet.isAbstract(modifierClassVisitor)) {
                source += "abstract class " + nameClassVisitor + " {\n";
            } else {
                source += "class " + nameClassVisitor + " {\n";
            }
        }

        //A. Making UML FIELD string
        for (String field : nameFieldVisitor) {
            //1. create field string of class UML
            int index = nameFieldVisitor.indexOf(field);
            // if field has associations with other classes, then it will not be printed in the class UML, but put into associationItemMap
            String substr1 = "";
            if (typeFieldVisitor.get(index).indexOf('[') >= 0) {
                substr1 += typeFieldVisitor.get(index).substring(0, typeFieldVisitor.get(index).indexOf('['));
            } else if (typeFieldVisitor.get(index).contains("Collection") || typeFieldVisitor.get(index).contains("List") || typeFieldVisitor.get(index).contains("Map") || typeFieldVisitor.get(index).contains("Set")) {
                substr1 += typeFieldVisitor.get(index).substring(typeFieldVisitor.get(index).indexOf('<') + 1, typeFieldVisitor.get(index).indexOf('>'));
            }

            if (CN.indexOf(typeFieldVisitor.get(index)) >= 0 || CN.indexOf(substr1) >= 0
                    || GN.indexOf(typeFieldVisitor.get(index)) >= 0 || GN.indexOf(substr1) >= 0) {
                AssociationItem associationItem = new AssociationItem();
                associationItem.startName = nameClassVisitor;
                if (substr1 != "") {
                    associationItem.endName = substr1;
                }
                else {
                    associationItem.endName = typeFieldVisitor.get(index);
                }

                associationItem.attributeName = field;

                if (substr1 != "") {
                    associationItem.ifMultiple = true;
                } else {
                    associationItem.ifMultiple = false;
                }
                assolist.add(associationItem);
            } else {

                String typefieldstr = "";
                if (typeFieldVisitor.get(index).indexOf('[') >= 0) {
                    typefieldstr += typeFieldVisitor.get(index).substring(0, typeFieldVisitor.get(index).indexOf('['));
                    typefieldstr += "(*)";
                } else if (typeFieldVisitor.get(index).contains("Collection") || typeFieldVisitor.get(index).contains("List") || typeFieldVisitor.get(index).contains("Map") || typeFieldVisitor.get(index).contains("Set")) {
                    typefieldstr += typeFieldVisitor.get(index).substring(typeFieldVisitor.get(index).indexOf('<') + 1, typeFieldVisitor.get(index).indexOf('>'));
                    typefieldstr += "(*)";
                } else {
                    typefieldstr += typeFieldVisitor.get(index);
                }

                if (ModifierSet.isPublic(modifierFieldVistor.get(index))) {
                    source += "+" + field + ":" + typefieldstr + "\n";
                } else if (ModifierSet.isPrivate(modifierFieldVistor.get(index))) {
                    source += "-" + field + ":" + typefieldstr + "\n";
                }
            }
        }

        source += "__\n";

        //B. making constructor UML String
        for (String methodName : nameConstructorVisitor) {
            int index = nameConstructorVisitor.indexOf(methodName);
            if (ModifierSet.isPublic(modifierConstructorVisitor.get(index))) {
                String parameterStr = "";

                for (Parameter parameterSingle : parameterListConstructorVisitor.get(index)) {
                    String[] parts = parameterSingle.toString().split(" ");
                    parameterStr += parts[1] + ":" + parameterSingle.getType();
                    if (parameterListConstructorVisitor.get(index).indexOf(parameterSingle) + 1 != parameterListConstructorVisitor.get(index).size())
                        parameterStr += ",";
                }
                source += "+" + methodName + "(" + parameterStr + ")" + "\n";
            }

            //find if any use of interface in parameters, save to useInterfaceList
            for (Parameter parameterSingle : parameterListConstructorVisitor.get(index)) {
                String substr1 = "";
                String paramtertype = parameterSingle.getType().toString();

                if (paramtertype.indexOf('[') >= 0) {
                    substr1 += paramtertype.substring(0, paramtertype.indexOf('['));
                } else if (paramtertype.contains("Collection") || paramtertype.contains("List") || paramtertype.contains("Map") || paramtertype.contains("Set")) {
                    substr1 += paramtertype.substring(paramtertype.indexOf('<') + 1, paramtertype.indexOf('>'));
                } else
                    substr1 += paramtertype;

                for (String interfaceName : GN) {
                    if (interfaceName.equals(substr1)) {
                        UseInterfaceItem useInterfaceItem = new UseInterfaceItem();
                        useInterfaceItem.interfaceName = interfaceName;
                        useInterfaceItem.useName = nameClassVisitor;

                        //if use is a class, added to useInterfaceList, ignore used by a interface
                        if (CN.contains(nameClassVisitor))
                            useInterfaceList.add(useInterfaceItem);
                    }
                }
            }
        }


       
        for (String methodName : nameVisitor) {
            int index = nameVisitor.indexOf(methodName);
            if (ModifierSet.isPublic(modifierVisitor.get(index)) || GN.contains(nameClassVisitor)) {
                String parameterStr = "";

                for (Parameter parameterSingle : parameterListMethodVisitor.get(index)) {
                    String[] parts = parameterSingle.toString().split(" ");
                    parameterStr += parts[1] + ":" + parameterSingle.getType();
                    if (parameterListMethodVisitor.get(index).indexOf(parameterSingle) + 1 != parameterListMethodVisitor.get(index).size())
                        parameterStr += ",";
                }

                source += "+" + methodName + "(" + parameterStr + "):" + typeVisitor.get(index) + "\n";
            }


            //find if any use of interface in parameters, save to useInterfaceList
            for (Parameter parameterSingle : parameterListMethodVisitor.get(index)) {
                String substr1 = "";
                String paramtertype = parameterSingle.getType().toString();

                if (paramtertype.indexOf('[') >= 0) {
                    substr1 += paramtertype.substring(0, paramtertype.indexOf('['));
                } else if (paramtertype.contains("Collection") || paramtertype.contains("List") || paramtertype.contains("Map") || paramtertype.contains("Set")) {
                    substr1 += paramtertype.substring(paramtertype.indexOf('<') + 1, paramtertype.indexOf('>'));
                } else
                    substr1 += paramtertype;

                for (String interfaceName : GN) {
                    if (interfaceName.equals(substr1)) {
                        UseInterfaceItem useInterfaceItem = new UseInterfaceItem();
                        useInterfaceItem.interfaceName = interfaceName;
                        useInterfaceItem.useName = nameClassVisitor;

                        //if use is a class, added to useInterfaceList, ignore used by a interface
                        if (CN.contains(nameClassVisitor))
                            useInterfaceList.add(useInterfaceItem);
                    }
                }
            }


            //find if any use of interface in return type, save to useInterfaceList
            String substr1 = "";
            String returntype = typeVisitor.get(index);
            if (returntype.indexOf('[') >= 0) {
                substr1 += returntype.substring(0, returntype.indexOf('['));
            } else if (returntype.contains("Collection") || returntype.contains("List") || returntype.contains("Map") || returntype.contains("Set")) {
                substr1 += returntype.substring(returntype.indexOf('<') + 1, returntype.indexOf('>'));
            } else
                substr1 += returntype;

            for (String interfaceName : GN) {
                if (interfaceName.equals(substr1)) {
                    UseInterfaceItem useInterfaceItem = new UseInterfaceItem();
                    useInterfaceItem.interfaceName = interfaceName;
                    useInterfaceItem.useName = nameClassVisitor;

                    //if use is a class, added to useInterfaceList, ignore use by a interface
                    if (CN.contains(nameClassVisitor))
                        useInterfaceList.add(useInterfaceItem);
                }
            }

        }
        source += "}\n";

        //D. find if any use of interface inside a method
        for (String innervarType : innerAttributeTypes) {
            for (String interfaceName : GN) {
                if (interfaceName.equals(innervarType)) {
                    UseInterfaceItem useInterfaceItem = new UseInterfaceItem();
                    useInterfaceItem.interfaceName = interfaceName;
                    useInterfaceItem.useName = nameClassVisitor;

                    //if use is a class, added to useInterfaceList, ignore use by a interface
                    if (CN.contains(nameClassVisitor))
                        useInterfaceList.add(useInterfaceItem);
                }
            }
        }

        csU.add(source);//print class string for UML
    }

    //2. create association UML
    public void createasU() {
        String source = "";
        while (!assolist.isEmpty()) {
            String class1 = assolist.get(0).startName;
            String class2 = assolist.get(0).endName;

            int i = 0;
            for (; i < assolist.size(); i++) {
                if (assolist.get(i).startName.equals(class2) && assolist.get(i).endName.equals(class1)) {
                    break;
                }
            }
            if (i < assolist.size()) {
                if (assolist.get(0).ifMultiple && assolist.get(i).ifMultiple) {
                    source += class1 + " \"*\"" + "--" + "\"*\" " + class2 + "\n";
                } else if (assolist.get(0).ifMultiple) {
                    source += class1 + " \"1\"" + " --" + "\"*\" " + class2 + "\n";
                } else if (assolist.get(i).ifMultiple) {
                    source += class1 + " \"*\"" + "-- " + "\"1\" " + class2 + "\n";
                } else {
                    source += class1 + " \"1\"" + " -- " + "\"1\" " + class2 + "\n";
                }
                assolist.remove(i);
                assolist.remove(0);
            } else {
                if (assolist.get(0).ifMultiple) {
                    if (assolist.get(0).endName.toUpperCase().equals(assolist.get(0).attributeName.toUpperCase())) {
                        source += class1 + " --" + "\"*\" " + class2 + "\n";
                    } else {
                        //source += class1 + " --" + "\"*\" " + class2 +":" + associationItemMap.get(0).attributeName + "\n";
                        source += class1 + " --" + "\"*\" " + class2 + "\n";
                    }

                } else {
                    //source += class1 + " --" + "\"1\" " + class2 +":" + associationItemMap.get(0).attributeName + "\n";
                    source += class1 + " --" + "\"1\" " + class2 + "\n";
                }
                assolist.remove(0);
            }
        }

        asU.add(source);
    }


    //3. create extend relation UML
    public void createesU() {
        String source = "";
        for (ExtendItem item : extlist) {
            source += item.superClassName + " <|-- " + item.subClassName + "\n";
        }
        esU.add(source);
    }

    //4. create Interface UML
    public void createisU() {
        String source = "";
        int usecase1 = 0;
        int usecase2 = 1;

        for (ImplementInterfaceItem item : implementInterfaceList) {
            source += item.interfaceName + " <|.. " + item.implementName + "\n";
        }

        for (UseInterfaceItem item : useInterfaceList) {
            source += item.useName + " ..> " + item.interfaceName + ": use\n";
        }
        isU.add(source);
    }

    public void clearTempStaticClass() {

        nameVisitor.clear();
        modifierVisitor.clear();
        typeVisitor.clear();
        parameterListMethodVisitor.clear();

        nameFieldVisitor.clear();
        modifierFieldVistor.clear();
        typeFieldVisitor.clear();

        nameConstructorVisitor.clear();
        modifierConstructorVisitor.clear();
        parameterListConstructorVisitor.clear();

        innerAttributeTypes.clear();
    }
}
