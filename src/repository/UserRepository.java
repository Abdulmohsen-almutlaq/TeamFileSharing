package repository;

import java.sql.*;
import java.util.*;

import model.User;
import database.Database;

public class UserRepository {

    public void create(User user) {
        try {
            var conn = Database.getInstance().getConnection();
            var stmt = conn.prepareStatement(
                "INSERT INTO users (username, email, userpassword, team_id) VALUES (?, ?, ?, ?)"
            );

            stmt.setString(1, user.username);
            stmt.setString(2, user.email);
            stmt.setString(3, user.password);
            stmt.setObject(4, user.teamId);

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User findById(int id) {
        try {
            var conn = Database.getInstance().getConnection();
            var stmt = conn.prepareStatement(
                "SELECT * FROM users WHERE id=?"
            );
            stmt.setInt(1, id);

            var rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("userpassword"),
                    (Integer) rs.getObject("team_id")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        try {
            var conn = Database.getInstance().getConnection();
            var stmt = conn.prepareStatement("SELECT * FROM users");
            var rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(
                    new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("userpassword"),
                        (Integer) rs.getObject("team_id")
                    )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void update(User user) {
        try {
            var conn = Database.getInstance().getConnection();
            var stmt = conn.prepareStatement(
                "UPDATE users SET username=?, email=?, userpassword=?, team_id=? WHERE id=?"
            );

            stmt.setString(1, user.username);
            stmt.setString(2, user.email);
            stmt.setString(3, user.password);
            stmt.setObject(4, user.teamId);
            stmt.setInt(5, user.id);

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try {
            var conn = Database.getInstance().getConnection();
            var stmt = conn.prepareStatement(
                "DELETE FROM users WHERE id=?"
            );

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User findByEmail(String email) {
        try {
            var conn = Database.getInstance().getConnection();
            var stmt = conn.prepareStatement(
                "SELECT * FROM users WHERE email=?"
            );
            stmt.setString(1, email);

            var rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("userpassword"),
                    (Integer) rs.getObject("team_id")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
