/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.digiboard.minhasfinancas.service;

import br.com.digiboard.minhasfinancas.exception.ErroAutenticacaoException;
import br.com.digiboard.minhasfinancas.exception.RegraNegocioException;
import br.com.digiboard.minhasfinancas.model.entity.Usuario;
import br.com.digiboard.minhasfinancas.model.repository.UsuarioRepository;
import br.com.digiboard.minhasfinancas.service.impl.UsuarioServiceImpl;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author digiboard
 */
//@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
    
    @SpyBean
    UsuarioServiceImpl service;
    @MockBean
    UsuarioRepository repository;
    
    @Test(expected = Test.None.class)
    public void deveSalvarUsuario() {
        // cenário
        Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
        Usuario usuario = new Usuario();
        usuario.setId(1l);
        usuario.setNome("nome");
        usuario.setEmail("email@email.com");
        usuario.setSenha("senha");
        
        Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
        
        // ação
        Usuario usuarioSalvo = service.salvar(new Usuario());
        
        // verificação
        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
        Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
        Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
        Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
    }
    
    @Test(expected = RegraNegocioException.class)
    public void naoDeveSalvarUmUsuarioJaCadastrado() {
        // cenário
        String email = "email@email.com";
        Usuario usuario = Usuario.builder().email(email).build();
        Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
        
        // ação
        service.salvar(usuario);
        
        // verificação
        Mockito.verify(repository, Mockito.never()).save(usuario);
    }
    
    @Test(expected = Test.None.class)
    public void deveAutenticarUmUsuarioComSucesso() {
        // cenário
        String email = "email@email.com";
        String senha = "senha";
        
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
        
        // ação
        Usuario result = service.autenticar(email, senha);
        
        Assertions.assertThat(result).isNotNull();
    }
    
    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
        // cenário
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        
        // ação
        Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "senha"));
        
        // Verificação
        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacaoException.class).hasMessage("Usuário não encontrado para o email informado.");
    }
    
    @Test
    public void deveLancarErroQuandoSenhaNaoBater() {
        String senha = "senha";
        Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
        
        //service
        Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "123"));
        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacaoException.class).hasMessage("Senha inválida.");
    }
    
    @Test(expected = Test.None.class)
    public void deveValidarEmail() {
        // cenário
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
        
        // ação
        service.validarEmail("email@email.com");
    }
    
    @Test(expected = RegraNegocioException.class)
    public void deveLancarErroAoValidarEmailQuandoExisteEmailCadastrado() {
        // cenário
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
        
        // ação
        service.validarEmail("usuario@email.com");
        
    }
}
