package com.grupo8.processadorconsultas.algebra;

public class GeradorGrafo {

    public static NoGrafo construirGrafo(String sql) {
        String query = sql.toLowerCase().trim().replaceAll("\\s+", " ");

        // Extraindo partes básicas (simplificado para o exemplo)
        String colunas = query.substring(query.indexOf("select") + 6, query.indexOf("from")).trim();
        String resto = query.substring(query.indexOf("from") + 4).trim();

        String tabela;
        String condicao = "";

        if (resto.contains("where")) {
            tabela = resto.substring(0, resto.indexOf("where")).trim();
            condicao = resto.substring(resto.indexOf("where") + 5).trim();
        } else {
            tabela = resto;
        }

        // Montando a Árvore (De cima para baixo: Raiz -> Folhas)
        NoGrafo raiz = new NoGrafo("PROJEÇÃO [π] (" + colunas + ")"); // A raiz é a projeção

        NoGrafo proximoNo = raiz;

        if (!condicao.isEmpty()) {
            NoGrafo noSelecao = new NoGrafo("SELEÇÃO [σ] (" + condicao + ")");
            proximoNo.adicionarFilho(noSelecao);
            proximoNo = noSelecao;
        }

        // Folha: A tabela
        NoGrafo noTabela = new NoGrafo("TABELA (" + tabela + ")");
        proximoNo.adicionarFilho(noTabela);

        return raiz;
    }
}