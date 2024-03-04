package springbook.user;

public class User {
    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;

    public User() {}

    public User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    public String getName(){
        return this.name;
    }
    public String getId() {
        return id;
    }
    public String getPassword() {
        return password;
    }
    public Level getLevel() {
        return level;
    }
    public int getLogin() {
        return login;
    }
    public int getRecommend() {
        return recommend;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setLevel(Level level) {
        this.level = level;
    }
    public void setLogin(int login) {
        this.login = login;
    }
    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }
}
