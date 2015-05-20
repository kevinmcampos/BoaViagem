package br.com.cafecomcodigo.boaviagem.data;

import android.provider.BaseColumns;

import java.util.Date;

public class Gasto {

    private Long id;
    private Date data;
    private String categoria;
    private String descricao;
    private Double valor;
    private String local;
    private Integer viagemId;

    public Gasto(){}

    public Gasto(Long id, Date data, String categoria, String descricao,
                 Double valor, String local, Integer viagemId) {
        this.id = id;
        this.data = data;
        this.categoria = categoria;
        this.descricao = descricao;
        this.valor = valor;
        this.local = local;
        this.viagemId = viagemId;
    }

    public static final class GastoEntry implements BaseColumns {
        public static final String TABELA = "gasto";
        public static final String VIAGEM_ID = "viagem_id";
        public static final String CATEGORIA = "categoria";
        public static final String DATA = "data";
        public static final String DESCRICAO = "descricao";
        public static final String VALOR = "valor";
        public static final String LOCAL = "local";
        public static final String[] COLUNAS = new String[]{
            _ID, VIAGEM_ID, CATEGORIA, DATA, DESCRICAO, VALOR, LOCAL
        };
    }
}
