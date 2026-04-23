package com.grupo8.processadorconsultas.model;

import java.util.List;
import java.util.Map;

import java.util.*;

public class Catalogo {
    // Mapa onde a chave é o nome da tabela e o valor é a lista de colunas
    private static final Map<String, List<String>> dicionario = new HashMap<>();

    static {
        // Dados extraídos diretamente do seu PDF (página 4)
        dicionario.put("categoria", Arrays.asList("idcategoria", "descricao"));
        dicionario.put("produto", Arrays.asList("idproduto", "nome", "descricao", "preco", "quantestoque", "categoria_idcategoria"));
        dicionario.put("tipocliente", Arrays.asList("idtipocliente", "descricao"));
        dicionario.put("cliente", Arrays.asList("idcliente", "nome", "email", "nascimento", "senha", "tipocliente_idtipocliente", "dataregistro"));
        dicionario.put("tipoendereco", Arrays.asList("idtipoendereco", "descricao"));
        dicionario.put("endereco", Arrays.asList("idendereco", "enderecopadrao", "logradouro", "numero", "complemento", "bairro", "cidade", "uf", "cep", "tipoendereco_idtipoendereco", "cliente_idcliente"));
        dicionario.put("telefone", Arrays.asList("numero", "cliente_idcliente"));
        dicionario.put("status", Arrays.asList("idstatus", "descricao"));
        dicionario.put("pedido", Arrays.asList("idpedido", "status_idstatus", "datapedido", "valortotalpedido", "cliente_idcliente"));
        dicionario.put("pedido_has_produto", Arrays.asList("idpedidoproduto", "precounitario", "pedido_idpedido", "produto_idproduto", "quantidade"));
    }

    public static boolean tabelaExiste(String tabela) {
        return dicionario.containsKey(tabela.toLowerCase());
    }

    public static boolean colunaExiste(String tabela, String coluna) {
        if (!tabelaExiste(tabela)) return false;
        return dicionario.get(tabela.toLowerCase()).contains(coluna.toLowerCase());
    }
}