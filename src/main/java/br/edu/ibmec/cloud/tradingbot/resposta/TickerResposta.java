package br.edu.ibmec.cloud.tradingbot.resposta;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TickerResposta {
  @JsonProperty("symbol")
  private String simbolo;

  @JsonProperty("price")
  private double ultimoPreco;
}
