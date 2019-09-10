package com.example.library;

import java.io.Serializable;

public class AtributosArquivos extends ValidacaoColorClick{
    private String id;
    private String nome;
    private String file;
    private String userUid;
    private String categoria;
    private String autorizado;
    private String nomeArquivo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFile() {
        return file;
    }

    public void setfile(String img) {
        this.file = img;
    }

    public String getuserUid() {
        return userUid;
    }

    public void setuserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(String altorizado) {
        this.autorizado = altorizado;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    @Override
    public String toString() {
        return this.nome;
    }


}
