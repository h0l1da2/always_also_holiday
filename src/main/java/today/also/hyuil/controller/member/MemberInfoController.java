package today.also.hyuil.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import today.also.hyuil.domain.dto.member.MemberInfoDto;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.service.member.inter.MemberJoinService;
import today.also.hyuil.service.web.WebService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/info")
public class MemberInfoController {

    private final WebService webService;
    private final MemberJoinService memberJoinService;

    public MemberInfoController(WebService webService, MemberJoinService memberJoinService) {
        this.webService = webService;
        this.memberJoinService = memberJoinService;
    }

    @GetMapping
    public String info() {
        return "member/info/infoMain";
    }

    @GetMapping("/modify")
    public String infoModify(HttpServletRequest request, Model model) {
        try {
            Long id = webService.getIdInSession(request);

            Member member = memberJoinService.findMyAccount(id);
            model.addAttribute("member", new MemberInfoDto(member));

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return "redirect:/loginForm?redirectUrl=/info";
        }

        return "member/info/infoForm";
    }
}
