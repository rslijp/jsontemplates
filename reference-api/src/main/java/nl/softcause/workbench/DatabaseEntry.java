package nl.softcause.workbench;

import java.util.HashMap;
import java.util.Map;
import lombok.*;
import nl.softcause.jsontemplates.definition.DescribeTemplateLibrary;
import nl.softcause.jsontemplates.model.ITemplateModelDefinition;
import nl.softcause.jsontemplates.nodes.INode;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class DatabaseEntry {
    @NonNull
    String token;
    @NonNull
    String commitUrl;
    @NonNull
    String cancelUrl;
    @NonNull
    public
    INode[] slots;
    @NonNull
    INode[] original;
    @NonNull
    DescribeTemplateLibrary library;
    @NonNull
    ITemplateModelDefinition model;

    boolean saved=false;

    Map<String, Object> additionalData=new HashMap<>();

    public DatabaseEntry update(INode[] update) {
        return new DatabaseEntry(token ,commitUrl, cancelUrl, update, original, library, model, true, additionalData);
    }

    public DatabaseEntry revert() {
        return new DatabaseEntry(token ,commitUrl, cancelUrl, original, original, library, model, false, additionalData);
    }

    public DatabaseEntry clone(){
        return new DatabaseEntry(token ,commitUrl, cancelUrl, slots.clone(), original.clone(), library, model, saved, additionalData!=null?new HashMap<>(additionalData):null);
    }
}