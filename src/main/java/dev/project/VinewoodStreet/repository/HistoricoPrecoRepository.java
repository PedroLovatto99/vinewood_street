package dev.project.VinewoodStreet.repository;

import dev.project.VinewoodStreet.models.EventoNoticiaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoPrecoRepository extends JpaRepository<EventoNoticiaModel, Long> {
}
