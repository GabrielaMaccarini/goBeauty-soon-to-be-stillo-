package com.example.gabim.godiva.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.gabim.godiva.modelos.ArrayDeCidades;
import com.example.gabim.godiva.modelos.Cidades;
import com.example.gabim.godiva.util.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabim on 25/02/2017.
 */

public class CidadeController {
    private Database db;
    private SQLiteDatabase instanciaDb;

    public CidadeController(Context context){
        db = new Database(context);
    }

    public List<Cidades> retrieveCidades(Context ctx, int id_estado){
        String[] campos = {"id", "nome"};
        String where = "id_estados = " + String.valueOf(id_estado);
        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("cidades", campos, where, null, null, null, null);
        List<Cidades> cidadeObjects = new ArrayList<>();

        if (cursor == null){
            return null;
        }

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Cidades cidades = new Cidades();
                cidades.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                cidades.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));

                cidadeObjects.add(cidades);
            } while (cursor.moveToNext());
        }
        instanciaDb.close();
        return cidadeObjects;
    }

    public int buscaIdCidade(String txtCidade){
        String[] campos = {"id"};
        String where = "nome = '"+txtCidade+"'";
        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("cidades", campos, where, null, null, null, null);
        int retornaIdCidade;

        if (cursor.moveToNext()){
            retornaIdCidade = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }else{
            retornaIdCidade = 0;
        }
        return retornaIdCidade;
    }

    public List<Cidades> buscaIdCidades(String buscaCidade){
        String[] campos = {"id"};
        String where = "upper(nome) like '"+buscaCidade.toUpperCase()+"%'";
        List<Cidades> cidades = new ArrayList<>();

        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("cidades", campos, where, null, null, null, null);

        while (cursor.moveToNext()){
            Cidades cidade = new Cidades();
            cidade.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));

            cidades.add(cidade);
        }

        return cidades;
    }
}
