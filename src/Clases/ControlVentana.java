/*
Copyright(C) 2019 TwitterProyectoCorp.
Este programa es de sofware libre: puede redistribuirlo y/o modificarlo bajo los
términos de la Licencia Públic General de GNU publicada por Free Sofware Foundation,
ya sea la versión 3 de la licencia o (a su elección) cualquier version anterior.
**
Este programa se distribuye con la esperanza de que sea útil, pero SIN NINGUNA
GARANTÍA; sin siquiera la garantía implícita de COMERCIABILIDAD O APTITUD PARA
UN PROPÓSITO EN PARTICULAR. Ver la Licencia Pública genenral GNU para mas
detaller.
**
Debería haber resivido una copia de la Licencia Pública General de GNU junto con
este programa. Si no, vea <http://www.gnu.org/licenses/>.
*/
package Clases;

import java.awt.Toolkit;
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
        dialog.initModality(Modality.APPLICATION_MODAL);
        Toolkit.getDefaultToolkit().beep();
        dialog.show();        
    }  
}
