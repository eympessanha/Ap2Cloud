package br.edu.ibmec.cloud.tradingbot.requisicao;

import lombok.Data;

@Data
public class OrdemRequisicao {
    private String simbolo;
    private String tipo; // "BUY" ou "SELL"
    private double quantidade;
    private double preco; // Opcional
}