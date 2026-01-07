package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.Usuario;
import com.AndresSanchezDev.SISTEMASPURI.repository.UsuarioRepository;
import com.AndresSanchezDev.SISTEMASPURI.repository.VendedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService{
    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Usuario save(Usuario usuario) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
