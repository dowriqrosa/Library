package com.example.library;

public class User {
    private static String uid;
    private static String nome;
    private static String email;
    private static String senha;
    private static String nascimento;
    private static String telefone;
    private static String ocupacao;
    private static String admin;

    public static String getUid() {
        return uid;
    }

    public static void setUid(String uid) {
        User.uid = uid;
    }

    public static String getNome() {
        return nome;
    }

    public static void setNome(String nome) {
        User.nome = nome;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static String getSenha() {
        return senha;
    }

    public static void setSenha(String senha) {
        User.senha = senha;
    }

    public static String getNascimento() {
        return nascimento;
    }

    public static void setNascimento(String nascimento) {
        User.nascimento = nascimento;
    }

    public static String getTelefone() {
        return telefone;
    }

    public static void setTelefone(String telefone) {
        User.telefone = telefone;
    }

    public static String getOcupacao() {
        return ocupacao;
    }

    public static void setOcupacao(String ocupacao) {
        User.ocupacao = ocupacao;
    }

    public static String getAdmin() {
        return admin;
    }

    public static void setAdmin(String admin) {
        User.admin = admin;
    }
}
