package today.also.hyuil.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final String BASE_URL = "http://localhost:8080";

    @GetMapping
    public String main() {
        return "index";
    }

    @GetMapping("/login/oauth2/{sns}")
    public String snsLoginMain() {
        return "index";
    }
}
