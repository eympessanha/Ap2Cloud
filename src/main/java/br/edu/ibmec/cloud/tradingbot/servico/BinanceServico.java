package br.edu.ibmec.cloud.tradingbot.servico;

import com.binance.connector.client.SpotClient;
import com.binance.connector.client.impl.SpotClientImpl;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import br.edu.ibmec.cloud.tradingbot.dto.BinanceApiDTOs.BinanceOrdemResposta;
import br.edu.ibmec.cloud.tradingbot.dto.BinanceApiDTOs.TickerResposta;

@Service
@Data
public class BinanceServico {
    private String baseUrl = "https://testnet.binance.vision";
    private String apiKey;
    private String secretKey;

    private final ObjectMapper objectMapper;

    public BinanceServico() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<TickerResposta> obterTickers(List<String> simbolos) {
        SpotClient client = new SpotClientImpl(apiKey, secretKey, baseUrl);
        Map<String, Object> parameters = new LinkedHashMap<>();

        // CORREÇÃO AQUI: Passe a lista diretamente, não como String JSON
        parameters.put("symbols", simbolos);

        String result = client.createMarket().ticker24H(parameters);

        try {
            return objectMapper.readValue(result, objectMapper.getTypeFactory().constructCollectionType(List.class, TickerResposta.class));
        } catch (Exception e) {
            System.err.println("Erro ao obter tickers da Binance: " + result);
            throw new RuntimeException("Erro ao processar resposta da Binance para tickers: " + e.getMessage() + ". Resposta recebida: " + result, e);
        }
    }

    public BinanceOrdemResposta criarOrdemMercado(String simbolo, double quantidade, String tipoOperacaoBinance) {
        SpotClient client = new SpotClientImpl(apiKey, secretKey, baseUrl);
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", simbolo);
        parameters.put("side", tipoOperacaoBinance); // "BUY" ou "SELL"
        parameters.put("type", "MARKET");
        parameters.put("quantity", quantidade);
        String result = client.createTrade().newOrder(parameters);
        try {
            return objectMapper.readValue(result, BinanceOrdemResposta.class);
        } catch (Exception e) {
            System.err.println("Erro ao criar ordem na Binance: " + result);
            throw new RuntimeException("Erro ao processar resposta da Binance para ordem", e);
        }
    }

    public List<String> obterParesDeTradingDisponiveis() {
        SpotClient publicClient = new SpotClientImpl(baseUrl);
        String exchangeInfo = publicClient.createMarket().exchangeInfo(new LinkedHashMap<>());
        try {
            JsonNode root = objectMapper.readTree(exchangeInfo);
            List<String> symbols = new ArrayList<>();
            if (root.has("symbols") && root.get("symbols").isArray()) {
                for (JsonNode symbolNode : root.get("symbols")) {
                    if (symbolNode.has("symbol")) {
                        symbols.add(symbolNode.get("symbol").asText());
                    }
                }
            }
            return symbols;
        } catch (Exception e) {
            System.err.println("Erro ao obter pares de trading da Binance. Resposta: " + exchangeInfo);
            throw new RuntimeException("Erro ao processar resposta da Binance para pares de trading: " + e.getMessage(), e);
        }
    }

    public double obterQuantidadeMinima(String simbolo) {
        SpotClient publicClient = new SpotClientImpl(baseUrl);
        String exchangeInfo = publicClient.createMarket().exchangeInfo(new LinkedHashMap<>());
        try {
            JsonNode root = objectMapper.readTree(exchangeInfo);
            if (root.has("symbols") && root.get("symbols").isArray()) {
                for (JsonNode symbolNode : root.get("symbols")) {
                    if (symbolNode.has("symbol") && symbolNode.get("symbol").asText().equals(simbolo)) {
                        if (symbolNode.has("filters") && symbolNode.get("filters").isArray()) {
                            for (JsonNode filterNode : symbolNode.get("filters")) {
                                if (filterNode.has("filterType") && filterNode.get("filterType").asText().equals("LOT_SIZE")) {
                                    if (filterNode.has("minQty")) {
                                        return filterNode.get("minQty").asDouble();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            throw new RuntimeException("Quantidade mínima para o símbolo " + simbolo + " não encontrada ou formato de resposta inesperado.");
        } catch (Exception e) {
            System.err.println("Erro ao obter quantidade mínima da Binance para " + simbolo + ". Resposta: " + exchangeInfo);
            throw new RuntimeException("Erro ao processar resposta da Binance para quantidade mínima: " + e.getMessage(), e);
        }
    }
}