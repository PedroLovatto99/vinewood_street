package dev.project.VinewoodStreet.dto.response;

import java.util.List;

public record EmpresaDetalheDTO(
        Long id,
        String nome,
        String sigla,
        String descricao,
        Double precoAtual,
        List<HistoricoPrecoDTO> grafico,
        List<EventoNoticiaDTO> noticiasRecentes

) {
}
