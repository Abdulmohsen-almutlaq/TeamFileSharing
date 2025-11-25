package repository;

import java.sql.*;
import java.util.*;

import model.Team;
import database.Database;

public class TeamRepository {

    public void create(Team team) {
        String sql = "INSERT INTO teams (name) VALUES (?)";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, team.name);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("CREATE Team failed: " + e.getMessage());
        }
    }

    public Team findById(int id) {
        String sql = "SELECT * FROM teams WHERE id=?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Team(
                    rs.getInt("id"),
                    rs.getString("name")
                );
            }

        } catch (SQLException e) {
            System.err.println("FIND Team failed: " + e.getMessage());
        }

        return null;
    }

    public List<Team> findAll() {
        List<Team> list = new ArrayList<>();
        String sql = "SELECT * FROM teams";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Team(
                    rs.getInt("id"),
                    rs.getString("name")
                ));
            }

        } catch (SQLException e) {
            System.err.println("FIND ALL Teams failed: " + e.getMessage());
        }

        return list;
    }

    public void update(Team team) {
        String sql = "UPDATE teams SET name=? WHERE id=?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, team.name);
            stmt.setInt(2, team.id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("UPDATE Team failed: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM teams WHERE id=?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("DELETE Team failed: " + e.getMessage());
        }
    }
}
