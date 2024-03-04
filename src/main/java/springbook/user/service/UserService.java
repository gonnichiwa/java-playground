package springbook.user.service;

import springbook.user.Level;
import springbook.user.User;
import springbook.user.dao.IUserDao;
import springbook.user.dao.UserDao;

import java.util.List;

public class UserService {
    IUserDao userDao;
    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    // 가입 후 50회 이상 로그인 하면 BASIC -> SILVER
    // SILVER 레벨 && 30번 추천이면 SILVER -> GOLD
    // 레벨 변경은 일정한 주기로 수행. 변경작업전에는 조건 충족하더라도 레벨 변경 없음.
    public void upgradeLevels(){
        List<User> users = userDao.getAll2();
//        for(User user : users){
//            Boolean changed = null;
//            if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
//                user.setLevel(Level.SILVER);
//                changed = true;
//            }
//            else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30){
//                user.setLevel(Level.GOLD);
//                changed = true;
//            }
//            else if (user.getLevel() == Level.GOLD) { changed = false; }
//            else { changed = false;}
//            if(changed) {
//                userDao.update(user);
//            }
//        }
    }

    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
