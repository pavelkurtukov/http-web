package ru.netology;

import ru.netology.handlers.Handlers;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    //    private static final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
    private static ExecutorService threadPool;
    private static ConcurrentHashMap<String, Map<String, Handlers>> handlers;

    public Server(int threads) {
        threadPool = Executors.newFixedThreadPool(threads);
        handlers = new ConcurrentHashMap<>();
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(() -> {
                    try {
                        connect(clientSocket);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(Socket clientSocket) throws URISyntaxException {
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             final BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream())) {

            // read only request line for simplicity
            // must be in form GET /path HTTP/1.1
            final String requestLine = in.readLine();
            final String[] parts = requestLine.split(" ");
            if (parts.length != 3) {
                return;
            }
            final Request request = Request.parseRequest(parts);

            if (!handlers.get(request.getMethod()).containsKey(request.getPath())) {
                notFound(out);
                return;
            }

            handlers.get(request.getMethod()).get(request.getPath()).handle(request, out);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void notFound(BufferedOutputStream out) throws IOException {
        out.write((
                "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.flush();
    }

    public void addHandler(String method, String path, Handlers handlers) {
        if (!Server.handlers.containsKey(method)) {
            Server.handlers.put(method, new HashMap<>());
        }
        Server.handlers.get(method).put(path, handlers);
    }
}