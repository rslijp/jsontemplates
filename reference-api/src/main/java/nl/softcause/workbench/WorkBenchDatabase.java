package nl.softcause.workbench;

import java.util.Map;
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

@RequiredArgsConstructor
public class WorkBenchDatabase {
    private static final Logger logger = LoggerFactory.getLogger(WorkBenchDatabase.class);

    @NonNull
    private IWorkBenchStore DATABASE;

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
        try {
            var slots = dto.asTemplate(entry.getLibrary());
            DATABASE.put(token, entry.update(slots));
            return true;
        } catch (Exception e){
            logger.error("Error on save of "+token, e);
            return false;
        }
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
        return new TemplateAndDescriptionDTO(TemplateDTO.asDTO(entry.slots), entry.getLibrary().describe(entry.getModel()), entry.getCommitUrl(), entry.getCancelUrl());
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
        DATABASE.put(token, entry.revert());
        return TemplateDTO.asDTO(entry.slots);
    }


}
