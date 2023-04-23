package today.also.hyuil.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/exception")
public class ExceptionController {

    @GetMapping("/access")
    public String noAccess() {
        return "exception/access";
    }
}
