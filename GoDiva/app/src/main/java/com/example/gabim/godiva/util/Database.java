package com.example.gabim.godiva.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;
import android.widget.Toast;

import com.example.gabim.godiva.modelos.Cidades;
import com.example.gabim.godiva.modelos.Estados;

/**
 * Created by gabim on 07/12/2016.
 */

public class Database extends SQLiteOpenHelper {
    private static final String BD_NOME = "goBeauty";
    private static final int BD_VERSION = 1;
    private SQLiteDatabase db;
    private SQLiteDatabase db2;

    public Database(Context contexto){
        super(contexto, BD_NOME, null, BD_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createTablePaises = "CREATE TABLE paises (\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n" +
                "nome  VARCHAR (50) NOT NULL,\n" +
                "sigla CHAR (3) NOT NULL\n" +
                ")";
        db.execSQL(createTablePaises);

        final String createTableDummy = "CREATE TABLE dummy (\n" +
                "nada VARCHAR(50))";
        db.execSQL(createTableDummy);

        db.execSQL("DELETE FROM dummy");

        db.execSQL("INSERT INTO dummy VALUES('')");

        final String createTableEstados = "CREATE TABLE estados (\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "nome VARCHAR (50) NOT NULL,\n" +
                "id_pais INTEGER REFERENCES paises (id) NOT NULL,\n" +
                "sigla CHAR (3) NOT NULL)\n";
        db.execSQL(createTableEstados);

        final String createTableCidades = "CREATE TABLE cidades (\n"+
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n"+
                "nome VARCHAR (50) NOT NULL,\n"+
                "id_estados INTEGER REFERENCES estados (id) NOT NULL)\n";
        db.execSQL(createTableCidades);

        final String createTableUsuario = "CREATE TABLE USUARIO ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
                "id_pais INTEGER REFERENCES paises (id) NOT NULL, "+
                "id_estado INTEGER REFERENCES estados (id) NOT NULL, "+
                "id_cidade INTEGER REFERENCES cidades (id) NOT NULL, "+
                "nome VARCHAR (100) NOT NULL, "+
                "username VARCHAR (100) NOT NULL, "+
                "data_nasc DATE NOT NULL, "+
                "endereco VARCHAR (100), "+
                "email VARCHAR (50)  NOT NULL, "+
                "senha VARCHAR (100) NOT NULL, "+
                "confirmar_senha VARCHAR (100) NOT NULL)";
        db.execSQL(createTableUsuario);

        final String createTableProprietario = "CREATE TABLE proprietarios( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "id_pais INTEGER REFERENCES paises (id) NOT NULL, "+
                "id_estado INTEGER REFERENCES estados (id) NOT NULL, "+
                "id_cidade INTEGER REFERENCES cidades (id) NOT NULL, "+
                "cnpj INTEGER NOT NULL, " +
                "nome_estabelecimento VARCHAR (100) NOT NULL, "+
                "username VARCHAR (100) NOT NULL, "+
                "email VARCHAR (50)  NOT NULL, "+
                "senha VARCHAR (100) NOT NULL, "+
                "confirmar_senha VARCHAR (100) NOT NULL)";
        db.execSQL(createTableProprietario);

        final String alterTableDrop = "ALTER TABLE PROPRIETARIOS DROP TELEFONE";
        db.execSQL(alterTableDrop);
        final String alterTableAdd = "ALTER TABLE PROPRIETARIOS ADD TELEFONE STRING NOT NULL";
        db.execSQL(alterTableAdd);


        DatabaseUpdate dbUpdate = new DatabaseUpdate(db);

        dbUpdate.inserePais(1, "Brasil", "BR");
        dbUpdate.insereEstado(1, "Santa Catarina", "SC", 1);
        dbUpdate.insereCidadesSC();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String alterTableUsuario = "DROP TABLE IF EXISTS USUARIO";
        db.execSQL(alterTableUsuario);
        this.onCreate(db);
    }

}
