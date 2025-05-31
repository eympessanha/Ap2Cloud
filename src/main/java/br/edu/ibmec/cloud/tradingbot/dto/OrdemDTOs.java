package br.edu.ibmec.cloud.tradingbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

// IMPORT A SER ADICIONADO
import br.edu.ibmec.cloud.tradingbot.dto.BinanceApiDTOs.PreenchimentoResposta; // <<== ADICIONE ESTA LINHA

public class OrdemDTOs {

    // --- Requisição ---
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrdemRequisicao {
        private String simbolo;
        private String tp_operacao; // "COMPRA" ou "VENDA"
        private double quantidade;
        private String tipo; // "MERCADO" ou "LIMITE"
        private Double preco; // Opcional, usado para ordens tipo "LIMITE"
    }

    // --- Respostas ---
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrdemCriadaResposta {
        private String message;
        private String ordem_id; // ID da ordem na Binance
        private String simbolo;
        private String tipo; // Tipo de ordem (MERCADO, LIMITE)
        private String tp_operacao; // Tipo de operação (COMPRA, VENDA)
        private double quantidade;
        private double preco; // Preço médio de execução
        private String status; // Status da ordem (EXECUTADA, PENDENTE)
        private List<PreenchimentoResposta> fills; // Agora o tipo será reconhecido
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrdemDetalheResposta {
        private String simbolo;
        private String ordem_id; // ID da ordem na Binance
        private double qtd_executada;
        private String tipo; // Tipo de ordem (MERCADO, LIMITE)
        private String tp_operacao; // Tipo de operação (COMPRA, VENDA)
        private double preco; // Preço médio de execução
        private String status; // Status da ordem
        private List<PreenchimentoResposta> fills; // Agora o tipo será reconhecido
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListaOrdensResposta {
        private int total;
        private List<OrdemDetalheResposta> ordens;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrdemAbertaDetalheResposta {
        private Integer id; // ID do RelatorioOrdemUsuario
        private String moeda;
        private double quantidade;
        private double preco_compra;
        private LocalDateTime data_operacao;
        private String status; // "EM CARTEIRA"
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListaOrdensAbertasResposta {
        private int total;
        private List<OrdemAbertaDetalheResposta> ordens_abertas;
    }
}