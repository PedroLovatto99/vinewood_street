import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Tooltip
} from 'chart.js';
import { Line } from 'react-chartjs-2';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Tooltip);

export default function Dashboard() {
  const navigate = useNavigate();
  const token = localStorage.getItem('vinewood_token');
  const isLogado = !!token;

  const [abaAtiva, setAbaAtiva] = useState('mercado');
  const [saldo, setSaldo] = useState(null);
  const [mercado, setMercado] = useState([]);
  const [noticias, setNoticias] = useState([]);
  const [portfolio, setPortfolio] = useState([]);
  const [tickerText, setTickerText] = useState('A aguardar notícias...');
  
  const [empresaModal, setEmpresaModal] = useState(null);
  
  const empresaModalRef = useRef(null);
  useEffect(() => { empresaModalRef.current = empresaModal?.id; }, [empresaModal]);

  // ==========================================
  // BUSCAS NA API
  // ==========================================
  const carregarMercado = async () => {
    try {
      const res = await fetch('http://localhost:8080/empresas');
      const dados = await res.json();
      setMercado(dados.content);
    } catch (e) { console.error("Erro no mercado", e); }
  };

  const carregarNoticias = async () => {
    try {
      const res = await fetch('http://localhost:8080/noticias');
      const dados = await res.json();
      setNoticias(dados.content);
      if (dados.content.length > 0) {
        setTickerText(dados.content.map(n => `[${n.siglaEmpresa}] ${n.titulo}`).join('  ✦  '));
      }
    } catch (e) { console.error("Erro nas notícias", e); }
  };

  const carregarCarteira = async () => {
    if (!isLogado) return;
    try {
      const res = await fetch('http://localhost:8080/acoes', {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (res.ok) {
        const dados = await res.json();
        setSaldo(dados.saldoDisponivel);
        setPortfolio(dados.minhasAcoes);
      } else {
        deslogar();
      }
    } catch (e) { console.error("Erro na carteira", e); }
  };

  const carregarDetalhesEmpresa = async (id) => {
    try {
      const res = await fetch(`http://localhost:8080/empresas/${id}`);
      const dados = await res.json();
      setEmpresaModal(dados);
    } catch (e) { alert("Erro ao carregar detalhes."); }
  };

  // ==========================================
  // TEMPO REAL
  // ==========================================
  useEffect(() => {
    carregarMercado();
    carregarNoticias();
    carregarCarteira();

    const intervalo = setInterval(() => {
      carregarMercado();
      carregarNoticias();
      if (empresaModalRef.current) {
        carregarDetalhesEmpresa(empresaModalRef.current);
      }
    }, 10000); // Atualiza a cada 10s

    return () => clearInterval(intervalo);
  }, []);

  // ==========================================
  // LÓGICA DE NEGOCIAÇÃO
  // ==========================================
  const deslogar = () => {
    localStorage.removeItem('vinewood_token');
    navigate('/');
  };

  const trocarAba = (aba) => {
    if (aba === 'portfolio' && !isLogado) {
      alert("ACESSO NEGADO: Inicie sessão para ver o seu portfólio.");
      navigate('/');
      return;
    }
    setAbaAtiva(aba);
  };

  const comprarAcao = async () => {
    const qtdString = window.prompt(`Quantas ações da ${empresaModal.sigla} deseja comprar?`);
    if (!qtdString) return;
    const quantidade = parseInt(qtdString, 10);
    if (isNaN(quantidade) || quantidade <= 0) return alert("Quantidade inválida.");

    try {
      const res = await fetch('http://localhost:8080/acoes/comprar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
        body: JSON.stringify({ sigla: empresaModal.sigla, quantidade })
      });
      if (res.ok) {
        alert(`Compradas ${quantidade}x [${empresaModal.sigla}]!`);
        carregarCarteira();
        setEmpresaModal(null);
      } else {
        const erro = await res.text();
        try {
          const erroJson = JSON.parse(erro);
          alert("Negado: " + (erroJson.message || "Verifique o seu saldo."));
        } catch {
          alert("Negado: " + erro);
        }
      }
    } catch (e) { alert("Erro de conexão."); }
  };

  const venderAcao = async (sigla, maxQtd) => {
    const qtdString = window.prompt(`Tem ${maxQtd}x de [${sigla}]. Quantas deseja vender?`);
    if (!qtdString) return;
    const quantidade = parseInt(qtdString, 10);
    if (isNaN(quantidade) || quantidade <= 0 || quantidade > maxQtd) return alert("Quantidade inválida.");

    try {
      const res = await fetch('http://localhost:8080/acoes/vender', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
        body: JSON.stringify({ sigla, quantidade })
      });
      if (res.ok) {
        alert(`Venda de ${quantidade}x [${sigla}] realizada com sucesso!`);
        carregarCarteira();
      } else {
        const erro = await res.text();
        alert("Ação negada: " + erro);
      }
    } catch (e) { alert("Erro de conexão."); }
  };

  const gerarDadosGrafico = (historico, sigla) => {
    if (!historico) return null;
    const valores = historico.map(h => h.preco);
    const corLinha = (valores[valores.length - 1] || 0) >= (valores[0] || 0) ? '#198754' : '#dc3545';
    
    return {
      labels: historico.map(h => {
        const d = new Date(h.dataHora);
        return `${d.getHours()}:${d.getMinutes().toString().padStart(2, '0')}`;
      }),
      datasets: [{
        label: sigla,
        data: valores,
        borderColor: corLinha,
        borderWidth: 2,
        pointRadius: 1,
        tension: 0.1
      }]
    };
  };

  return (
    <div className="bg-darker min-vh-100 pb-5">
      
      {/* CABEÇALHO */}
      <header className="p-3 shadow-sm" style={{ backgroundColor: '#7b9e57' }}>
        <div className="container d-flex justify-content-between align-items-center">
          <div>
            <h1 className="display-6 fw-bold text-white mb-0">VINEWOOD STREET</h1>
            <p className="text-dark fw-bold mb-0 small">LIDANDO COM OS ALTOS E BAIXOS DA VIDA</p>
          </div>
          
<div className="p-2 rounded text-end" style={{ backgroundColor: '#141414', minWidth: '150px' }}>
            {isLogado ? (
              <>
                {/* Alterado aqui: cor cinza clara fixa para não sumir no fundo preto */}
                <span className="small d-block fw-bold" style={{ color: '#9ca3af' }}>Meu Saldo</span>
                <span className="fs-4 fw-bold text-white">${saldo !== null ? saldo.toFixed(2) : '...'}</span>
                <button onClick={deslogar} className="btn btn-sm btn-outline-danger d-block w-100 mt-2">Sair</button>
              </>
            ) : (
              <>
                <span className="fs-5 fw-bold text-white d-block mb-2">Acesso Visitante</span>
                <button onClick={() => navigate('/')} className="btn btn-sm btn-outline-success w-100">Fazer Login</button>
              </>
            )}
          </div>
        </div>
      </header>

      {/* TICKER */}
      <div className="bg-black py-1 overflow-hidden" style={{ color: '#7b9e57' }}>
        <marquee behavior="scroll" direction="left">{tickerText}</marquee>
      </div>

      <main className="container my-4">
        {/* ABAS */}
<ul className="nav nav-tabs mb-0" style={{ borderBottomColor: '#7b9e57' }}>
          {['mercado', 'noticias', 'portfolio'].map(aba => (
            <li className="nav-item" key={aba}>
              <button 
                onClick={() => trocarAba(aba)}
                className="nav-link border-0 text-capitalize fw-bold"
                style={{ 
                  color: abaAtiva === aba ? '#ffffff' : '#9ca3af', // Branco se ativo, cinza claro se inativo
                  backgroundColor: abaAtiva === aba ? '#242424' : '#141414',
                  borderTop: abaAtiva === aba ? '2px solid #7b9e57' : 'none',
                  borderRadius: '4px 4px 0 0'
                }}
              >
                {aba === 'noticias' ? 'Notícias' : aba}
              </button>
            </li>
          ))}
        </ul>

        <div className="p-4 rounded-bottom shadow" style={{ backgroundColor: '#242424', border: '1px solid #7b9e57', borderTop: 'none' }}>
          
          {/* TABELA DE MERCADO */}
          {abaAtiva === 'mercado' && (
            <div className="table-responsive">
              <table className="table table-dark table-hover align-middle mb-0">
                <thead>
                  <tr className="text-muted border-bottom border-secondary">
                    <th>Sigla</th>
                    <th>Empresa</th>
                    <th className="text-end">Preço Atual</th>
                    <th className="text-end">Tendência (3h)</th>
                  </tr>
                </thead>
                <tbody style={{ cursor: 'pointer' }}>
                  {mercado.length === 0 ? (
                    <tr><td colSpan="4" className="text-center py-4">Carregando mercado...</td></tr>
                  ) : mercado.map(emp => (
                    <tr key={emp.id} onClick={() => carregarDetalhesEmpresa(emp.id)}>
                      <td className="fw-bold">{emp.sigla}</td>
                      <td>{emp.nome}</td>
                      <td className="text-end">${emp.precoAtual.toFixed(2)}</td>
                      <td className={`text-end fw-bold ${emp.variacaoUltimas24h >= 0 ? 'text-success' : 'text-danger'}`}>
                        {emp.variacaoUltimas24h.toFixed(2)}% {emp.variacaoUltimas24h >= 0 ? '▲' : '▼'}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          {/* NOTÍCIAS */}
          {abaAtiva === 'noticias' && (
            <div>
              <h4 className="mb-4" style={{ color: '#7b9e57' }}>Notícias de Los Santos</h4>
              {noticias.length === 0 ? <p className="text-muted">A aguardar notícias...</p> : noticias.map(n => (
                <div key={n.id} className="mb-4 border-bottom border-secondary pb-3">
                  <span className={`badge me-2 ${n.impacto === 'POSITIVO' ? 'bg-success' : n.impacto === 'NEGATIVO' ? 'bg-danger' : 'bg-secondary'}`}>
                    {n.impacto}
                  </span>
                  <span className="fw-bold fs-5" style={{ color: '#7b9e57' }}>{n.siglaEmpresa}</span> 
                  <span className="fw-bold text-white fs-5"> - {n.titulo}</span>
                  <p className="text-light mt-2 mb-0 fst-italic" style={{ opacity: 0.8 }}>"{n.conteudo}"</p>
                  <small className="text-secondary">{new Date(n.dataPublicacao).toLocaleString()}</small>
                </div>
              ))}
            </div>
          )}

          {/* PORTFÓLIO */}
          {abaAtiva === 'portfolio' && (
            <div className="table-responsive">
              <table className="table table-dark table-hover align-middle mb-0">
                <thead>
                  <tr className="text-muted border-bottom border-secondary">
                    <th>Ativo</th>
                    <th>Empresa</th>
                    <th className="text-end">Qtd</th>
                    <th className="text-end">Médio Pago</th>
                    <th className="text-end">Atual</th>
                    <th className="text-end">Lucro/Prej</th>
                    <th className="text-end">Ação</th>
                  </tr>
                </thead>
                <tbody>
                        {portfolio.length === 0 ? (
                        <tr><td colSpan="7" className="text-center py-4">Carteira vazia</td></tr>
                        ) : portfolio.map(acao => (
                        <tr key={acao.sigla}>
                            <td className="fw-bold" style={{ color: '#7b9e57' }}>{acao.sigla}</td>
                            <td>{acao.nomeEmpresa}</td>
                            <td className="text-end">{acao.quantidade}</td>
                            
                            {/* ALTERAÇÃO AQUI: de text-muted para style fixo */}
                            <td className="text-end" style={{ color: '#9ca3af' }}>
                            ${acao.precoMedioCompra.toFixed(2)}
                            </td>
                            
                            <td className="text-end fw-bold text-white">${acao.precoAtualMercado.toFixed(2)}</td>
                            <td className={`text-end fw-bold ${acao.lucroPrejuizoPercentual >= 0 ? 'text-success' : 'text-danger'}`}>
                            {acao.lucroPrejuizoPercentual.toFixed(2)}% {acao.lucroPrejuizoPercentual >= 0 ? '▲' : '▼'}
                            </td>
                            <td className="text-end">
                            <button onClick={() => venderAcao(acao.sigla, acao.quantidade)} className="btn btn-sm btn-danger fw-bold py-0">VENDER</button>
                            </td>
                        </tr>
                        ))}
                    </tbody>
              </table>
            </div>
          )}
        </div>
      </main>

      {/* MODAL */}
      {empresaModal && (
        <div className="modal show d-block" style={{ backgroundColor: 'rgba(0,0,0,0.8)' }}>
          <div className="modal-dialog modal-lg modal-dialog-centered">
            <div className="modal-content border-success" style={{ backgroundColor: '#242424' }}>
              <div className="modal-header border-secondary">
                <h5 className="modal-title fw-bold text-white">{empresaModal.nome} ({empresaModal.sigla})</h5>
                <button type="button" className="btn-close btn-close-white" onClick={() => setEmpresaModal(null)}></button>
              </div>
              <div className="modal-body">
                <div className="row">
                  <div className="col-md-8">
                    <p className="text-muted small mb-2">{empresaModal.descricao}</p>
                    <div className="p-2 rounded" style={{ backgroundColor: '#141414' }}>
                      {empresaModal.grafico && (
                        <Line 
                          data={gerarDadosGrafico(empresaModal.grafico, empresaModal.sigla)} 
                          options={{ responsive: true, animation: false, plugins: { legend: { display: false } } }} 
                        />
                      )}
                    </div>
                  </div>
                  
                  <div className="col-md-4 d-flex flex-column justify-content-center">
                    <div className="p-3 rounded mb-3 text-center border border-secondary" style={{ backgroundColor: '#141414' }}>
                    {/* Alterado de text-muted para style fixo */}
                    <span className="d-block small fw-bold" style={{ color: '#9ca3af' }}>Preço Atual</span>
                    <span className="fs-2 fw-bold text-white">${empresaModal.precoAtual.toFixed(2)}</span>
                    </div>
                    {isLogado ? (
                      <button onClick={comprarAcao} className="btn btn-success btn-lg fw-bold w-100">COMPRAR</button>
                    ) : (
                      <div className="alert alert-warning small text-center p-2 mb-0">Inicie sessão para negociar.</div>
                    )}
                  </div>
                </div>

                {empresaModal.noticiasRecentes && empresaModal.noticiasRecentes.length > 0 && (
                  <div className="mt-3 bg-dark p-2 rounded border border-secondary">
                    <span className={`badge mb-1 ${empresaModal.noticiasRecentes[0].impacto === 'POSITIVO' ? 'bg-success' : 'bg-danger'}`}>
                      {empresaModal.noticiasRecentes[0].impacto}
                    </span>
                    <p className="mb-0 small fst-italic text-light" style={{ opacity: 0.9 }}>{empresaModal.noticiasRecentes[0].titulo}</p>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}