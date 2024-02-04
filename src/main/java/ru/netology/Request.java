package ru.netology;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Request {
    private final String method;
    private final String path;
    private final String version;
    private final List<NameValuePair> queryParams;

    private Request(String method, String path, String version) throws URISyntaxException {
        this.method = method;
        this.path = path.contains("?") ? path.substring(0, path.indexOf("?")) : path;
        this.version = version;
        this.queryParams = URLEncodedUtils.parse(new URI(path), StandardCharsets.UTF_8);
        // Демонстрация функционала
        System.out.println("path = " + this.path);
        System.out.println("Список параметров: " + this.queryParams.toString());
        System.out.println("Конкретный параметр (test): " + getQueryParam("test"));
    }

    public static Request parseRequest(String[] parts) throws URISyntaxException {
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

    public List<NameValuePair> getQueryParams() {
        return queryParams;
    }

    public String getQueryParam(String paramName) {
        String paramValue = queryParams.stream()
                            .filter(nameValuePair -> nameValuePair.getName().equals(paramName))
                            .findFirst()
                            .map(NameValuePair::getValue)
                            .orElse(null);

        return paramValue;
    }
}