package br.edu.ibmec.cloud.tradingbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

public class MoedasAtivasDTOs {

    // --- Requisição ---
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdicionarMoedaAtivaRequisicao {
        private String simbolo; // Para uma única moeda
        private List<String> simbolos; // Para múltiplas moedas
    }

    // --- Respostas ---
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TradingPairsResposta {
        private boolean success;
        private List<String> trading_pairs;
        private int count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoedaAtivaDetalheResposta {
        private Integer id; // ID do TickerUsuario
        private String simbolo;
        private double ultimo_preco;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListaMoedasAtivasResposta {
        private int total;
        private List<MoedaAtivaDetalheResposta> moedas;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdicionarMoedaIgnorada {
        private String simbolo;
        private String motivo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdicionarMoedasAtivasResposta {
        private String message;
        private List<String> adicionadas;
        private List<AdicionarMoedaIgnorada> ignoradas;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MinQuantityResposta {
        private boolean success;
        private double min_quantity;
    }
}