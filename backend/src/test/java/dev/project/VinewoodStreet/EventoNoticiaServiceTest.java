package dev.project.VinewoodStreet;

import dev.project.VinewoodStreet.dto.mapper.EventoNoticiaMapper;
import dev.project.VinewoodStreet.dto.response.EventoNoticiaDTO;
import dev.project.VinewoodStreet.enums.TipoImpacto;
import dev.project.VinewoodStreet.models.EmpresaModel;
import dev.project.VinewoodStreet.models.EventoNoticiaModel;
import dev.project.VinewoodStreet.repository.EventoNoticiaRepository;
import dev.project.VinewoodStreet.service.EventoNoticiaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventoNoticiaServiceTest {

    @Mock
    private EventoNoticiaRepository noticiaRepo;

    @Mock
    private EventoNoticiaMapper noticiaMapper;

    @InjectMocks
    private EventoNoticiaService noticiaService;

    @Test
    void listarTodasAsNoticiasComSucesso() {

        Pageable paginacao = PageRequest.of(0, 10);
        EmpresaModel empresa = new EmpresaModel();
        empresa.setSigla("PIS");

        EventoNoticiaModel noticia = new EventoNoticiaModel(1L, empresa, "Título Escândalo", "Conteúdo gerado", TipoImpacto.NEGATIVO, -5.0, LocalDateTime.now());
        Page<EventoNoticiaModel> paginaSimulada = new PageImpl<>(List.of(noticia));

        EventoNoticiaDTO dtoEsperado = new EventoNoticiaDTO("PIS", "Título Escândalo", "Conteúdo gerado", TipoImpacto.NEGATIVO, LocalDateTime.now());

        when(noticiaRepo.findAll(any(Pageable.class))).thenReturn(paginaSimulada);
        when(noticiaMapper.toResponse(noticia)).thenReturn(dtoEsperado);

        Page<EventoNoticiaDTO> resultado = noticiaService.listarNoticias(paginacao);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Título Escândalo", resultado.getContent().get(0).titulo());
        assertEquals(TipoImpacto.NEGATIVO, resultado.getContent().get(0).impacto());
    }






}
