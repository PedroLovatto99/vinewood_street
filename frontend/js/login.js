// js/login.js

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById('loginForm');
    const emailInput = document.getElementById('emailInput');
    const senhaInput = document.getElementById('senhaInput');
    const divErro = document.getElementById('mensagemErro');
    const btnEntrar = document.getElementById('btnEntrar');

    form.addEventListener('submit', async (evento) => {
        evento.preventDefault(); // Impede a página de recarregar

        // Reseta o estado visual
        divErro.classList.add('d-none');
        btnEntrar.disabled = true;
        btnEntrar.innerHTML = '<span class="spinner-border spinner-border-sm" aria-hidden="true"></span> Autenticando...';

        const loginData = {
            email: emailInput.value,
            senha: senhaInput.value
        };

        try {
            // Chama a API do Spring Boot rodando no Docker
            const resposta = await fetch('http://localhost:8080/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginData)
            });

            if (resposta.ok) {
                const dados = await resposta.json();
                
                // Salva o Token JWT no navegador
                localStorage.setItem('vinewood_token', dados.token);
                
                // Redireciona para o Painel Principal (que criaremos a seguir)
                window.location.href = 'dashboard.html';
            } else {
                // Se o Spring Boot retornar 403 Forbidden ou outro erro
                const textoErro = await resposta.text();
                mostrarErro(textoErro || "Credenciais inválidas. Tente novamente.");
            }
        } catch (erro) {
            console.error("Erro de conexão:", erro);
            mostrarErro("Servidor indisponível. A conexão com Los Santos falhou.");
        } finally {
            // Volta o botão ao estado original
            btnEntrar.disabled = false;
            btnEntrar.innerText = "Acessar Sistema";
        }
    });

    function mostrarErro(mensagem) {
        divErro.innerText = mensagem;
        divErro.classList.remove('d-none'); // Remove a classe do Bootstrap que esconde a div
    }
});