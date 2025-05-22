package br.edu.ibmec.cloud.tradingbot.resposta;

import lombok.Data;
import java.util.List;

@Data
public class OrdemResposta {
    private String simbolo;
    private String ordemId;
    private String status;
    private String tipo;
    private String lado;
    private double quantidade;
    private List<PreenchimentoResposta> fills;
}