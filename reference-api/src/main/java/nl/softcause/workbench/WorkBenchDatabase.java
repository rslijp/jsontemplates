package nl.softcause.workbench;

import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.softcause.dto.TemplateAndDescriptionDTO;
import nl.softcause.dto.TemplateDTO;
import nl.softcause.jsontemplates.definition.DescribeTemplateLibrary;
import nl.softcause.jsontemplates.nodes.INode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class WorkBenchDatabase {
    private static final Logger logger = LoggerFactory.getLogger(WorkBenchDatabase.class);

    @NonNull
    private IWorkBenchStore DATABASE;

    public boolean save(String token, String commitUrl ,String cancelUrl, DescribeTemplateLibrary library, Class modelClass,  INode[] slots) {
        logger.info("Saving {}", token);
        var dto = TemplateDTO.asDTO(slots);
        DATABASE.put(token, new DatabaseEntry(token, commitUrl, cancelUrl, dto, dto, library, modelClass));
        return true;
    }

    public boolean update(String token, TemplateDTO dto) {
        if (!DATABASE.containsKey(token)) {
            return false;
        }
        var entry = DATABASE.get(token);
        try {
//            var slots = dto.asTemplate(entry.getLibrary());
            DATABASE.put(token, entry.update(dto));
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
        return entry.slots;
    }

    public Map<String,Object> getAdditionalData(String token) {
        logger.info("Get additional data of {}", token);
        if (!DATABASE.containsKey(token)) {
            return null;
        }
        var entry = DATABASE.get(token);
        return entry.getAdditionalData();
    }

    public void saveAdditionalData(String token, Map<String,Object> additionalData) {
        logger.info("Saving additional data of {}", token);
        if (!DATABASE.containsKey(token)) {
            return;
        }
        var entry = DATABASE.get(token);
        entry = entry.update(additionalData);
        DATABASE.put(token, entry);
    }

    public TemplateAndDescriptionDTO getFullDto(String token) {
        logger.info("Get with description of {}", token);
        if (!DATABASE.containsKey(token)) {
            return null;
        }
        var entry = DATABASE.get(token);
        return new TemplateAndDescriptionDTO(entry.slots, entry.getLibrary().describe(entry.getModel()), entry.getCommitUrl(), entry.getCancelUrl());
    }

    public INode[] getNodes(String token) {
        logger.info("Get nodes of {}", token);
        if (!DATABASE.containsKey(token)) {
            return null;
        }
        var entry = DATABASE.get(token);
        return entry.slots.asTemplate(entry.library);
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
        return entry.slots.clone();
    }


}
