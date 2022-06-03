package com.example.clientfx;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JThread extends Thread {

    ContactEditorUI editor;

    public JThread(String name, ContactEditorUI editor) {
        super(name);
        this.editor = editor;
    }

    public void run() {

        System.out.printf("%s started... \n", Thread.currentThread().getName());

        try {
            while (editor.work) {

                String coord = ContactEditorUI.reader.readLine();
                System.out.println(coord);
                if (coord.contains("coordinates")) {
                    String[] Arguments = coord.split("\\|");
                    double x = Double.parseDouble(Arguments[1]);
                    double y = Double.parseDouble(Arguments[2]);
                    int size = Integer.parseInt(Arguments[3]);
                    System.out.println(x);
                    System.out.println(y);
                    System.out.println(size);

                    editor.img.setTranslateY(y);
                    editor.img.setTranslateX(x);
                    editor.img.setFitHeight(size);
                    editor.img.setFitWidth(size);
                }
                if (coord.contains("stop")) {
                    editor.stop = true;
                }
                if (coord.contains("countClick")) {
                    String[] Arguments = coord.split("\\|");

                    editor.numberClick = Integer.parseInt(Arguments[1]);
                    editor.allClick = Integer.parseInt(Arguments[2]);
                    editor.allClick = editor.allClick + editor.numberClick;
                    editor.result = Double.parseDouble(Arguments[3]);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(JThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.printf("%s fiished... \n", Thread.currentThread().getName());
    }
}
