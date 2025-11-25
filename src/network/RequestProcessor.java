package network;

import service.UserService;
import service.FileService;
import filehandler.LocalFileManager;
import model.User;
import model.FileItem;

import java.util.List;
import java.io.IOException;

public class RequestProcessor {
    private final UserService userService;
    private final FileService fileService;
    private final LocalFileManager fileManager;

    public RequestProcessor() {
        this.userService = new UserService();
        this.fileService = new FileService();
        this.fileManager = new LocalFileManager();
    }

    public Object handleRequest(String method, String fullPath, byte[] bodyBytes) {
        RequestParser parser = new RequestParser(fullPath);
        String body = new String(bodyBytes);
        String path = parser.getPath();
        
        switch (path) {
            case "/":
            case "/index.html":
                return serveIndexHtml();
            case "/style.css":
                return serveStaticFile("src/style.css");
            case "/script.js":
                return serveStaticFile("src/script.js");
            case "/login":
                return processLogin(parser, body);
            case "/register":
                return processRegister(parser, body);
            case "/upload":
                return processUpload(parser, bodyBytes);
            case "/files":
                return processListFiles(parser);
            case "/download":
                return processDownload(parser);
            case "/delete":
                return processDelete(parser);
            default:
                return handleUnknownEndpoint(method);
        }
    }

    private String serveIndexHtml() {
        try {
            return fileManager.readFile("src/index.html");
        } catch (IOException e) {
            return "Error loading index.html: " + e.getMessage();
        }
    }

    private String serveStaticFile(String path) {
        try {
            return fileManager.readFile(path);
        } catch (IOException e) {
            return "Error loading file: " + e.getMessage();
        }
    }

    private String processLogin(RequestParser parser, String body) {
        parser.parseBodyIfEmpty(body, "email", "password");

        String email = parser.get("email");
        String password = getPassword(parser);

        if (email == null || password == null) {
            return "Usage: /login?email=...&password=... or POST body";
        }

        User user = userService.login(email, password);
        if (user == null) {
            return "Login Failed";
        }
        
        return String.format("LOGIN_SUCCESS,%d,%s,%d", user.id, user.username, user.teamId);
    }

    private String processRegister(RequestParser parser, String body) {
        parser.parseBodyIfEmpty(body, "username", "email", "password", "teamId");

        String username = parser.get("username");
        String email = parser.get("email");
        String password = getPassword(parser);
        String teamIdStr = parser.get("teamId");

        if (username == null || email == null || password == null) {
            return "Usage: /register?username=...&email=...&password=...&teamId=... or POST body";
        }

        try {
            Integer teamId = parseTeamId(teamIdStr);
            userService.createUser(username, email, password, teamId);
            return "Registration Successful";
        } catch (Exception e) {
            return "Registration Failed";
        }
    }

    private String processUpload(RequestParser parser, byte[] body) {
        String filename = parser.get("filename");
        String uploaderIdStr = parser.get("uploaderId");
        String teamIdStr = parser.get("teamId");

        if (isInvalidUploadRequest(filename, uploaderIdStr, teamIdStr, body)) {
            return "Usage: /upload?filename=...&uploaderId=...&teamId=... [Body: Content]";
        }

        try {
            int uploaderId = Integer.parseInt(uploaderIdStr);
            int teamId = Integer.parseInt(teamIdStr);

            String savedPath = fileManager.saveFile(filename, body, teamId);
            fileService.createFile(filename, savedPath, teamId, uploaderId);

            return "File Uploaded Successfully: " + filename;
        } catch (Exception e) {
            e.printStackTrace();
            return "Upload Failed: " + e.getMessage();
        }
    }

    private String processListFiles(RequestParser parser) {
        String teamIdStr = parser.get("teamId");
        if (teamIdStr == null) {
            return "Usage: /files?teamId=...";
        }

        try {
            int teamId = Integer.parseInt(teamIdStr);
            List<FileItem> files = fileService.getFilesByTeam(teamId);
            if (files.isEmpty()) {
                return "No files found.";
            }
            return formatFileList(files);
        } catch (NumberFormatException e) {
            return "Invalid Team ID";
        }
    }

    private Object processDownload(RequestParser parser) {
        String fileIdStr = parser.get("fileId");
        if (fileIdStr == null) {
            return "Usage: /download?fileId=...";
        }

        try {
            int fileId = Integer.parseInt(fileIdStr);
            FileItem file = fileService.getFile(fileId);
            
            if (file == null) {
                return "File not found with ID: " + fileId;
            }

            byte[] data = fileManager.readFileBytes(file.filepath);
            return new DownloadResponse(file.filename, data);
        } catch (NumberFormatException e) {
            return "Invalid file ID format.";
        } catch (Exception e) {
            return "Download Failed: " + e.getMessage();
        }
    }

    private String processDelete(RequestParser parser) {
        String fileIdStr = parser.get("fileId");
        String userIdStr = parser.get("userId");

        if (fileIdStr == null || userIdStr == null) {
            return "Usage: /delete?fileId=...&userId=...";
        }

        try {
            int fileId = Integer.parseInt(fileIdStr);
            int userId = Integer.parseInt(userIdStr);

            fileService.deleteFile(fileId, userId);
            return "File Deleted Successfully";
        } catch (NumberFormatException e) {
            return "Invalid ID format";
        } catch (Exception e) {
            return "Delete Failed: " + e.getMessage();
        }
    }

    private String handleUnknownEndpoint(String method) {
        if ("GET".equalsIgnoreCase(method)) {
            return "Welcome to TeamFileSharing! Use POST /register or /login";
        }
        return "Unknown Endpoint";
    }

    // --- Helper Methods ---

    private String getPassword(RequestParser parser) {
        String password = parser.get("password");
        return (password != null) ? password : parser.get("passowrd"); // Legacy typo support
    }

    private Integer parseTeamId(String teamIdStr) {
        return (teamIdStr != null && !teamIdStr.isEmpty()) ? Integer.parseInt(teamIdStr) : null;
    }

    private boolean isInvalidUploadRequest(String filename, String uploaderId, String teamId, byte[] body) {
        return filename == null || uploaderId == null || teamId == null || body.length == 0;
    }

    private String formatFileList(List<FileItem> files) {
        StringBuilder sb = new StringBuilder("Files:\n");
        for (FileItem file : files) {
            sb.append(String.format("ID: %d | Name: %s | TeamID: %d | UploaderID: %d\n", 
                file.id, file.filename, file.teamId, file.uploadedBy));
        }
        return sb.toString();
    }
}
