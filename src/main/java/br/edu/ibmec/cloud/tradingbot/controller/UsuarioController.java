package br.edu.ibmec.cloud.tradingbot.controller;

import br.edu.ibmec.cloud.tradingbot.modelo.Usuario;
import br.edu.ibmec.cloud.tradingbot.modelo.ConfiguracaoUsuario;
import br.edu.ibmec.cloud.tradingbot.modelo.TickerUsuario;
import br.edu.ibmec.cloud.tradingbot.repositorio.ConfiguracaoUsuarioRepositorio;
import br.edu.ibmec.cloud.tradingbot.repositorio.UsuarioRepositorio;
import br.edu.ibmec.cloud.tradingbot.repositorio.TickerUsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ConfiguracaoUsuarioRepositorio configuracaoUsuarioRepositorio;

    @Autowired
    private TickerUsuarioRepositorio tickerUsuarioRepositorio;

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        usuarioRepositorio.save(usuario);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @GetMapping("{identificador}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable("identificador") Integer identificador) {
        return usuarioRepositorio.findById(identificador)
                .map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("{identificador}/configuracao")
    public ResponseEntity<Usuario> associarConfiguracao(@PathVariable("identificador") Integer identificador, @RequestBody ConfiguracaoUsuario configuracao) {
        Optional<Usuario> usuarioOpt = usuarioRepositorio.findById(identificador);

        if (usuarioOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        configuracaoUsuarioRepositorio.save(configuracao);

        Usuario usuario = usuarioOpt.get();
        usuario.getConfiguracoes().add(configuracao);
        usuarioRepositorio.save(usuario);

        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @PostMapping("{identificador}/ticker")
    public ResponseEntity<Usuario> associarTicker(@PathVariable("identificador") Integer identificador, @RequestBody TickerUsuario ticker) {
        Optional<Usuario> usuarioOpt = usuarioRepositorio.findById(identificador);

        if (usuarioOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        tickerUsuarioRepositorio.save(ticker);

        Usuario usuario = usuarioOpt.get();
        usuario.getTickers().add(ticker);
        usuarioRepositorio.save(usuario);

        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }
}