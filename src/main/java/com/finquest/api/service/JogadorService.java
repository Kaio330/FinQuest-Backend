package com.finquest.api.service;

import com.finquest.api.model.Jogador;
import com.finquest.api.repository.JogadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JogadorService {

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Jogador cadastrar(Jogador jogador) {
        jogador.setSenha(passwordEncoder.encode(jogador.getSenha()));
        return jogadorRepository.save(jogador);
    }

    public List<Jogador> listarTodos() {
        return jogadorRepository.findAll();
    }

    public Optional<Jogador> buscarPorId(Long id) {
        return jogadorRepository.findById(id);
    }

    public Optional<Jogador> buscarPorEmail(String email) {
        return jogadorRepository.findByEmail(email);
    }

    public void deletar(Long id) {
        jogadorRepository.deleteById(id);
    }

    public Jogador darXp(Long jogadorId, int xp) {
        Jogador jogador = jogadorRepository.findById(jogadorId)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        jogador.setXpPlayer(jogador.getXpPlayer() + xp);
        verificarNivel(jogador);

        return jogadorRepository.save(jogador);
    }

    public boolean temXpSuficiente(Long jogadorId, int nivelDaLicao) {
        Jogador jogador = jogadorRepository.findById(jogadorId)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        int xpMinimo = (nivelDaLicao - 1) * 100;
        return jogador.getXpPlayer() >= xpMinimo;
    }

    private void verificarNivel(Jogador jogador) {
        while (true) {
            int xpParaProximoNivel = jogador.getNivelAtual() * 100;
            if (jogador.getXpPlayer() >= xpParaProximoNivel) {
                jogador.setNivelAtual(jogador.getNivelAtual() + 1);
            } else {
                break;
            }
        }
    }
}