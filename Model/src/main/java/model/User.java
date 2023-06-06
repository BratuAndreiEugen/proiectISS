package model;

public class User {
    private Integer id;
    private String email;
    private String username;
    private Integer admin;

    public User(){};

    public User(Integer id, String email, String username, Integer admin) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.admin = admin;
    }

    public User(String email, String username, Integer admin) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.admin = admin;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", admin=" + admin +
                '}';
    }
}
