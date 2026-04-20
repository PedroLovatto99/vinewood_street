package dev.project.VinewoodStreet.controller;

import dev.project.VinewoodStreet.dto.request.ComprarAcaoRequest;
import dev.project.VinewoodStreet.dto.request.VenderAcaoRequest;
import dev.project.VinewoodStreet.dto.response.AcaoCarteiraResponse;
import dev.project.VinewoodStreet.models.UserModel;
import dev.project.VinewoodStreet.service.AcaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/acoes")
public class AcaoController {

    AcaoService acaoService;


    @GetMapping
    @Operation(summary = "Listar saldo e ações do usuário", description = "Listar o saldo e ações que o usuário possui")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saldo e ações do usuário")
    })
    public ResponseEntity<AcaoCarteiraResponse> verCarteira(@AuthenticationPrincipal UserModel usuarioLogado) {

        Long id = usuarioLogado.getId();

        AcaoCarteiraResponse carteiraAcao = acaoService.buscarCarteira(id);

        return ResponseEntity.ok(carteiraAcao);

    }


    @PostMapping("/comprar")
    @Operation(summary = "Comprar ação", description = "Comprar uma quantidade de ações")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ação comprada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na compra. Dados inválidos")
    })
    public ResponseEntity<String> comprarAcao(@AuthenticationPrincipal UserModel usuarioLogado,
                                              @Valid @RequestBody ComprarAcaoRequest request) {
        acaoService.comprarAcao(usuarioLogado, request);

        return ResponseEntity.status(HttpStatus.CREATED).body("Ação/ações comprada(s) com sucesso!");

    }


    @PostMapping("/vender")
    @Operation(summary = "Vender ação", description = "Vender uma quantidade de ações")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ação vendida com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na venda. Dados inválidos")
    })
    public ResponseEntity<String> venderAcao(@AuthenticationPrincipal UserModel usuarioLogado,
                                             @Valid @RequestBody VenderAcaoRequest request) {
        acaoService.venderAcao(usuarioLogado, request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ação/ações vendida(s) com sucesso!");

    }



}
