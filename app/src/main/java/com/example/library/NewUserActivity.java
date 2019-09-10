package com.example.library;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewUserActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText nomeCompleto, email, senha, nascimento, telefone, ocupacao;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        progressBar = (ProgressBar) findViewById(R.id.progressBarNewUser);
        progressBar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        nomeCompleto = (EditText) findViewById(R.id.nomeCompleto);
        email = (EditText) findViewById(R.id.Email);
        senha = (EditText) findViewById(R.id.Senha);
        ocupacao =(EditText) findViewById(R.id.campoocupacao);
        nascimento = (EditText) findViewById(R.id.nascimento);
        telefone =(EditText) findViewById(R.id.campotelefone);
        SimpleMaskFormatter data = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher nascimentoW = new MaskTextWatcher(nascimento, data);
        nascimento.addTextChangedListener(nascimentoW);
        SimpleMaskFormatter tel = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher telW = new MaskTextWatcher(telefone,tel);
        telefone.addTextChangedListener(telW);
    }


    public void btSalvar(View view){
        progressBar.setVisibility(View.VISIBLE);
        String sNome, sEmaill, sSenha, sNascimento, sTelefone, sOcupacao;
        sNome = nomeCompleto.getText().toString();
        sEmaill = email.getText().toString();
        sSenha = senha.getText().toString();
        sNascimento =nascimento.getText().toString();
        sTelefone = telefone.getText().toString();
        sOcupacao = ocupacao.getText().toString();
        if((!sNome.equals("")) && (!sEmaill.equals("")) && (!sSenha.equals("")) && (!sNascimento.equals("")) && (!sTelefone.equals("") && (!sOcupacao.equals("")))) {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), senha.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                task.getException().getMessage().toString();
                                Toast.makeText(NewUserActivity.this, task.getException().getMessage().toString(),
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            } else {
                                DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("admin").setValue("0");
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("nome").setValue(nomeCompleto.getText().toString());
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(email.getText().toString());
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ocupacao").setValue(ocupacao.getText().toString());
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("nascimento").setValue(nascimento.getText().toString());
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("telefone").setValue(telefone.getText().toString());
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                Toast.makeText(NewUserActivity.this, "Login criado com sucesso!",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                finish();
                                //DatabaseReference myRef = database.getReference("Estoque").push();
                                // myRef.setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            }
                        }
                    });
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(NewUserActivity.this, "Preencha todos os campos.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void onClick(View v) {
        if (v.getId() == R.id.bVoltar) {
            finish();
        }
    }
}
