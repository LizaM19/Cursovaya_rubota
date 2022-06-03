package com.example.clientfx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ContactEditorUI {
    public static Socket clientSocket;
    public static BufferedReader reader;
    public static OutputStreamWriter writer;
    private static String nameUser;
    public static Boolean work = true;
    public ImageView img;
    long startTimer;
    long finishTimer;
    public Button quit;
    public Label won;
    public VBox form;

    @FXML
    private TextField tName;
    @FXML
    private Label name;
    @FXML
    private Button start;

    @FXML
    private Label hello;
    public Integer numberClick;
    public Integer allClick;
    public boolean stop;
    public Double result;

    @FXML
    protected void onHelloButtonClick() throws IOException {

        try {
            try {
                String host = "127.0.0.1";
                int port = 8008;
                clientSocket = new Socket(host, port);
                if (clientSocket == null) {

                    System.out.println("Не удалось подключиться к серверу");
                }
                writer = new OutputStreamWriter(clientSocket.getOutputStream());
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                nameUser = tName.getText();

                start.setVisible(false);
                hello.setVisible(false);
                tName.setVisible(false);
                //name.setVisible(true);
                img.setVisible(true);

                // name.setText("Name: " + nameUser);
                writer.write(nameUser + "\n");
                writer.write("#game\n"); // отправляем сообщение на сервер
                writer.flush();
                startTimer = System.currentTimeMillis();
                try {
                    JThread t = new JThread("JThread", this);
                    t.start();

                } catch (Exception e) {
                    if (e.getCause() != null) {
                        System.out.println(e.getMessage());
                    } else {
                        System.out.println(e.getMessage());
                    }
                }
            } finally {
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }


    public void cliclM(MouseEvent mouseEvent) throws IOException, InterruptedException {
        System.out.println("Star hit ");
        writer.write("#click\n"); // отправляем сообщение на сервер
        writer.flush();

        if (stop == true) {
            writer.write("#stop\n"); // отправляем сообщение на сервер
            writer.flush();
            img.setVisible(false);
            end();
        }
    }

    public void end() throws InterruptedException {
        won.setVisible(true);
        quit.setVisible(true);
        finishTimer = System.currentTimeMillis();
        long elapsed = (finishTimer - startTimer) / 1000;
        Thread.sleep(300);
        won.setText("Congratulations, " + nameUser + " You did it in " + elapsed + " seconds \n" + "Number of hits: " + numberClick + "\\" + allClick + "\n" +
                "Average reaction rate: " + result);
        System.out.println("Time, s: " + elapsed);
    }

    public void close(MouseEvent mouseEvent) throws IOException { //Кнопка закрытия приложения

        writer.write("#quit\n");
        System.out.printf("%s fiished... \n", Thread.currentThread().getName());
        writer.close();
        reader.close();
        work = false;
        clientSocket.close();
        Stage stage = (Stage) quit.getScene().getWindow();
        stage.close();
    }
}