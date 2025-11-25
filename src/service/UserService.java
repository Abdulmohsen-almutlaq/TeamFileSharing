package service;

import repository.UserRepository;
import model.User;

public class UserService {
    private UserRepository repo = new UserRepository();

    public void createUser(String username, String email, String password, Integer teamId) {
        User user = new User(0, username, email, password, teamId);
        repo.create(user);
    }

    public User getUser(int id) {
        return repo.findById(id);
    }

    public void updateUser(User user) {
        repo.update(user);
    }

    public void deleteUser(int id) {
        repo.delete(id);
    }

    public User login(String email, String password) {
        User user = repo.findByEmail(email);
        if (user != null && user.password.equals(password)) {
            return user;
        }
        return null;
    }
}
