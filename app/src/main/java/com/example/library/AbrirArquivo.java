package com.example.library;


import android.content.ActivityNotFoundException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.widget.Toast;

public class AbrirArquivo {

    public void open(Context context,String url){

        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.parse(url), "application/pdf");

        Intent intent = Intent.createChooser(target, "Abrir arquivo");
        try {
            Toast.makeText(context,"Abrindo PDF",Toast.LENGTH_LONG).show();
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //Caso o usuário não tenha um visualizador de PDF, instrua-o aqui a open
        }
    }

}
