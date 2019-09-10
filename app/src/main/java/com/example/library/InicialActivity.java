package com.example.library;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InicialActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        mAuth = FirebaseAuth.getInstance();
        verificarLogin();
    }

    public void btnCadastrar(View view) {
        Intent cadastrar = new Intent(this, NewUserActivity.class);
        startActivity(cadastrar);
    }

    public void btnLogin(View view) {
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }

    @Override
    public void onPause(){
        super.onPause();
        verificarLogin();
    }
    @Override
    public void onRestart(){
        super.onRestart();
        verificarLogin();
    }

    public void verificarLogin(){
        final Context context = this;
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //FirebaseAuth.getInstance().getCurrentUser().getUid()
                    Intent menuInicialActivity = new Intent(getApplicationContext(), MenuInicialActivity.class);
                    Toast.makeText(context, "Bem vindo!", Toast.LENGTH_LONG).show();
                    startActivity(menuInicialActivity);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
            finish();
        }
    }
}
