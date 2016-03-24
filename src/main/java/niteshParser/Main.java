package niteshParser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.type.ClassOrInterfaceType;






import java.io.File;
import java.io.FileInputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;

public class Main {

        public static void main(String[] args) throws Exception {

            
        	if (args.length < 2) {
                throw new InvalidParameterException("Too few parameters (" + args.length + ").");
            }
        	String path1 = args[0].toString();
        	String path2 = args[1].toString();
        	File folder = new File(path1);
    		File[] listOfFiles = folder.listFiles();
            creategrammar parser1 = new creategrammar();
            
            for (int i = 0; i < listOfFiles.length; i++) {
      		  File file = listOfFiles[i];
      		  if (file.isFile() && file.getName().endsWith(".java")) {
      		    String files = file.toString();
      		    System.out.println("list of files"+ files);
                FileInputStream in = new FileInputStream(files);
                CompilationUnit cu = JavaParser.parse(in);
                in.close();
      		  

                new creategrammar.ClassVisitor().visit(cu, null);
                UMLClass parser = new UMLClass();
                parser.classorinstance(parser1);
            }
        }


           
            for (int i = 0; i < listOfFiles.length; i++) {
      		  File file = listOfFiles[i];
      		  if (file.isFile() && file.getName().endsWith(".java")) {
      		    String files = file.toString();
      		    
                FileInputStream in = new FileInputStream(files);
                CompilationUnit cu = JavaParser.parse(in);
                in.close();

                new creategrammar.ClassVisitor().visit(cu, null);
                new creategrammar.MethodVisitor().visit(cu, null);
                new creategrammar.FieldVisitor().visit(cu, null);
                new creategrammar.ConstructorVisitor().visit(cu, null);
                new creategrammar.VariableDecVisitor().visit(cu, null);

                parser1.createcsU(); 
                parser1.clearTempStaticClass();
            }
        }          
            parser1.createasU();
          
            parser1.createesU();
          
            parser1.createisU();
           
            Gen.umlGenerator(parser1.csU, parser1.asU, parser1.esU, parser1.isU, path2);

            
        }

    }

