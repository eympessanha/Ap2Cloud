package br.edu.ibmec.cloud.tradingbot.servico;

import com.binance.connector.client.SpotClient;
import com.binance.connector.client.impl.SpotClientImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;

@Service
@Data
public class BinanceServico {
    private String baseUrl = "https://testnet.binance.vision";
    private String apiKey;
    private String secretKey;

    public String obterTickers(ArrayList<String> simbolos) {
        SpotClient cliente = new SpotClientImpl(apiKey, secretKey, baseUrl);
        Map<String, Object> parametros = new LinkedHashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            parametros.put("symbols", mapper.writeValueAsString(simbolos));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter símbolos para JSON", e);
        }
        return cliente.createMarket().ticker(parametros);
    }

    public String criarOrdemMercado(String simbolo, double quantidade, String tipo) {
        SpotClient cliente = new SpotClientImpl(apiKey, secretKey, baseUrl);
        Map<String, Object> parametros = new LinkedHashMap<>();
        parametros.put("symbol", simbolo);
        parametros.put("side", tipo);
        parametros.put("type", "MARKET");
        parametros.put("quantity", quantidade);
        return cliente.createTrade().newOrder(parametros);
    }
}