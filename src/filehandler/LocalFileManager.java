package filehandler;

import java.io.IOException;
import java.nio.file.*;

public class LocalFileManager {
    
    private static final String STORAGE_DIR = "storage/";

    public String saveFile(String filename, byte[] content, int teamId) throws IOException {
        Path storagePath = Paths.get(STORAGE_DIR, "team_" + teamId);
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }

        // Create a unique filename to avoid collisions
        String uniqueFilename = System.currentTimeMillis() + "_" + filename;
        Path filePath = storagePath.resolve(uniqueFilename);
        
        Files.write(filePath, content);
        
        return filePath.toString();
    }

    public byte[] readFileBytes(String pathStr) throws IOException {
        Path path = Paths.get(pathStr);
        if (Files.exists(path)) {
            return Files.readAllBytes(path);
        }
        throw new IOException("File not found: " + pathStr);
    }

    public String readFile(String pathStr) throws IOException {
        return new String(readFileBytes(pathStr));
    }

    public void deleteFile(String pathStr) throws IOException {
        Path path = Paths.get(pathStr);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }
}