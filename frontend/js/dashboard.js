document.addEventListener("DOMContentLoaded", () => {
    // Referências DOM
    const tabelaBody = document.getElementById('tabelaEmpresas');
    const ticker = document.getElementById('tickerNoticias');
    const saldoUsuario = document.getElementById('saldoUsuario');
    const btnLogin = document.getElementById('btnLogin');
    const btnComprar = document.getElementById('btnComprar');
    const avisoLogin = document.getElementById('avisoLogin');
    
    // Inicializar o Modal do Bootstrap
    const modalElement = document.getElementById('modalEmpresa');
    const modalBS = new bootstrap.Modal(modalElement);
    
    // Variável para guardar o gráfico e evitar sobreposição
    let chartInstancia = null;

    // 1. Verificação de Autenticação
    const token = localStorage.getItem('vinewood_token');
    const isLogado = !!token;

    // Configuração inicial da Tela
    if (isLogado) {
        carregarCarteira();
        btnComprar.classList.remove('d-none');
    } else {
        saldoUsuario.classList.add('d-none');
        document.querySelector('#areaUsuario span.text-muted').classList.add('d-none');
        btnLogin.classList.remove('d-none');
        avisoLogin.classList.remove('d-none');
    }

    // 2. Carregamentos Iniciais
    carregarMercado();
    carregarNoticiasTicker();

    // ==========================================
    // FUNÇÕES DE BUSCA NA API
    // ==========================================

    async function carregarCarteira() {
        try {
            const resposta = await fetch('http://localhost:8080/acoes', {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (resposta.ok) {
                const dados = await resposta.json();
                saldoUsuario.innerText = `$${dados.saldoDisponivel.toFixed(2)}`;
            } else {
                // Se der 403, o token expirou
                localStorage.removeItem('vinewood_token');
                window.location.reload();
            }
        } catch (erro) {
            console.error("Erro ao carregar carteira:", erro);
        }
    }

    async function carregarMercado() {
        try {
            const resposta = await fetch('http://localhost:8080/empresas');
            const dados = await resposta.json();
            
            tabelaBody.innerHTML = ''; // Limpa a tabela
            
            dados.content.forEach(empresa => {
                // Define a cor e a seta baseado na variação
                const isPositivo = empresa.variacaoUltimas24h >= 0;
                const corTexto = isPositivo ? 'text-success' : 'text-danger';
                const icone = isPositivo ? '▲' : '▼';
                
                const tr = document.createElement('tr');
                tr.onclick = () => abrirDetalhesEmpresa(empresa.id);
                
                tr.innerHTML = `
                    <td class="fw-bold">${empresa.sigla}</td>
                    <td>${empresa.nome}</td>
                    <td class="text-end">$${empresa.precoAtual.toFixed(2)}</td>
                    <td class="text-end ${corTexto} fw-bold">
                        ${empresa.variacaoUltimas24h.toFixed(2)}% ${icone}
                    </td>
                `;
                tabelaBody.appendChild(tr);
            });
        } catch (erro) {
            tabelaBody.innerHTML = '<tr><td colspan="4" class="text-center text-danger">A BAWSAQ está fora do ar.</td></tr>';
        }
    }

    async function carregarNoticiasTicker() {
        try {
            const resposta = await fetch('http://localhost:8080/noticias');
            const dados = await resposta.json();
            
            if (dados.content.length > 0) {
                const manchetes = dados.content.map(n => `[${n.siglaEmpresa}] ${n.titulo}`).join('  ✦  ');
                ticker.innerText = manchetes;
            }
        } catch (e) {
            console.log("Erro ao carregar ticker de notícias.");
        }
    }

    async function abrirDetalhesEmpresa(id) {
        try {
            const resposta = await fetch(`http://localhost:8080/empresas/${id}`);
            const empresa = await resposta.json();
            
            // Preenche os dados do Modal
            document.getElementById('modalNomeEmpresa').innerText = `${empresa.nome} (${empresa.sigla})`;
            document.getElementById('modalDescricao').innerText = empresa.descricao;
            document.getElementById('modalPrecoAtual').innerText = `$${empresa.precoAtual.toFixed(2)}`;
            
            // Pega a última notícia da IA, se existir
            if (empresa.noticiasRecentes && empresa.noticiasRecentes.length > 0) {
                const ultimaNoticia = empresa.noticiasRecentes[0];
                document.getElementById('noticiaEmpresa').innerText = ultimaNoticia.titulo;
                const badge = document.getElementById('badgeImpacto');
                badge.innerText = ultimaNoticia.impacto;
                badge.className = `badge mb-1 ${ultimaNoticia.impacto === 'POSITIVO' ? 'bg-success' : 'bg-danger'}`;
            } else {
                document.getElementById('noticiaEmpresa').innerText = "Nenhum escândalo recente.";
                document.getElementById('badgeImpacto').className = 'badge bg-secondary mb-1';
                document.getElementById('badgeImpacto').innerText = "NEUTRO";
            }

            // Renderiza o gráfico
            renderizarGrafico(empresa.grafico, empresa.sigla);
            
            // Abre o Modal do Bootstrap
            modalBS.show();
            
        } catch (erro) {
            alert("Erro ao buscar detalhes da empresa.");
        }
    }

    // ==========================================
    // LÓGICA DO GRÁFICO (Chart.js)
    // ==========================================
    function renderizarGrafico(dadosHistorico, sigla) {
        const ctx = document.getElementById('graficoAcao').getContext('2d');
        
        // Regra de Ouro do Chart.js: Destruir o gráfico antigo antes de desenhar um novo por cima
        if (chartInstancia) {
            chartInstancia.destroy();
        }

        // Extrai os valores e as datas do JSON que o Spring Boot mandou
        const labels = dadosHistorico.map(h => {
            const data = new Date(h.dataHora);
            return `${data.getHours()}:${data.getMinutes().toString().padStart(2, '0')}`;
        });
        const valores = dadosHistorico.map(h => h.preco);

        // Verifica a tendência para pintar a linha de verde ou vermelho
        const valorInicial = valores[0] || 0;
        const valorFinal = valores[valores.length - 1] || 0;
        const corLinha = valorFinal >= valorInicial ? '#198754' : '#dc3545'; // Bootstrap success ou danger

        chartInstancia = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: `Preço ${sigla}`,
                    data: valores,
                    borderColor: corLinha,
                    backgroundColor: 'rgba(0,0,0,0)', // Transparente
                    borderWidth: 2,
                    pointRadius: 1, // Pontos bem pequenos
                    tension: 0.1 // Curvatura suave
                }]
            },
            options: {
                responsive: true,
                plugins: { legend: { display: false } },
                scales: {
                    x: { ticks: { color: '#888' }, grid: { color: '#333' } },
                    y: { ticks: { color: '#888' }, grid: { color: '#333' } }
                }
            }
        });
    }
});