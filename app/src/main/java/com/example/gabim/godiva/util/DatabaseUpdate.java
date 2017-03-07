package com.example.gabim.godiva.util;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.gabim.godiva.modelos.ArrayDeCidades;

/**
 * Created by gabim on 25/02/2017.
 */

public class DatabaseUpdate {
    private SQLiteDatabase database;

    public DatabaseUpdate(){

    }

    public DatabaseUpdate(SQLiteDatabase db){
        database = db;
    }

    public void inserePais(int codigo, String nome, String sigla){
        String ifNotExists = "INSERT INTO paises\n" +
                "SELECT " + Integer.toString(codigo) + " as id, '"+nome+"' as nome, '"+sigla+"' as sigla " +
                "FROM dummy WHERE NOT EXISTS (SELECT 1 " +
                "FROM paises WHERE nome = '"+nome+"')";
        try {
            database.execSQL(ifNotExists);
        }
        catch(SQLException e){
            Toast.makeText(null, "Não inseriu estado" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void insereEstado(int codigo, String nome, String sigla, int pais){
        String ifNotExists = "INSERT INTO estados\n" +
                "SELECT " + Integer.toString(codigo) + " as id, '"+nome+"' as nome, '"+sigla+"' as sigla, "+Integer.toString(pais)+" as id_pais " +
                "FROM dummy WHERE NOT EXISTS (SELECT 1 " +
                "FROM estados WHERE nome = '"+nome+"')";
        try {
            database.execSQL(ifNotExists);
        }
        catch(SQLException e){
            Toast.makeText(null, "Não inseriu estado" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void insereCidade(String nome, int id_estado){
        String ifNotExists = "INSERT INTO cidades\n" +
                "SELECT null as id, '"+nome+"' as nome, '"+id_estado+"' as sigla " +
                "FROM dummy WHERE NOT EXISTS (SELECT 1 " +
                "FROM cidades WHERE nome = '"+nome+"')";
        database.execSQL(ifNotExists);
    }

    public void insereCidadesSC(){
        String[] cidadesSC;

        ArrayDeCidades arrayCity = new ArrayDeCidades();

        cidadesSC = arrayCity.getCidadeSC();

        for (int i = 0; i < cidadesSC.length -1; i++){
            insereCidade(cidadesSC[i], 1);
        }
    }
}
