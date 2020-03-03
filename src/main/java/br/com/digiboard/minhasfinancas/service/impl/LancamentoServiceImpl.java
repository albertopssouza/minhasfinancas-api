/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.digiboard.minhasfinancas.service.impl;

import br.com.digiboard.minhasfinancas.exception.RegraNegocioException;
import br.com.digiboard.minhasfinancas.model.entity.Lancamento;
import br.com.digiboard.minhasfinancas.model.enums.StatusLancamento;
import br.com.digiboard.minhasfinancas.model.enums.TipoLancamento;
import br.com.digiboard.minhasfinancas.model.repository.LancamentoRepository;
import br.com.digiboard.minhasfinancas.service.LancamentoService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author digiboard
 */
@Service
public class LancamentoServiceImpl implements LancamentoService {
    
    @Autowired
    private LancamentoRepository repository;

    @Override
    @Transactional
    public Lancamento salvar(Lancamento lancamento) {
        validar(lancamento);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        return repository.save(lancamento);
    }

    @Override
    @Transactional
    public Lancamento atualizar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        return repository.save(lancamento);
    }

    @Override
    public void deletar(Long id) {
        Objects.requireNonNull(id);
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lancamento> buscar(Lancamento lancamentoFilter) {
        Example example = Example.of(lancamentoFilter, ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));
        return repository.findAll(example);
    }

    @Override
    public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
        lancamento.setStatus(status);
        atualizar(lancamento);
    }

    @Override
    public void validar(Lancamento lancamento) {
        if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals(""))
            throw new RegraNegocioException("Informe uma descrição válida.");
        
        if (lancamento.getMes() < 1 || lancamento.getMes() > 12)
            throw new RegraNegocioException("Informe um mês válido.");
        
        if (String.valueOf(lancamento.getAno()).length() != 4)
            throw new RegraNegocioException("Informe um ano válido.");
        
        if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null)
            throw new RegraNegocioException("Informe um usuário válido.");
        
        if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1)
            throw new RegraNegocioException("Informe um valor válido.");
        
        if (lancamento.getTipo() == null)
            throw new RegraNegocioException("Informe um tipo de lançamento.");
    }

    @Override
    public Optional<Lancamento> obterPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal obterSaldoPorUsuario(Long id) {
        BigDecimal receitas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.RECEITA);
        BigDecimal despesas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.DESPESA);
        
        if (receitas == null)
            receitas = BigDecimal.ZERO;
        
        if (despesas == null)
            despesas = BigDecimal.ZERO;
        
        return receitas.subtract(despesas);
    }
    
}
