import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.Level;
import springbook.user.User;
import springbook.user.dao.IUserDao;
import springbook.user.dao.IUserDaoJdbc;
import springbook.user.service.*;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECOMMAND_FOR_GOLD;

public class UserServiceTest {
    List<User> users;
    IUserDao userDao;
    UserService userService;
    UserServiceImpl userServiceImpl;
    PlatformTransactionManager transactionManager; // case upgradeAllOrNothing()에서 트랜잭션 실패 확인용
    MailSender mailSender;

    ApplicationContext context;

    @Before public void SetUp() {
        this.context
                = new ClassPathXmlApplicationContext("applicationContext.xml");
//        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class); // DaoFactory.userDao() 생성자로 IUserDao 리턴하는걸로 바꿔줘야함.
        this.userDao = context.getBean("userDao", IUserDao.class);
        this.userService = context.getBean("userService", UserService.class);
        this.transactionManager = context.getBean("myPlatformTransactionManager", DataSourceTransactionManager.class);
        this.mailSender = context.getBean("myMailSender", MockMailSender.class);


        this.users = Arrays.asList(
                new User("1","n1","p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0, "n1@mail.com"),
                new User("2","n2","p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "n2@mail.com"),
                new User("3","n3","p3", Level.SILVER, 60, MIN_RECOMMAND_FOR_GOLD-1, "n3@mail.com"),
                new User("4","n4","p4", Level.SILVER, 60, MIN_RECOMMAND_FOR_GOLD, "n4@mail.com"),
                new User("5","n5","p5", Level.GOLD, 100, 100, "n5@mail.com")
        );
    }

    @Test public void upgradeNextLevelAllUsers(){
        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        userService.upgradeNextLevel();

        checkLevelUpgrade(users.get(0), false);
        checkLevelUpgrade(users.get(1), true);
        checkLevelUpgrade(users.get(2), false);
        checkLevelUpgrade(users.get(3), true);
        checkLevelUpgrade(users.get(4), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotUpgradeLevel(){
        Level[] levels = Level.values();
        User user = users.get(0);

        for(Level level: levels){
            if(level.nextLevel() != null) continue;
            user.setLevel(level);
            user.upgradeNextLevel();
        }
    }
    private void checkLevelUpgrade(User user, boolean upgraded) {
        User updatedUser = userDao.get2(user.getId());
        if(upgraded){
            assertThat(updatedUser.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(updatedUser.getLevel(), is(user.getLevel()));
        }

    }

    // 신규 가입 사용자의 레벨은 BASIC으로 한다면?
    // ++ 특별한 이유로 미리 레벨을 정해서 신규 생성해야한다면? (ex 새로운 관리자)
    @Test public void add(){
        userDao.deleteAll();

        // given
        User userWithLevel = users.get(4); // GOLD 레벨인 유저, 등록 후에도 GOLD 유지되는지 확인
        User userWITHOUTLevel = users.get(0); // newbie
             userWITHOUTLevel.setLevel(null); // 등록중 BASIC이 설정되어야 하니까 Level null 처리.

        // when
        userService.add(userWithLevel);
        userService.add(userWITHOUTLevel);

        // then
        User userWithLevelRead = userDao.get2(userWithLevel.getId());
        User userWITHOUTLevelRead = userDao.get2(userWITHOUTLevel.getId());

        assertThat(userWithLevelRead.getLevel(), is(userWithLevelRead.getLevel()));
        assertThat(userWITHOUTLevelRead.getLevel(), is(userWITHOUTLevelRead.getLevel()));
    }

    @Test
    @DirtiesContext // 본 테스트 클래스 컨텍스트를 이 테스트 케이스에서 덮어씀.
    public void upgradeAllOrNothing() throws Exception {
        TestUserServiceImpl testUserServiceImpl = new TestUserServiceImpl(users.get(3).getId());
        // TestUserService는 applicationContext에 추가 안한 bean이므로 userDao와 dataSource를 bean DI 해줌.
        testUserServiceImpl.setUserDao(this.userDao);
        testUserServiceImpl.setMailSender(this.mailSender);

        // 프록시 처리 할 때 데코레이션 적용 예::
        // context에서 UserServiceTx(UserService 공구리객체)가
        // UserServiceImpl(UserService 공구리객체)을 보게 함.
//        UserServiceTx txUserService = new UserServiceTx();
//        txUserService.setTransactionManager(this.transactionManager);
//        txUserService.setUserService(testUserService);

        // 다이내믹 프록시 처리 할 때::
//        TransactionHandler txHandler = new TransactionHandler(); // 프록시에서 타겟(TestUserService)이 뭔지 전달해줌.
//        txHandler.setTransactionManager(this.transactionManager);
//        txHandler.setTarget(testUserServiceImpl);
//        txHandler.setPattern("upgradeNextLevel");
//        UserService txUserService = (UserService) Proxy.newProxyInstance(
//                TransactionHandler.class.getClassLoader(),
//                new Class[] {UserService.class}, // 다이내믹 프록시(Proxy.newProxyInstance)가 구현해야할 인터페이스
//                txHandler
//        );

        // 다이내믹 프록시의 DI적용 팩토리빈::
        // upgradeAllOrNothing() 테스트가 현재 context상 운영 소스와는 달리
        // TestUserServiceImpl을 타겟으로 봐야하기 때문에
        // 팩토리빈이 타겟삼을 구현객체를 다시 지정해줬다.
//        TxProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", TxProxyFactoryBean.class);
//        txProxyFactoryBean.setTarget(testUserServiceImpl);
//        UserService txUserService = (UserService) txProxyFactoryBean.getObject();

        // 포인트컷 적용
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(testUserServiceImpl);
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("upgrade*");
        TransactionAdvice txAdvice = new TransactionAdvice();
        txAdvice.setTransactionManager(this.transactionManager);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, txAdvice));
        UserService txUserService = (UserService) pfBean.getObject();

        userDao.deleteAll();
        for(User user: users) userDao.add(user);

        try {
            txUserService.upgradeNextLevel();
            // 위 upgradeNextLevelAllUsers() 정상 종료 되면 본 테스트케이스 이상있으므로 실패처리
            fail("TestUserServiceException excepted, but exit 0");
        } catch (TestUserServiceException e){

        }

        checkLevelUpgrade(users.get(1), false);
    }

    @Test
    public void mockUpgradeLevels() throws Exception {
        /*
        * 셋팅
        * */
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        // mock 구조 생성
        IUserDaoJdbc mockUserDao = mock(IUserDaoJdbc.class);
        when(mockUserDao.getAll2()).thenReturn(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        /*
        * 실행
        * */
        userServiceImpl.upgradeNextLevel();

        /*
        * 결과 확인
        * */
        // verify() : 메소드 호출구조 확인
        // mockUserDao의 update() 가 2번 호출되어 파라미터 상관없이(any) User.class 오브젝트를 업데이트 하였는가?
        verify(mockUserDao, times(2)).update(any(User.class));
        // mockUserDao의 update() 가 users.get(1)을 업데이트 하였는가?
        verify(mockUserDao).update(users.get(1));
        // users.get(1) 결과 데이터 확인
        assertThat(users.get(1).getLevel(), is(Level.SILVER));
        // mockUserDao의 update() 가 users.get(3)을 업데이트 하였는가?
        verify(mockUserDao).update(users.get(3));
        // users.get(3) 결과 데이터 확인
        assertThat(users.get(3).getLevel(), is(Level.GOLD));

        // 메일 발송 결과 확인
        // mockMailSender 오브젝트가 send()를 두번 호출하였는가?
        // send 할 메세지를 캡처해놔.
        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class); // 메세지 캡처에 쓰는 ArgumentCaptor<T>
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        // 보낸 메일 메세지들의 검증.
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
        assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));

    }


}
