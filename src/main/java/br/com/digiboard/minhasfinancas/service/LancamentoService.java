/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.digiboard.minhasfinancas.service;

import br.com.digiboard.minhasfinancas.model.entity.Lancamento;
import br.com.digiboard.minhasfinancas.model.enums.StatusLancamento;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author digiboard
 */
public interface LancamentoService {
    
    Lancamento salvar(Lancamento lancamento);
    
    Lancamento atualizar(Lancamento lancamento);
    
    void deletar(Long id);
    
    List<Lancamento> buscar(Lancamento lancamentoFilter);
    
    void atualizarStatus(Lancamento lancamento, StatusLancamento status);
    
    void validar(Lancamento lancamento);
    
    Optional<Lancamento> obterPorId(Long id);
    
    BigDecimal obterSaldoPorUsuario(Long id);
    
}
