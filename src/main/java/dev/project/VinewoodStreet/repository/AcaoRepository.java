package dev.project.VinewoodStreet.repository;

import dev.project.VinewoodStreet.models.AcaoModel;
import dev.project.VinewoodStreet.models.EmpresaModel;
import dev.project.VinewoodStreet.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AcaoRepository extends JpaRepository<AcaoModel, Long> {

    Optional<AcaoModel> findByUsuarioAndEmpresa(UserModel usuario, EmpresaModel empresa);

}
