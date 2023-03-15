package today.also.hyuil.controller.member;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import today.also.hyuil.config.security.CustomUserDetailsService;
import today.also.hyuil.domain.dto.member.LoginDto;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MemberLoginController {

    private final CustomUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberLoginController(CustomUserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/loginForm")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginDto());
        return "member/loginForm";
    }

    @PostMapping
    public String login(@ModelAttribute LoginDto loginDto, HttpServletRequest request) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getMemberId());

        boolean matches = passwordEncoder.matches(
                loginDto.getPassword(), userDetails.getPassword());

        if (!matches) {
            return "redirect:/loginForm";
        }



        if (request.getRequestURI() != null) {
            return "redirect:/"+request.getRequestURI();
        }
        return "index";
    }
}
