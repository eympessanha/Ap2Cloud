package br.edu.ibmec.cloud.tradingbot.controller;

import br.edu.ibmec.cloud.tradingbot.dto.MoedasAtivasDTOs.*;
import br.edu.ibmec.cloud.tradingbot.resposta.MensagemResposta;
import br.edu.ibmec.cloud.tradingbot.servico.MoedasAtivasServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/moedas") // Endpoint raiz da API de moedas ativas
public class MoedasAtivasController {

    @Autowired
    private MoedasAtivasServico moedasAtivasServico;

    // GET /moedas_ativas/trading-pairs - Listar Pares de Trading Disponíveis
    @GetMapping("/pares")
    public ResponseEntity<TradingPairsResposta> listarParesDeTradingDisponiveis() {
        TradingPairsResposta resposta = moedasAtivasServico.listarParesDeTradingDisponiveis();
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    // GET /moedas_ativas/{usuario_id} - Listar Moedas Ativas do Usuário
    @GetMapping("/{usuario_id}")
    public ResponseEntity<ListaMoedasAtivasResposta> listarMoedasAtivasDoUsuario(@PathVariable("usuario_id") Integer usuarioId) {
        ListaMoedasAtivasResposta resposta = moedasAtivasServico.listarMoedasAtivasDoUsuario(usuarioId);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    // POST /moedas_ativas/{usuario_id} - Adicionar Moeda Ativa
    @PostMapping("/{usuario_id}")
    public ResponseEntity<AdicionarMoedasAtivasResposta> adicionarMoedaAtiva(@PathVariable("usuario_id") Integer usuarioId, @RequestBody AdicionarMoedaAtivaRequisicao requisicao) {
        AdicionarMoedasAtivasResposta resposta = moedasAtivasServico.adicionarMoedaAtiva(usuarioId, requisicao);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    // DELETE /moedas_ativas/{usuario_id}/{moeda_id} - Remover Moeda Ativa por ID
    @DeleteMapping("/{usuario_id}/{moeda_id}")
    public ResponseEntity<MensagemResposta> removerMoedaAtivaPorId(@PathVariable("usuario_id") Integer usuarioId, @PathVariable("moeda_id") Integer moedaId) {
        MensagemResposta resposta = moedasAtivasServico.removerMoedaAtivaPorId(usuarioId, moedaId);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    // DELETE /moedas_ativas/{usuario_id}/simbolo/{simbolo} - Remover Moeda Ativa por Símbolo
    @DeleteMapping("/{usuario_id}/simbolo/{simbolo}")
    public ResponseEntity<MensagemResposta> removerMoedaAtivaPorSimbolo(@PathVariable("usuario_id") Integer usuarioId, @PathVariable("simbolo") String simbolo) {
        MensagemResposta resposta = moedasAtivasServico.removerMoedaAtivaPorSimbolo(usuarioId, simbolo);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    // GET /moedas_ativas/binance/min_quantity/{simbolo} - Obter Quantidade Mínima para Trading
    @GetMapping("/binance/min_quantity/{simbolo}")
    public ResponseEntity<MinQuantityResposta> obterQuantidadeMinimaParaTrading(@PathVariable("simbolo") String simbolo) {
        MinQuantityResposta resposta = moedasAtivasServico.obterQuantidadeMinimaParaTrading(simbolo);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }
}