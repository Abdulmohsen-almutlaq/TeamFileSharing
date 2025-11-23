package service;

import java.util.List;

import model.FileItem;
import repository.FileRepository;

public class FileService {

    private FileRepository repo = new FileRepository();

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

    // UPDATE
    public void updateFile(int id, String filename, String filepath, Integer teamId, Integer uploadedBy) {
        FileItem file = new FileItem(id, filename, filepath, teamId, uploadedBy);
        repo.update(file);
        System.out.println("File updated (ID " + id + ")");
    }

    // DELETE
    public void deleteFile(int id) {
        repo.delete(id);
        System.out.println("File deleted (ID " + id + ")");
    }
}
