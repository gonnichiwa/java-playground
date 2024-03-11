import springbook.user.User;
import springbook.user.service.UserServiceImpl;

public class TestUserServiceImpl extends UserServiceImpl {
    private String id;

    public TestUserServiceImpl(String id) {
        this.id = id;
    }

    @Override
    protected void upgradeNextLevel(User user) {
        if(user.getId().equalsIgnoreCase(this.id)) throw new TestUserServiceException();
        super.upgradeNextLevel(user);
    }
}
