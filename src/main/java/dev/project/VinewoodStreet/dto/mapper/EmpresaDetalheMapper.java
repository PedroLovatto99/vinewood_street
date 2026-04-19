package dev.project.VinewoodStreet.dto.mapper;

import dev.project.VinewoodStreet.dto.response.EmpresaDetalheDTO;
import dev.project.VinewoodStreet.dto.response.EventoNoticiaDTO;
import dev.project.VinewoodStreet.dto.response.HistoricoPrecoDTO;
import dev.project.VinewoodStreet.models.EmpresaModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmpresaDetalheMapper {

    public EmpresaDetalheDTO toResponse(EmpresaModel empresaDetalhe) {

        List<HistoricoPrecoDTO> graficoMapeado = empresaDetalhe.getHistorico().stream()
                .map(historico -> new HistoricoPrecoDTO(
                        historico.getPreco(),
                        historico.getDataHora()
                ))
                .toList();


        List<EventoNoticiaDTO> noticiasMapeadas = empresaDetalhe.getNoticias().stream()
                .map(noticia -> new EventoNoticiaDTO(
                        empresaDetalhe.getSigla(),
                        noticia.getTitulo(),
                        noticia.getConteudoGerado(),
                        noticia.getImpacto(),
                        noticia.getDataPublicacao()
                ))
                .toList();


        EmpresaDetalheDTO empresa = new EmpresaDetalheDTO(
             empresaDetalhe.getId(),
             empresaDetalhe.getNome(),
             empresaDetalhe.getSigla(),
             empresaDetalhe.getDescricao(),
             empresaDetalhe.getPrecoAtual(),
             graficoMapeado,
             noticiasMapeadas

        );

        return empresa;


    }


}
