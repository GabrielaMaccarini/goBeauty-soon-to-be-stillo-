package com.example.gabim.godiva.controller;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.gabim.godiva.modelos.Cidades;
import com.example.gabim.godiva.modelos.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabim on 03/03/2017.
 */

public class ProprietarioController extends UsuarioController {
    private SQLiteDatabase instanciaDb;
    private Context context;

    public ProprietarioController(Context context) {
        super(context);
        this.context = context;
    }

//    public Usuario getByUsername(String user){
//        String[] campos = {"id", "nome_estabelecimento", "username"};
//        String where = "username = '"+user+"'";
//        instanciaDb = db.getReadableDatabase();
//
//        Cursor cursor = instanciaDb.query("usuarios", campos, where, null, null, null, null);
//        if (cursor == null){
//            return null;
//        }
//
//        cursor.moveToFirst();
//        instanciaDb.close();
//
//        Usuario usuario = new Usuario();
//        usuario.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
//        usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
//        usuario.setNomeEstabelecimento(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
//        return usuario;
//    }

    public List<Usuario> getEstebelecimentosCidadeID(int idCidade){
        String[] campos = {"id", "nome_estabelecimento", "email"};
        String where = "id_cidade = " + Integer.toString(idCidade);
        List<Usuario> listaUsuario = new ArrayList<>();

        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("usuarios", campos, where, null, null, null, null);

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

    public List<Usuario> trazEstabCidade(String buscaCidade) {
        //-- Vamos Buscar por Partes
        //-- 1º ID da cidade
        //-- 2º Estabelecimentos com aquelas cidades
        //-- 3º Em progresso
        String[] campos = null;
        String where = null;

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

    public Cursor retrieve(){
        String[] campos = {"nome_estabelecimento"};
        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("usuarios", campos, null, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        instanciaDb.close();
        return cursor;
    }
    public Usuario getById(int id){
        String[] campos = {"id", "cnpj", "nome_estabelecimento", "username", "telefone", "email", "senha", "confirmar_senha"};
        String where = "id = " + id;
        instanciaDb = db.getReadableDatabase();

        Cursor cursor = instanciaDb.query("usuarios", campos, where, null, null, null, null);
        if (cursor == null){
            return null;
        }

        cursor.moveToFirst();
        instanciaDb.close();

        Usuario usuario = new Usuario();
        usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        usuario.setCNPJ(cursor.getInt(cursor.getColumnIndexOrThrow("cnpj")));
        usuario.setNomeEstabelecimento(cursor.getString(cursor.getColumnIndexOrThrow("nome_estabelecimento")));
        usuario.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
        usuario.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
        usuario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        usuario.setSenha(cursor.getString(cursor.getColumnIndexOrThrow("senha")));
        usuario.setConfirmaSenha(cursor.getString(cursor.getColumnIndexOrThrow("confirmar_senha")));
        return usuario;
    }
    public long update(final Usuario usuario){
        ContentValues dados = new ContentValues();
        long resultado;

        instanciaDb = db.getWritableDatabase();
        dados.put("cnpj", usuario.getCNPJ());
        dados.put("nome_estabelecimento", usuario.getNomeEstabelecimento());
        dados.put("telefone", usuario.getTelefone());
        dados.put("username", usuario.getUsername());
        dados.put("data_nasc", usuario.getData_nasc());
        dados.put("email", usuario.getEmail());
        dados.put("senha", usuario.getSenha());
        dados.put("confirmar_senha", usuario.getConfirmaSenha());

        String where = "id = " + usuario.getId();

        resultado = instanciaDb.update("usuarios", dados, where, null);
        instanciaDb.close();

        return resultado;
    }
}