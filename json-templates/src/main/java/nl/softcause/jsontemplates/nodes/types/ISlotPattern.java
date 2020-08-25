package nl.softcause.jsontemplates.nodes.types;

public interface ISlotPattern {

    String match(String name, Class current, int nodesInSlot);

    String getDescription();

    ISlotPattern getBasePattern();

    Class[] getLimit();

}
