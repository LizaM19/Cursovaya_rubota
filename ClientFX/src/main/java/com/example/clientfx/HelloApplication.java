package com.example.clientfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 524, 374);
        stage.setTitle("Games");
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void stop() throws IOException {

        ContactEditorUI.writer.write("quit\n");
        System.out.printf("%s fiished... \n", Thread.currentThread().getName());
        ContactEditorUI.writer.close();
        ContactEditorUI.reader.close();
        ContactEditorUI.clientSocket.close();
    }
    public static void main(String[] args) {
        launch();
    }
}