package br.edu.ibmec.cloud.tradingbot.modelo;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
public class TickerUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer identificador;

    @Column
    private String simbolo;
}