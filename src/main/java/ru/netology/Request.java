package ru.netology;

public class Request {
    private final String method;
    private final String path;
    private final String version;

    private Request(String method, String path, String version) {
        this.method = method;
        this.path = path;
        this.version = version;
    }

    public static Request parseRequest(String[] parts) {
        String method = parts[0];
        String path = parts[1];
        String version = parts[2];
        return new Request(method, path, version);
    }

    public String getMethod() {
        return method;
    }


    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }
}