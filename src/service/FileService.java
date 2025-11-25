package service;

import java.util.List;
import java.io.IOException;

import model.FileItem;
import repository.FileRepository;
import filehandler.LocalFileManager;

public class FileService {

    private FileRepository repo = new FileRepository();
    private LocalFileManager fileManager = new LocalFileManager();

    // CREATE
    public void createFile(String filename, String filepath, Integer teamId, Integer uploadedBy) {
        FileItem file = new FileItem(0, filename, filepath, teamId, uploadedBy);
        repo.create(file);
        System.out.println("File created: " + filename);
    }

    // READ by ID
    public FileItem getFile(int id) {
        return repo.findById(id);
    }

    // READ all
    public List<FileItem> getAllFiles() {
        return repo.findAll();
    }

    // READ by Team
    public List<FileItem> getFilesByTeam(int teamId) {
        return repo.findByTeamId(teamId);
    }

    // UPDATE
    public void updateFile(int id, String filename, String filepath, Integer teamId, Integer uploadedBy) {
        FileItem file = new FileItem(id, filename, filepath, teamId, uploadedBy);
        repo.update(file);
        System.out.println("File updated (ID " + id + ")");
    }

    // DELETE
    public void deleteFile(int id, int userId) throws Exception {
        FileItem file = repo.findById(id);
        if (file == null) {
            throw new Exception("File not found");
        }

        if (file.uploadedBy != userId) {
            throw new Exception("Unauthorized: You can only delete your own files.");
        }

        // Delete from disk
        try {
            fileManager.deleteFile(file.filepath);
        } catch (IOException e) {
            System.err.println("Warning: Could not delete file from disk: " + e.getMessage());
        }

        // Delete from DB
        repo.delete(id);
        System.out.println("File deleted (ID " + id + ") by user " + userId);
    }
}
