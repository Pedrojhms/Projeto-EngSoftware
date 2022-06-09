package com.example.projetoraonifinal.service;

import com.example.projetoraonifinal.model.entity.Usuario;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario salvar(Usuario usuario);

    void validarEmail(String email);

}
