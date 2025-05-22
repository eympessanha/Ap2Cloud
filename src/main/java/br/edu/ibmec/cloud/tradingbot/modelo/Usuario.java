package br.edu.ibmec.cloud.tradingbot.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer identificador;

    @Column
    private String nomeUsuario;

    @Column
    private String senha;

    @Column
    private String chaveApiBinance;

    @Column
    private String chaveSecretaBinance;

    @Column
    private Double saldoInicial;

    @OneToMany
    @JoinColumn(name = "usuario_id", referencedColumnName = "identificador")
    private List<ConfiguracaoUsuario> configuracoes;

    @OneToMany
    @JoinColumn(name = "usuario_id", referencedColumnName = "identificador")
    private List<TickerUsuario> tickers;

    @OneToMany
    @JoinColumn(name = "usuario_id", referencedColumnName = "identificador")
    private List<RelatorioOrdemUsuario> relatoriosOrdens;
}