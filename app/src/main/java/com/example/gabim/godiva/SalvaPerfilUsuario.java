package com.example.gabim.godiva;

import com.example.gabim.godiva.modelos.Usuario;

/**
 * Created by gabim on 16/05/2017.
 */

public class SalvaPerfilUsuario {
    private static Usuario user;


    public static Usuario getUser() {
        return user;
    }

    public static void setUser(Usuario user) {
        SalvaPerfilUsuario.user = user;
    }
}
