package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.config.TokenService;
import com.raizesdonordeste.api.domain.dto.AutenticacaoDTO;
import com.raizesdonordeste.api.domain.dto.TokenResponseDTO;
import com.raizesdonordeste.api.domain.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AutenticacaoController(UsuarioRepository usuarioRepository,
                                  PasswordEncoder passwordEncoder,
                                  TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> efetuarLogin(@RequestBody @Valid AutenticacaoDTO dto) {
        var usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new IllegalArgumentException("E-mail ou senha inválidos");
        }

        var token = tokenService.gerarToken(usuario.getEmail());
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }
}