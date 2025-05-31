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

    @Column(unique = true, nullable = false)
    private String nomeUsuario; // Corresponde ao usuario_login

    @Column(nullable = false)
    private String senha; // Corresponde ao usuario_senha

    @Column
    private String chaveApiBinance;

    @Column
    private String chaveSecretaBinance;

    // Saldo inicial pode ser considerado um "saldo em fiat" ou um ponto de partida.
    // O saldo real em cripto precisaria ser obtido da Binance.
    @Column
    private Double saldoInicial;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "configuracao_id", referencedColumnName = "identificador")
    private ConfiguracaoUsuario configuracao; // Agora é uma relação OneToOne

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id", referencedColumnName = "identificador")
    private List<TickerUsuario> tickers;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id", referencedColumnName = "identificador")
    private List<RelatorioOrdemUsuario> relatoriosOrdens;
}