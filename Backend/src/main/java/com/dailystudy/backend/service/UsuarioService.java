package com.dailystudy.backend.service;

import com.dailystudy.backend.dto.UsuarioRegistro;
import com.dailystudy.backend.model.Usuario;
import com.dailystudy.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Usuario registroUsuario(UsuarioRegistro dto) {
        Usuario username = new Usuario();
        username.setUsername(dto.getUsername());
        username.setEmail(dto.getEmail());

        String cipherText = passwordEncoder.encode(dto.getSenha());
        username.setSenha(cipherText);

        return usuarioRepository.save(username);
    }
}
