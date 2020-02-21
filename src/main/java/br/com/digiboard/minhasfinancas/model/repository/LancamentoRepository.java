/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.digiboard.minhasfinancas.model.repository;

import br.com.digiboard.minhasfinancas.model.entity.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author digiboard
 */
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
    
}
