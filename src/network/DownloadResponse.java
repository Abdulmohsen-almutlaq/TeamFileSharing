package network;

public class DownloadResponse {
    public String filename;
    public byte[] data;

    public DownloadResponse(String filename, byte[] data) {
        this.filename = filename;
        this.data = data;
    }
}
