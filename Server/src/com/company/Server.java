package com.company;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Server implements Runnable {
    public static InetAddress ipAddress;
    ArrayList<Long> nums = new ArrayList<>(); // Массив реакций пользователей в секундах

    public static Socket clientDialog;
    public static String response;
    long start;
    long finish;
    OutputStreamWriter writer;
    BufferedReader reader;
    Integer countClick = 0;
    Integer autoClick = 0;
    int i = 0;
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public Server(Socket client) {
        Server.clientDialog = client;
    }

    public void newCoordinates() {
        finish = System.currentTimeMillis();
        long elapsed = (finish - start) / 1000;
        nums.add(elapsed);
        double x = -200 + Math.random() * 460;
        double y = Math.random() * 300;
        int size = (int) (15 + (Math.random() * 45));
        response = "#coordinates|" + x + "|" + y + "|" + size;
        ++i;
        start = System.currentTimeMillis();
    }

    static double average(ArrayList<Long> list) { //Подсчет среднего значения в списке

        double sum = 0;
        for (int i = 1; i < list.size(); i++) {
            sum += list.get(i);
        }
        return sum / list.size();
    }

    public void timer() {

        final Runnable runnable = new Runnable() {
            int countdownStarter = 20;

            public void run() {
                double x = -200 + Math.random() * 460;
                double y = Math.random() * 300;
                int size = (int) (15 + (Math.random() * 45));
                try {
                    writer.write("#coordinates|" + x + "|" + y + "|" + size + "\n"); //Отправка координат на сервер
                    writer.flush();
                    ++autoClick;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(countdownStarter);
                countdownStarter--;
                if (countdownStarter < 0) {
                    try {
                        writer.write("stop\n");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Timer Over! - " + countClick);
                    scheduler.shutdown();
                }
            }
        };
        scheduler.scheduleAtFixedRate(runnable, 0, 1, SECONDS);
    }

    @Override
    public void run() {
        Thread current = Thread.currentThread();
        try {
            // инициируем каналы общения в сокете, для сервера
            writer = new OutputStreamWriter(clientDialog.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(clientDialog.getInputStream()));
            // String userName=reader.readLine();
            ipAddress = clientDialog.getInetAddress();
            System.out.println("Client " + reader.readLine() + " connected");
            writer.write("Success \n");
            writer.flush();
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // основная рабочая часть //
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            // начинаем диалог с подключенным клиентом в цикле, пока сокет не
            // закрыт клиентом
            while (!clientDialog.isClosed()) {
                System.out.println("Server reading from channel");
                // серверная нить ждёт в канале чтения (input stream) получения
                // данных клиента после получения данных считывает их
                response = reader.readLine();
                System.out.println("Request: " + response);
                if (response.equalsIgnoreCase("quit")) {

                    // если кодовое слово получено, то инициализируется закрытие
                    // серверной нити
                    System.out.println("Client initialize connections suicide ...");
                    writer.write("Server reply - " + response + "\n");

                    Thread.sleep(300);
                    break;
                }

                String[] commands = response.split("#");
                for (String command : commands) {
                    if (command.isEmpty()) {
                        continue;
                    }
                    if (command.contains("game")) {
                        System.out.println("game");
                        timer();
                    }
                    if (command.contains("click")) {
                        System.out.println("click");
                        newCoordinates();
                        ++countClick;
                        writer.write("Server reply - " + response + "\n");
                    }
                    if (command.contains("quit")) {
                        System.out.println("quit");
                        current.interrupt();
                    }
                    if (command.contains("stop")) {
                        System.out.println("stop");
                        System.out.println(nums + "\n");
                        System.out.println(average(nums));

                        writer.write("countClick|" + countClick + "|" + autoClick + "|" + average(nums) + "\n");
                        System.out.println("Timer Over!" + countClick);
                        scheduler.shutdown();
                    }
                }

                // освобождаем буфер сетевых сообщений
                writer.flush();
                // возвращаемся в начало для считывания нового сообщения
            }
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // основная рабочая часть //
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            scheduler.shutdown();
            // если условие выхода - верно выключаем соединения
            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");

            // закрываем сначала каналы сокета !
            reader.close();
            writer.close();

            // потом закрываем сокет общения с клиентом в нити моносервера
            clientDialog.close();
            System.out.println("Closing connections & channels - DONE.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
