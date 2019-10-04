package nl.softcause.jsontemplates.model;

import lombok.Data;

@Data
public class RecursiveNestedDefinition {

    public String name;

    public RecursiveNestedDefinition nested;
}
