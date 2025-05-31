package br.edu.ibmec.cloud.tradingbot.servico;

import br.edu.ibmec.cloud.tradingbot.dto.UsuarioDTOs.*;
import br.edu.ibmec.cloud.tradingbot.modelo.ConfiguracaoUsuario;
import br.edu.ibmec.cloud.tradingbot.modelo.Usuario;
import br.edu.ibmec.cloud.tradingbot.repositorio.ConfiguracaoUsuarioRepositorio;
import br.edu.ibmec.cloud.tradingbot.repositorio.UsuarioRepositorio;
import br.edu.ibmec.cloud.tradingbot.resposta.MensagemResposta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UsuarioServico {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ConfiguracaoUsuarioRepositorio configuracaoUsuarioRepositorio;

    public UsuarioCriadoResposta criarUsuario(CriarUsuarioRequisicao requisicao) {
        if (usuarioRepositorio.findByNomeUsuario(requisicao.getUsuario_login()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe");
        }

        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(requisicao.getUsuario_login());
        usuario.setSenha(requisicao.getUsuario_senha()); // Idealmente, aqui a senha seria hash
        usuario.setChaveApiBinance(requisicao.getUsuario_binanceApiKey());
        usuario.setChaveSecretaBinance(requisicao.getUsuario_binanceSecretKey());
        usuario.setSaldoInicial(0.0); // Saldo inicial pode ser 0 ou definido

        ConfiguracaoUsuario config = new ConfiguracaoUsuario();
        config.setPercentualLucro(requisicao.getPct_ganho()); // <<== CORREÇÃO AQUI: era setPercentualGanho
        config.setPercentualPerda(requisicao.getPct_perda());
        config.setQuantidadePorOrdem(requisicao.getValor_compra());

        usuario.setConfiguracao(config); // Associa a configuração ao usuário
        usuarioRepositorio.save(usuario); // Persiste usuário e sua configuração devido a CascadeType.ALL

        return new UsuarioCriadoResposta("Usuário criado com sucesso", usuario.getIdentificador());
    }

    public LoginResposta fazerLogin(LoginRequisicao requisicao) {
        Usuario usuario = usuarioRepositorio.findByNomeUsuario(requisicao.getUsuario_login());
        if (usuario == null || !usuario.getSenha().equals(requisicao.getUsuario_senha())) { // Comparação de senha simples, idealmente usar BCrypt
            return new LoginResposta(false, null, "Credenciais inválidas");
        }
        return new LoginResposta(true, usuario.getIdentificador(), "Login efetuado com sucesso");
    }

    public UsuarioDetalhesResposta obterUsuario(Integer usuarioId) {
        return usuarioRepositorio.findById(usuarioId)
                .map(usuario -> {
                    boolean hasKeys = usuario.getChaveApiBinance() != null && !usuario.getChaveApiBinance().isEmpty() &&
                                    usuario.getChaveSecretaBinance() != null && !usuario.getChaveSecretaBinance().isEmpty();
                    // O saldo real dependeria de uma integração mais profunda com a carteira Binance.
                    // Por simplicidade, usaremos o saldo inicial ou um valor mock.
                    Double saldoAtual = usuario.getSaldoInicial(); // Ou obter da Binance se implementar

                    return new UsuarioDetalhesResposta(usuario.getIdentificador(), usuario.getNomeUsuario(), saldoAtual, hasKeys);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public MensagemResposta excluirUsuario(Integer usuarioId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        usuarioRepositorio.delete(usuario); // CascadeType.ALL em Usuario deve remover as entidades filhas

        return new MensagemResposta("Usuário excluído com sucesso");
    }

    public ConfiguracaoUsuarioResposta obterConfiguracoes(Integer usuarioId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (usuario.getConfiguracao() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Configurações não encontradas para este usuário");
        }

        ConfiguracaoUsuario config = usuario.getConfiguracao();
        return new ConfiguracaoUsuarioResposta(config.getQuantidadePorOrdem(), config.getPercentualLucro(), config.getPercentualPerda());
    }

    public MensagemResposta atualizarConfiguracoes(Integer usuarioId, ConfiguracaoUsuarioRequisicao requisicao) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        ConfiguracaoUsuario config = usuario.getConfiguracao();
        if (config == null) {
            // Se não houver configuração existente, crie uma nova
            config = new ConfiguracaoUsuario();
            usuario.setConfiguracao(config);
        }

        config.setQuantidadePorOrdem(requisicao.getValor_compra());
        config.setPercentualLucro(requisicao.getPct_ganho()); // <<== CORREÇÃO AQUI TAMBÉM: era setPercentualGanho
        config.setPercentualPerda(requisicao.getPct_perda());

        configuracaoUsuarioRepositorio.save(config); // Salva a configuração atualizada ou nova
        usuarioRepositorio.save(usuario); // Garante que a associação seja salva, se for nova

        return new MensagemResposta("Configuração atualizada com sucesso");
    }

    // Adicione um método findByNomeUsuario no UsuarioRepositorio
    // E no modelo Usuario, adicione um construtor @AllArgsConstructor e @NoArgsConstructor, se não tiver

}