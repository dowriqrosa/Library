package com.example.library;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CarregarListadeArquivos {

    private static List<AtributosArquivos> list = new ArrayList<AtributosArquivos>();
    private final AdapterListView adaptador;
    private final Context context;
    private final SwipeRefreshLayout swipeLayout;
    private int escolha;
    private ListView listView;
    static ChildEventListener eventListener;
    static DatabaseReference myRef;
    public CarregarListadeArquivos(ListView listView, Context contextt, Activity home,SwipeRefreshLayout swipeLayoutt, int escolha ){
        adaptador = new AdapterListView (list, home);
        this.escolha = escolha;
        this.context = contextt;
        this.swipeLayout = swipeLayoutt;
        this.listView = listView;
        arquivos();
    }

    public void arquivos(){
        list.clear();
        adaptador.clear();
        listView.setAdapter(adaptador);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("File");
        baixarDadosFirebase(myRef);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long l) {
                list.get(posicao).getFile();
                AbrirArquivo abrirArquivo = new AbrirArquivo();
                abrirArquivo.open(context, list.get(posicao).getFile());
            }
        });
    }

    public void baixarDadosFirebase(final DatabaseReference myRef){
        //myRef.removeEventListener();
        eventListener =new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if ( (escolha == 1) && (dataSnapshot.getValue(AtributosArquivos.class).getAutorizado().equals("1")) && (dataSnapshot.getValue(AtributosArquivos.class).getCategoria().toString().equals("Social")) ) {
                    list.add(dataSnapshot.getValue(AtributosArquivos.class));
                    adaptador.notifyDataSetChanged();
                    swipeLayout.setRefreshing(false);
                }else if ( (escolha == 2) && (dataSnapshot.getValue(AtributosArquivos.class).getAutorizado().equals("1")) && (dataSnapshot.getValue(AtributosArquivos.class).getCategoria().toString().equals("Fisica")) ) {
                    list.add(dataSnapshot.getValue(AtributosArquivos.class));
                    adaptador.notifyDataSetChanged();
                    swipeLayout.setRefreshing(false);
                }else if ( (escolha == 3) && (dataSnapshot.getValue(AtributosArquivos.class).getAutorizado().equals("1")) && (dataSnapshot.getValue(AtributosArquivos.class).getCategoria().toString().equals("Mental")) ) {
                    list.add(dataSnapshot.getValue(AtributosArquivos.class));
                    adaptador.notifyDataSetChanged();
                    swipeLayout.setRefreshing(false);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //list.add(dataSnapshot.getValue(AtributosArquivos.class));
               // adaptador.notifyDataSetChanged();
                //swipeLayout.setRefreshing(false);
               // swipeLayout.setRefreshing(true);
                myRef.removeEventListener(eventListener);
                arquivos();
                swipeLayout.setRefreshing(false);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                swipeLayout.setRefreshing(true);
                myRef.removeEventListener(eventListener);
                arquivos();
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRef.addChildEventListener(eventListener);
    }

}
