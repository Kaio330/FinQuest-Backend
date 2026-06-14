package com.finquest.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Entity
@Table(name = "jogadores")
@Data
public class Jogador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nomePlayer;

    @Email
    @NotBlank
    @Column(unique=true)
    private String email;

    @NotBlank
    private String senha;

    private int nivelAtual=1;
    private int xpPlayer=0;
    private int vidasJogador=1;

    // Endereço
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
}
