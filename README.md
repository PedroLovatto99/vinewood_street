# 📈 Vinewood Street - Bolsa de valores
Um simulador Full-Stack da bolsa de valores de Los Santos. Você compra e vende ações (como Pißwasser, Ammu-Nation e Maze Bank), acompanha os gráficos em tempo real e vê o mercado reagir a notícias geradas por Inteligência Artificial.

---

### 💻 Stack do Projeto

**Front-End:** React.js, React Router, Bootstrap 5 & Chart.js  
**Back-End:** Java 21 & Spring Boot 3  
**Inteligência Artificial:** LangChain4j e Google Gemini API  
**Persistência/ORM:** Spring Data JPA / Hibernate  
**Banco de Dados Relacional:** PostgreSQL  
**Banco de Dados Vetorial:** ChromaDB (Para memória da IA)  
**Cache:** Redis  
**Segurança:** Spring Security & JWT  
**Versionamento de Banco:** Flyway  
**Documentação da API:** Swagger / OpenAPI  
**Infraestrutura:** Docker
**Build Tool:** Maven  

---

### O que o sistema faz?
* **Mercado Vivo:** As cotações flutuam automaticamente a cada 1 minuto via Spring Boot.
* **Corretora Completa:** Sistema de login (JWT), saldo inicial de $10.000, compra, venda e cálculo de lucro/prejuízo.
* **Gráficos (Chart.js):** Histórico de preços das últimas 3 horas com indicadores de tendência.
* **Modo Visitante:** Acesso liberado para ver o mercado e as notícias, com bloqueio apenas de operações financeiras.
* **Full Docker:** Front-end, Back-end e Bancos de Dados sobem juntos com um único comando.

---

### Inteligência Artificial e RAG

A cada 1 minuto, a inteligência artificial, por meio do Gemini e do contexto via RAG, cria uma notícia que pode ser positiva, neutra ou negativa sobre uma empresa, impactando o valor das ações.

---

### Requisitos

* **Docker** e **Docker Compose**.
* Uma chave de API do **Google Gemini** (disponível no Google AI Studio).

---

### Como Rodar (Modo Rápido)

**1. Clonar o repositório e entrar na pasta gerada**

```bash
git clone https://github.com/PedroLovatto99/vinewood_street.git
cd sintonia-wasteland
```

**2. Configurar variáveis**

Crie um arquivo `.env` na raiz do projeto (use o `.env.example` como base) e insira sua chave do Gemini:
```env
GEMINI_API_KEY=sua_chave_aqui
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DB=vinewood_db
```
**3. Subir o sistema**

Na raiz do projeto, rode o comando:
```env
docker-compose up --build
```
**4. Acessar**

Após o Docker finalizar o build, abra o navegador em:
```env
http://localhost:3000
```
---

## Documentação da API (Swagger)
Visualize e teste todos os endpoints em tempo real acessando:

http://localhost:8080/swagger-ui/index.html
