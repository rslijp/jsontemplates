package nl.softcause.jsontemplates.nodes.types;

public interface ISlotPattern {

    boolean match(Object object);

    String getDescription();

    ISlotPattern getBasePattern();
}
