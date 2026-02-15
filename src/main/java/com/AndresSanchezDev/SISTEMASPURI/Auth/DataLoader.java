package com.AndresSanchezDev.SISTEMASPURI.Auth;

import com.AndresSanchezDev.SISTEMASPURI.entity.Rol;
import com.AndresSanchezDev.SISTEMASPURI.entity.Usuario;
import com.AndresSanchezDev.SISTEMASPURI.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("🔧 =========================================");
        System.out.println("🔧 INICIANDO CREACIÓN DE USUARIOS DE PRUEBA");
        System.out.println("🔧 =========================================");

        // Crear ADMIN
        if (usuarioRepository.findByUsuario("puri").isEmpty()) {
            crearUsuarioAdmin();
        } else {
            System.out.println("⏭️  Usuario admin ya existe, omitiendo...");
        }

        // Crear VENDEDOR
        if (usuarioRepository.findByUsuario("Alocatee").isEmpty()) {
            crearUsuarioVendedor();
        } else {
            System.out.println("⏭️  Usuario vendedor ya existe, omitiendo...");
        }

        // Crear REPARTIDOR
        if (usuarioRepository.findByUsuario("alfredotubebita123").isEmpty()) {
            crearUsuarioRepartidor();
        } else {
            System.out.println("⏭️  Usuario repartidor ya existe, omitiendo...");
        }

        System.out.println("🎯 =========================================");
        System.out.println("🎯 CREACIÓN DE USUARIOS COMPLETADA");
        System.out.println("🎯 =========================================");
    }

    private void crearUsuarioAdmin() {
        try {
            // 1. Crear Usuario
            Usuario admin = new Usuario();
            admin.setNombre("Edwin Puri Castro");
            admin.setCorreo("jyr@hotmai.com");
            admin.setRol(Rol.ADMINISTRADOR);
            admin.setTelefono("987437118");
            admin.setUsuario("puri");
            admin.setContrasena(passwordEncoder.encode("puri123"));
            // Guardar
            usuarioRepository.save(admin);

            System.out.println("✅ ADMIN CREADO:");
            System.out.println("   👤 Usuario: puri");
            System.out.println("   🔑 Password: puri123");
            System.out.println("   📋 Permisos: Acceso total al sistema");

        } catch (Exception e) {
            System.err.println("❌ Error creando usuario admin: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void crearUsuarioVendedor() {
        try {
            // 1. Crear Usuario
            Usuario vendedor = new Usuario();
            vendedor.setNombre("Cristian");
            vendedor.setCorreo("crt@hotmail.com");
            vendedor.setRol(Rol.VENDEDOR);
            vendedor.setUsuario("Alocatee");
            vendedor.setTelefono("123456789");
            vendedor.setContrasena(passwordEncoder.encode("alocatee123"));

            Usuario vendedor2 = new Usuario();
            vendedor2.setNombre("Wilmer");
            vendedor2.setCorreo("wilmerBebita@hotmail.com");
            vendedor2.setRol(Rol.VENDEDOR);
            vendedor2.setUsuario("WilmerMapero");
            vendedor2.setTelefono("123456789");
            vendedor2.setContrasena(passwordEncoder.encode("luna123"));

            Usuario vendedor3 = new Usuario();
            vendedor3.setNombre("Alfredo");
            vendedor3.setCorreo("wilmerBebita@hotmail.com");
            vendedor3.setRol(Rol.VENDEDOR);
            vendedor3.setUsuario("labeba");
            vendedor3.setTelefono("123456789");
            vendedor3.setContrasena(passwordEncoder.encode("bebe123"));

            Usuario vendedor4 = new Usuario();
            vendedor3.setNombre("Victor");
            vendedor3.setCorreo("v@gmail.com");
            vendedor3.setRol(Rol.VENDEDOR);
            vendedor3.setUsuario("victor123");
            vendedor3.setTelefono("123456789");
            vendedor3.setContrasena(passwordEncoder.encode("palomino"));
            // Guardar
            usuarioRepository.save(vendedor);
            usuarioRepository.save(vendedor2);
            usuarioRepository.save(vendedor3);
            usuarioRepository.save(vendedor4);

            System.out.println("✅ VENDEDORES CREADO:");
            System.out.println("   👤 Usuarios: Alocatee,WilmerMapero,labeba,victor123");
            System.out.println("   🔑 Password: alocatee123,luna123,bebe123,palomino");
            System.out.println("   📋 Permisos: Accesos Limitados");

        } catch (Exception e) {
            System.err.println("❌ Error creando usuario admin: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void crearUsuarioRepartidor() {
        try {
            // 1. Crear Usuario
            Usuario repartidor = new Usuario();
            repartidor.setNombre("Alfredo la bebita");
            repartidor.setCorreo("alfredBebita@hotmail.com");
            repartidor.setRol(Rol.REPARTIDOR);
            repartidor.setUsuario("alfredotubebita123");
            repartidor.setTelefono("985465232");
            repartidor.setContrasena(passwordEncoder.encode("ocote123"));
            // Guardar
            usuarioRepository.save(repartidor);

            System.out.println("✅ REPARTIDOR CREADO:");
            System.out.println("   👤 Usuario: alfredotubebita123");
            System.out.println("   🔑 Password: ocote123");
            System.out.println("   📋 Permisos: Accesos Limitados");

        } catch (Exception e) {
            System.err.println("❌ Error creando usuario admin: " + e.getMessage());
            e.printStackTrace();
        }
    }
}