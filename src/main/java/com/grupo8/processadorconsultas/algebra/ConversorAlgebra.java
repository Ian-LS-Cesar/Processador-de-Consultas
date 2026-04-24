package com.grupo8.processadorconsultas.algebra;

public class ConversorAlgebra {

    public static String converter(String sql) {
        String query = sql.toLowerCase().trim().replaceAll("\\s+", " ");
        // Remove ponto e vírgula final se existir
        if (query.endsWith(";")) {
            query = query.substring(0, query.length() - 1).trim();
        }

        // Extração das partes
        String colunas = query.substring(query.indexOf("select") + 6, query.indexOf("from")).trim();
        String tabelaPart;
        String condicao = "";

        if (query.contains("where")) {
            tabelaPart = query.substring(query.indexOf("from") + 4, query.indexOf("where")).trim();
            condicao = query.substring(query.indexOf("where") + 5).trim();
        } else {
            tabelaPart = query.substring(query.indexOf("from") + 4).trim();
        }

        // Formata a condição: substitui 'and' por '^'
        condicao = condicao.replace(" and ", " ^ ");

        // Tratamento de JOIN para o padrão |X| condicao
        if (tabelaPart.contains("join")) {
            // Exemplo simples: "cliente join pedido on cliente.idcliente = pedido.cliente_idcliente"
            // Para: "cliente |X| cliente.idcliente = pedido.cliente_idcliente pedido"
            String[] partesJoin = tabelaPart.split("join|on");
            if (partesJoin.length >= 3) {
                String t1 = partesJoin[0].trim();
                String condJoin = partesJoin[2].trim();
                String t2 = partesJoin[1].trim();
                tabelaPart = String.format("%s |X| %s %s", firstUpperCase(t1), condJoin, firstUpperCase(t2));
            }
        } else {
            tabelaPart = firstUpperCase(tabelaPart);
        }

        StringBuilder algebra = new StringBuilder();
        algebra.append("π ").append(colunas).append(" (");

        if (!condicao.isEmpty()) {
            algebra.append("σ ").append(condicao).append(" (");
        }

        algebra.append(tabelaPart);

        if (!condicao.isEmpty()) algebra.append(")");
        algebra.append(")");

        return algebra.toString();
    }

    private static String firstUpperCase(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}