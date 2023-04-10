package today.also.hyuil.controller.fanLetter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import today.also.hyuil.domain.dto.fanLetter.FanLetterWriteDto;

@Controller
@RequestMapping("/fanLetter")
public class FanLetterController {

    @GetMapping
    public String fanLetterList(Model model) {

        return "fanLetter/boardList";
    }

    @GetMapping("/write")
    public String write(Model model) {
        model.addAttribute("fanLetter", new FanLetterWriteDto());
        return "fanLetter/writePage";
    }

//    @PostMapping("/write")
//    public String write()
}
