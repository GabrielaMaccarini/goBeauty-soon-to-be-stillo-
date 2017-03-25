package com.example.gabim.godiva;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gabim.godiva.controller.UsuarioController;
import com.example.gabim.godiva.modelos.Usuario;
import com.example.gabim.godiva.util.Database;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity {
    private Database db;
    private SQLiteDatabase instanciaDb;

    EditText edtUserLogin;
    EditText edtSenhaLogin;
    Button btnEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Usuario usuario = new Usuario();
        edtUserLogin = (EditText) findViewById(R.id.edtUserLogin);
        edtSenhaLogin = (EditText) findViewById(R.id.edtSenhaLogin);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtSenhaLogin.getText().toString().length() == 0 && edtUserLogin.getText().toString().length() == 0) {
                    Toast.makeText(LoginActivity.this, "Os campos são obrigatórios!", Toast.LENGTH_LONG).show();
                }
                UsuarioController crud = new UsuarioController(getBaseContext());
                int teste = crud.VerificaLogin("username = '" + edtUserLogin.getText() + "' and senha = '" + edtSenhaLogin.getText().toString() + "'");

                if (teste > 0) {
                    Toast.makeText(getApplicationContext(), "Redirecionando...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, PaginaInicial.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Usuário ou senha incorretos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}