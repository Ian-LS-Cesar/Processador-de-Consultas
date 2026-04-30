package com.grupo8.processadorconsultas.view;

import com.grupo8.processadorconsultas.algebra.ConversorAlgebra;
import com.grupo8.processadorconsultas.algebra.GeradorGrafo;
import com.grupo8.processadorconsultas.algebra.NoGrafo;
import com.grupo8.processadorconsultas.optimizer.OtimizadorHeuristico;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.grupo8.processadorconsultas.model.Catalogo; // Certifique-se que o pacote está correto
import parser.ValidadorSQL;

public class MainApp extends Application {

    private TextArea inputSQL;
    private TextArea outputAlgebra;
    private TextArea outputGrafo;
    private TextArea outputPlano;

    private static class PartesSQL {
        final String colunas;
        final String tabela;
        final String condicao;

        PartesSQL(String colunas, String tabela, String condicao) {
            this.colunas = colunas;
            this.tabela = tabela;
            this.condicao = condicao;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Processador de Consultas SQL - Unifor");

        // --- ENTRADA (HU1) ---
        VBox painelEntrada = new VBox(10);
        painelEntrada.setPadding(new Insets(15));

        Label lblSQL = new Label("Consulta SQL:");
        inputSQL = new TextArea();
        inputSQL.setPromptText("Ex: SELECT nome FROM cliente WHERE idcliente = 1");
        inputSQL.setPrefHeight(100);

        Button btnProcessar = new Button("Processar e Otimizar");
        btnProcessar.setMaxWidth(Double.MAX_VALUE);
        btnProcessar.setOnAction(e -> acaoBotaoProcessar());

        painelEntrada.getChildren().addAll(lblSQL, inputSQL, btnProcessar);

        // --- SAÍDAS (HU2, HU3, HU4, HU5) ---
        GridPane gridSaidas = new GridPane();
        gridSaidas.setHgap(10);
        gridSaidas.setVgap(10);
        gridSaidas.setPadding(new Insets(15));

        // Configurando áreas de texto para os resultados
        outputAlgebra = criarAreaSaida("Álgebra Relacional");
        outputGrafo = criarAreaSaida("Grafo de Operadores (Otimizado)");
        outputPlano = criarAreaSaida("Plano de Execução");

        gridSaidas.add(new Label("Álgebra Relacional (HU2):"), 0, 0);
        gridSaidas.add(outputAlgebra, 0, 1);
        gridSaidas.add(new Label("Grafo Otimizado (HU3/HU4):"), 1, 0);
        gridSaidas.add(outputGrafo, 1, 1);
        gridSaidas.add(new Label("Ordem de Execução (HU5):"), 0, 2, 2, 1);
        gridSaidas.add(outputPlano, 0, 3, 2, 1);

        // Ajuste de colunas 50/50
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(50);
        gridSaidas.getColumnConstraints().addAll(col, col);

        BorderPane root = new BorderPane();
        root.setTop(painelEntrada);
        root.setCenter(gridSaidas);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TextArea criarAreaSaida(String titulo) {
        TextArea ta = new TextArea();
        ta.setEditable(false);
        ta.setWrapText(true);
        return ta;
    }

    private void acaoBotaoProcessar() {
        String sql = inputSQL.getText().trim();
        try {
            // HU1: Validação
            ValidadorSQL.validar(sql);

            // HU2: Álgebra
            outputAlgebra.setText(ConversorAlgebra.converter(sql));

            // HU4: Grafo Otimizado
            NoGrafo grafoOtimizado = OtimizadorHeuristico.otimizar(sql);
            outputGrafo.setText(grafoOtimizado.imprimir(""));

            // HU5: Plano de Execução (Ordem) com base na SQL atual
            PartesSQL partes = extrairPartesBasicas(sql);
            outputPlano.setText(gerarPlanoExecucao(partes.tabela, partes.condicao, partes.colunas));

        } catch (Exception ex) {
            outputAlgebra.clear();
            outputGrafo.clear();
            outputPlano.clear();
            mostrarAlerta("Erro", ex.getMessage());
        }
    }

    private PartesSQL extrairPartesBasicas(String sql) throws Exception {
        String query = sql.trim().replaceAll("\\s+", " ");
        String queryLower = query.toLowerCase();

        int idxSelect = queryLower.indexOf("select");
        int idxFrom = queryLower.indexOf("from");

        if (idxSelect != 0 || idxFrom < 0) {
            throw new Exception("Erro de Sintaxe: A consulta deve iniciar com SELECT e conter FROM.");
        }

        String colunas = query.substring(idxSelect + 6, idxFrom).trim();
        String resto = query.substring(idxFrom + 4).trim();

        String tabela;
        String condicao = "";

        int idxWhere = resto.toLowerCase().indexOf("where");
        if (idxWhere >= 0) {
            tabela = resto.substring(0, idxWhere).trim();
            condicao = resto.substring(idxWhere + 5).trim();
        } else {
            tabela = resto;
        }

        if (colunas.isEmpty() || tabela.isEmpty()) {
            throw new Exception("Erro de Sintaxe: SELECT/FROM incompletos.");
        }

        return new PartesSQL(colunas, tabela, condicao);
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private String gerarPlanoExecucao(String tabela, String condicao, String colunas) {
        StringBuilder sb = new StringBuilder();
        int passo = 1;

        sb.append(passo++).append(". ACESSAR TABELA: ").append(tabela).append("\n");
        if (!condicao.isEmpty()) {
            sb.append(passo++).append(". FILTRAR LINHAS (SELEÇÃO): ").append(condicao).append("\n");
        }
        sb.append(passo++).append(". FILTRAR COLUNAS (PROJEÇÃO): ").append(colunas).append("\n");
        sb.append(passo).append(". RETORNAR RESULTADO FINAL.");

        return sb.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}