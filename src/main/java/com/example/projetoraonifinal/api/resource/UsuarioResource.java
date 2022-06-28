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
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioResource {

    private final UsuarioService service;
    private final JwtService jwtService;

    @GetMapping("/buscar/{id}")
    public ResponseEntity buscarUsuario(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.buscar(id));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/salvar")
    public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {

        try {
            Usuario usuario = Usuario.builder()
                    .nome(dto.getNome())
                    .email(dto.getEmail())
                    .senha(dto.getSenha())
                    .cep(dto.getCep())
                    .logradouro(dto.getLogradouro())
                    .bairro(dto.getBairro())
                    .cidade(dto.getCidade())
                    .uf(dto.getUf())
                    .numero(dto.getNumero())
                    .complemento(dto.getComplemento())
                    .build();
            Usuario usuarioSalvo = service.salvar(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity editar(@PathVariable("id") long id, @RequestBody UsuarioDTO dto) {

        try {
            Usuario usuario = Usuario.builder()
                    .id(id)
                    .nome(dto.getNome())
                    .email(dto.getEmail())
                    .senha(dto.getSenha())
                    .cep(dto.getCep())
                    .logradouro(dto.getLogradouro())
                    .bairro(dto.getBairro())
                    .cidade(dto.getCidade())
                    .uf(dto.getUf())
                    .numero(dto.getNumero())
                    .complemento(dto.getComplemento())
                    .build();
            Usuario usuarioSalvo = service.editar(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity deletarUsuario(@PathVariable Long id) {
        try {
            //service.deletar(id);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
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
