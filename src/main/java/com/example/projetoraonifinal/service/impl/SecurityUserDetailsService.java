package com.example.projetoraonifinal.service.impl;

import com.example.projetoraonifinal.model.entity.Usuario;
import com.example.projetoraonifinal.api.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SecurityUserDetailsService implements UserDetailsService {


    private UsuarioRepository usuarioRepository;

    public SecurityUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Usuario usuarioEncontrado = usuarioRepository
                .findById(Long.parseLong(id))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não cadastrado."));

        return User.builder()
                .username(usuarioEncontrado.getEmail())
                .password(usuarioEncontrado.getSenha())
                .roles("USER")
                .build();
    }
}
