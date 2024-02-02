package ru.netology.handlers;

import ru.netology.Request;

import java.io.BufferedOutputStream;

@FunctionalInterface
public interface Handlers {
    void handle(Request request, BufferedOutputStream out);
}