package com.example.gabim.godiva.controller;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import com.example.gabim.godiva.modelos.Proprietario;
import com.example.gabim.godiva.modelos.Usuario;
import com.example.gabim.godiva.util.Database;

/**
 * Created by gabim on 03/03/2017.
 */

public class ProprietarioController extends UsuarioController {
    private Database db;
    private SQLiteDatabase instanciaDb;


    public ProprietarioController(Context context) {
        super(context);
    }

    public long create(Proprietario proprietario){
        ContentValues dados = new ContentValues();
        long resultado = 1;

        instanciaDb = db.getWritableDatabase();
        dados.put("id_cidade", proprietario.getCidade());
        dados.put("id_estado", proprietario.getEstado());
        dados.put("id_pais", proprietario.getPais());
        dados.put("cnpj", proprietario.getCNPJ());
        dados.put("nome_estabelecimento", proprietario.getNome());
        dados.put("username", proprietario.getUsername());
        dados.put("telefone", proprietario.getTelefone());
        dados.put("email", proprietario.getEmail());
        dados.put("senha", proprietario.getSenha());
        dados.put("confirmar_senha", proprietario.getConfirmaSenha());
        try{
            resultado = instanciaDb.insert("proprietarios", null, dados);
            instanciaDb.close();
        }catch (SQLException ex){
            Toast.makeText(null, ""+ ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return resultado;
    }
    public Cursor retrieve(){
        String[] campos = {"id", "cnpj", "nome_estabelecimento", "username", "telefone", "email", "senha", "confirmar_senha"};
        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("proprietarios", campos, null, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        instanciaDb.close();
        return cursor;
    }
    public Proprietario getById(int id){
        String[] campos = {"id", "cnpj", "nome_estabelecimento", "username", "telefone", "email", "senha", "confirmar_senha"};
        String where = "id = " + id;
        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("proprietarios", campos, where, null, null, null, null);
        if (cursor == null){
            return null;
        }

        cursor.moveToFirst();
        instanciaDb.close();

        Proprietario proprietario = new Proprietario();
        proprietario.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        proprietario.setCNPJ(cursor.getInt(cursor.getColumnIndexOrThrow("cnpj")));
        proprietario.setNomeEstabelecimento(cursor.getString(cursor.getColumnIndexOrThrow("nome_estabelecimento")));
        proprietario.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
        proprietario.setTelefone(cursor.getInt(cursor.getColumnIndexOrThrow("telefone")));
        proprietario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        proprietario.setSenha(cursor.getString(cursor.getColumnIndexOrThrow("senha")));
        proprietario.setConfirmaSenha(cursor.getString(cursor.getColumnIndexOrThrow("confirmar_senha")));
        return proprietario;
    }
    public long update(final Proprietario proprietario){
        ContentValues dados = new ContentValues();
        long resultado;

        instanciaDb = db.getWritableDatabase();
        dados.put("cnpj", proprietario.getCNPJ());
        dados.put("nome_estabelecimento", proprietario.getNomeEstabelecimento());
        dados.put("username", proprietario.getUsername());
        dados.put("data_nasc", proprietario.getData_nasc());
        /*dados.put("endereco", usuario.get());
        dados.put("endereco", usuario.getEndereco());
        dados.put("endereco", usuario.getEndereco());*/
        dados.put("email", proprietario.getEmail());
        dados.put("senha", proprietario.getSenha());
        dados.put("confirmar_senha", proprietario.getConfirmaSenha());

        String where = "id = " + proprietario.getId();

        resultado = instanciaDb.update("proprietarios", dados, where, null);
        instanciaDb.close();

        return resultado;
    }
}