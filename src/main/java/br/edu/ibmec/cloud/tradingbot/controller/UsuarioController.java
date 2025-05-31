package br.edu.ibmec.cloud.tradingbot.controller;

import br.edu.ibmec.cloud.tradingbot.dto.UsuarioDTOs.*;
import br.edu.ibmec.cloud.tradingbot.resposta.MensagemResposta;
import br.edu.ibmec.cloud.tradingbot.servico.UsuarioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario") // Endpoint raiz da API de usuário
public class UsuarioController {

    @Autowired
    private UsuarioServico usuarioServico;

    // POST /usuario - Criar Usuário
    @PostMapping
    public ResponseEntity<UsuarioCriadoResposta> criarUsuario(@RequestBody CriarUsuarioRequisicao requisicao) {
        UsuarioCriadoResposta resposta = usuarioServico.criarUsuario(requisicao);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    // POST /usuario/login - Login de Usuário
    @PostMapping("/login")
    public ResponseEntity<LoginResposta> loginUsuario(@RequestBody LoginRequisicao requisicao) {
        LoginResposta resposta = usuarioServico.fazerLogin(requisicao);
        if (resposta.isSuccess()) {
            return new ResponseEntity<>(resposta, HttpStatus.OK);
        }
        return new ResponseEntity<>(resposta, HttpStatus.UNAUTHORIZED); // Ou HttpStatus.BAD_REQUEST, dependendo da granularidade
    }

    // GET /usuario/{usuario_id} - Obter Usuário
    @GetMapping("/{usuario_id}")
    public ResponseEntity<UsuarioDetalhesResposta> obterUsuario(@PathVariable("usuario_id") Integer usuarioId) {
        UsuarioDetalhesResposta usuario = usuarioServico.obterUsuario(usuarioId);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    // DELETE /usuario/{usuario_id} - Excluir Usuário
    @DeleteMapping("/{usuario_id}")
    public ResponseEntity<MensagemResposta> excluirUsuario(@PathVariable("usuario_id") Integer usuarioId) {
        MensagemResposta resposta = usuarioServico.excluirUsuario(usuarioId);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    // GET /usuario/{usuario_id}/config - Obter Configurações do Usuário
    @GetMapping("/{usuario_id}/config")
    public ResponseEntity<ConfiguracaoUsuarioResposta> obterConfiguracoesUsuario(@PathVariable("usuario_id") Integer usuarioId) {
        ConfiguracaoUsuarioResposta configuracao = usuarioServico.obterConfiguracoes(usuarioId);
        return new ResponseEntity<>(configuracao, HttpStatus.OK);
    }

    // PUT /usuario/{usuario_id}/config - Atualizar Configurações do Usuário
    @PutMapping("/{usuario_id}/config")
    public ResponseEntity<MensagemResposta> atualizarConfiguracoesUsuario(@PathVariable("usuario_id") Integer usuarioId, @RequestBody ConfiguracaoUsuarioRequisicao requisicao) {
        MensagemResposta resposta = usuarioServico.atualizarConfiguracoes(usuarioId, requisicao);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }
}