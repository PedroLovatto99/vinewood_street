package dev.project.VinewoodStreet.service;

import dev.project.VinewoodStreet.dto.mapper.EventoNoticiaMapper;
import dev.project.VinewoodStreet.dto.response.EventoNoticiaDTO;
import dev.project.VinewoodStreet.models.EventoNoticiaModel;
import dev.project.VinewoodStreet.repository.EventoNoticiaRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventoNoticiaService {

    EventoNoticiaMapper noticiaMapper;
    EventoNoticiaRepository noticiaRepo;

    public EventoNoticiaService(EventoNoticiaMapper noticiaMapper, EventoNoticiaRepository noticiaRepo) {
        this.noticiaMapper = noticiaMapper;
        this.noticiaRepo = noticiaRepo;
    }


    @Cacheable("lista_noticias")
    public Page<EventoNoticiaDTO> listarNoticias(Pageable paginacao) {
        Page<EventoNoticiaModel> paginaDeNoticias = noticiaRepo.findAll(paginacao);
        return paginaDeNoticias.map(noticiaMapper::toResponse);
    }

}
