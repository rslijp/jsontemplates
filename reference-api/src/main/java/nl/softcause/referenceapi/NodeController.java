package nl.softcause.referenceapi;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.softcause.dialogs.Dialog;
import nl.softcause.dto.TemplateAndDescriptionDTO;
import nl.softcause.dto.TemplateDTO;
import nl.softcause.jsontemplates.definition.DescribeTemplateLibrary;
import nl.softcause.jsontemplates.definition.TemplateDescription;
import nl.softcause.jsontemplates.model.DefinedModel;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.wizard.Wizard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/workbench-api")
public class NodeController {

    private static final Logger logger = LoggerFactory.getLogger(NodeController.class);

    private final WorkBenchDatabase database;

    public NodeController(WorkBenchDatabase database) {
        this.database = database;
    }


    @GetMapping(path = "/{token}")
    public @ResponseBody TemplateAndDescriptionDTO describe(@PathVariable String token){
        logger.info("Download template ["+token+"]");
        return database.getFullDto(token);
    }

    @PostMapping(path = "/{token}")
    public @ResponseBody boolean save(@PathVariable String token, @RequestBody TemplateDTO dto){
        logger.info("Upload template ["+token+"]: ");
        logger.info("DTO ["+token+"]: "+dto);
        return database.update(token, dto);
    }

    @PutMapping(path = "/revert/{token}")
    public @ResponseBody TemplateDTO revert(@PathVariable String token){
        logger.info("Revert template ["+token+"]: ");
        return database.revert(token);
    }

}