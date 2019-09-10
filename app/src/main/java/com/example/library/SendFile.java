package com.example.library;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class SendFile {
    private AtributosArquivos file;
    private UploadTask uploadTask;
    private View v;
    public static boolean envioTerminado = false;
    //private ProgressBar mProgressBar;

    public boolean salve(final String nome, String path, final String categoria, final ProgressBar mProgressBar, final Context context){
        envioTerminado = false;
        try{

            file = new AtributosArquivos();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("File").push();
            file.setNomeArquivo(myRef.getKey().toString());
            /*file = new AtributosArquivos();
            file.setNome(nome);
            file.setuserUid(User.getUid().toString());
            file.setCategoria(categoria);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference("File").push();
           // myRef.setValue(file);
            file.setId(myRef.getKey().toString());
            myRef.setValue(file);
            //*/
            Uri fil = (Uri) Uri.fromFile(new File(path));
            final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("File").child(file.getNomeArquivo());
            uploadTask = mStorageRef.putFile(fil);
            //file.setImg(mStorageRef.getDownloadUrl().toString());
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    file.setAutorizado("0");
                    file.setNome(nome);
                    file.setuserUid(User.getUid().toString());
                    file.setCategoria(categoria);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("File").push();
                    // myRef.setValue(file);
                    //myRef.setValue(file);
                    file.setfile(taskSnapshot.getMetadata().getDownloadUrl().toString());
                    file.setId(myRef.getKey().toString());
                    myRef.setValue(file);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, "Arquivo enviado!", Toast.LENGTH_SHORT).show();
                    terminoEnvio();
                    //System.out.println("sucess"+file.getFile());
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {


                }
            });


            //file.setImg(mStorageRef.getMetadata().getResult().getDownloadUrl().toString());
            //upImg();
            //file.setImg(urlFoto);
            // myRef.setValue(file);
            //Toast.makeText(this, "arquivo cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            //Intent cadastro = new Intent(this, Principal.class);
            //this.finish();
            //startActivity(cadastro);
            return true;
        }catch (Exception e){
            e.getStackTrace();
            //Toast.makeText(this, "erro!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public boolean salveUrl(String nome, String url, String categoria, ProgressBar mProgressBar, Context context){
        envioTerminado = false;
        try{

            file = new AtributosArquivos();
            file.setAutorizado("0");
            file.setNome(nome);
            file.setuserUid(User.getUid().toString());
            file.setfile(url);
            file.setCategoria(categoria);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference("File").push();
            //myRef.setValue(file);
            file.setId(myRef.getKey().toString());
            myRef.setValue(file);
            //mProgressBar.setProgress(100);
            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "Arquivo enviado!", Toast.LENGTH_SHORT).show();
            terminoEnvio();
            return true;
        }catch (Exception e){
            e.getStackTrace();
            //Toast.makeText(this, "erro!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static boolean isEnvioTerminado() {
        return envioTerminado;
    }
    
    public void terminoEnvio(){
        envioTerminado = true;
    } 


}
