package dev.project.VinewoodStreet.service;

import dev.project.VinewoodStreet.dto.mapper.AcaoMapper;
import dev.project.VinewoodStreet.dto.request.ComprarAcaoRequest;
import dev.project.VinewoodStreet.dto.request.VenderAcaoRequest;
import dev.project.VinewoodStreet.dto.response.AcaoCarteiraResponse;
import dev.project.VinewoodStreet.dto.response.AcaoCompradaDTO;
import dev.project.VinewoodStreet.models.AcaoModel;
import dev.project.VinewoodStreet.models.EmpresaModel;
import dev.project.VinewoodStreet.models.UserModel;
import dev.project.VinewoodStreet.repository.AcaoRepository;
import dev.project.VinewoodStreet.repository.EmpresaRepository;
import dev.project.VinewoodStreet.repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class AcaoService {

    AcaoRepository acaoRepo;
    UsuarioRepository userRepo;
    EmpresaRepository empreRepo;
    AcaoMapper acaoMapper;

    public AcaoService(AcaoRepository acaoRepo, UsuarioRepository userRepo, AcaoMapper acaoMapper) {
        this.acaoRepo = acaoRepo;
        this.userRepo = userRepo;
        this.acaoMapper = acaoMapper;
    }

    @Transactional(readOnly = true)
    public AcaoCarteiraResponse buscarCarteira(UserModel usuario) {

        UserModel usuarioModel = userRepo.findById(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado no sistema."));

        Double saldo = usuario.getSaldoCaixa();

        List<AcaoCompradaDTO> acoesDto = usuario.getAcoes().stream()
                .map(acao -> acaoMapper.toResponse(acao))
                .toList();

        return new AcaoCarteiraResponse(saldo, acoesDto);
    }

    @Transactional
    public void comprarAcao(UserModel usuarioLogado, ComprarAcaoRequest request) {

        EmpresaModel empresa = empreRepo.findBySigla(request.sigla())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada em Los Santos!"));

        Double custoTotal = empresa.getPrecoAtual() * request.quantidade();

        if (usuarioLogado.getSaldoCaixa() < custoTotal) {
            throw new RuntimeException("Saldo insuficiente!");
        }

        usuarioLogado.setSaldoCaixa(usuarioLogado.getSaldoCaixa() - custoTotal);

        AcaoModel novaAcao = new AcaoModel();
        novaAcao.setUsuario(usuarioLogado);
        novaAcao.setEmpresa(empresa);
        novaAcao.setQuantidade(request.quantidade());
        novaAcao.setPrecoMedioCompra(empresa.getPrecoAtual());

        userRepo.save(usuarioLogado);
        acaoRepo.save(novaAcao);
    }

    @Transactional
    public void venderAcao(UserModel usuarioLogado, VenderAcaoRequest request) {

        EmpresaModel empresa = empreRepo.findBySigla(request.sigla())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada!"));

        AcaoModel acaoDoUsuario = acaoRepo.findByUsuarioAndEmpresa(usuarioLogado, empresa)
                .orElseThrow(() -> new RuntimeException("Você não possui ações desta empresa para vender."));

        if (acaoDoUsuario.getQuantidade() < request.quantidade()) {
            throw new RuntimeException("Você está tentando vender mais ações do que possui!");
        }

        Double valorDaVenda = empresa.getPrecoAtual() * request.quantidade();

        usuarioLogado.setSaldoCaixa(usuarioLogado.getSaldoCaixa() + valorDaVenda);

        int novaQuantidade = acaoDoUsuario.getQuantidade() - request.quantidade();

        if (novaQuantidade == 0) {
            acaoRepo.delete(acaoDoUsuario);
        } else {
            acaoDoUsuario.setQuantidade(novaQuantidade);
            acaoRepo.save(acaoDoUsuario);
        }

        userRepo.save(usuarioLogado);
    }



}
