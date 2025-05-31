package br.edu.ibmec.cloud.tradingbot.controller;

import br.edu.ibmec.cloud.tradingbot.dto.OrdemDTOs.*;
import br.edu.ibmec.cloud.tradingbot.servico.OrdemServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ordem") // Endpoint raiz da API de ordem
public class OrdemController {

    @Autowired
    private OrdemServico ordemServico;

    // POST /ordem/{usuario_id} - Criar Ordem
    @PostMapping("/{usuario_id}")
    public ResponseEntity<OrdemCriadaResposta> criarOrdem(@PathVariable("usuario_id") Integer usuarioId, @RequestBody OrdemRequisicao requisicao) {
        OrdemCriadaResposta resposta = ordemServico.criarOrdem(usuarioId, requisicao);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    // GET /ordem/{usuario_id} - Listar Ordens
    @GetMapping("/{usuario_id}")
    public ResponseEntity<ListaOrdensResposta> listarOrdens(@PathVariable("usuario_id") Integer usuarioId) {
        ListaOrdensResposta resposta = ordemServico.listarOrdens(usuarioId);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    // GET /ordem/{usuario_id}/{ordem_id} - Obter Ordem Específica
    @GetMapping("/{usuario_id}/{ordem_id}")
    public ResponseEntity<OrdemDetalheResposta> obterOrdemEspecifica(@PathVariable("usuario_id") Integer usuarioId, @PathVariable("ordem_id") String ordemId) {
        OrdemDetalheResposta resposta = ordemServico.obterOrdemEspecifica(usuarioId, ordemId);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    // GET /ordem/relatorios/{usuario_id}/abertos - Listar Ordens Abertas
    @GetMapping("/{usuario_id}/abertos")
    public ResponseEntity<ListaOrdensAbertasResposta> listarOrdensAbertas(@PathVariable("usuario_id") Integer usuarioId) {
        ListaOrdensAbertasResposta resposta = ordemServico.listarOrdensAbertas(usuarioId);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }
}