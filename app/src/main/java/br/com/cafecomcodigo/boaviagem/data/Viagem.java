package br.com.cafecomcodigo.boaviagem.data;

import android.provider.BaseColumns;

import java.util.Date;

public class Viagem {

    private Long id;
    private String destino;
    private Integer tipoViagem;
    private Date dataChegada;
    private Date dataSaida;
    private Double orcamento;
    private Integer quantidadePessoas;

    public Viagem(){}

    public Viagem(Long id, String destino, Integer tipoViagem,
                  Date dataChegada, Date dataSaida, Double orcamento,
                  Integer quantidadePessoas) {
        this.id = id;
        this.destino = destino;
        this.tipoViagem = tipoViagem;
        this.dataChegada = dataChegada;
        this.dataSaida = dataSaida;
        this.orcamento = orcamento;
        this.quantidadePessoas = quantidadePessoas;
    }

    public static final class ViagemEntry implements BaseColumns {
        public static final String TABELA = "viagem";
        public static final String DESTINO = "destino";
        public static final String DATA_CHEGADA = "data_chegada";
        public static final String DATA_SAIDA = "data_saida";
        public static final String ORCAMENTO = "orcamento";
        public static final String QUANTIDADE_PESSOAS =
                "quantidade_pessoas";
        public static final String TIPO_VIAGEM = "tipo_viagem";
        public static final String[] COLUNAS = new String[]{
                _ID, DESTINO, DATA_CHEGADA, DATA_SAIDA,
                TIPO_VIAGEM, ORCAMENTO, QUANTIDADE_PESSOAS };
    }
}
