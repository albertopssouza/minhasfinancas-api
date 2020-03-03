/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.digiboard.minhasfinancas.api.controller;

import br.com.digiboard.minhasfinancas.api.dto.UsuarioDTO;
import br.com.digiboard.minhasfinancas.exception.ErroAutenticacaoException;
import br.com.digiboard.minhasfinancas.exception.RegraNegocioException;
import br.com.digiboard.minhasfinancas.model.entity.Usuario;
import br.com.digiboard.minhasfinancas.service.LancamentoService;
import br.com.digiboard.minhasfinancas.service.UsuarioService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author digiboard
 */
@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService service;
    @Autowired
    private LancamentoService lancamentoService;
    
    @PostMapping
    public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .build();
        
        try {
            return new ResponseEntity(service.salvar(usuario), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
        
        try {
            return ResponseEntity.ok(service.autenticar(dto.getEmail(), dto.getSenha()));
        } catch (ErroAutenticacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
    }
    
    @GetMapping("/{id}/saldo")
    public ResponseEntity obterSaldo(@PathVariable("id") Long id) {
        BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
        return ResponseEntity.ok(saldo);
    }
}
