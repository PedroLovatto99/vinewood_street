package dev.project.VinewoodStreet;


import dev.project.VinewoodStreet.dto.mapper.EmpresaCardsMapper;
import dev.project.VinewoodStreet.dto.mapper.EmpresaDetalheMapper;
import dev.project.VinewoodStreet.dto.response.EmpresaDetalheDTO;
import dev.project.VinewoodStreet.dto.response.EmpresasCardsDTO;
import dev.project.VinewoodStreet.models.EmpresaModel;
import dev.project.VinewoodStreet.repository.EmpresaRepository;
import dev.project.VinewoodStreet.service.EmpresaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmpresaServiceTest {

    @Mock
    private EmpresaRepository empresaRepo;

    @Mock
    private EmpresaCardsMapper emprCardMapper;

    @Mock
    private EmpresaDetalheMapper empreDetalhesMapper;

    @InjectMocks
    private EmpresaService empresaService;

    @Test
    void listarTodasAsEmpresasComSucesso() {

        Pageable paginacao = PageRequest.of(0, 10);

        EmpresaModel empresa1 = new EmpresaModel(1L, "PIS", "Pißwasser", "Cerveja alemã", 15.50, null, null);
        EmpresaModel empresa2 = new EmpresaModel(2L, "MAZ", "Maze Bank", "Banco gigante", 250.00, null, null);
        Page<EmpresaModel> paginaSimulada = new PageImpl<>(List.of(empresa1, empresa2));

        EmpresasCardsDTO dto1 = new EmpresasCardsDTO(1L, "PIS", "Pißwasser", 15.50, 0.0);
        EmpresasCardsDTO dto2 = new EmpresasCardsDTO(2L, "MAZ", "Maze Bank", 250.00, 0.0);

        when(empresaRepo.findAll(any(Pageable.class))).thenReturn(paginaSimulada);
        when(emprCardMapper.toResponse(empresa1)).thenReturn(dto1);
        when(emprCardMapper.toResponse(empresa2)).thenReturn(dto2);

        Page<EmpresasCardsDTO> resultado = empresaService.listarEmpresas(paginacao);

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals("PIS", resultado.getContent().get(0).sigla());
    }

    @Test
    void buscarDetalhesDaEmpresaComSucesso() {

        Long id = 1L;
        EmpresaModel empresa = new EmpresaModel(1L, "PIS", "Pißwasser", "Cerveja", 15.50, List.of(), List.of());
        EmpresaDetalheDTO dtoEsperado = new EmpresaDetalheDTO(1L, "Pißwasser", "PIS", "Cerveja", 15.50, List.of(), List.of());

        when(empresaRepo.findById(id)).thenReturn(Optional.of(empresa));
        when(empreDetalhesMapper.toResponse(empresa)).thenReturn(dtoEsperado);

        EmpresaDetalheDTO resultado = empresaService.empresaDetalhes(id);

        assertNotNull(resultado);
        assertEquals("PIS", resultado.sigla());
        assertEquals(15.50, resultado.precoAtual());
    }

    @Test
    void retornarNuloQuandoEmpresaNaoExiste() {
        Long id = 99L;
        when(empresaRepo.findById(id)).thenReturn(Optional.empty());

        EmpresaDetalheDTO resultado = empresaService.empresaDetalhes(id);

        assertNull(resultado, "Deveria retornar nulo quando o ID não é encontrado");
    }

}
