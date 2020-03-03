/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.digiboard.minhasfinancas.api.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author digiboard
 */
@Getter
@Setter
@NoArgsConstructor
public class LancamentoDTO {
    
    private Long id;
    private String descricao;
    private int mes;
    private int ano;
    private BigDecimal valor;
    private Long usuario;
    private String tipo;
    private String status;
    
}
