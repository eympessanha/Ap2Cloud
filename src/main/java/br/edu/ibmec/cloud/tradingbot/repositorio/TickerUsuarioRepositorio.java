package br.edu.ibmec.cloud.tradingbot.repositorio;

import br.edu.ibmec.cloud.tradingbot.modelo.TickerUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TickerUsuarioRepositorio extends JpaRepository<TickerUsuario, Integer> {
}