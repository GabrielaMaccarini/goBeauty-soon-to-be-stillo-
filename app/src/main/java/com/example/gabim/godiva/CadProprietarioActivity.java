package com.example.gabim.godiva;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gabim.godiva.controller.ProprietarioController;
import com.example.gabim.godiva.controller.UsuarioController;
import com.example.gabim.godiva.modelos.Cidades;
import com.example.gabim.godiva.modelos.Estados;
import com.example.gabim.godiva.modelos.Proprietario;
import com.example.gabim.godiva.modelos.Usuario;

import java.util.List;

public class CadProprietarioActivity extends AppCompatActivity {
    List<Estados> AllEstados;
    List<Cidades> AllCidades;

    public static class Mask {
        public static String unmask(String s) {
            return s.replaceAll("[.]", "").replaceAll("[-]", "")
                    .replaceAll("[/]", "").replaceAll("[(]", "")
                    .replaceAll("[)]", "");
        }

        public static TextWatcher insert(final String mask, final EditText ediTxt) {
            return new TextWatcher() {
                boolean isUpdating;
                String old = "";
                public void onTextChanged(CharSequence s, int start, int before,int count) {
                    String str = CadastroActivity.Mask.unmask(s.toString());
                    String mascara = "";
                    if (isUpdating) {
                        old = str;
                        isUpdating = false;
                        return;
                    }
                    int i = 0;
                    for (char m : mask.toCharArray()) {
                        if (m != '#' && str.length() > old.length()) {
                            mascara += m;
                            continue;
                        }
                        try {
                            mascara += str.charAt(i);
                        } catch (Exception e) {
                            break;
                        }
                        i++;
                    }
                    isUpdating = true;
                    ediTxt.setText(mascara);
                    ediTxt.setSelection(mascara.length());
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void afterTextChanged(Editable s) {}
            };
        }
    }

    public boolean validaCampoVazio(EditText edt){
        String conteudo = edt.getText().toString().trim();
        if(!TextUtils.isEmpty(conteudo)){
            return true;
        }
        edt.requestFocus();
        edt.setError("Campo obrigatório");
        return false;
    }


    public boolean validaCampos(){
        return validaCampoVazio(etNomeEstabelecimento) && validaCampoVazio(etCNPJ) && validaCampoVazio(etTelefoneProp) &&
                validaCampoVazio(etEmailProp) && validaCampoVazio(etUserProp) && validaCampoVazio(etSenhaProp)
                    && validaCampoVazio(etConfSenhaProp);
    }

    EditText etNomeEstabelecimento; EditText etCNPJ;Spinner spnCidadeCad;Spinner spnEstadoCad;
    EditText etTelefoneProp;EditText etEmailProp;EditText etUserProp;EditText etSenhaProp;EditText etConfSenhaProp;
    String idProprietario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_proprietario);

        EditText etNomeEstabelecimento = (EditText) findViewById(R.id.etNomeEstabelecimento);
        EditText etCNPJ = (EditText) findViewById(R.id.etCNPJ);
        EditText etTelefoneProp = (EditText) findViewById(R.id.etTelefoneProp);
        EditText etEmailProp = (EditText) findViewById(R.id.etEmailProp);
        EditText etUserProp = (EditText) findViewById(R.id.etUserProp);
        EditText etSenhaProp = (EditText) findViewById(R.id.etSenhaProp);
        EditText etConfSenhaProp = (EditText) findViewById(R.id.etConfSenhaProp);
        Button btnCadastraEst = (Button) findViewById(R.id.btnCadastraEst);

    }

    public void validaCadastro(View view) {
        if (!validaCampos()) {
        } else if (!etSenhaProp.getText().toString().equals(etConfSenhaProp.getText().toString())) {
            Toast.makeText(getApplicationContext(), "As senhas não coincidem.", Toast.LENGTH_LONG).show();
        } else {
            Proprietario proprietario = new Proprietario();
            proprietario.setCNPJ(Integer.parseInt(etCNPJ.getText().toString()));
            proprietario.setNome(etNomeEstabelecimento.getText().toString());
            proprietario.setTelefone(Integer.parseInt(etTelefoneProp.getText().toString()));
            proprietario.setPais(1);
            proprietario.setEstado(spnEstadoCad.getSelectedItemPosition() + 1);
            proprietario.setCidade(spnEstadoCad.getSelectedItemPosition() + 1);
            proprietario.setEmail(etEmailProp.getText().toString());
            proprietario.setUsername(etUserProp.getText().toString());
            proprietario.setSenha(etSenhaProp.getText().toString());
            proprietario.setConfirmaSenha(etConfSenhaProp.getText().toString());


            ProprietarioController crudProp = new ProprietarioController(getBaseContext());
            long retorno = 0;

            if (!TextUtils.isEmpty(idProprietario)) {
                proprietario.setId(Integer.parseInt(idProprietario));
                retorno = crudProp.update(proprietario);
            } else {
                retorno = crudProp.create(proprietario);
            }
            if (retorno == -1) {
                Toast.makeText(getBaseContext(), "Erro ao salvar usuário", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), proprietario.getNome() + ", você foi cadastrado com sucesso", Toast.LENGTH_SHORT).show();
            }
            Intent abrirPaginaInicial = new Intent(this, PaginaInicialActivity.class);
            startActivity(abrirPaginaInicial);
        }
    }
}
