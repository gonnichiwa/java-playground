package springbook.user;

public class User {
    private String id;
    private String name;
    private String password;

    public User() {}

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
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

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }
}
