package nl.softcause.jsontemplates.nodes.base;

public class NodeUtil {
    public static String slotName(String fieldName) {
        fieldName = fieldName.endsWith("Node") ? fieldName.substring(0, fieldName.length() - 4) : fieldName;
        return fieldName;
    }
}
