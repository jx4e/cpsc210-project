package uml;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

// Represents a class table
public class ClassTable {
    private Class<?> clazz;
    private String name;
    private ArrayList<String> fields;
    private ArrayList<String> methods;

    private static final String classNameElement = "<mxCell id=\"ID_NUMBER\" value=\"CLASS_NAME\" " 
            + "style=\"html=1;whiteSpace=wrap;swimlane;fontStyle=2;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1" 
            + ";startSize=26;horizontalStack=0;resizeParent=1;resizeLast=0;collapsible=1;marginBottom=0;rounded=0;"
            + "shadow=0;strokeWidth=1;\" vertex=\"1\" parent=\"1\">\n" 
            + 
                "\t\t\t<mxGeometry x=\"X_POSITION\" y=\"120\" width=\"160\" height=\"TOTAL_HEIGHT\" as=\"geometry\">\n" 
                + 
                "\t\t\t\t<mxRectangle x=\"X_POSITION\" y=\"140\" width=\"160\" " 
                + "height=\"TITLE_HEIGHT\" as=\"alternateBounds\"/>\n" 
                + 
                "\t\t\t</mxGeometry>\n" 
                + 
                "\t\t</mxCell>";

    private static final String fieldMethodElement = "<mxCell id=\"ID_NUMBER\" value=\"NAME\" style=\"text;align=left" 
            + ";verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];" 
            + "portConstraint=eastwest;\" vertex=\"1\" parent=\"TABLE_ID\">\n"
            +
                "\t\t\t<mxGeometry y=\"26\" width=\"160\" height=\"26\" as=\"geometry\"/>\n" 
                + 
                "\t\t</mxCell>";

    private static final String separatorElement = "<mxCell id=\"ID_NUMBER\" value=\"\" style=\"line;html=1;" 
            + "strokeWidth=1;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;" 
            + 
            "rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;\" vertex=\"1\" parent=\"TABLE_ID\">\n" 
            +
                "\t\t\t<mxGeometry y=\"104\" width=\"160\" height=\"8\" as=\"geometry\"/>\n" 
                + 
                "\t\t</mxCell>";
    
    public ClassTable(Class<?> clazz, boolean simple) {
        this.clazz = clazz;
        this.name = clazz.getName();
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();

        if (simple) {
            return;
        }

        for (Field field : clazz.getDeclaredFields()) {
            fields.add(formatField(field));
        }

        for (Method method : clazz.getDeclaredMethods()) {
            methods.add(formatMethod(method));
        }
    }

    private String formatField(Field field) {
        return getAccessModifier(field.getModifiers()) + field.getName() + " : " + field.getType().getSimpleName();
    }

    private String formatMethod(Method method) {
        return getAccessModifier(method.getModifiers()) + method.getName() 
                + "()" + " : " + method.getReturnType().getSimpleName();
    }

    private String getAccessModifier(int modifier) {
        if (Modifier.isPublic(modifier)) {
            return "+";
        } else if (Modifier.isPrivate(modifier)) {
            return "-";
        } else if (Modifier.isProtected(modifier)) {
            return "#";
        }

        return "";
    }

    @SuppressWarnings("methodlength")
    @Override
    public String toString() {
        String id = TableManager.getTableManager().getNextString();
        int totalHeight = 0;
        
        String fieldMethodElement = "";

        for (String field : fields) {
            System.out.println("Field " + field);

            String fieldElement = ClassTable.fieldMethodElement.replace("NAME", field)
                    .replace("ID_NUMBER", TableManager.getTableManager().getNextString())
                    .replace("TABLE_ID", id);;
            
            fieldMethodElement += fieldElement + "\n";
            totalHeight += 26;
        }

        if (!fields.isEmpty() && !methods.isEmpty()) {
            fieldMethodElement += ClassTable.separatorElement
                    .replace("ID_NUMBER", TableManager.getTableManager().getNextString())
                    .replace("TABLE_ID", id) + "\n";
            totalHeight += 8;
        }

        for (String method : methods) {
            System.out.println("method " + method);

            String methodElement = ClassTable.fieldMethodElement.replace("NAME", method)
                    .replace("ID_NUMBER", TableManager.getTableManager().getNextString())
                    .replace("TABLE_ID", id);
            
            fieldMethodElement += methodElement + "\n";
            totalHeight += 26;
        }

        String x = TableManager.getTableManager().getNextXString();

        String title = name;

        if (Modifier.isInterface(clazz.getModifiers())) {
            title = "&amp;laquo;interface&amp;raquo;&lt;br&gt;&lt;b&gt;" + name + "&lt;/b&gt;";
        } else if (Modifier.isAbstract(clazz.getModifiers())) {
            title = "&amp;laquo;abstract&amp;raquo;&lt;br&gt;&lt;b&gt;" + name + "&lt;/b&gt;";
        }

        int titleHeight = clazz.isInterface() ? 26 * 2 : 26;

        String classNameElement = ClassTable.classNameElement.replace("CLASS_NAME", title)
                .replace("ID_NUMBER", id)
                .replace("TOTAL_HEIGHT", String.valueOf(totalHeight + titleHeight))
                .replace("TITLE_HEIGHT", String.valueOf(titleHeight))
                .replace("X_POSITION", x);

        return classNameElement + "\n" + fieldMethodElement;
    }
}
