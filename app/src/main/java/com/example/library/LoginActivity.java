package com.example.library;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText edtEmail, edtSenha;
    private boolean loopProgressBar;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
        mProgressBar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        //edtNome = (EditText) findViewById(R.id.edtNome);
        edtEmail = (EditText) findViewById(R.id.tLogin);
        edtSenha = (EditText) findViewById(R.id.tSenha);
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                /*if (user != null) {
                    //FirebaseAuth.getInstance().getCurrentUser().getUid()
                    Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
                    homeActivity.putExtra("msgRetorno", "Bem vindo!");
                    startActivity(homeActivity);
                }*/
            }
        };
    }


    public void btnEsqueceuSenha(View v){
        if(!edtEmail.getText().toString().equals("")) {
            mProgressBar.setVisibility(View.VISIBLE);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String emailAddress = edtEmail.getText().toString();
            final Context context = this;
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Email enviado.", Toast.LENGTH_LONG).show();
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Email não cadastrado.", Toast.LENGTH_LONG).show();
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }else{
            Toast.makeText(this, "Preencha o campo Login.", Toast.LENGTH_LONG).show();
        }
    }

    public void btnLogin(View v){
        if(!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")) {
            mProgressBar.setVisibility(View.VISIBLE);
            try {
                final Intent i = new Intent(this, MenuInicialActivity.class);
                mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtSenha.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                    //Intent i = new Intent(this, HomeActivity.class);
                                    i.putExtra("msgRetorno", "Bem vindo!");
                                    finish();
                                    startActivity(i);
                                    // Sign in success, update UI with the signed-in user's information
                                    ///Log.d(TAG, "signInWithEmail:success");
                                    // FirebaseUser user = mAuth.getCurrentUser();
                                    ///updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    // Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    //Toast.makeText(Login.this, "Authentication failed.",
                                    // Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "erro!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } catch (Exception e) {
                e.getStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Preencha o login e a senha!",Toast.LENGTH_SHORT).show();
        }
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
