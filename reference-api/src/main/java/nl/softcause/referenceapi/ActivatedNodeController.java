package nl.softcause.referenceapi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/workbench-api")
public class ActivatedNodeController extends NodeController {

    public ActivatedNodeController(ActivatedWorkBenchDatabase database) {
        super(database);
    }
}

