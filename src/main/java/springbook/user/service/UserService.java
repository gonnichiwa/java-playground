package springbook.user.service;

import springbook.user.Level;
import springbook.user.User;
import springbook.user.dao.IUserDao;

import java.util.List;

public class UserService {
    IUserDao userDao;
    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    // 가입 후 50회 이상 로그인 하면 BASIC -> SILVER
    // SILVER 레벨 && 30번 추천이면 SILVER -> GOLD
    // 레벨 변경은 일정한 주기로 수행. 변경작업전에는 조건 충족하더라도 레벨 변경 없음.
    public void upgradeNextLevelAllUsers(){
        List<User> users = userDao.getAll2();
        for(User user : users){
            if(canUpgradeNextLevel(user)){
                upgradeNextLevel(user);
            }
        }
    }

    private void upgradeNextLevel(User user) {
        user.upgradeNextLevel(); // 서비스가 다음레벨 뭔지까지 설정해줄려니 책임이 무겁다..
        userDao.update(user);
    }

    private boolean canUpgradeNextLevel(User user) {
        Level currentLev = user.getLevel();
        switch (currentLev){
            case BASIC: return (user.getLogin() >= 50);
            case SILVER: return (user.getRecommend() >= 30);
            case GOLD : return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLev);
        }
    }

    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
