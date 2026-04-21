package dev.project.VinewoodStreet.controller;

import dev.project.VinewoodStreet.dto.response.EventoNoticiaDTO;
import dev.project.VinewoodStreet.service.EventoNoticiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/noticias")
@CrossOrigin(origins = "*")
public class EventoNoticiaController {

    EventoNoticiaService noticiaService;

    public EventoNoticiaController(EventoNoticiaService noticiaService) {
        this.noticiaService = noticiaService;
    }

    @GetMapping
    @Operation(summary = "Listar noticas", description = "Listar todas as noticias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "lista de noticias")
    })
    public ResponseEntity<Page<EventoNoticiaDTO>> listarNoticias(@PageableDefault(size = 10) Pageable paginacao) {
        Page<EventoNoticiaDTO> noticias = noticiaService.listarNoticias(paginacao);
        return ResponseEntity.ok(noticias);
    }



}
