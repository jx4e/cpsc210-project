package uml;

import java.util.stream.Collectors;

// Represents a class diagram
public class ClassDiagram {
    private java.util.List<ClassTable> tables;

    private static final String table = "<mxGraphModel>\n" 
                + 
                "\t<root>\n" 
                + 
                "\t\t<mxCell id=\"0\"/>\n" 
                + 
                "\t\t<mxCell id=\"1\" parent=\"0\"/>\n" 
                + 
                "\t\tCLASS_TABLE_ELEMENT\n" 
                + 
                "\t</root>\n" 
                + 
                "</mxGraphModel>";

    public ClassDiagram(java.util.List<Class<?>> classes, boolean simple) {
        tables = classes.stream().map(clazz -> new ClassTable(clazz, simple)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        String tableElements = "";

        for (ClassTable table : tables) {
            tableElements += table + "\n";
        }

        return table.replace("CLASS_TABLE_ELEMENT", tableElements);
    }
}
