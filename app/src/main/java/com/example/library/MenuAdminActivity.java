package com.example.library;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MenuAdminActivity extends AppCompatActivity {

    private ListView listView;
    private static List<AtributosArquivos> list = new ArrayList<AtributosArquivos>();
    private ProgressBar progressBar;
    private static ChildEventListener eventListener;
    private DatabaseReference myRef;
    private static List<AtributosArquivos> listAlteracao = new ArrayList<AtributosArquivos>();
    private boolean escolha;
    private  AdapterView.OnItemLongClickListener itemClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        setTitle("");
        listView = (ListView) findViewById(R.id.listArquivos);
        progressBar = (ProgressBar) findViewById(R.id.progressBarAdmin);
        progressBar.setVisibility(View.VISIBLE);
        list.clear();
        listAlteracao.clear();
    }

    @Override
    public void onBackPressed() {
        myRef.removeEventListener(eventListener);
        this.finish();
        super.onBackPressed();

    }

    public void btnVoltar(View view) {
        myRef.removeEventListener(eventListener);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
        listarArquivos();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.aprovar) {
            escolha = true;
            boxConfirmacao();
            return true;
        }else{
            escolha = false;
            boxConfirmacao();
        }

        return super.onOptionsItemSelected(item);
    }




    public void boxConfirmacao(){
        if(listAlteracao.size() >0) {
            AlertDialog.Builder confirmacao = new AlertDialog.Builder(this);
            confirmacao.setMessage("Deseja prosseguir com esta ação?");
            confirmacao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < listAlteracao.size(); i++) {
                        acao(listAlteracao.get(i));
                    }
                    listAlteracao.clear();
                    listarArquivos();
                }
            });
            confirmacao.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });
            confirmacao.show();
        }else {
            Toast.makeText(getBaseContext(),"Selecione pelo menos um iten!",Toast.LENGTH_SHORT).show();
        }
    }

    public void listarArquivos(){
        list.clear();
        final Context context = this;
        final AdapterListView adaptador = new AdapterListView (list, this);
        listView.setAdapter(adaptador);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("File");
        baixarDadosFirebase(myRef,adaptador);
        itemClickListener = new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //listView.setSelection(position);
                if(!list.get(position).isColor()){
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    list.get(position).setColor(true);
                    listAlteracao.add(list.get(position));
                }else {
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    listAlteracao.remove(list.get(position));
                    list.get(position).setColor(false);
                }
                list.get(position).setClickLong(true);
                return false;
            }
        };
        listView.setOnItemLongClickListener(itemClickListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               // view.setOnLongClickListener();
                if(!list.get(position).isClickLong()) {
                    list.get(position).getFile();
                    AbrirArquivo abrirArquivo = new AbrirArquivo();
                    abrirArquivo.open(context, list.get(position).getFile());
                    //Toast.makeText(getBaseContext(),,Toast.LENGTH_SHORT).show();
                }else {
                    list.get(position).setClickLong(false);
                }
            }
        });

    }


       public void baixarDadosFirebase(DatabaseReference myReff, final AdapterListView adaptador){
        //myRef.removeEventListener();
           eventListener = new ChildEventListener() {


               @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if ( (dataSnapshot.getValue(AtributosArquivos.class).getAutorizado().equals("0"))) {
                        list.add(dataSnapshot.getValue(AtributosArquivos.class));
                        adaptador.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    myRef.removeEventListener(eventListener);
                    listarArquivos();
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
           myReff.addChildEventListener(eventListener);
    }

    public void acao(AtributosArquivos atributosArquivos){
        if(escolha){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("File").child(atributosArquivos.getId()).child("altorizado");
            myRef.setValue("1");
            Toast.makeText(getBaseContext(),"Arquivos aprovados",Toast.LENGTH_SHORT).show();
        }else{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("File");
            myRef.child(atributosArquivos.getId()).removeValue();
            StorageReference desertRef = FirebaseStorage.getInstance().getReference("File");
            desertRef.child(atributosArquivos.getNomeArquivo()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                }
            });
            Toast.makeText(getBaseContext(),"Arquivos Removidos",Toast.LENGTH_SHORT).show();
        }
        myRef.removeEventListener(eventListener);
    }


}
