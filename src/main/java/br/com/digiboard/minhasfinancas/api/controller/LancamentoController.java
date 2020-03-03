/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.digiboard.minhasfinancas.api.controller;

import br.com.digiboard.minhasfinancas.api.dto.AtualizaStatusDTO;
import br.com.digiboard.minhasfinancas.api.dto.LancamentoDTO;
import br.com.digiboard.minhasfinancas.exception.RegraNegocioException;
import br.com.digiboard.minhasfinancas.model.entity.Lancamento;
import br.com.digiboard.minhasfinancas.model.entity.Usuario;
import br.com.digiboard.minhasfinancas.model.enums.StatusLancamento;
import br.com.digiboard.minhasfinancas.model.enums.TipoLancamento;
import br.com.digiboard.minhasfinancas.service.LancamentoService;
import br.com.digiboard.minhasfinancas.service.UsuarioService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author digiboard
 */
@RestController
@RequestMapping("api/lancamentos")
public class LancamentoController {
    
    @Autowired
    private LancamentoService service;
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping
    public ResponseEntity buscar(
            @RequestParam(value = "descricao", required = false) String descricao, 
            @RequestParam(value = "mes", required = false) Integer mes, 
            @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam("usuario") Long idUsuario) {
        
        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);
        
        Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
        if (!usuario.isPresent()) {
            return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para ");
        } else {
            lancamentoFiltro.setUsuario(usuario.get());
        }
        
        List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
        
        return ResponseEntity.ok(lancamentos);
    }
    
    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
        try {
            Lancamento lancamento = converter(dto);
            return new ResponseEntity(service.salvar(lancamento), HttpStatus.CREATED); 
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping
    public ResponseEntity atualizar(@RequestBody LancamentoDTO dto) {
        try {
            Lancamento lancamento = converter(dto);
            return ResponseEntity.ok(service.atualizar(lancamento)); 
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/atualiza-status")
    public ResponseEntity atualizarStatus(@RequestBody AtualizaStatusDTO dto) {
        try {
            StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
            if (statusSelecionado == null)
                return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento, envie um status válido.");
            
            Optional<Lancamento> lancamento = service.obterPorId(dto.getId());
            lancamento.get().setStatus(statusSelecionado);
            return ResponseEntity.ok(service.atualizar(lancamento.get()));
            
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        service.deletar(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
    private Lancamento converter(LancamentoDTO dto) {
        Lancamento lancamento = new Lancamento();
        if (dto.getId() != null)
            lancamento.setId(dto.getId());
        
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setAno(dto.getAno());
        lancamento.setMes(dto.getMes());
        lancamento.setValor(dto.getValor());
        
        Usuario usuario = usuarioService
                .obterPorId(dto.getUsuario())
                .orElseThrow( () -> new RegraNegocioException("Usuário não encontrado para o Id informado"));
        
        lancamento.setUsuario(usuario);
        if (dto.getTipo() != null)
            lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
        
        if (dto.getStatus() != null)
            lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
            
        return lancamento;
    }
    
}
