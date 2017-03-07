package com.example.gabim.godiva.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.gabim.godiva.modelos.Estados;
import com.example.gabim.godiva.util.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabim on 25/02/2017.
 */

public class EstadoController {
    private Database db;
    private SQLiteDatabase instanciaDb;

    public EstadoController(Context context){
        db = new Database(context);
    }

    public List<Estados> retrieveEstados(Context ctx){
        String[] campos = {"id", "nome"};
        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("estados", campos, null, null, null, null, null);

        List<Estados> estadosObjects = new ArrayList<>();

        if (cursor == null){
            return null;
        }

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Estados estadosObject = new Estados();

                estadosObject.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                estadosObject.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));

                estadosObjects.add(estadosObject);
            } while (cursor.moveToNext());
        }

        instanciaDb.close();
        return estadosObjects;
    }

    public long deleteEstados(){
        instanciaDb = db.getReadableDatabase();
        long resultado = instanciaDb.delete("estados", null, null);
        return resultado;
    }
}
