package today.also.hyuil.service.member.inter;

import javax.mail.internet.MimeMessage;

public interface MailService {
    String joinCodeSend(String toEmail);
    MimeMessage joinMailForm(String toEmail);
    String contextJoin(String code);

}
