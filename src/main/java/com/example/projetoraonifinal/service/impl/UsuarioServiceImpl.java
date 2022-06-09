package com.example.projetoraonifinal.service.impl;

import com.example.projetoraonifinal.exception.AutenticacaoException;
import com.example.projetoraonifinal.exception.RegraNegocioException;
import com.example.projetoraonifinal.model.entity.Usuario;
import com.example.projetoraonifinal.model.repository.UsuarioRepository;
import com.example.projetoraonifinal.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private UsuarioRepository repository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);

        if (!usuario.isPresent()) {
            throw new AutenticacaoException("Nenhum usuário cadastrado com o e-mail informado.");
        }

        if (!usuario.get().getSenha().equals(senha)) {
            throw new AutenticacaoException("Senha incorreta.");
        }

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

        if (existe) {
            throw new RegraNegocioException("Já existe um usuario cadastrado com esse e-mail!");
        }
    }
}
