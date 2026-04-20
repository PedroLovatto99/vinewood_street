package dev.project.VinewoodStreet.dto.mapper;

import dev.project.VinewoodStreet.dto.response.EventoNoticiaDTO;
import dev.project.VinewoodStreet.enums.TipoImpacto;
import dev.project.VinewoodStreet.models.EventoNoticiaModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventoNoticiaMapper {

    public EventoNoticiaDTO toResponse(EventoNoticiaModel eventonoticia) {

        EventoNoticiaDTO noticia = new EventoNoticiaDTO(

                eventonoticia.getEmpresa().getSigla(),
                eventonoticia.getTitulo(),
                eventonoticia.getConteudoGerado(),
                eventonoticia.getImpacto(),
                eventonoticia.getDataPublicacao()

        );

        return noticia;

    }

}
