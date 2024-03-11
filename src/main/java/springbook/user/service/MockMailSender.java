package springbook.user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

// 테스트용 DummyMailSender
public class MockMailSender extends JavaMailSenderImpl implements MailSender {
    @Override
    public void send(SimpleMailMessage simpleMailMessage) throws MailException {
        System.out.println("mail sended(mockup)");
    }

    @Override
    public void send(SimpleMailMessage[] simpleMailMessages) throws MailException {
        System.out.println("mail sended(mockup)");
    }
}
