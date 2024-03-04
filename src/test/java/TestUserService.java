import springbook.user.User;
import springbook.user.service.UserService;

public class TestUserService extends UserService {
    private String id;

    public TestUserService(String id) {
        this.id = id;
    }

    @Override
    protected void upgradeNextLevel(User user) {
        if(user.getId().equalsIgnoreCase(this.id)) throw new TestUserServiceException();
        super.upgradeNextLevel(user);
    }
}
