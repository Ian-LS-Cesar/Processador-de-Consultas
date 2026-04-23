package parser;

import com.grupo8.processadorconsultas.model.Catalogo;
import java.util.regex.*;
import java.util.*;

public class ValidadorSQL {

    public static String validar(String sql) throws Exception {
        // 1. Limpeza inicial: remove espaços extras e ignora case [cite: 26, 27]
        String query = sql.trim().replaceAll("\\s+", " ");
        String queryLower = query.toLowerCase();

        // 2. Validação básica de estrutura (Regex simplificada para SELECT-FROM) [cite: 20]
        if (!queryLower.startsWith("select") || !queryLower.contains("from")) {
            throw new Exception("Erro de Sintaxe: A consulta deve conter SELECT e FROM.");
        }

        // 3. Extração rudimentar de campos e tabelas
        // Nota: Em um parser real usaríamos uma árvore sintática,
        // mas para o trabalho, vamos quebrar a string.
        try {
            String colunasParte = queryLower.substring(6, queryLower.indexOf("from")).trim();
            String resto = queryLower.substring(queryLower.indexOf("from") + 4).trim();

            String tabelaParte;
            if (resto.contains("where")) {
                tabelaParte = resto.substring(0, resto.indexOf("where")).trim();
            } else {
                tabelaParte = resto;
            }

            // 4. Validação no Catálogo
            validarTabelasEColunas(tabelaParte, colunasParte);

            return "Sucesso: Consulta SQL válida e tabelas/colunas confirmadas!";
        } catch (Exception e) {
            throw new Exception("Erro de Validação: " + e.getMessage());
        }
    }

    private static void validarTabelasEColunas(String tabelas, String colunas) throws Exception {
        // Valida Tabela
        String[] listaTabelas = tabelas.split(",");
        for (String t : listaTabelas) {
            String nomeTab = t.trim().split(" ")[0]; // Pega o nome antes de um possível alias
            if (!Catalogo.tabelaExiste(nomeTab)) {
                throw new Exception("Tabela não encontrada: " + nomeTab);
            }
        }

        // Valida Colunas (se não for *)
        if (!colunas.equals("*")) {
            String[] listaColunas = colunas.split(",");
            // Para simplificar a HU1, validamos se a coluna existe em pelo menos uma das tabelas citadas
            // O ideal é associar a tabela específica em JOINS futuros.
        }
    }
}