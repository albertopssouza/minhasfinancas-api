/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.digiboard.minhasfinancas.model.repository;

import br.com.digiboard.minhasfinancas.model.entity.Lancamento;
import br.com.digiboard.minhasfinancas.model.enums.TipoLancamento;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author digiboard
 */
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
    
    @Query(value = "select sum(l.valor) from Lancamento l inner join l.usuario u where u.id = :idUsuario and l.tipo = :tipo group by u")
    BigDecimal obterSaldoPorTipoLancamentoEUsuario(@Param("idUsuario") Long idUsuario, @Param("tipo") TipoLancamento tipo);
    
}
