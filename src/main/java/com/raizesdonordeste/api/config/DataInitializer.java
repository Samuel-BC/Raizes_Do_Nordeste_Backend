package com.raizesdonordeste.api.config;

import com.raizesdonordeste.api.domain.model.Perfil;
import com.raizesdonordeste.api.domain.model.Produto;
import com.raizesdonordeste.api.domain.model.Unidade;
import com.raizesdonordeste.api.domain.model.Usuario;
import com.raizesdonordeste.api.domain.repository.ProdutoRepository;
import com.raizesdonordeste.api.domain.repository.UnidadeRepository;
import com.raizesdonordeste.api.domain.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final UnidadeRepository unidadeRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UnidadeRepository unidadeRepository,
                           ProdutoRepository produtoRepository,
                           UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder) {
        this.unidadeRepository = unidadeRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (unidadeRepository.count() == 0) {
            Unidade u1 = new Unidade(null, "Unidade Recife - Boa Viagem", "Recife", true);
            Unidade u2 = new Unidade(null, "Unidade Salvador - Pelourinho", "Salvador", true);
            unidadeRepository.saveAll(List.of(u1, u2));
        }

        if (produtoRepository.count() == 0) {
            Produto p1 = new Produto(null, "Cuscuz Completo com Carne de Sol", "Cuscuz tradicional temperado com queijo coalho e carne de sol", new BigDecimal("22.50"), 50, true);
            Produto p2 = new Produto(null, "Tapioca de Rendada de Queijo Coalho", "Tapioca crocante recheada com queijo coalho e presunto", new BigDecimal("16.00"), 30, true);
            Produto p3 = new Produto(null, "Suco Natural de Cajá 500ml", "Suco natural da fruta cajá gelado", new BigDecimal("8.50"), 100, true);
            produtoRepository.saveAll(List.of(p1, p2, p3));
        }

        if (usuarioRepository.count() == 0) {
            Usuario user = Usuario.builder()
                    .nome("Maria Silva")
                    .email("cliente@raizes.com")
                    .senha(passwordEncoder.encode("123456"))
                    .perfil(Perfil.CLIENTE)
                    .consentimentoLgpd(true)
                    .build();

            Usuario admin = Usuario.builder()
                    .nome("Administrador")
                    .email("admin@raizes.com")
                    .senha(passwordEncoder.encode("123456"))
                    .perfil(Perfil.ADMIN)
                    .consentimentoLgpd(true)
                    .build();

            usuarioRepository.saveAll(List.of(user, admin));
        }
    }
}