package br.edu.ibmec.cloud.tradingbot.controller;

import br.edu.ibmec.cloud.tradingbot.modelo.Usuario;
import br.edu.ibmec.cloud.tradingbot.modelo.RelatorioOrdemUsuario;
import br.edu.ibmec.cloud.tradingbot.repositorio.UsuarioRepositorio;
import br.edu.ibmec.cloud.tradingbot.repositorio.RelatorioOrdemUsuarioRepositorio;
import br.edu.ibmec.cloud.tradingbot.requisicao.OrdemRequisicao;
import br.edu.ibmec.cloud.tradingbot.resposta.OrdemResposta;
import br.edu.ibmec.cloud.tradingbot.servico.BinanceServico;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("{identificador}/ordem")
public class OrdemController {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private RelatorioOrdemUsuarioRepositorio relatorioOrdemUsuarioRepositorio;

    @Autowired
    private BinanceServico binanceServico;

    @PostMapping
    public ResponseEntity<OrdemResposta> enviarOrdem(@PathVariable("identificador") int usuarioId, @RequestBody OrdemRequisicao requisicao) {
        Optional<Usuario> usuarioOpt = usuarioRepositorio.findById(usuarioId);

        if (usuarioOpt.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Usuario usuario = usuarioOpt.get();

        binanceServico.setApiKey(usuario.getChaveApiBinance());
        binanceServico.setSecretKey(usuario.getChaveSecretaBinance());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            String resultado = binanceServico.criarOrdemMercado(
                    requisicao.getSimbolo(),
                    requisicao.getQuantidade(),
                    requisicao.getTipo()
            );
            OrdemResposta resposta = mapper.readValue(resultado, OrdemResposta.class);

            if ("BUY".equals(requisicao.getTipo())) {
                RelatorioOrdemUsuario relatorio = new RelatorioOrdemUsuario();
                relatorio.setSimbolo(requisicao.getSimbolo());
                relatorio.setQuantidade(requisicao.getQuantidade());
                relatorio.setPrecoCompra(resposta.getFills().get(0).getPreco());
                relatorio.setDataOperacao(LocalDateTime.now());

                relatorioOrdemUsuarioRepositorio.save(relatorio);

                usuario.getRelatoriosOrdens().add(relatorio);
                usuarioRepositorio.save(usuario);
            }

            if ("SELL".equals(requisicao.getTipo())) {
                RelatorioOrdemUsuario ordemCompra = null;
                for (RelatorioOrdemUsuario item : usuario.getRelatoriosOrdens()) {
                    if (item.getSimbolo().equals(requisicao.getSimbolo()) && item.getPrecoVenda() == 0) {
                        ordemCompra = item;
                        break;
                    }
                }
                if (ordemCompra != null) {
                    ordemCompra.setPrecoVenda(resposta.getFills().get(0).getPreco());
                    relatorioOrdemUsuarioRepositorio.save(ordemCompra);
                }
            }

            return new ResponseEntity<>(resposta, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}