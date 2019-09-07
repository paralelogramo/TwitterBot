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

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Clase xxx: - Clase que representa blablablabla.
 * @author Fernando Pino,  Carmen Ortega,
 *          Vicente Rojas, Kevin Douglas.
 * @version 1.0
 */
public class Usuario { 
    private String nombreUsuario;    
    private String clave;
    private String correo;
    private int codigo;   
    private final Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{6,9}$", Pattern.MULTILINE);
    private Matcher matcher;    

    public Usuario(String nombreUsuario, String clave,String correo) {
        this.nombreUsuario = nombreUsuario;        
        this.clave = clave;
        this.correo = correo;
        this.codigo = getCodigo(clave);
        this.matcher = pattern.matcher("");
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public boolean setNombreUsuario(String nombreUsuario) {
        this.matcher = pattern.matcher(nombreUsuario);
        if (matcher.matches()) {
            this.nombreUsuario = matcher.group(0);
            return true;          
        }
        else
            return false;
    }    

    public void setClave(String clave) {
        this.clave = clave;
        this.codigo = getCodigo(clave);
    }   
    
    private int getCodigo(String aux){
        return Arrays.deepHashCode(aux.split(""));
    }
    
    public boolean contraseñaValida(String aux){       
        return getCodigo(aux)==codigo;
    }     
    
    public boolean recuperacionValida(int aux){       
        return aux==getCodigo(clave);
    }       
    
    public int getClave(){
        return this.codigo;
    }    

    @Override
    public boolean equals(Object obj) {
        return this.hashCode()==obj.hashCode()&&this.getClass().equals(obj.getClass());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.nombreUsuario.toUpperCase());             
        return hash;
    }
}