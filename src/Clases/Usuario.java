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
    private int codigo;   
    private final Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{6,9}$", Pattern.MULTILINE);
    private Matcher matcher;    

    public Usuario(String nombreUsuario, String clave) {
        this.nombreUsuario = nombreUsuario;        
        this.clave = clave;
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