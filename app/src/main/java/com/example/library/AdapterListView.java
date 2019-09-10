package com.example.library;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterListView extends BaseAdapter {
    private final List<AtributosArquivos> Arquivos;
    private final Activity act;


    public AdapterListView(List<AtributosArquivos> Arquivos, Activity act) {
        this.Arquivos = Arquivos;
        this.act = act;
    }

    @Override
    public int getCount() {
        return Arquivos.size();
    }

    @Override
    public Object getItem(int position) {
        return Arquivos   .get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater()
                .inflate(R.layout.lista_de_arquivos_personalizado, parent, false);

       AtributosArquivos curso = Arquivos.get(position);

        TextView nome = (TextView)
                view.findViewById(R.id.lista_curso_personalizada_nome);
        TextView descricao = (TextView)
                view.findViewById(R.id.lista_curso_personalizada_descricao);
        ImageView imagem = (ImageView)
                view.findViewById(R.id.lista_curso_personalizada_imagem);

        nome.setText(curso.getNome());
        descricao.setText(curso.getCategoria());
        //descricao.setText(curso.getDescricao());
        //imagem.setImageResource(R.drawable.ic_menu_camera);

        return view;
    }

    public void clear() {
        Arquivos.clear();
        notifyDataSetChanged();
    }
}
