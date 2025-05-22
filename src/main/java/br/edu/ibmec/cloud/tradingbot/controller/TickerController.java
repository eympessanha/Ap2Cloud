package br.edu.ibmec.cloud.tradingbot.controller;

import br.edu.ibmec.cloud.tradingbot.modelo.Usuario;
import br.edu.ibmec.cloud.tradingbot.modelo.TickerUsuario;
import br.edu.ibmec.cloud.tradingbot.repositorio.UsuarioRepositorio;
import br.edu.ibmec.cloud.tradingbot.resposta.TickerResposta;
import br.edu.ibmec.cloud.tradingbot.servico.BinanceServico;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("{identificador}/tickers")
public class TickerController {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private BinanceServico binanceServico;

    @GetMapping
    public ResponseEntity<List<TickerResposta>> obterTickers(@PathVariable("identificador") int identificador) {
    Optional<Usuario> usuarioOpt = usuarioRepositorio.findById(identificador);

    if (usuarioOpt.isEmpty())
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    Usuario usuario = usuarioOpt.get();

    ArrayList<String> tickers = new ArrayList<>();
    for (TickerUsuario item : usuario.getTickers()) {
        tickers.add(item.getSimbolo());
    }

    if (tickers.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    binanceServico.setApiKey(usuario.getChaveApiBinance());
    binanceServico.setSecretKey(usuario.getChaveSecretaBinance());

    String resultado = binanceServico.obterTickers(tickers);

    System.out.println("Tickers enviados: " + tickers);
    System.out.println("Resultado da Binance: " + resultado);

    if (resultado.trim().startsWith("{")) {
        // Erro da Binance
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
        List<TickerResposta> resposta = objectMapper.readValue(resultado,
                new TypeReference<List<TickerResposta>>() {});
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
}