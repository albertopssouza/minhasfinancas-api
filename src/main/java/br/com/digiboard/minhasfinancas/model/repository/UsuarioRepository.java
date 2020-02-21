/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.digiboard.minhasfinancas.model.repository;

import br.com.digiboard.minhasfinancas.model.entity.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author digiboard
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // é o msm que select * from usuario where exists(email)
    boolean existsByEmail(String email);
    
    // é o msm que select * from usuario where email = :email
    Optional<Usuario> findByEmail(String email);
    
}
