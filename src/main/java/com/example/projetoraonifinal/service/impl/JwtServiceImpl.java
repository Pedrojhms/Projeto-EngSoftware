package com.example.projetoraonifinal.service.impl;

import com.example.projetoraonifinal.model.entity.Usuario;
import com.example.projetoraonifinal.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiracao}")
    private String expiracao;

    @Value("${jwt.chave-assinatura}")
    private String chaveAssinatura;

    @Override
    public String gerarToken(Usuario usuario) {
        long exp = Long.parseLong(expiracao);
        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(exp);
        Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
        Date data = Date.from(instant);

        String dataHoraExpiracaoToken = dataHoraExpiracao.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        return Jwts.builder()
                .setExpiration(data)
                .claim("userid", usuario.getId())
                .claim("nome", usuario.getNome())
                .claim("dataHoraExpiracao", dataHoraExpiracaoToken)
                .signWith(SignatureAlgorithm.HS512, chaveAssinatura)
                .compact();
    }

    @Override
    public Claims obterClaims(String token) throws ExpiredJwtException {
        return Jwts.parser().setSigningKey(chaveAssinatura).parseClaimsJws(token).getBody();
    }

    @Override
    public boolean isTokenValido(String token) {
        try {
            Claims claims = obterClaims(token);
            Date dataExp =claims.getExpiration();

            LocalDateTime dataExpiracao = dataExp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            return !LocalDateTime.now().isAfter(dataExpiracao);
        }catch (ExpiredJwtException e) {
            return false;
        }
    }

    @Override
    public String obterLoginUsuario(String token) {
        Claims claims = obterClaims(token);
        return claims.getSubject();
    }
}
