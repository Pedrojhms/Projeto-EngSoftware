package com.example.projetoraonifinal.service.impl;

import com.example.projetoraonifinal.exception.AutenticacaoException;
import com.example.projetoraonifinal.exception.RegraNegocioException;
import com.example.projetoraonifinal.model.entity.Usuario;
import com.example.projetoraonifinal.api.repository.UsuarioRepository;
import com.example.projetoraonifinal.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private UsuarioRepository repository;
    private PasswordEncoder encoder;

    public UsuarioServiceImpl(UsuarioRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public Usuario buscar(Long id) {
        Optional<Usuario> usuario = repository.findById(id);

        if (!usuario.isPresent()){
            throw new RegraNegocioException("Nenhum usuário encontrado para o id " + id);
        }
        return usuario.get();
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);

        if (!usuario.isPresent()) {
            throw new AutenticacaoException("Nenhum usuário cadastrado com o e-mail informado.");
        }

        boolean isSenha = encoder.matches(senha, usuario.get().getSenha());

        if (!isSenha) {
            throw new AutenticacaoException("Senha incorreta.");
        }

        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvar(Usuario usuario) {
        validarEmail(usuario.getEmail());

        criptografarSenha(usuario);

        return repository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario editar(Usuario usuario) {
        Optional<Usuario> usuarioSalvo = repository.findById(usuario.getId());

        if (!usuarioSalvo.isPresent()){
            throw new RegraNegocioException("Nenhum usuário encontrado para o id " + usuario.getId());
        }

        criptografarSenha(usuario);

        return repository.save(usuario);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        Optional<Usuario> usuarioSalvo = repository.findById(id);

        if (!usuarioSalvo.isPresent()){
            throw new RegraNegocioException("Nenhum usuário encontrado para o id " + id);
        }

        repository.deleteById(id);
    }

    private void criptografarSenha(Usuario usuario) {
        String senha = usuario.getSenha();
        String senhaCript = encoder.encode(senha);
        usuario.setSenha(senhaCript);
    }
    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);

        if (existe) {
            throw new RegraNegocioException("Já existe um usuario cadastrado com esse e-mail!");
        }
    }
}
