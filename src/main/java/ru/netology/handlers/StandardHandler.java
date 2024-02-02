package ru.netology.handlers;

import ru.netology.Request;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StandardHandler implements Handlers {

    @Override
    public void handle(Request request, BufferedOutputStream out) {
        try {
            final Path filePath = Path.of(".", "public", request.getPath());
            final String mimeType = Files.probeContentType(filePath);
            final long length = Files.size(filePath);
            out.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + mimeType + "\r\n" +
                            "Content-Length: " + length + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            Files.copy(filePath, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}