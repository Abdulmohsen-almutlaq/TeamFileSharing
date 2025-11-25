package network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final RequestProcessor requestProcessor;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.requestProcessor = new RequestProcessor();
    }

    @Override
    public void run() {
        try (
            BufferedInputStream in = new BufferedInputStream(clientSocket.getInputStream());
            OutputStream out = clientSocket.getOutputStream()
        ) {
            handleClientRequest(in, out);
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            closeSocket();
        }
    }

    private void handleClientRequest(BufferedInputStream in, OutputStream out) throws IOException {
        String requestLine = readLine(in);
        if (requestLine == null || requestLine.isEmpty()) return;
        
        System.out.println("Request: " + requestLine);
        
        String[] requestParts = parseRequestLine(requestLine);
        String method = requestParts[0];
        String path = requestParts[1];

        int contentLength = readContentLength(in);
        byte[] bodyBytes = readBody(in, contentLength);

        Object response = requestProcessor.handleRequest(method, path, bodyBytes);
        sendResponse(out, response, path);
    }

    private String[] parseRequestLine(String requestLine) {
        String[] parts = requestLine.split(" ");
        String method = parts.length > 0 ? parts[0] : "GET";
        String path = parts.length > 1 ? parts[1] : "/";
        return new String[]{method, path};
    }

    private int readContentLength(BufferedInputStream in) throws IOException {
        int contentLength = 0;
        String line;
        while ((line = readLine(in)) != null && !line.isEmpty()) {
            if (line.startsWith("Content-Length:")) {
                try {
                    contentLength = Integer.parseInt(line.substring("Content-Length:".length()).trim());
                } catch (NumberFormatException ignored) {
                    // Ignore invalid content length headers
                }
            }
        }
        return contentLength;
    }

    private byte[] readBody(BufferedInputStream in, int contentLength) throws IOException {
        if (contentLength <= 0) return new byte[0];
        
        byte[] bodyBytes = new byte[contentLength];
        int bytesRead = 0;
        while (bytesRead < contentLength) {
            int result = in.read(bodyBytes, bytesRead, contentLength - bytesRead);
            if (result == -1) break;
            bytesRead += result;
        }
        return bodyBytes;
    }

    private void sendResponse(OutputStream out, Object response, String path) throws IOException {
        byte[] responseBytes;
        String filename = null;
        String contentType = "text/plain";

        if (response instanceof String) {
            responseBytes = ((String) response).getBytes(StandardCharsets.UTF_8);
            if (path.equals("/") || path.equals("/index.html")) {
                contentType = "text/html";
            } else if (path.endsWith(".css")) {
                contentType = "text/css";
            } else if (path.endsWith(".js")) {
                contentType = "application/javascript";
            }
        } else if (response instanceof DownloadResponse) {
            DownloadResponse dr = (DownloadResponse) response;
            responseBytes = dr.data;
            filename = dr.filename;
            contentType = "application/octet-stream";
        } else if (response instanceof byte[]) {
            responseBytes = (byte[]) response;
        } else {
            responseBytes = new byte[0];
        }

        writeHeaders(out, contentType, responseBytes.length, filename);
        out.write(responseBytes);
        out.flush();
    }

    private void writeHeaders(OutputStream out, String contentType, int length, String filename) throws IOException {
        out.write("HTTP/1.1 200 OK\r\n".getBytes(StandardCharsets.UTF_8));
        out.write(("Content-Type: " + contentType + "\r\n").getBytes(StandardCharsets.UTF_8));
        if (filename != null) {
            out.write(("Content-Disposition: attachment; filename=\"" + filename + "\"\r\n").getBytes(StandardCharsets.UTF_8));
        }
        out.write(("Content-Length: " + length + "\r\n").getBytes(StandardCharsets.UTF_8));
        out.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }

    private void closeSocket() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readLine(BufferedInputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = in.read()) != -1) {
            if (c == '\r') {
                in.mark(1);
                int next = in.read();
                if (next != '\n') {
                    in.reset();
                }
                break;
            } else if (c == '\n') {
                break;
            }
            sb.append((char) c);
        }
        if (c == -1 && sb.length() == 0) return null;
        return sb.toString();
    }
}
