package dev.project.VinewoodStreet.dto.mapper;

import dev.project.VinewoodStreet.dto.response.EmpresasCardsDTO;
import dev.project.VinewoodStreet.models.EmpresaModel;
import org.springframework.stereotype.Component;
import dev.project.VinewoodStreet.utils.FuncoesAuxiliares;

@Component
public class EmpresaCardsMapper {

    public EmpresasCardsDTO toResponse(EmpresaModel empresa) {

        Double variacao24h = FuncoesAuxiliares.calcularVariacao3h(empresa);

        EmpresasCardsDTO empresaResponse = new EmpresasCardsDTO(
               empresa.getId(),
               empresa.getSigla(),
               empresa.getNome(),
               empresa.getPrecoAtual(),
               variacao24h
        );

        return empresaResponse;

    }

}
