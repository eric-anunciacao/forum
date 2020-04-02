package com.easys.forum.config.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.easys.forum.model.Usuario;
import com.easys.forum.repository.UsuarioRepository;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private final TokenService tokenService;

	private final UsuarioRepository usuarioRepository;

	public TokenAuthenticationFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String token = getToken(request);
		boolean valid = tokenService.isTokenValid(token);
		if (valid) {
			authenticate(token);
		}

		chain.doFilter(request, response);
	}

	private void authenticate(String token) {
		Long userId = tokenService.getUserId(token);
		Optional<Usuario> optional = usuarioRepository.findById(userId);
		if (optional.isPresent()) {
			Usuario usuario = optional.get();
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null,
					usuario.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
	}

	private String getToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		return token.substring(7, token.length());
	}

}
