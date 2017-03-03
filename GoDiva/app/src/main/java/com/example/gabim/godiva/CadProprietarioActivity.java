package com.example.gabim.godiva;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.gabim.godiva.modelos.Cidades;
import com.example.gabim.godiva.modelos.Estados;

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
}
