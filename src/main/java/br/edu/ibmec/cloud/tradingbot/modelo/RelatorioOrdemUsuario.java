package br.edu.ibmec.cloud.tradingbot.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class RelatorioOrdemUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int identificador; // ID local do banco de dados

    @Column
    private String ordemIdBinance; // ID da ordem na Binance (String)

    @Column(nullable = false)
    private String simbolo;

    @Column(nullable = false)
    private double quantidade;

    @Column(nullable = false)
    private double precoCompra;

    @Column
    private double precoVenda; // Pode ser 0 ou null se a ordem ainda não foi vendida

    @Column(nullable = false)
    private LocalDateTime dataOperacao;

    @Column(nullable = false)
    private String status; // Ex: "EM_CARTEIRA", "VENDIDA", "CANCELADA", etc.

    @Column // Novo campo para o tipo de operação (COMPRA/VENDA)
    private String tpOperacao;

    @Column // Novo campo para o tipo de ordem (MERCADO/LIMITE)
    private String tipoOrdem;

    @ManyToOne
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private Usuario usuario;
}