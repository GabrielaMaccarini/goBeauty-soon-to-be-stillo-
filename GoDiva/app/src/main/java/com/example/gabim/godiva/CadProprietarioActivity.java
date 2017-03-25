package com.example.gabim.godiva;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gabim.godiva.controller.CidadeController;
import com.example.gabim.godiva.controller.EstadoController;
import com.example.gabim.godiva.controller.ProprietarioController;
import com.example.gabim.godiva.modelos.Cidades;
import com.example.gabim.godiva.modelos.Estados;
import com.example.gabim.godiva.modelos.Proprietario;

import java.util.List;

public class CadProprietarioActivity extends AppCompatActivity {
    List<Estados> AllEstados;
    List<Cidades> AllCidades;
    EditText etNomeEstabelecimento;
    EditText etCNPJ;
    Spinner spnCidadeCad;
    Spinner spnEstadoCad;
    EditText etTelefoneProp;
    EditText etEmailProp;
    EditText etUserProp;
    EditText etSenhaProp;
    EditText etConfSenhaProp;
    Button btnCadastraEst;
    String idProprietario;

    public boolean validaCampoVazio(EditText edt) {
        String conteudo = edt.getText().toString().trim();
        if (!TextUtils.isEmpty(conteudo)) {
            return true;
        }
        edt.requestFocus();
        edt.setError("Campo obrigatório");
        return false;
    }

    public boolean validaCampos() {
        return validaCampoVazio(etNomeEstabelecimento) && validaCampoVazio(etCNPJ) && validaCampoVazio(etTelefoneProp) &&
                validaCampoVazio(etEmailProp) && validaCampoVazio(etUserProp) && validaCampoVazio(etSenhaProp)
                && validaCampoVazio(etConfSenhaProp);
    }

    public void trazEstados(Spinner spn) {
        EstadoController crud = new EstadoController(getBaseContext());
        AllEstados = crud.retrieveEstados(getApplicationContext());

        String array[] = new String[AllEstados.size()];

        for (int i = 0; i < AllEstados.size(); i++) {
            array[i] = AllEstados.get(i).getNome();
        }

        if (AllEstados.size() > 0) {
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
            spn.setAdapter(spinnerArrayAdapter);
        }
    }

    public void trazCidades(Spinner spn) {
        if (spnEstadoCad.getCount() > 0) {
            CidadeController crud = new CidadeController(getBaseContext());
            String estadoDaCidade = spnEstadoCad.getItemAtPosition(spnEstadoCad.getSelectedItemPosition()).toString();

            int id_estado = 0;
            for (int i = 0; i < AllEstados.size(); i++) {
                if (AllEstados.get(i).getNome().trim().equals(estadoDaCidade.trim())) {
                    id_estado = AllEstados.get(i).getId();
                }
            }

            if (id_estado > 0) {
                AllCidades = crud.retrieveCidades(getApplicationContext(), id_estado);
                if (!AllCidades.isEmpty()) {
                    String array[] = new String[AllCidades.size()];
                    for (int i = 0; i < AllCidades.size(); i++) {
                        array[i] = AllCidades.get(i).getNome();
                    }

                    ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn.setAdapter(spinnerArrayAdapter);
                }
            }
        } else {
            Toast.makeText(this, "Nenhum estado inserido", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_proprietario);

        etNomeEstabelecimento = (EditText) findViewById(R.id.etNomeEstabelecimento);
        etCNPJ = (EditText) findViewById(R.id.etCNPJ);
        spnCidadeCad = (Spinner) findViewById(R.id.spnCidadeCad);
        spnEstadoCad = (Spinner) findViewById(R.id.spnEstadoCad);
        etTelefoneProp = (EditText) findViewById(R.id.etTelefoneProp);
        etEmailProp = (EditText) findViewById(R.id.etEmailProp);
        etUserProp = (EditText) findViewById(R.id.etUserProp);
        etSenhaProp = (EditText) findViewById(R.id.etSenhaProp);
        etConfSenhaProp = (EditText) findViewById(R.id.etConfSenhaProp);
        btnCadastraEst = (Button) findViewById(R.id.btnCadastraEst);

        trazEstados(spnEstadoCad);
        trazCidades(spnCidadeCad);

    }

    public void validaCadastro(View view) {
        if (!etSenhaProp.getText().toString().equals(etConfSenhaProp.getText().toString())) {
            Toast.makeText(getApplicationContext(), "As senhas não coincidem.", Toast.LENGTH_LONG).show();
        } else {
            Proprietario proprietario = new Proprietario();

            proprietario.setCNPJ(Integer.parseInt(etCNPJ.getText().toString()));
            proprietario.setTelefone(etTelefoneProp.getText().toString());
            proprietario.setNomeEstabelecimento(etNomeEstabelecimento.getText().toString());
            proprietario.setPais(1);
            proprietario.setEstado(spnEstadoCad.getSelectedItemPosition());
            proprietario.setCidade(spnEstadoCad.getSelectedItemPosition());
            proprietario.setEmail(etEmailProp.getText().toString());
            proprietario.setUsername(etUserProp.getText().toString());
            proprietario.setSenha(etSenhaProp.getText().toString());
            proprietario.setConfirmaSenha(etConfSenhaProp.getText().toString());


            ProprietarioController crudProp = new ProprietarioController(getBaseContext());
            long retorno = 1;

            if (!TextUtils.isEmpty(idProprietario)) {
                proprietario.setId(Integer.parseInt(idProprietario));
                retorno = crudProp.update(proprietario);
            } else {
                retorno = crudProp.create(proprietario);
            }
            if (retorno == -1) {
                Toast.makeText(getBaseContext(), "Erro ao salvar estabelecimento", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), "O estabelecimento "+proprietario.getNomeEstabelecimento()+" foi cadastrado com sucesso", Toast.LENGTH_SHORT).show();
            }
            Intent abrirPaginaInicial = new Intent(this, PaginaInicial.class);
            startActivity(abrirPaginaInicial);
        }
    }
}