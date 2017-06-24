package com.example.gabim.godiva.modelos;

/**
 * Created by gabim on 20/12/2016.
 */

public class Usuario {
    private int id;
    private int perfil;
    private String nome;
    private String username;
    private String data_nasc;
    private int cidade;
    private int estado;
    private int pais;
    private String email;
    private String senha;
    private String confirmaSenha;
    private String nomeEstabelecimento;
    private int CNPJ;
    private String telefone;

    public String getNomeEstabelecimento() {
        return nomeEstabelecimento;
    }

    public void setNomeEstabelecimento(String nomeEstabelecimento) {
        this.nomeEstabelecimento = nomeEstabelecimento;
    }

    public int getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(int CNPJ) {
        this.CNPJ = CNPJ;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return this.nomeEstabelecimento;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getData_nasc() {
        return data_nasc;
    }

    public void setData_nasc(String data_nasc) {
        this.data_nasc = data_nasc;
    }

    public int getCidade() {
        return cidade;
    }
    public void setCidade(int cidade) {
        this.cidade = cidade;
    }

    public int getEstado() {return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    public int getPais() { return pais; }
    public void setPais(int pais) { this.pais = pais; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }
}
