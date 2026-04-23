package com.grupo8.processadorconsultas.algebra;

import java.util.ArrayList;
import java.util.List;

public class NoGrafo {
    private String operacao;
    private List<NoGrafo> filhos;

    public NoGrafo(String operacao) {
        this.operacao = operacao;
        this.filhos = new ArrayList<>();
    }

    public void adicionarFilho(NoGrafo filho) {
        this.filhos.add(filho);
    }

    // Método para imprimir a árvore com indentação (Visualização do Grafo)
    public String imprimir(String prefixo) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefixo).append("└── ").append(operacao).append("\n");
        for (NoGrafo filho : filhos) {
            sb.append(filho.imprimir(prefixo + "    "));
        }
        return sb.toString();
    }
}
