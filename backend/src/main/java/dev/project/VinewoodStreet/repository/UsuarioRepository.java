package dev.project.VinewoodStreet.repository;

import dev.project.VinewoodStreet.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UserModel, Long> {

    Optional<UserDetails> findUserByEmail(String username);
}
