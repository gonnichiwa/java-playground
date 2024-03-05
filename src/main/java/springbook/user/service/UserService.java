package springbook.user.service;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.user.Level;
import springbook.user.User;
import springbook.user.dao.IUserDao;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

public class UserService {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMAND_FOR_GOLD = 30;

    private IUserDao userDao;
    private PlatformTransactionManager transactionManager;
    private MailSender mailSender;

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    // 가입 후 50회 이상 로그인 하면 BASIC -> SILVER
    // SILVER 레벨 && 30번 추천이면 SILVER -> GOLD
    // 레벨 변경은 일정한 주기로 수행. 변경작업전에는 조건 충족하더라도 레벨 변경 없음.
    public void upgradeNextLevelAllUsers(){
        TransactionStatus status
                = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            List<User> users = userDao.getAll2();
            for(User user : users){
                if(canUpgradeNextLevel(user)){
                    upgradeNextLevel(user);
                    sendUpgradeMail(user);
                }
            }
            this.transactionManager.commit(status);
        } catch (RuntimeException e){
            this.transactionManager.rollback(status);
            throw e;
        }
    }

    // 테스트 하고싶은데, 메일서버 준비 안됐다면?
    private void sendUpgradeMail(User user) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl(); // spring JavaMailSender의 구현클래스.
//        mailSender.setHost("mail.server.com");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("admin@ksug.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name());

        this.mailSender.send(mailMessage);
    }

    protected void upgradeNextLevel(User user) {
        user.upgradeNextLevel(); // 서비스가 다음레벨 뭔지까지 설정해줄려니 책임이 무겁다..
        userDao.update(user);
    }

    private boolean canUpgradeNextLevel(User user) {
        Level currentLev = user.getLevel();
        switch (currentLev){
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMAND_FOR_GOLD);
            case GOLD : return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLev);
        }
    }

    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
