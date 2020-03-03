/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.digiboard.minhasfinancas.service;

import br.com.digiboard.minhasfinancas.model.entity.Usuario;
import java.util.Optional;

/**
 *
 * @author digiboard
 */
public interface UsuarioService {
    
    Usuario autenticar(String email, String senha);
            
    Usuario salvar(Usuario usuario);
    
    void validarEmail(String email);
    
    Optional<Usuario> obterPorId(Long id);
    
}
