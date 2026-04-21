package dev.project.VinewoodStreet;

import dev.project.VinewoodStreet.dto.mapper.AcaoMapper;
import dev.project.VinewoodStreet.dto.request.ComprarAcaoRequest;
import dev.project.VinewoodStreet.dto.request.VenderAcaoRequest;
import dev.project.VinewoodStreet.dto.response.AcaoCarteiraResponse;
import dev.project.VinewoodStreet.models.AcaoModel;
import dev.project.VinewoodStreet.models.EmpresaModel;
import dev.project.VinewoodStreet.models.UserModel;
import dev.project.VinewoodStreet.repository.AcaoRepository;
import dev.project.VinewoodStreet.repository.EmpresaRepository;
import dev.project.VinewoodStreet.repository.UsuarioRepository;
import dev.project.VinewoodStreet.service.AcaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AcaoServiceTest {

    @Mock
    private AcaoRepository acaoRepo;

    @Mock
    private UsuarioRepository userRepo;

    @Mock
    private EmpresaRepository empreRepo;

    @Mock
    private AcaoMapper acaoMapper;

    @InjectMocks
    private AcaoService acaoService;


    @Test
    void buscarCarteiraComSucesso() {
        Long userId = 1L;
        UserModel user = new UserModel();
        user.setId(userId);
        user.setSaldoCaixa(5000.0);
        user.setAcoes(List.of());

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        AcaoCarteiraResponse resposta = acaoService.buscarCarteira(userId);

        assertNotNull(resposta);
        assertEquals(5000.0, resposta.saldoDisponivel());
    }


    @Test
    void comprarAcaoComSucesso() {
        Long userId = 1L;
        ComprarAcaoRequest request = new ComprarAcaoRequest("PIS", 10);

        UserModel user = new UserModel();
        user.setId(userId);
        user.setSaldoCaixa(1000.0);

        EmpresaModel empresa = new EmpresaModel();
        empresa.setSigla("PIS");
        empresa.setPrecoAtual(15.0);

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(empreRepo.findBySigla("PIS")).thenReturn(Optional.of(empresa));

        acaoService.comprarAcao(userId, request);

        assertEquals(850.0, user.getSaldoCaixa());

        verify(acaoRepo, times(1)).save(any(AcaoModel.class));
        verify(userRepo, times(1)).save(user);
    }

    @Test
    void erroAoComprarAcaoComSaldoInsuficiente() {
        Long userId = 1L;
        ComprarAcaoRequest request = new ComprarAcaoRequest("MAZ", 10);

        UserModel user = new UserModel();
        user.setSaldoCaixa(100.0);

        EmpresaModel empresa = new EmpresaModel();
        empresa.setSigla("MAZ");
        empresa.setPrecoAtual(250.0);

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(empreRepo.findBySigla("MAZ")).thenReturn(Optional.of(empresa));

        Exception exception = assertThrows(RuntimeException.class, () -> acaoService.comprarAcao(userId, request));

        assertEquals("Saldo insuficiente!", exception.getMessage());
        verify(acaoRepo, never()).save(any(AcaoModel.class));
    }


    @Test
    void venderAcaoParcialmenteComSucesso() {
        Long userId = 1L;
        VenderAcaoRequest request = new VenderAcaoRequest("PIS", 2);

        UserModel user = new UserModel();
        user.setSaldoCaixa(100.0);

        EmpresaModel empresa = new EmpresaModel();
        empresa.setSigla("PIS");
        empresa.setPrecoAtual(20.0);

        AcaoModel acaoComprada = new AcaoModel();
        acaoComprada.setQuantidade(5);
        acaoComprada.setEmpresa(empresa);
        acaoComprada.setUsuario(user);

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(empreRepo.findBySigla("PIS")).thenReturn(Optional.of(empresa));
        when(acaoRepo.findByUsuarioAndEmpresa(user, empresa)).thenReturn(Optional.of(acaoComprada));

        acaoService.venderAcao(userId, request);

        assertEquals(140.0, user.getSaldoCaixa());
        assertEquals(3, acaoComprada.getQuantidade());

        verify(acaoRepo, times(1)).save(acaoComprada);
        verify(acaoRepo, never()).delete(any());
    }

    @Test
    void venderTodasAsAcoesApagaO_Registo() {
        Long userId = 1L;
        VenderAcaoRequest request = new VenderAcaoRequest("PIS", 5);

        UserModel user = new UserModel(); user.setSaldoCaixa(100.0);
        EmpresaModel empresa = new EmpresaModel(); empresa.setSigla("PIS"); empresa.setPrecoAtual(20.0);

        AcaoModel acaoComprada = new AcaoModel();
        acaoComprada.setQuantidade(5);

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(empreRepo.findBySigla("PIS")).thenReturn(Optional.of(empresa));
        when(acaoRepo.findByUsuarioAndEmpresa(user, empresa)).thenReturn(Optional.of(acaoComprada));

        acaoService.venderAcao(userId, request);

        verify(acaoRepo, times(1)).delete(acaoComprada);
    }

    @Test
    void erroAoVenderMaisAcoesDoQuePossui() {
        Long userId = 1L;
        VenderAcaoRequest request = new VenderAcaoRequest("PIS", 10);

        UserModel user = new UserModel();
        EmpresaModel empresa = new EmpresaModel(); empresa.setSigla("PIS");

        AcaoModel acaoComprada = new AcaoModel();
        acaoComprada.setQuantidade(5);

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(empreRepo.findBySigla("PIS")).thenReturn(Optional.of(empresa));
        when(acaoRepo.findByUsuarioAndEmpresa(user, empresa)).thenReturn(Optional.of(acaoComprada));

        Exception exception = assertThrows(RuntimeException.class, () -> acaoService.venderAcao(userId, request));

        assertEquals("Você está tentando vender mais ações do que possui!", exception.getMessage());
        verify(userRepo, never()).save(any());
    }
}