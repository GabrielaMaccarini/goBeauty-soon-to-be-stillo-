package com.example.gabim.godiva.modelos;

/**
 * Created by gabim on 03/03/2017.
 */

public class Proprietario extends Usuario {
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
}