package springbook.user.dao;

import springbook.user.User;

import java.util.List;

public interface IUserDao {
    void add(User user);
    User get(String id);
    User get2(String id);
    List<User> getAll();
    List<User> getAll2();
    void deleteAll();
    int getCount();
    int getCount2();
    int getCount3();
    void update(User user);
}
