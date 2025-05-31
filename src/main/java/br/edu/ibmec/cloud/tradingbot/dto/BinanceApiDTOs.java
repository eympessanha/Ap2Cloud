package br.edu.ibmec.cloud.tradingbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

public class BinanceApiDTOs {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreenchimentoResposta {
        private double preco;
        private double quantidade;
    }

    // Esta classe é a resposta direta da API da Binance para uma ordem
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BinanceOrdemResposta {
        @JsonProperty("symbol")
        private String simbolo;
        @JsonProperty("orderId")
        private String ordemId; // ID da ordem na Binance
        @JsonProperty("status")
        private String status;
        @JsonProperty("type")
        private String tipo; // Tipo de ordem (MARKET, LIMIT)
        @JsonProperty("side")
        private String lado; // Lado da operação (BUY, SELL)
        @JsonProperty("executedQty")
        private double quantidadeExecutada; // Quantidade executada
        @JsonProperty("cummulativeQuoteQty")
        private double precoMedioExecucao; // Preço médio ponderado ou valor total de cotação
        @JsonProperty("fills")
        private List<PreenchimentoResposta> fills;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TickerResposta {
        @JsonProperty("symbol")
        private String simbolo;

        @JsonProperty("price")
        private double ultimoPreco;
    }
}