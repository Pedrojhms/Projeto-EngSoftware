package com.example.projetoraonifinal.model.repository;

import com.example.projetoraonifinal.api.repository.UsuarioRepository;
import com.example.projetoraonifinal.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveVerificarExistenciaEmail() {

        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);

        boolean result = repository.existsByEmail("usuario@teste.com");

        Assertions.assertThat(result).isTrue();

    }

    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioComEmail() {
        boolean result = repository.existsByEmail("usuario@teste.com");

        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void deveSalvarUsuario() {
        Usuario usuario = criarUsuario();

        Usuario usuarioSalvo = repository.save(usuario);

        Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
    }

    @Test
    public void deveBuscarUsuarioEmail() {
        Usuario usuario = criarUsuario();

        entityManager.persist(usuario);

        Optional<Usuario> result = repository.findByEmail("usuario@teste.com");

        Assertions.assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void deveBuscarUsuarioInexistentePorEmail() {
        Optional<Usuario> result = repository.findByEmail("usuario@teste.com");

        Assertions.assertThat(result.isPresent()).isFalse();
    }

    public static Usuario criarUsuario() {
        return Usuario.builder().nome("usuario").email("usuario@teste.com").senha("1234").build();
    }
}
