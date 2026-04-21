package dev.project.VinewoodStreet.controller;

import dev.project.VinewoodStreet.dto.response.EmpresaDetalheDTO;
import dev.project.VinewoodStreet.dto.response.EmpresasCardsDTO;
import dev.project.VinewoodStreet.service.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {

    EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping
    @Operation(summary = "Listar empresas na tela inicial", description = "Listar todas as empresas na tela inicial")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "lista de empresas")
    })
    public ResponseEntity<Page<EmpresasCardsDTO>> listarEmpresas(@PageableDefault(size = 10)Pageable paginacao) {
        Page<EmpresasCardsDTO> empresas = empresaService.listarEmpresas(paginacao);
        return ResponseEntity.ok(empresas);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Listar detalhes da empresa", description = "Encontrar empresa específica pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    public ResponseEntity<?> empresaDetalhes(@PathVariable Long id) {

        EmpresaDetalheDTO empresa = empresaService.empresaDetalhes(id);
        if(empresa != null) {
            return ResponseEntity.ok(empresa);
        } else {
            return ResponseEntity.status((HttpStatus.NOT_FOUND))
                    .body("Empresa não encontrada");
        }

    }




}
