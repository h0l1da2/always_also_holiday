package today.also.hyuil.service.member;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.util.Random;

import static javax.mail.Message.RecipientType;

@Service
@PropertySource("classpath:application.yml")
public class MailServiceImpl implements MailService{

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine; //타임리프를 사용하기 위한 객체
    @Value("${mail.id}")
    private String fromEmail;

    private MimeMessage message;
    private String title;
    private String randomCode;
    private String randomPwd;
    private final String charset = "UTF-8";
    private final String html = "html";

    public MailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public String joinCodeSend(String toEmail) {
        createCode();
        title = "오늘도 휴일 * 가입 코드";

        try {
            message = mailSender.createMimeMessage();
            message.addRecipients(RecipientType.TO, toEmail);
            message.setSubject(title);
            message.setFrom(fromEmail);
            message.setText(contextJoin(randomCode), charset, html);
            mailSender.send(message);

        } catch (MessagingException e) {
            return "전송 오류";
        }

        return randomCode;
    }

    @Override
    public MimeMessage joinMailForm(String toEmail) {
        return null;
    }

    @Override
    public String contextJoin(String code) {
        Context context = setContext("code", code);
        return templateEngine.process("email/joinMailForm", context);
    }

    public void createCode() {
        int min = 1000;
        int max = 9999;
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        int code = random.nextInt(max-min)+min;

        randomCode = buffer.append(code).toString();
    }

    private Context setContext(String key, String value) {
        Context context = new Context();
        context.setVariable(key, value);
        return context;
    }
}
