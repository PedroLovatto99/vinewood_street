package dev.project.VinewoodStreet.utils;

import dev.project.VinewoodStreet.models.EmpresaModel;
import dev.project.VinewoodStreet.models.HistoricoPrecoModel;

import java.time.LocalDateTime;
import java.util.Comparator;


public class FuncoesAuxiliares {

    public static Double calcularVariacao24h(EmpresaModel empresa) {

        if (empresa.getHistorico() == null || empresa.getHistorico().isEmpty()) {
            return 0.0;
        }

        LocalDateTime exatas24hAtras = LocalDateTime.now().minusHours(24);

        Double precoAntigo = empresa.getHistorico().stream()
                .filter(h -> h.getDataHora().isAfter(exatas24hAtras))
                .min(Comparator.comparing(HistoricoPrecoModel::getDataHora))
                .map(HistoricoPrecoModel::getPreco)
                .orElse(empresa.getPrecoAtual());

        if (precoAntigo == 0.0) {
            return 0.0;
        }

        return ((empresa.getPrecoAtual() - precoAntigo) / precoAntigo) * 100.0;
    }



}
