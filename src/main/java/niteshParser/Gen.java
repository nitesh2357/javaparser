package niteshParser;

import net.sourceforge.plantuml.SourceStringReader;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;

public class Gen {
    public static void umlGenerator(/*String output,*/Collection<String> classStrUML, Collection<String> associationStrUML, Collection<String> extendStrUML, Collection<String> association, String path2) throws Exception {
        OutputStream png = new FileOutputStream(path2+"/output.png");
        String sourceString = "@startuml\n";
        sourceString += "title Class Diagram\n";
        sourceString += "skinparam classAttributeIconSize 0\n";
        sourceString += "skinparam usecaseFontSize 1\n";
        sourceString += "skinparam usecaseFontColor #A80036\n";


        for (String item : classStrUML) {
            sourceString += item;
        }

        for (String item : associationStrUML) {
            sourceString += item;
        }

        for (String item : extendStrUML) {
            sourceString += item;
        }

       for (String item : association) {
            sourceString += item;
        }

        sourceString += "@enduml\n";
        SourceStringReader reader = new SourceStringReader(sourceString);
        String desc = reader.generateImage(png); 
    }
}
