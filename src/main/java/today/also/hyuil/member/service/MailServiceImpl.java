package today.also.hyuil.member.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import today.also.hyuil.member.domain.type.Mail;

import java.util.Random;

import static jakarta.mail.Message.RecipientType;

@Service
@PropertySource("classpath:application.yml")
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    private MimeMessage message;
    @Value("${mail.id}")
    private String fromEmail;
    private String title;
    private String template;

    private String randomCode = "";
    private final String charset = "UTF-8";
    private final String html = "html";

    public MailServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public String mailSend(Mail mail, String userEmail) throws MessagingException {
        String result = "";
        if (mail.equals(Mail.JOIN)) {
            randomCode = createCode();
            result = randomCode;
        }
        MimeMessage template = setTemplate(mail, userEmail, randomCode);
        sendMail(template);
        return result;
    }

    @Override
    public String createCode() {
        String values = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int codeLength = 5;

        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        /**
         * values 길이 안에서 랜덤한 숫자를 가져오고
         * 랜덤 숫자를 values 의 인덱스로 가져와서 builder 로 String 에 추가함.
         */
        for (int i = 0; i < codeLength; i++) {
            int randomIndex = random.nextInt(values.length());
            builder.append(values.charAt(randomIndex));
        }
        return builder.toString();
    }
    @Override
    public MimeMessage setTemplate(Mail type, String userEmail, String randomCode) throws MessagingException {

        String key = "";
        String value = "";

        if (type.equals(Mail.JOIN)) {
            title = "오늘도 휴일 * 가입 코드 전송";
            template = "email/joinMailForm";
            // randomCode , template , key=randomCode , value = code
            key = "randomCode";
            value = randomCode;
        }

        message = mailSender.createMimeMessage();
        message.addRecipients(RecipientType.TO, userEmail);
        message.setSubject(title);
        message.setFrom(fromEmail);
        message.setText(getContext(key, value, template), charset, html);
        return message;
    }

    @Override
    public String getContext(String key, String value, String template) {
        Context context = setContext(key, value);
        return templateEngine.process(template, context);
    }

    @Override
    public void sendMail(MimeMessage message) {
        mailSender.send(message);
    }

    private Context setContext(String key, String value) {
        Context context = new Context();
        context.setVariable(key, value);
        return context;
    }
}
