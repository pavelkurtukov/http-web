package ru.netology;

import ru.netology.handlers.HandlerForClassic;
import ru.netology.handlers.StandardHandler;

import java.net.URISyntaxException;

public class Main  {
  public static final int PORT = 9999;
  public static final int THREADS = 64;

  public static void main(String[] args) throws URISyntaxException {
    Server server = new Server(THREADS);

    // добавление хендлеров (обработчиков)
    server.addHandler("GET", "/index.html", new StandardHandler());
    server.addHandler("GET", "/spring.svg", new StandardHandler());
    server.addHandler("GET", "/classic.html", new HandlerForClassic());

    server.start(PORT);
  }
}