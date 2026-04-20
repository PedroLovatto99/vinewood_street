package dev.project.VinewoodStreet.dto.mapper;

import dev.project.VinewoodStreet.dto.response.AcaoCarteiraResponse;
import dev.project.VinewoodStreet.dto.response.AcaoCompradaDTO;
import dev.project.VinewoodStreet.models.AcaoModel;
import org.springframework.stereotype.Component;

@Component
public class AcaoMapper {

    public AcaoCompradaDTO toResponse(AcaoModel acaoModel) {

        Double precoAtual = acaoModel.getEmpresa().getPrecoAtual();
        Double precoPago = acaoModel.getPrecoMedioCompra();

        Double lucro_prejuizo = ((precoAtual - precoPago) / precoPago) * 100.0;


        AcaoCompradaDTO acao = new AcaoCompradaDTO(
            acaoModel.getId(),
            acaoModel.getEmpresa().getSigla(),
            acaoModel.getEmpresa().getNome(),
            acaoModel.getQuantidade(),
            acaoModel.getPrecoMedioCompra(),
            precoPago,
            lucro_prejuizo

        );

       return acao;

    }

}
