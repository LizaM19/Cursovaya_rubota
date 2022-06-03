package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Game {
    long startt;
    long finish;
    public static void Start() throws IOException {
        System.out.println("Game");




        // double x = -200 + Math.random() * 460;
       // double y = Math.random() * 300;
       // int size = (int) (15+(Math.random() * 45));
       // Server.response = "#coordinates|" + x + "|" + y+"|" + size;

        //Server.writer.write("#coordinates|" + x + "|" + y + "\n"); //Отправка координат на сервер
        //Server.writer.flush();



        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        final Runnable runnable = new Runnable() {
            int countdownStarter = 20;

            public void run() {
                OutputStreamWriter writer;
                BufferedReader reader;
                try {
                    writer = new OutputStreamWriter(Server.clientDialog.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    reader = new BufferedReader(new InputStreamReader(Server.clientDialog.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                double x = -200 + Math.random() * 460;
                double y = Math.random() * 300;
                int size = (int) (15+(Math.random() * 45));
                Server.response = "#coordinates|" + x + "|" + y+"|" + size;
//Server.writer.write("#coordinates|" + x + "|" + y + "\n"); //Отправка координат на сервер
                //Server.writer.flush();
                System.out.println(countdownStarter);
                countdownStarter--;

                if (countdownStarter < 0) {
                    System.out.println("Timer Over!");
                    scheduler.shutdown();
                }
            }
        };
        scheduler.scheduleAtFixedRate(runnable, 0, 5, SECONDS);

    }
}
