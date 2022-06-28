package com.example.projetoraonifinal.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private String email;
    private String nome;
    private String senha;
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
    private Integer numero;
    private String complemento;
}
