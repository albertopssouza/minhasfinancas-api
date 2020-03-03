/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.digiboard.minhasfinancas.service.impl;

import br.com.digiboard.minhasfinancas.exception.ErroAutenticacaoException;
import br.com.digiboard.minhasfinancas.exception.RegraNegocioException;
import br.com.digiboard.minhasfinancas.model.entity.Usuario;
import br.com.digiboard.minhasfinancas.model.repository.UsuarioRepository;
import br.com.digiboard.minhasfinancas.service.UsuarioService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author digiboard
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {
    
    @Autowired
    private UsuarioRepository repository;

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);
        if (!usuario.isPresent())
            throw new ErroAutenticacaoException("Usuário não encontrado para o email informado.");
            
        if (!usuario.get().getSenha().equals(senha))
            throw new ErroAutenticacaoException("Senha inválida.");
        
        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvar(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if (existe)
            throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
    }

    @Override
    public Optional<Usuario> obterPorId(Long id) {
        return repository.findById(id);
    }
    
}
