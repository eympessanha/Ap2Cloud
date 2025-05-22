package br.edu.ibmec.cloud.tradingbot.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class RelatorioOrdemUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int identificador;

    @Column
    private String simbolo;

    @Column
    private double quantidade;

    @Column
    private double precoCompra;

    @Column
    private double precoVenda;

    @Column
    private LocalDateTime dataOperacao;
}