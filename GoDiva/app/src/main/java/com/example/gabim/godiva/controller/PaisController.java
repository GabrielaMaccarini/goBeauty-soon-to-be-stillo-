package com.example.gabim.godiva.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.gabim.godiva.modelos.Estados;
import com.example.gabim.godiva.modelos.Paises;
import com.example.gabim.godiva.util.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabim on 25/02/2017.
 */

public class PaisController {
    private Database db;
    private SQLiteDatabase instanciaDb;

    public PaisController(Context context){
        db = new Database(context);
    }

    public long inserePais(){
        ContentValues dados = new ContentValues();
        long resultado = 1;

        instanciaDb = db.getWritableDatabase();
        dados.put("nome", "Brasil");
        dados.put("sigla", "BR");

        resultado = instanciaDb.insert("paises", null, dados);
        instanciaDb.close();

        return resultado;
    }
    public List<Paises> retrievePais(Context ctx){
        String[] campos = {"id" ,"nome"};
        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("paises", campos, null, null, null, null, null);

        List<Paises> paisObjects = new ArrayList<>();

        if (cursor == null){
            return null;
        }

        Toast.makeText(ctx, String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();
        cursor.moveToFirst();
        do {
            Paises paisObject = new Paises();

            paisObject.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            paisObject.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));

            paisObjects.add(paisObject);
        } while (cursor.moveToNext());

        instanciaDb.close();
        return paisObjects;
    }
}
