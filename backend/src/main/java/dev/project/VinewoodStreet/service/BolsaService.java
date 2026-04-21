package dev.project.VinewoodStreet.service;

import dev.project.VinewoodStreet.IA.JornalistaIA;
import dev.project.VinewoodStreet.enums.TipoImpacto;
import dev.project.VinewoodStreet.models.EmpresaModel;
import dev.project.VinewoodStreet.models.EventoNoticiaModel;
import dev.project.VinewoodStreet.repository.EmpresaRepository;
import dev.project.VinewoodStreet.repository.EventoNoticiaRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class BolsaService {

    private final EmpresaRepository empresaRepo;
    private final EventoNoticiaRepository noticiaRepo;
    private final JornalistaIA jornalista;

    public BolsaService(EmpresaRepository empresaRepo, EventoNoticiaRepository noticiaRepo, JornalistaIA jornalista) {
        this.empresaRepo = empresaRepo;
        this.noticiaRepo = noticiaRepo;
        this.jornalista = jornalista;
    }

    @Transactional
    //@Scheduled(fixedRate = 600000) // 10 minutos
    @Scheduled(fixedRate = 60000) // 1 minuto
    @Caching(evict = {
            @CacheEvict(value = "lista_empresas", allEntries = true),
            @CacheEvict(value = "lista_noticias", allEntries = true)
    })
    public void gerarEventoAleatorio() {

        List<EmpresaModel> empresas = empresaRepo.findAll();
        EmpresaModel empresa = empresas.get(new Random().nextInt(empresas.size()));

        TipoImpacto impacto = TipoImpacto.values()[new Random().nextInt(TipoImpacto.values().length)];

        String noticiaBruta = jornalista.gerarNoticiaMaldosa(empresa.getNome(), impacto);
        String[] partes = noticiaBruta.split("\\|");

        double variacao = calcularPercentual(impacto);

        double novoPrecoBruto = empresa.getPrecoAtual() * (1 + (variacao / 100));

        double novoPreco = Math.round(novoPrecoBruto * 100.0) / 100.0;

        EventoNoticiaModel novaNoticia = new EventoNoticiaModel();
        novaNoticia.setEmpresa(empresa);
        novaNoticia.setTitulo(partes[0].trim());
        novaNoticia.setConteudoGerado(partes[1].trim());
        novaNoticia.setImpacto(impacto);
        novaNoticia.setPercentualVariacao(variacao);
        novaNoticia.setDataPublicacao(LocalDateTime.now());

        empresa.setPrecoAtual(novoPreco);

        empresaRepo.save(empresa);
        noticiaRepo.save(novaNoticia);
    }

    private double calcularPercentual(TipoImpacto impacto) {
        Random r = new Random();
        return switch (impacto) {
            case POSITIVO -> 2.0 + (10.0 * r.nextDouble());
            case NEGATIVO -> -12.0 + (10.0 * r.nextDouble());
            case NEUTRO -> -1.0 + (2.0 * r.nextDouble());
        };
    }
}
