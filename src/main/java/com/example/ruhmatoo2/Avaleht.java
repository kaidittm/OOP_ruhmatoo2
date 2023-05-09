package com.example.ruhmatoo2;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Avaleht extends Application {

    @Override
    public void start(Stage peaLava) throws Exception {
        Group juur = new Group(); // luuakse juur
        BorderPane borderPane = new BorderPane();

        Pane wrapperPane = new Pane();
        borderPane.setCenter(wrapperPane);
        Canvas lõuend = new Canvas();
        wrapperPane.getChildren().add(lõuend);

        peaLava.widthProperty().addListener((obs, oldVal, newVal) -> {
            double laius = newVal.doubleValue();
            joonistaLeht(borderPane, laius, lõuend.getHeight());
        });
        peaLava.heightProperty().addListener((obs, oldVal, newVal) -> {
            double kõrgus = newVal.doubleValue();
            joonistaLeht(borderPane, lõuend.getWidth(), kõrgus);
        });

        joonistaLeht(borderPane, 500, 450);
        juur.getChildren().add(lõuend);  // lõuend lisatakse juure alluvaks
        Scene stseen1 = new Scene(juur);  // luuakse stseen

        lõuend.widthProperty().bind(peaLava.widthProperty());
        lõuend.heightProperty().bind(peaLava.heightProperty());

        peaLava.setTitle("Avaleht");  // lava tiitelribale pannakse tekst
        peaLava.setScene(stseen1);  // lavale lisatakse stseen
        peaLava.show();  // lava tehakse nähtavaks
    }

    public static BorderPane joonistaLeht(BorderPane bp, double x, double y) {
        PasswordField salasõnaVäli = new PasswordField();
        bp.setCenter(salasõnaVäli);
        return bp;
    }

    public static void main(String[] args) {
        launch();
    }
}

