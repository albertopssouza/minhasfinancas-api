/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.digiboard.minhasfinancas.model.repository;

import br.com.digiboard.minhasfinancas.model.entity.Usuario;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author digiboard
 */
//@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {
    
    @Autowired
    UsuarioRepository repository;
    
    @Autowired
    TestEntityManager entityManager;
    
    @Test
    public void deveVerificarAExistenciaDeUmEmail() {
        // cenário
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);
        // ação / execução
        boolean result = repository.existsByEmail("usuario@email.com");
        
        // verificação
        Assertions.assertThat(result).isTrue();
    }
    
    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
        // ação
        boolean result = repository.existsByEmail("usuario@email.com");
        
        // verificação
        Assertions.assertThat(result).isFalse();
    }
    
    @Test
    public void devePersistirUmUsuarioNaBaseDeDados() {
        // cenário
        Usuario usuario = criarUsuario();
        
        // ação
        Usuario usuarioSalvo = repository.save(usuario);
        
        Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
    }
    
    @Test
    public void deveBuscarUsuarioPorEmail() {
        // cenário
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);
        
        // verificação
        Optional<Usuario> result = repository.findByEmail("usuario@email.com");
        
        Assertions.assertThat(result.isPresent()).isTrue();
    }
    
    public static Usuario criarUsuario() {
        return Usuario
                .builder()
                .nome("usuario")
                .email("usuario@email.com")
                .build();
    }
    
    @Test
    public void deveRetornarVazioAoBuscarPorEmailQuandoNaoExisteNaBase() {
        
        // verificação
        Optional<Usuario> result = repository.findByEmail("usuario@email.com");
        
        Assertions.assertThat(result.isPresent()).isFalse();
    }
}
