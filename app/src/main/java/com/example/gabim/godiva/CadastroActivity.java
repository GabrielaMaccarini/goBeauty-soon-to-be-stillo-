package com.example.gabim.godiva;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gabim.godiva.controller.CidadeController;
import com.example.gabim.godiva.controller.EstadoController;
import com.example.gabim.godiva.controller.UsuarioController;
import com.example.gabim.godiva.modelos.Cidades;
import com.example.gabim.godiva.modelos.Estados;
import com.example.gabim.godiva.modelos.Usuario;
import android.text.Editable;
import android.text.TextWatcher;
import java.util.List;


public class CadastroActivity extends AppCompatActivity {
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
                    String str = Mask.unmask(s.toString());
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
        return validaCampoVazio(edtNomeCad) && validaCampoVazio(edtNascCad) && validaCampoVazio(edtEmailCad) &&
                validaCampoVazio(edtUserCad) && validaCampoVazio(edtSenhaCad) && validaCampoVazio(edtConfSenhaCad);
    }

    public void salvarInfos(View view){
        if (!validaCampos()){
            return;
        }
    }

    public void trazEstados(Spinner spn){
        EstadoController crud = new EstadoController(getBaseContext());
        AllEstados = crud.retrieveEstados(getApplicationContext());

        String array[] = new String[AllEstados.size()];

        for (int i = 0; i < AllEstados.size(); i++){
            array[i] = AllEstados.get(i).getNome();
        }

        if (AllEstados.size() > 0) {
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
            spn.setAdapter(spinnerArrayAdapter);
        }
    }

    public void trazCidades(Spinner spn){
        if (spnEstadoCad.getCount() > 0){
            CidadeController crud = new CidadeController(getBaseContext());
            String estadoDaCidade = spnEstadoCad.getItemAtPosition(spnEstadoCad.getSelectedItemPosition()).toString();

            int id_estado = 0;
            for (int i = 0; i < AllEstados.size(); i++){
                if (AllEstados.get(i).getNome().trim().equals(estadoDaCidade.trim())){
                    id_estado = AllEstados.get(i).getId();
                }
            }

            if (id_estado > 0){
                AllCidades = crud.retrieveCidades(getApplicationContext(), id_estado);
                if (!AllCidades.isEmpty()) {
                    String array[] = new String[AllCidades.size()];
                    for (int i = 0; i < AllCidades.size(); i++) {
                        array[i] = AllCidades.get(i).getNome();
                    }

                    ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
                    spn.setAdapter(spinnerArrayAdapter);
                }
            }
        }else{
            Toast.makeText(this, "Nenhum estado inserido", Toast.LENGTH_SHORT).show();
        }
    }

    EditText edtNomeCad; EditText edtNascCad;Spinner spnCidadeCad;Spinner spnEstadoCad;
    EditText edtEmailCad;EditText edtUserCad;EditText edtSenhaCad;EditText edtConfSenhaCad;
    Button btnCadastrar;
    String idUsuario; Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        edtNomeCad      = (EditText) findViewById(R.id.edtNomeCad);
        edtNascCad      = (EditText) findViewById(R.id.edtNascCad);
        spnCidadeCad    = (Spinner) findViewById(R.id.spnCidadeCad);
        spnEstadoCad    = (Spinner) findViewById(R.id.spnEstadoCad);
        edtEmailCad     = (EditText) findViewById(R.id.edtEmailCad);
        edtUserCad      = (EditText) findViewById(R.id.edtUserCad);
        edtSenhaCad     = (EditText) findViewById(R.id.edtSenhaCad);
        edtConfSenhaCad = (EditText) findViewById(R.id.edtConfSenha);
        btnCadastrar    = (Button) findViewById(R.id.btnCadastrar);
        edtNascCad.addTextChangedListener(Mask.insert("##/##/####", edtNascCad));
        trazEstados(spnEstadoCad);
        trazCidades(spnCidadeCad);

        ActionBar cadastroProprietario = getSupportActionBar();
        cadastroProprietario.setTitle("Tela de cadastro");
        cadastroProprietario.setDisplayHomeAsUpEnabled(true);

        idUsuario = this.getIntent().getStringExtra("id");
        if (!TextUtils.isEmpty(idUsuario)){
            UsuarioController crud = new UsuarioController(getBaseContext());
            usuario = crud.getById(Integer.parseInt(idUsuario));
            edtNomeCad.setText(usuario.getNome());
            edtNascCad.setText(usuario.getData_nasc());
            edtEmailCad.setText(usuario.getEmail());
            edtUserCad.setText(usuario.getUsername());
            edtSenhaCad.setText(usuario.getSenha());
            edtConfSenhaCad.setText(usuario.getConfirmaSenha());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater criaMenu = getMenuInflater();
        criaMenu.inflate(R.menu.menu_cadastro_proprietario, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.CadastroProprietario:
                Intent abreProprietario = new Intent(this, CadProprietarioActivity.class);
                startActivity(abreProprietario);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void validaCadastro(View view){
        if(!validaCampos()){
        }
        else if(!edtSenhaCad.getText().toString().equals(edtConfSenhaCad.getText().toString())){
            Toast.makeText(getApplicationContext(), "As senhas não coincidem.", Toast.LENGTH_LONG).show();
        }
        else{

            Usuario usuario = new Usuario();
            usuario.setNome(edtNomeCad.getText().toString());
            usuario.setData_nasc(edtNascCad.getText().toString());
            usuario.setPais(1);
            usuario.setEstado(spnEstadoCad.getSelectedItemPosition()+1);
            usuario.setCidade(spnEstadoCad.getSelectedItemPosition()+1);
            usuario.setEmail(edtEmailCad.getText().toString());
            usuario.setUsername(edtUserCad.getText().toString());
            usuario.setSenha(edtSenhaCad.getText().toString());
            usuario.setConfirmaSenha(edtConfSenhaCad.getText().toString());


            UsuarioController crud = new UsuarioController(getBaseContext());
            long retorno= 0;

            if(!TextUtils.isEmpty(idUsuario)){
                usuario.setId(Integer.parseInt(idUsuario));
                retorno = crud.update(usuario);
            }else{
                retorno = crud.create(usuario);
            }
            if (retorno == -1){
                Toast.makeText(getBaseContext(), "Erro ao salvar usuário", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getBaseContext(), usuario.getNome()+", você foi cadastrado com sucesso", Toast.LENGTH_SHORT).show();
            }
            Intent abrirPaginaInicial = new Intent(this, PaginaInicialActivity.class);
            startActivity(abrirPaginaInicial);
        }
    }
}