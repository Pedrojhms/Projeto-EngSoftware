package com.example.projetoraonifinal.model.service;

import com.example.projetoraonifinal.exception.AutenticacaoException;
import com.example.projetoraonifinal.exception.RegraNegocioException;
import com.example.projetoraonifinal.model.entity.Usuario;
import com.example.projetoraonifinal.model.repository.UsuarioRepository;
import com.example.projetoraonifinal.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImpl service;

    @MockBean
    UsuarioRepository repository;

    @Test
    public void validarEmailSucesso(){
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

        Assertions.assertDoesNotThrow(() -> service.validarEmail("usuario@teste.com"));
    }

    @Test
    public void validarEmailJaExistente(){
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

        Assertions.assertThrows(RegraNegocioException.class, () -> service.validarEmail("usuario@teste.com"));
    }

    @Test
    public void autenticarUsuarioSucesso() {
        String email = "usuario@teste.com";
        String senha = "1234";

        Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

        Usuario result = service.autenticar(email, senha);

        Assertions.assertNotNull(result);
    }

    @Test
    public void autenticarUsuarioJaCadastrado() {
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(AutenticacaoException.class, () -> service.autenticar("usuario@teste.com", "1234"), "Nenhum usuário cadastrado com o e-mail informado.");
    }

    @Test
    public void autenticarSenhaIncorreta() {

        String senha = "1234";
        Usuario usuario = Usuario.builder().email("usuario@teste.com").senha(senha).build();

        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

        Assertions.assertThrows(AutenticacaoException.class, () -> service.autenticar("usuario@teste.com", "123"), "Senha incorreta.");
    }

    @Test
    public void salvarUsuarioSucesso() {
        Mockito.doNothing().when(service).validarEmail(Mockito.anyString());

        Usuario usuario = Usuario.builder().id(1L).nome("Usuario").email("usuario@teste.com").senha("1234").build();

        Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioSalvo = service.salvar(new Usuario());

        Assertions.assertNotNull(usuarioSalvo);
        Assertions.assertEquals(1L, usuarioSalvo.getId());
        Assertions.assertEquals("Usuario", usuarioSalvo.getNome());
        Assertions.assertEquals("usuario@teste.com", usuarioSalvo.getEmail());
        Assertions.assertEquals("1234", usuarioSalvo.getSenha());
    }

    @Test
    public void salvarUsuarioComEmailCadastrado() {
        String email = "usuario@teste.com";
        Usuario usuario = Usuario.builder().email(email).build();
        Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);

        Assertions.assertThrows(RegraNegocioException.class, () -> service.salvar(usuario));

        Mockito.verify(repository, Mockito.never()).save(usuario);
    }

}
