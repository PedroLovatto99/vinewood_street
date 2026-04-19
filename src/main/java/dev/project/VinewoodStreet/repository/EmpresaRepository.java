package dev.project.VinewoodStreet.repository;

import dev.project.VinewoodStreet.models.EmpresaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<EmpresaModel, Long> {


    Optional<EmpresaModel> findBySigla(String sigla);

}
