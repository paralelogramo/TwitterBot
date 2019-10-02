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

import javafx.scene.image.Image;
import twitter4j.MediaEntity;
import twitter4j.Status;

/**
 *
 * Clase xxx: - Clase que representa blablablabla.
 * @author Fernando Pino,  Carmen Ortega,
 *          Vicente Rojas, Kevin Douglas.
 * @version 1.0
 */
public class Tweet {
    private Image imagenPerfil;
    private final Image imagenTweet;
    private final String usuario;
    private final String texto;    
    private final long id;    
    private final Status tweet;
    
    public Tweet(Status status, Image imgPerfil){
        this.tweet = status;
        this.id = status.getId();
        this.usuario = status.getUser().getScreenName();
        this.texto = status.getText();
        this.imagenPerfil = imgPerfil;        
        MediaEntity [] aux = status.getMediaEntities();
        if(aux.length!=0&&aux[0].getType().equals("photo"))
            this.imagenTweet = new Image (aux[0].getMediaURL());    
        else
            this.imagenTweet = null;
    }
    
    public void setImagenPerfil(Image imgPerfil){
        this.imagenPerfil = imgPerfil;
    }
    public Image getImagenPerfil() {
        return imagenPerfil;
    }

    public Image getImagenTweet() {
        return imagenTweet;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getTexto() {
        return texto;
    }

    public long getId() {
        return id;
    }    

    public Status getTweet() {
        return tweet;
    }   

    @Override
    public String toString() {
        return "Tweet{" + "imagenPerfil=" + imagenPerfil + ", imagenTweet=" + imagenTweet + ", usuario=" + usuario + ", texto=" + texto + ", id=" + id + '}';
    }    
}