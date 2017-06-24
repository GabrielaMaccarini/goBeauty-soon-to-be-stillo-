package com.example.gabim.godiva;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import com.example.gabim.godiva.controller.ProprietarioController;
import com.example.gabim.godiva.controller.UsuarioController;
import com.example.gabim.godiva.modelos.Usuario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

public class PaginaInicial extends AppCompatActivity implements OnMapReadyCallback {
    Usuario userPerfil = new Usuario();
    ListView listaEstCidade = null;
    SearchView svvPesquisa = null;
    String botaoLogin = "2131755175";
    ArrayList<String> listaEstabelecimentos = new ArrayList<>();
    private GoogleMap mMap;

    //-- ID's
    ArrayList<String> listaEstabelecimentosId = new ArrayList<>(); //-- ID do estabelecimento - precisa ser guardado

    //-- Email do ESTABELECIMENTO
    ArrayList<String> listaEstabelecimentosEmail = new ArrayList<>();
    ArrayAdapter<String> listaEstadoCidadeAdapter;

    public String[] AtualizaEstabelecimentos(Context ct, String pesquisa) {
        UsuarioController pc = new UsuarioController(ct);
        List<Usuario> lp = pc.trazEstabCidade(pesquisa);

        String array[] = new String[lp.size()];

        if (!lp.isEmpty()) {
            for (int i = 0; i < lp.size(); i++) {
                array[i] = lp.get(i).getNome();
//                Toast.makeText(ct, lp.get(i).getNome(), Toast.LENGTH_SHORT).show();
            }
        }
        return array;
    }

    public List<Usuario> trazUsuarios(String pesquisa) {
        Usuario user;
        user = userPerfil;
        Toast.makeText(getBaseContext(), user.getPerfil(), Toast.LENGTH_SHORT).show();
        user.getPerfil();
        return null;
    }

    public List<Usuario> AtualizaEstabelecimentos2(Context ct, String pesquisa) {
        UsuarioController usuarioController = new UsuarioController(ct);
        return usuarioController.trazEstabCidade(pesquisa);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar paginaInicial = getSupportActionBar();
        setContentView(R.layout.activity_pagina_inicial);

        if(botaoLogin.equals(getIntent().getStringExtra("botao"))) {
            UsuarioController crud = new UsuarioController(getBaseContext());
            userPerfil = crud.getByUsername(getIntent().getStringExtra("username"));
        }

        listaEstCidade = (ListView) findViewById(R.id.listaPesquisaCidade);
        svvPesquisa = (SearchView) findViewById(R.id.svPesquisa);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec abaPesquisaCidade = tabHost.newTabSpec("Compras pendentes");
        abaPesquisaCidade.setContent(R.id.tabPesquisaCidade);
        abaPesquisaCidade.setIndicator("Pesquisar cidade");

        TabHost.TabSpec abaPesquisaLocal = tabHost.newTabSpec("Compras Realizadas");
        abaPesquisaLocal.setContent(R.id.tabPesquisaLocal);
        abaPesquisaLocal.setIndicator("Pesquisar Local");

        tabHost.addTab(abaPesquisaCidade);
        tabHost.addTab(abaPesquisaLocal);

        paginaInicial.setTitle("Página Inicial");
        paginaInicial.setDisplayHomeAsUpEnabled(false);

        listaEstadoCidadeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaEstabelecimentos);
        listaEstCidade.setAdapter(listaEstadoCidadeAdapter);

        svvPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //-- Busca os estabelecimentos e coloca em um "Array de objetos"
                List<Usuario> listaAtualizadaEstabelecimentos = AtualizaEstabelecimentos2(getBaseContext(), svvPesquisa.getQuery().toString());

                //-- Só inicia o for se não foi null ou vazio
                listaEstadoCidadeAdapter.clear();
                listaEstabelecimentosEmail.clear();
                listaEstabelecimentosId.clear();

                if (!listaAtualizadaEstabelecimentos.isEmpty()) {
                    for (int i = 0; i < listaAtualizadaEstabelecimentos.size(); i++) {
                        //-- Adiciona o nome do estabelecimento na "Tela"/"Lista de Estabelecimentos"
                        //-- Busca o ID e Nome do estabelecimento
                        int id = listaAtualizadaEstabelecimentos.get(i).getId();
                        String nome = listaAtualizadaEstabelecimentos.get(i).getNome();
                        String email = listaAtualizadaEstabelecimentos.get(i).getEmail();

                        //-- Salvando o ID para uso posterior
                        listaEstabelecimentosId.add(Integer.toString(id));
                        listaEstabelecimentosEmail.add(email);
                        listaEstadoCidadeAdapter.add(nome);
                    }
                }
                listaEstadoCidadeAdapter.notifyDataSetChanged();
                return true;
            }

        });
        listaEstCidade.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                ProprietarioController proprietarioControl = new ProprietarioController(getApplicationContext());

                String[] to = new String[]{listaEstabelecimentosEmail.get(position).toString()};
                String subject = "Reserva de Horário";
                String emailText = "";

                emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, "E-mail"));
                return false;
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(-33.867, 151.206);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Sydney"));
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
//            if (botaoLogin.equals(getIntent().getStringExtra("botao"))) {
                if (userPerfil.getPerfil() == 1) {
                    MenuInflater criaMenu = getMenuInflater();
                    criaMenu.inflate(R.menu.menu_cadastro_proprietario, menu);
                    return true;
                }
//            }
            return false;
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

}