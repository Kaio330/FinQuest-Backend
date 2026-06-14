package com.finquest.api.controller;

import com.finquest.api.dto.JogadorResponse;
import com.finquest.api.model.Jogador;
import com.finquest.api.service.JogadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jogadores")
public class JogadorController {

    @Autowired
    private JogadorService jogadorService;

    @PostMapping("/cadastrar")
    public ResponseEntity<JogadorResponse> cadastrar(@RequestBody @Valid Jogador jogador) {
        Jogador novo = jogadorService.cadastrar(jogador);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(novo));
    }

    @GetMapping
    public ResponseEntity<List<JogadorResponse>> listarTodos() {
        return ResponseEntity.ok(jogadorService.listarTodos().stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JogadorResponse> buscarPorId(@PathVariable Long id) {
        return jogadorService.buscarPorId(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/xp")
    public ResponseEntity<JogadorResponse> darXp(@PathVariable Long id, @RequestParam int quantidade) {
        try {
            return ResponseEntity.ok(toResponse(jogadorService.darXp(id, quantidade)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable long id) {
        jogadorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private JogadorResponse toResponse(Jogador j) {
        return new JogadorResponse(
                j.getId(), j.getNomePlayer(), j.getEmail(),
                j.getNivelAtual(), j.getXpPlayer(), j.getVidasJogador(),
                j.getCep(), j.getLogradouro(), j.getNumero(),
                j.getComplemento(), j.getBairro(), j.getCidade(), j.getEstado()
        );
    }
}
