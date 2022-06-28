package com.example.projetoraonifinal.service;

import com.example.projetoraonifinal.model.entity.Usuario;

public interface UsuarioService {

    Usuario buscar(Long id);
    Usuario autenticar(String email, String senha);

    Usuario salvar(Usuario usuario);

    Usuario editar(Usuario usuario);

    void deletar(Long id);

    void validarEmail(String email);

}
