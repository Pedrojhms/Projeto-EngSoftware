package com.example.projetoraonifinal.api.resource;

import com.example.projetoraonifinal.api.dto.TokenDTO;
import com.example.projetoraonifinal.api.dto.UsuarioDTO;
import com.example.projetoraonifinal.exception.AutenticacaoException;
import com.example.projetoraonifinal.exception.RegraNegocioException;
import com.example.projetoraonifinal.model.entity.Usuario;
import com.example.projetoraonifinal.service.JwtService;
import com.example.projetoraonifinal.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioResource {

    private final UsuarioService service;
    private final JwtService jwtService;

    @PostMapping("/salvar")
    public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {

        try {
            Usuario usuario = Usuario.builder().nome(dto.getNome()).email(dto.getEmail()).senha(dto.getSenha()).build();
            Usuario usuarioSalvo = service.salvar(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/autenticar")
    public ResponseEntity<?> autenticar(@RequestBody UsuarioDTO dto) {
        try {
            Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
            String token = jwtService.gerarToken(usuarioAutenticado);

            TokenDTO tokenDTO = new TokenDTO(usuarioAutenticado.getNome(), token);

            return ResponseEntity.ok(tokenDTO);
        } catch (AutenticacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
