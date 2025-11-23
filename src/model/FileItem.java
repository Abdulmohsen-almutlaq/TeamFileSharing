package model;

public class FileItem {
    public int id;
    public String filename;
    public String filepath;
    public Integer teamId;
    public Integer uploadedBy;

    public FileItem(int id, String filename, String filepath, Integer teamId, Integer uploadedBy) {
        this.id = id;
        this.filename = filename;
        this.filepath = filepath;
        this.teamId = teamId;
        this.uploadedBy = uploadedBy;
    }
}
