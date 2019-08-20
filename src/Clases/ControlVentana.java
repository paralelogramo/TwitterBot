/*
 * Copyright (C) 2019 TwitterProyectCorp.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package Clases;

import java.util.concurrent.atomic.AtomicReference;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * Clase xxx: - Clase que representa blablablabla.
 * @author Fernando Pino,  Carmen Ortega,
 *          Vicente Rojas, Kevin Douglas.
 * @version 1.0
 */
public class ControlVentana {
    public void arrastrarVentana(AnchorPane panel) {
        AtomicReference<Double> x = new AtomicReference<>((double) 0);
        AtomicReference<Double> y = new AtomicReference<>((double) 0);
        panel.setOnMousePressed(e -> {
            Stage stage = (Stage) panel.getScene().getWindow();
            x.set(stage.getX() - e.getScreenX());
            y.set(stage.getY() - e.getScreenY());
        });
        panel.setOnMouseDragged(e -> {
            Stage stage = (Stage) panel.getScene().getWindow();
            stage.setX(e.getScreenX() + x.get());
            stage.setY(e.getScreenY() + y.get());           
        });
    }
    public void cerrarPrograma(MouseEvent event){
        System.exit(0); 
    }
    public void popUp(String title, String menssaje) {
        final Stage dialog = new Stage();        
        dialog.initModality(Modality.APPLICATION_MODAL);       
        dialog.setTitle(title);
        VBox dialogVbox = new VBox(20);
        Text msj = new Text("/ ! \\  "+menssaje+"  / ! \\");
        msj.setScaleX(1.5);
        msj.setScaleY(1.2);
        dialogVbox.getChildren().add(msj);
        dialogVbox.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(dialogVbox, 400, 80);
        dialog.setScene(dialogScene);
        dialog.setResizable(false);
        dialog.setAlwaysOnTop(true);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.show();        
    }  
}
