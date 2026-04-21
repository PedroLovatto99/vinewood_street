import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Auth() {
  const navigate = useNavigate();
  
  const [isLoginMode, setIsLoginMode] = useState(true);
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  
  const [loading, setLoading] = useState(false);
  const [erro, setErro] = useState('');
  const [sucesso, setSucesso] = useState('');

  const alternarModo = (e) => {
    e.preventDefault();
    setIsLoginMode(!isLoginMode);
    setErro('');
    setSucesso('');
    setSenha('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setErro('');
    setSucesso('');

    const url = isLoginMode 
      ? 'http://localhost:8080/auth/login' 
      : 'http://localhost:8080/auth/register';
      
    const payload = isLoginMode 
      ? { email, senha } 
      : { nome, email, senha };

    try {
      const resposta = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (resposta.ok) {
        if (isLoginMode) {
          const dados = await resposta.json();
          localStorage.setItem('vinewood_token', dados.token);
          navigate('/dashboard'); 
        } else {
          setSucesso('Conta aprovada! Você recebeu $10.000 de bônus inicial. Faça login abaixo.');
          setIsLoginMode(true);
          setSenha('');
        }
      } else {
        // A MUDANÇA ESTÁ AQUI: Vamos pegar o erro exato que o Java mandou!
        const erroBackend = await resposta.text();
        console.error("Erro do Spring Boot:", erroBackend);
        
        setErro(isLoginMode 
          ? "Acesso negado. E-mail ou palavra-passe incorretos." 
          : "Erro do Servidor: " + erroBackend); // Mostra o erro real na tela
      }
    } catch (error) {
      setErro("Servidor indisponível. A conexão com Vinewood falhou.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="d-flex align-items-center justify-content-center vh-100 bg-darker">
      <main className="form-signin w-100" style={{ maxWidth: '400px', padding: '1rem' }}>
        <div className="card shadow-lg border-success border-top border-4 bg-dark">
          <div className="card-body p-5 text-center">
            
            <h1 className="display-5 fw-bold text-success mb-1">BAWSAQ</h1>
            <p className="text-light mb-4">
              {isLoginMode ? "Terminal de Corretores" : "Registo de Novo Corretor"}
            </p>

            <form onSubmit={handleSubmit}>
              
              {erro && <div className="alert alert-danger small text-start">{erro}</div>}
              {sucesso && <div className="alert alert-success small text-start">{sucesso}</div>}

              {!isLoginMode && (
                <div className="mb-3 text-start">
                  <label className="form-label text-success fw-bold small">Nome Completo</label>
                  <input 
                    type="text" 
                    className="form-control text-light bg-dark border-secondary" 
                    placeholder="Ex: Trevor Philips"
                    value={nome}
                    onChange={(e) => setNome(e.target.value)}
                    required
                  />
                </div>
              )}

              <div className="mb-3 text-start">
                <label className="form-label text-success fw-bold small">E-mail Corporativo</label>
                <input 
                  type="email" 
                  className="form-control text-light bg-dark border-secondary" 
                  placeholder="trevor@lsmail.com" 
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>
              
              <div className="mb-4 text-start">
                <label className="form-label text-success fw-bold small">Palavra-passe</label>
                <input 
                  type="password" 
                  className="form-control text-light bg-dark border-secondary" 
                  placeholder="••••••••" 
                  value={senha}
                  onChange={(e) => setSenha(e.target.value)}
                  required
                />
              </div>

              <button className="btn btn-success w-100 py-2 fw-bold" type="submit" disabled={loading}>
                {loading ? 'Processando...' : (isLoginMode ? 'Acessar Sistema' : 'Criar Conta')}
              </button>
              
              <div className="mt-4">
                <a href="/" onClick={alternarModo} className="text-success text-decoration-none small fw-bold">
                  {isLoginMode ? "Não possui credenciais? Solicitar Acesso" : "Já possui uma conta? Fazer Login"}
                </a>
              </div>
              <div className="mt-3">
                <button 
                  type="button" 
                  onClick={() => navigate('/dashboard')} 
                  className="btn btn-outline-secondary w-100 py-2 fw-bold"
                >
                  Acessar como Visitante
                </button>
              </div>
              
            </form>
          </div>
        </div>
      </main>
    </div>
  );
}