package springbook.user.service;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.user.Level;
import springbook.user.User;
import springbook.user.dao.IUserDao;

import javax.sql.DataSource;
import java.util.List;

public class UserService {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMAND_FOR_GOLD = 30;

    private IUserDao userDao;
    private PlatformTransactionManager transactionManager;

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
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
                }
            }
            this.transactionManager.commit(status);
        } catch (RuntimeException e){
            this.transactionManager.rollback(status);
            throw e;
        }
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
