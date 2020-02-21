/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.digiboard.minhasfinancas.api.dto;

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
public class UsuarioDTO {
    
    private String email;
    private String nome;
    private String senha;
    
}
