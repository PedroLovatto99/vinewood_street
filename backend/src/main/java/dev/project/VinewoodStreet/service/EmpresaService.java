package dev.project.VinewoodStreet.service;

import dev.project.VinewoodStreet.dto.mapper.EmpresaCardsMapper;
import dev.project.VinewoodStreet.dto.mapper.EmpresaDetalheMapper;
import dev.project.VinewoodStreet.dto.response.EmpresaDetalheDTO;
import dev.project.VinewoodStreet.dto.response.EmpresasCardsDTO;
import dev.project.VinewoodStreet.models.EmpresaModel;
import dev.project.VinewoodStreet.repository.EmpresaRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmpresaService {

    EmpresaRepository empresaRepo;
    EmpresaCardsMapper emprCardMapper;
    EmpresaDetalheMapper empreDetalhesMapper;

    public EmpresaService(EmpresaRepository empresaRepo, EmpresaCardsMapper emprCardMapper, EmpresaDetalheMapper empreDetalhesMapper) {
        this.empresaRepo = empresaRepo;
        this.emprCardMapper = emprCardMapper;
        this.empreDetalhesMapper = empreDetalhesMapper;
    }

    @Cacheable("lista_empresas")
    public Page<EmpresasCardsDTO> listarEmpresas(Pageable paginacao) {
        Page<EmpresaModel> paginaDeEmpresas = empresaRepo.findAll(paginacao);
        return paginaDeEmpresas.map(emprCardMapper::toResponse);

    }

    public EmpresaDetalheDTO empresaDetalhes(Long id) {
        Optional<EmpresaModel> empresa = empresaRepo.findById(id);
        return empresa.map(empreDetalhesMapper::toResponse).orElse(null);

    }


}
