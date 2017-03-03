package com.example.gabim.godiva.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.gabim.godiva.modelos.ArrayDeCidades;
import com.example.gabim.godiva.modelos.Cidades;
import com.example.gabim.godiva.modelos.Estados;
import com.example.gabim.godiva.modelos.Usuario;
import com.example.gabim.godiva.util.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;

/**
 * Created by gabim on 20/12/2016.
 */

public class UsuarioController {
    private Database db;
    private SQLiteDatabase instanciaDb;

    public UsuarioController(Context context){
        db = new Database(context);
    }
    public UsuarioController(){

    }

    public long create(Usuario usuario){
        ContentValues dados = new ContentValues();
        long resultado = 1;

        instanciaDb = db.getWritableDatabase();
        dados.put("nome", usuario.getNome());
        dados.put("username", usuario.getUsername());
        dados.put("data_nasc", usuario.getData_nasc());
        dados.put("id_cidade", usuario.getCidade());
        dados.put("id_estado", usuario.getEstado());
        dados.put("id_pais", usuario.getPais());
        dados.put("email", usuario.getEmail());
        dados.put("senha", usuario.getSenha());
        dados.put("confirmar_senha", usuario.getConfirmaSenha());
        try{
            resultado = instanciaDb.insert("USUARIO", null, dados);
            instanciaDb.close();
        }catch (SQLException ex){
            Toast.makeText(null, ""+ ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return resultado;
    }

    public Cursor retrieve(){
        String[] campos = {"id", "nome", "username", "data_nasc", "endereco", "email", "senha", "confirmar_senha"};
        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("USUARIO", campos, null, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        instanciaDb.close();
        return cursor;
    }

    public int VerificaLogin(String where){
        String[] campos = {"nome"};
        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("USUARIO", campos, where, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }

        instanciaDb.close();
        return cursor.getCount();
    }

    public Usuario getById(int id){
        String[] campos = {"id", "nome", "username", "data_nasc", "endereco", "email", "senha", "confirmar_senha"};
        String where = "id = " + id;
        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("USUARIO", campos, where, null, null, null, null);
        if (cursor == null){
            return null;
        }

        cursor.moveToFirst();
        instanciaDb.close();

        Usuario usuario = new Usuario();
        usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        usuario.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
        usuario.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
        usuario.setData_nasc(cursor.getString(cursor.getColumnIndexOrThrow("data_nasc")));
        usuario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        usuario.setSenha(cursor.getString(cursor.getColumnIndexOrThrow("senha")));
        usuario.setConfirmaSenha(cursor.getString(cursor.getColumnIndexOrThrow("confirmar_senha")));
        return usuario;
    }
    public long update(final Usuario usuario){
        ContentValues dados = new ContentValues();
        long resultado;

        instanciaDb = db.getWritableDatabase();
        dados.put("nome", usuario.getNome());
        dados.put("username", usuario.getUsername());
        dados.put("data_nasc", usuario.getData_nasc());
        /*dados.put("endereco", usuario.get());
        dados.put("endereco", usuario.getEndereco());
        dados.put("endereco", usuario.getEndereco());*/
        dados.put("email", usuario.getEmail());
        dados.put("senha", usuario.getSenha());
        dados.put("confirmar_senha", usuario.getConfirmaSenha());

        String where = "id = " + usuario.getId();

        resultado = instanciaDb.update("usuarios", dados, where, null);
        instanciaDb.close();

        return resultado;
    }
    public long delete(final Usuario Usuario){
        String where = "id = " + Usuario.getId();
        instanciaDb = db.getReadableDatabase();
        long resultado = instanciaDb.delete("usuarios", where, null);
        return resultado;
    }
}
