package repository;

import java.sql.*;
import java.util.*;

import model.FileItem;
import database.Database;

public class FileRepository {

    public void create(FileItem file) {
        String sql = "INSERT INTO files (filename, filepath, team_id, uploaded_by) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, file.filename);
            stmt.setString(2, file.filepath);
            stmt.setObject(3, file.teamId);
            stmt.setObject(4, file.uploadedBy);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("CREATE File failed: " + e.getMessage());
        }
    }

    public FileItem findById(int id) {
        String sql = "SELECT * FROM files WHERE id=?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new FileItem(
                    rs.getInt("id"),
                    rs.getString("filename"),
                    rs.getString("filepath"),
                    (Integer) rs.getObject("team_id"),
                    (Integer) rs.getObject("uploaded_by")
                );
            }

        } catch (SQLException e) {
            System.err.println("FIND File failed: " + e.getMessage());
        }

        return null;
    }

    public List<FileItem> findAll() {
        List<FileItem> list = new ArrayList<>();
        String sql = "SELECT * FROM files";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new FileItem(
                    rs.getInt("id"),
                    rs.getString("filename"),
                    rs.getString("filepath"),
                    (Integer) rs.getObject("team_id"),
                    (Integer) rs.getObject("uploaded_by")
                ));
            }

        } catch (SQLException e) {
            System.err.println("FIND ALL Files failed: " + e.getMessage());
        }

        return list;
    }

    public List<FileItem> findByTeamId(int teamId) {
        List<FileItem> list = new ArrayList<>();
        String sql = "SELECT * FROM files WHERE team_id=?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teamId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new FileItem(
                    rs.getInt("id"),
                    rs.getString("filename"),
                    rs.getString("filepath"),
                    (Integer) rs.getObject("team_id"),
                    (Integer) rs.getObject("uploaded_by")
                ));
            }

        } catch (SQLException e) {
            System.err.println("FIND Files by Team failed: " + e.getMessage());
        }

        return list;
    }

    public void update(FileItem file) {
        String sql = "UPDATE files SET filename=?, filepath=?, team_id=?, uploaded_by=? WHERE id=?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, file.filename);
            stmt.setString(2, file.filepath);
            stmt.setObject(3, file.teamId);
            stmt.setObject(4, file.uploadedBy);
            stmt.setInt(5, file.id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("UPDATE File failed: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM files WHERE id=?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("DB: Deleted file with ID " + id);
            } else {
                System.out.println("DB: File with ID " + id + " not found or already deleted.");
            }

        } catch (SQLException e) {
            System.err.println("DELETE File failed: " + e.getMessage());
        }
    }
}
