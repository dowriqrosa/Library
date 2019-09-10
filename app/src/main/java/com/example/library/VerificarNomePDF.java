package com.example.library;

public class VerificarNomePDF {
    //private String nomee; //= "http://www.lbd.dcc.ufmg.br/colecoes/sbseg/2014/0036.pdf";
    public boolean extensaoPDF(String nome){
        //this.nomee = nome;
        boolean retorno = false ;
        int tamanhoNome = nome.length();
        if(tamanhoNome > 11){
            //System.out.println(nome.charAt(tamanhoNome-1));
            char f = nome.charAt(tamanhoNome-1);
            char d = nome.charAt(tamanhoNome-2);
            char p = nome.charAt(tamanhoNome-3);
            char ponto = nome.charAt(tamanhoNome-4);
            if (f == 'f' || f == 'F'){
                if(d == 'd' || d == 'D'){
                    if(p =='p' || p == 'P'){
                        if(ponto == '.'){
                            retorno = true;
                        }
                    }
                }
            }
        }
        return retorno;
    }

    public String extrairNome(String nome){


        return nome;
    }
}
