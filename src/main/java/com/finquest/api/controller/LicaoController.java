package com.finquest.api.controller;

import com.finquest.api.model.Licao;
import com.finquest.api.service.JogadorService;
import com.finquest.api.service.LicaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/licoes")
public class LicaoController {

    @Autowired
    private LicaoService licaoService;

    @Autowired
    private JogadorService jogadorService;

    @PostMapping
    public ResponseEntity<Licao> cadastrar(@RequestBody @Valid Licao licao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(licaoService.cadastrar(licao));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Licao>> listarTodos() {
        return ResponseEntity.ok(licaoService.listarTodos());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Licao> buscarPorId(@PathVariable Long id) {
        return licaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/{id}/jogador/{jogadorId}")
    public ResponseEntity<?> buscarComVerificacao(
            @PathVariable Long id,
            @PathVariable Long jogadorId) {

        return licaoService.buscarPorId(id).map(licao -> {
            boolean temAcesso = jogadorService.temXpSuficiente(jogadorId, licao.getNivelId().intValue());
            if (!temAcesso) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of(
                                "erro", "XP insuficiente para acessar esta lição",
                                "xpNecessario", (licao.getNivelId().intValue() - 1) * 100
                        ));
            }
            return ResponseEntity.ok(licao);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        licaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}