package com.company;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

class Main {
    static ExecutorService executeIt = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {

        var port = 8008;
        try (
                ServerSocket serverSocket = new ServerSocket(port))
        {
            System.out.println("The server started locally (localhost) on the port: " + port);

            // стартуем цикл при условии, что серверный сокет не закрыт
            while (!serverSocket.isClosed()) {

                // подключения к сокету общения под именем - "clientDialog" на
                // серверной стороне
                Socket client = serverSocket.accept();
                // после получения запроса на подключение сервер создаёт сокет
                // для общения с клиентом и отправляет его в отдельную нить
                // в Runnable(при необходимости можно создать Callable)
                executeIt.execute(new Server(client));
                System.out.print("Connection accepted.");
            }
            // закрытие пула нитей после завершения работы всех нитей
            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
