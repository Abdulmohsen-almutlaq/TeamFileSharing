package model;

public class User {
    public int id;
    public String username;
    public String email;
    public String password;
    public Integer teamId;

    public User(int id, String username, String email, String password, Integer teamId) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.teamId = teamId;
    }
}