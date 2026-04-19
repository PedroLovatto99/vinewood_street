package dev.project.VinewoodStreet.dto.mapper;

import dev.project.VinewoodStreet.dto.response.HistoricoPrecoDTO;
import dev.project.VinewoodStreet.models.HistoricoPrecoModel;
import org.springframework.stereotype.Component;

@Component
public class HistoricoPrecoMapper {

    public HistoricoPrecoDTO toResponse(HistoricoPrecoModel precoPontoModel) {

        HistoricoPrecoDTO precoPonto = new HistoricoPrecoDTO(

                precoPontoModel.getPreco(),
                precoPontoModel.getDataHora()
        );

        return precoPonto;

    }




}
