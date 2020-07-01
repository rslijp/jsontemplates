package nl.softcause.referenceapi;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.softcause.dto.TemplateAndDescriptionDTO;
import nl.softcause.dto.TemplateDTO;
import nl.softcause.jsontemplates.definition.DescribeTemplateLibrary;
import nl.softcause.jsontemplates.model.DefinedModel;
import nl.softcause.jsontemplates.model.ITemplateModelDefinition;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.INode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkBenchDatabase {
    private static final Logger logger = LoggerFactory.getLogger(WorkBenchDatabase.class);
    private Map<String, DatabaseEntry> DATABASE = new ConcurrentHashMap<>();

    public boolean save(String token, String commitUrl ,String cancelUrl, DescribeTemplateLibrary library, ITemplateModelDefinition model,  INode[] slots) {
        logger.info("Saving {}", token);
        DATABASE.put(token, new DatabaseEntry(token, commitUrl, cancelUrl, slots, slots, library, model));
        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean save(String token, String commitUrl ,String cancelUrl, DescribeTemplateLibrary library, Class modelClass, INode[] slots) {
        ITemplateModelDefinition modelDefinition = new TemplateModel<>(new DefinedModel<>(modelClass));
        return save(token, commitUrl, cancelUrl, library, modelDefinition, slots);
    }

    public boolean update(String token, TemplateDTO dto) {
        if (!DATABASE.containsKey(token)) {
            return false;
        }
        var entry = DATABASE.get(token);
        var slots = dto.asTemplate(entry.getLibrary());
        entry.slots = slots;
        entry.saved = true;
        return true;
    }

    public TemplateDTO getDto(String token) {
        logger.info("Get of {}", token);
        if (!DATABASE.containsKey(token)) {
            return null;
        }
        var entry = DATABASE.get(token);
        return TemplateDTO.asDTO(entry.slots);
    }

    public Map<String,Object> getAdditionalData(String token) {
        logger.info("Get addtional data of {}", token);
        if (!DATABASE.containsKey(token)) {
            return null;
        }
        var entry = DATABASE.get(token);
        return entry.getAdditionalData();
    }

    public TemplateAndDescriptionDTO getFullDto(String token) {
        logger.info("Get with description of {}", token);
        if (!DATABASE.containsKey(token)) {
            return null;
        }
        var entry = DATABASE.get(token);
        return new TemplateAndDescriptionDTO(TemplateDTO.asDTO(entry.slots), entry.library.describe(entry.model), entry.commitUrl, entry.cancelUrl);
    }

    public INode[] getNodes(String token) {
        logger.info("Get nodes of {}", token);
        if (!DATABASE.containsKey(token)) {
            return null;
        }
        var entry = DATABASE.get(token);
        return entry.slots;
    }

    public void remove(String token) {
        logger.info("Remove {}", token);
        DATABASE.remove(token);
    }

    public TemplateDTO revert(String token) {
        logger.info("Revert of {}", token);
        if (!DATABASE.containsKey(token)) {
            return null;
        }
        var entry = DATABASE.get(token);
        entry.slots = entry.original;
        return TemplateDTO.asDTO(entry.slots);
    }


    @SuppressWarnings("UnusedAssignment")
    @Data
    @RequiredArgsConstructor
    class DatabaseEntry {
        @NonNull
        String token;
        @NonNull
        String commitUrl;
        @NonNull
        String cancelUrl;
        @NonNull
        INode[] slots;
        @NonNull
        INode[] original;
        @NonNull
        DescribeTemplateLibrary library;
        @NonNull
        ITemplateModelDefinition model;
        boolean saved=false;
        Map<String, Object> additionalData=new HashMap<>();
    }
}
