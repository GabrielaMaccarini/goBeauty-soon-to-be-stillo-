package com.example.gabim.godiva;

import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.gabim.godiva.controller.UsuarioController;
import com.example.gabim.godiva.modelos.Usuario;
import com.example.gabim.godiva.util.Database;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private Database db;
    private SQLiteDatabase instanciaDb;

    EditText edtUserLogin;
    EditText edtSenhaLogin;
    Button btnEntrar;
    LoginButton loginButton;
    CallbackManager callbackManager;


    public boolean validaLogin() {
        if (edtUserLogin.getText().toString().length() == 0 && edtSenhaLogin.getText().toString().length() == 0) {
            Toast.makeText(LoginActivity.this, "Os campos são obrigatórios!", Toast.LENGTH_LONG).show();
            //return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.gabim.godiva",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        loginButton = (LoginButton) findViewById(R.id.loginButton);
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(LoginActivity.this, PaginaInicial.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login cancelado pelo usuário.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, "Ocorreu um erro ao fazer o login.", Toast.LENGTH_SHORT).show();
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Intent intent = new Intent(LoginActivity.this, PaginaInicial.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Não entrou(cancelou login)", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, "Não entrou(erro login)", Toast.LENGTH_SHORT).show();
                    }
                });

        edtUserLogin = (EditText) findViewById(R.id.edtUserLogin);
        edtSenhaLogin = (EditText) findViewById(R.id.edtSenhaLogin);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validaLogin()) {
                    UsuarioController crud = new UsuarioController(getBaseContext());
                    int teste = crud.VerificaLogin("username = '" + edtUserLogin.getText() + "' and senha = '" + edtSenhaLogin.getText().toString() + "'");
                    if (teste > 0) {
                        Toast.makeText(getApplicationContext(), "Redirecionando...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, PaginaInicial.class);
                        intent.putExtra("username", edtUserLogin.getText().toString());
                        intent.putExtra("botao", Integer.toString(view.getId()));
                        Toast.makeText(LoginActivity.this, ""+view.getId(), Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Usuário ou senha incorretos.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == RC_SIGN_IN){
//            if(resultCode == RESULT_OK){
//                //user logou
//                Log.d("AUTH", auth.getCurrentUser().getEmail());
//            }
//        }
//    }
}