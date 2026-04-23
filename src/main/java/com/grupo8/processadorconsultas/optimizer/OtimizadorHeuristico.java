package com.grupo8.processadorconsultas.optimizer;

import com.grupo8.processadorconsultas.algebra.NoGrafo;

public class OtimizadorHeuristico {

    public static NoGrafo otimizar(String sql) {
        // Extração de dados (simplificada para o exemplo)
        String query = sql.toLowerCase().trim().replaceAll("\\s+", " ");
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

        // --- APLICAÇÃO DAS HEURÍSTICAS ---

        // 1. O nó folha é sempre a tabela
        NoGrafo noTabela = new NoGrafo("TABELA (" + tabela + ")");

        // 2. Heurística: Seleção primeiro (Redução de Tuplas) [cite: 137]
        NoGrafo nivelAnterior = noTabela;
        if (!condicao.isEmpty()) {
            NoGrafo noSelecao = new NoGrafo("SELEÇÃO OTIMIZADA [σ] (" + condicao + ")");
            noSelecao.adicionarFilho(noTabela);
            nivelAnterior = noSelecao;
        }

        // 3. Heurística: Projeção logo após a seleção (Redução de Atributos)
        // Mesmo que no SQL o SELECT venha antes, no grafo otimizado ele "desce" para filtrar as colunas cedo.
        NoGrafo noProjecao = new NoGrafo("PROJEÇÃO OTIMIZADA [π] (" + colunas + ")");
        noProjecao.adicionarFilho(nivelAnterior);

        return noProjecao; // A raiz agora é a projeção otimizada
    }
}
