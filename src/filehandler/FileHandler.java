package filehandler;

import java.io.IOException;

public interface FileHandler {
    void upload(String source, String target) throws IOException;
    void download(String source, String target) throws IOException;
    void delete(String path) throws IOException;
}
