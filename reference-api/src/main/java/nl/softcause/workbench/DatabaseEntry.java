package nl.softcause.workbench;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.*;
import nl.softcause.dto.NodeDTO;
import nl.softcause.dto.TemplateDTO;
import nl.softcause.jsontemplates.definition.DescribeTemplateLibrary;
import nl.softcause.jsontemplates.model.DefinedModel;
import nl.softcause.jsontemplates.model.TemplateModel;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class DatabaseEntry implements Serializable {

    private static Map<Class, TemplateModel> RUNTIME_CACHE = new ConcurrentHashMap<>();

    @NonNull
    String token;
    @NonNull
    String commitUrl;
    @NonNull
    String cancelUrl;
    @NonNull
    public
    TemplateDTO slots;
    @NonNull
    TemplateDTO original;
    @NonNull
    DescribeTemplateLibrary library;
    @NonNull
    Class modelClass;

    boolean saved=false;

    Map<String, Object> additionalData=new HashMap<>();

    public DatabaseEntry update(TemplateDTO update) {
        return new DatabaseEntry(token ,commitUrl, cancelUrl, update, original, library, modelClass, true, additionalData);
    }

    public DatabaseEntry revert() {
        return new DatabaseEntry(token ,commitUrl, cancelUrl, original, original, library, modelClass, false, additionalData);
    }

    public DatabaseEntry clone(){
        return new DatabaseEntry(token ,commitUrl, cancelUrl, slots.clone(), original.clone(), library, modelClass, saved, additionalData!=null?new HashMap<>(additionalData):null);
    }

    public DatabaseEntry update(Map<String, Object> update) {
        return new DatabaseEntry(token ,commitUrl, cancelUrl, slots.clone(), original.clone(), library, modelClass, saved, update!=null?new HashMap<>(update):null);
    }

    @JsonIgnore
    @SuppressWarnings("unchecked")
    public TemplateModel getModel(){
        return RUNTIME_CACHE.computeIfAbsent(modelClass, (c)->new TemplateModel<>(new DefinedModel<>(c)));
    }
}