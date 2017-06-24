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
    protected Database db;
    private SQLiteDatabase instanciaDb;
    Context context;

    public UsuarioController(Context context){
        db = new Database(context);
        this.context = context;
    }

    public long create(Usuario usuario){
        ContentValues dados = new ContentValues();
        long resultado = 1;

        instanciaDb = db.getWritableDatabase();
        dados.put("perfil", usuario.getPerfil());
        dados.put("nome", usuario.getNome());
        dados.put("username", usuario.getUsername());
        dados.put("data_nasc", usuario.getData_nasc());
        dados.put("id_cidade", usuario.getCidade());
        dados.put("id_estado", usuario.getEstado());
        dados.put("id_pais", usuario.getPais());
        if(usuario.getPerfil() == 3) {
            dados.put("cnpj", usuario.getCNPJ());
            dados.put("telefone", usuario.getTelefone());
            dados.put("nome_estabelecimento", usuario.getNomeEstabelecimento());
        }
        dados.put("email", usuario.getEmail());
        dados.put("senha", usuario.getSenha());
        try{
            resultado = instanciaDb.insert("USUARIO", null, dados);
            instanciaDb.close();
        }catch (SQLException ex){
            Toast.makeText(null, ""+ ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return resultado;
    }

    public Cursor retrieve(){
        String[] campos = {"perfil"};
        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("USUARIO", campos, null, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        instanciaDb.close();
        return cursor;
    }

    public int VerificaLogin(String where){
        String[] campos = {"nome", "senha"};
        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("USUARIO", campos, where, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }

        instanciaDb.close();
        return cursor.getCount();
    }
    public Usuario getById(int id){
        String[] campos = {"id"};
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
        return usuario;
    }

    public Usuario getByUsername(String user){
        String[] campos = {"id", "nome", "nome_estabelecimento", "username", "perfil"};
        String where = "username = '"+user+"'";
        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("USUARIO", campos, where, null, null, null, null);
        if (cursor == null){
            return null;
        }
        cursor.moveToFirst();
        instanciaDb.close();

        Usuario usuario = new Usuario();
        usuario.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
        usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        usuario.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
        usuario.setNomeEstabelecimento(cursor.getString(cursor.getColumnIndexOrThrow("nome_estabelecimento")));
        usuario.setPerfil(cursor.getInt(cursor.getColumnIndexOrThrow("perfil")));
        return usuario;
    }

    public long update(final Usuario usuario){
        ContentValues dados = new ContentValues();
        long resultado;

        instanciaDb = db.getWritableDatabase();
        dados.put("perfil", usuario.getPerfil());
        dados.put("nome", usuario.getNome());
        dados.put("username", usuario.getUsername());
        dados.put("data_nasc", usuario.getData_nasc());
        dados.put("email", usuario.getEmail());
        dados.put("senha", usuario.getSenha());
        dados.put("cnpj", usuario.getCNPJ());
        dados.put("telefone", usuario.getTelefone());
        dados.put("nome_estabelecimento", usuario.getNomeEstabelecimento());

        String where = "id = " + usuario.getId();

        resultado = instanciaDb.update("usuario", dados, where, null);
        instanciaDb.close();

        return resultado;
    }
//    public long delete(final Usuario Usuario){
//        String where = "id = " + Usuario.getId();
//        instanciaDb = db.getReadableDatabase();
//        long resultado = instanciaDb.delete("usuarios", where, null);
//        return resultado;
//    }
//  ----------------PROPRIETARIO----------------//
    public List<Usuario> trazEstabCidade(String buscaCidade) {
        instanciaDb = db.getReadableDatabase(); //-- Conexão com Banco de Dados

        //-- Buscando as Cidades
        CidadeController cidadeController = new CidadeController(this.context);
        List<Cidades> cidades = cidadeController.buscaIdCidades(buscaCidade);
        List<Usuario> usuariosGeral = new ArrayList<>();

        for (int i = 0; i < cidades.size(); i++) {
            List<Usuario> usuarios = getEstebelecimentosCidadeID(cidades.get(i).getId());
            for (int ii = 0; ii < usuarios.size(); ii++) {
                usuariosGeral.add(usuarios.get(ii));
            }
        }
        return usuariosGeral;
    }

    public List<Usuario> getEstebelecimentosCidadeID(int idCidade){
        String[] campos = {"id", "nome_estabelecimento", "email"};
        String where = "id_cidade = " + Integer.toString(idCidade) + " and perfil = 3";
        List<Usuario> listaUsuario = new ArrayList<>();

        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("usuario", campos, where, null, null, null, null);

        while (cursor.moveToNext()){
            Usuario usuario = new Usuario();
            usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            usuario.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome_estabelecimento")));
            usuario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));

            listaUsuario.add(usuario);
        }
        instanciaDb.close();
        return listaUsuario;
    }
    public Usuario getPropById(int id){
        String[] campos = {"id", "cnpj", "nome_estabelecimento", "username", "telefone", "email", "senha", "confirmar_senha"};
        String where = "id = " + id;
        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("usuario", campos, where, null, null, null, null);
        if (cursor == null){
            return null;
        }

        cursor.moveToFirst();
        instanciaDb.close();

        Usuario usuario = new Usuario();
        usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        usuario.setPerfil(cursor.getInt(cursor.getColumnIndexOrThrow("perifl")));
        usuario.setCNPJ(cursor.getInt(cursor.getColumnIndexOrThrow("cnpj")));
        usuario.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
        usuario.setNomeEstabelecimento(cursor.getString(cursor.getColumnIndexOrThrow("nome_estabelecimento")));
        usuario.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
        usuario.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
        usuario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        usuario.setSenha(cursor.getString(cursor.getColumnIndexOrThrow("senha")));
        return usuario;
    }
}
