package com.example.library;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Pag1.OnFragmentInteractionListener, Pag2.OnFragmentInteractionListener{


    private NavigationView navigationView;
    private Menu menu;
    private SwipeRefreshLayout swipeLayout;
    private List<AtributosArquivos> list = new ArrayList<AtributosArquivos>();
    private ListView listView;
    //private static boolean reload;
    private CarregarListadeArquivos carregarListadeArquivos;
    private int listaTipo;
    private ProgressBar progressBar;
    private EditText localArquivo;
    private ImageButton imgAdd;
    private VerificarNomePDF verificarNomePDF = new VerificarNomePDF();
    private Context context = this;
    private final int MY_PERMISSIONS_REQUEST_STORAGE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle("Saude Box");
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Mensenger Welcome
        Intent i = getIntent();
        if(i!=null){
            String msgs = i.getStringExtra("saude");
            if(msgs.equals("social")) {
                listaTipo = 1;
                Toast.makeText(this, "Saude Social", Toast.LENGTH_LONG).show();
            }else if (msgs.equals("fisica")){
                listaTipo = 2;
                Toast.makeText(this, "Saude Fisica", Toast.LENGTH_LONG).show();
            }else {
                listaTipo = 3;
                Toast.makeText(this, "Saude Mental", Toast.LENGTH_LONG).show();
            }
        }

        paginas();

        //permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_STORAGE);

                // MY_PERMISSIONS_REQUEST_CAMERA is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        this.menu = navigationView.getMenu();
        this.menu.getItem(3).setVisible(false);
        //carregar nome e email do usuario logado
        getInformationUSer();
        //progressBar()
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.home,menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.fisica) {
            // Handle the camera action
            CarregarListadeArquivos.myRef.removeEventListener(CarregarListadeArquivos.eventListener);
            listaTipo = 2;
            listarArquivos();
        } else if (id == R.id.mental) {
            CarregarListadeArquivos.myRef.removeEventListener(CarregarListadeArquivos.eventListener);
            listaTipo = 3;
            listarArquivos();
        } else if (id == R.id.social) {
            CarregarListadeArquivos.myRef.removeEventListener(CarregarListadeArquivos.eventListener);
            listaTipo = 1;
            listarArquivos();
        } else if (id == R.id.listAdmin) {
            new Thread(){
                @Override
                public void run() {
                    Intent menuAdmin = new Intent(context, MenuAdminActivity.class);
                    startActivity(menuAdmin);
                }

            }.start();
        } else if (id == R.id.trocaUsuario) {
            Intent login = new Intent(context,LoginActivity.class);
            startActivity(login);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getInformationUSer(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefEmail = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email");
        DatabaseReference myRefUser = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("nome");
        DatabaseReference myRefUid = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uid");
        DatabaseReference myRefAdmin = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("admin");


        myRefAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                if (value.equals("1")){
                    User.setAdmin(value.toString());
                    menu.getItem(3).setVisible(true);
                    //item.setVisible(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        myRefEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                User.setNome(value.toString());
                user();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        myRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                User.setEmail(value.toString());
                user();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        myRefUid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                User.setUid(value.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        funcaoRecarregarListadeArquivos();
        listarArquivos();
        progressBar = (ProgressBar) findViewById(R.id.progressBarNewF);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void user(){
        TextView nome = (TextView) findViewById(R.id.textName);
        nome.setText(User.getNome());
        TextView email = (TextView) findViewById(R.id.textEmail);
        email.setText(User.getEmail());
    }

    private static String path = null;
    private static final String TAG = "0";

    public void btSalvar(View v){
        Context context = this;
        progressBar.setVisibility(View.VISIBLE);
        EditText edtUrl = (EditText) findViewById(R.id.fileLocal);
        //Button bt = (Button) findViewById(R.id.buttonSend);
        RadioButton social = (RadioButton) findViewById(R.id.social);
        RadioButton fisica = (RadioButton) findViewById(R.id.fisica);
        RadioButton mental = (RadioButton) findViewById(R.id.mental);
        Button bt = (Button) findViewById(R.id.buttonSend);
        EditText edtNome = (EditText) findViewById(R.id.nameFile);
        imgAdd = (ImageButton) findViewById(R.id.uploadFile);
        localArquivo = (EditText) findViewById(R.id.fileLocal);
        String nome = edtNome.getText().toString();
        SendFile send =new  SendFile();
        bt.setEnabled(false);
        localArquivo.setEnabled(false);
        imgAdd.setEnabled(false);
        edtNome.setEnabled(false);
        social.setEnabled(false);
        fisica.setEnabled(false);
        mental.setEnabled(false);
        String categoria;
        if(social.isChecked()) {
            categoria = "Social";
        }else if(fisica.isChecked()){
            categoria = "Fisica";
        }else{
            categoria = "Mental";
        }
        if(path != null) {
            if (verificarNomePDF.extensaoPDF(edtUrl.getText().toString())) {
                //Toast.makeText(this,"é pdf",Toast.LENGTH_LONG).show();
                if(edtNome.getText().toString() != null){
                    if(social.isChecked() || fisica.isChecked() || mental.isChecked()) {
                        send.salve(nome,path, categoria, progressBar, context);
                        limpaCampo();
                        //String url = edtUrl.getText().toString();
                        //send.salveUrl(nome, url, categoria, progressBar);

                    }else{
                        Toast.makeText(this, "Selecione uma categoria!", Toast.LENGTH_LONG).show();
                        enableCampos(true);
                    }
                }else{
                    Toast.makeText(this, "Preencha campo nome!", Toast.LENGTH_LONG).show();
                    enableCampos(true);
                }
            } else {
                Toast.makeText(this, "Não é pdf ou o link não é valido", Toast.LENGTH_LONG).show();
                enableCampos(true);
            }
        }else if(edtUrl.getText().toString() != null){
            if (verificarNomePDF.extensaoPDF(edtUrl.getText().toString())) {
                //Toast.makeText(this,"é pdf",Toast.LENGTH_LONG).show();
                if(edtNome.getText().toString() != null){
                    if(social.isChecked() || fisica.isChecked() || mental.isChecked()) {
                        String url = edtUrl.getText().toString();
                        send.salveUrl(nome, url, categoria, progressBar,context);
                        limpaCampo();
                    }else{
                        Toast.makeText(this, "Selecione uma categoria!", Toast.LENGTH_LONG).show();
                        enableCampos(true);
                    }
                }else{
                    Toast.makeText(this, "Preencha campo nome!", Toast.LENGTH_LONG).show();
                    enableCampos(true);
                }
            } else {
                Toast.makeText(this, "Não é pdf ou o link não é valido", Toast.LENGTH_LONG).show();
                enableCampos(true);
            }
        }else{
            Toast.makeText(this, "Preencha todos os campos solicitados!", Toast.LENGTH_LONG).show();
            enableCampos(true);
        }
    }

    public boolean nomeVazio(String nome){
        if(null == nome){
            return false;
        }
        return true;
    }

    private static final int FILE_SELECT_CODE = 0;
    public void btAddFile(View v){
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecione o Arquivo"),FILE_SELECT_CODE);
    }

    public void enableCampos(boolean boole){
        RadioButton social = (RadioButton) findViewById(R.id.social);
        RadioButton fisica = (RadioButton) findViewById(R.id.fisica);
        RadioButton mental = (RadioButton) findViewById(R.id.mental);
        Button bt = (Button) findViewById(R.id.buttonSend);
        EditText edtNome = (EditText) findViewById(R.id.nameFile);
        progressBar.setVisibility(View.INVISIBLE);
        bt.setEnabled(boole);
        localArquivo.setEnabled(boole);
        imgAdd.setEnabled(boole);
        edtNome.setEnabled(boole);
        social.setEnabled(boole);
        fisica.setEnabled(boole);
        mental.setEnabled(boole);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG,"File Uri: " + uri.toString());
                    try {
                        path = FileUtils.getPath(this, uri);
                        //BOta no text  path
                        if(path != null) {
                            if(verificarNomePDF.extensaoPDF(path)) {
                                localArquivo = (EditText) findViewById(R.id.fileLocal);
                                localArquivo.setText(path);
                                //imgAdd = (ImageButton) findViewById(R.id.uploadFile);
                                //imgAdd.setImageResource(android.R.drawable.checkbox_on_background);
                            }else{
                                Toast.makeText(this,"Selecione um arquivo do tipo pdf",Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(this,"Selecionar arquivo atravez de um Geranciador de arquivos",Toast.LENGTH_LONG).show();
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "File Path: " + path);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void paginas(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter myPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);//setUpWithViewPager(viewPager);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void listarArquivos(){
        carregarListadeArquivos = null;
        swipeLayout.setRefreshing(true);
        listView = (ListView) findViewById(R.id.listArquivos);
        final AdapterListView adaptador = new AdapterListView (list, this);
        carregarListadeArquivos = new CarregarListadeArquivos(listView,this,this, swipeLayout, listaTipo);
    }

    public void funcaoRecarregarListadeArquivos(){
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(false);
            }
        });

    }

    public void limpaCampo(){
        new Thread(){
            @Override
            public void run(){

                while(!SendFile.isEnvioTerminado()){

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        limpa();
                    }
                });

            }
        }.start();

    }
    public void limpa(){
        EditText n = (EditText) findViewById(R.id.nameFile);
        localArquivo.setText("");
        n.setText("");
        enableCampos(true);
    }

}
