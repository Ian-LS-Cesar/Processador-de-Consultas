package com.grupo8.processadorconsultas.algebra;

public class ConversorAlgebra {

    public static String converter(String sql) {
        String query = sql.toLowerCase().trim().replaceAll("\\s+", " ");

        // Extração simples das partes (Baseado nas regras da HU2)
        String colunas = query.substring(query.indexOf("select") + 6, query.indexOf("from")).trim();

        String tabela;
        String condicao = "";

        if (query.contains("where")) {
            tabela = query.substring(query.indexOf("from") + 4, query.indexOf("where")).trim();
            condicao = query.substring(query.indexOf("where") + 5).trim();
        } else {
            tabela = query.substring(query.indexOf("from") + 4).trim();
        }

        // Montando a expressão: π colunas ( σ condicao ( tabela ) )
        StringBuilder algebra = new StringBuilder();

        // 1. Projeção (π) -
        algebra.append("π ").append(colunas).append(" (");

        // 2. Seleção (σ) se houver WHERE -
        if (!condicao.isEmpty()) {
            algebra.append("σ ").append(condicao).append(" (");
        }

        // 3. Tabela (Tratando JOIN como símbolo de junção ⨝ se necessário)
        if (tabela.contains("join")) {
            tabela = tabela.replace("join", "⨝");
        }
        algebra.append(tabela);

        // Fechando parênteses
        if (!condicao.isEmpty()) algebra.append(")");
        algebra.append(")");

        return algebra.toString();
    }
}