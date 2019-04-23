# Aplicação Servidor ![SpringBoot 2.1.4](https://img.shields.io/badge/springboot-2.1.4-green.svg) ![PostgreeSQL 11.4](https://img.shields.io/badge/postgreSQL-2.1.4-green.svg)
Essa aplicação foi desenvolvida utilizando o framework SpringBoot 2.1.4. Seu objetivo é fornecer um backend capaz de atender requisições via API, alimentando assim a aplicação "client" contida também nesse repositório.

Ela fornece as seguintes funcionalidades:

    1. API:
        - Endpoint para cadastro de novas Prssoas -> POST /api/pessoas;
        - Atualização dos dados de uma determinada Pessoa -> PUT /api/pessoas/{pessoaCpf} ;
        - Remoção dos dados de uma determinada Pessoa -> DELETE /api/pessoas/{pessoaCpf} ;
        - Leitura de todas as Pessoas cadastradas -> GET /api/pessoas/;
    
 Para maiores informações da API acesse: http://localhost:8080/swagger-ui.html
 
## Modelo de Dados

Um objeto da Classe Pesso é composto de:
    
- **cpf:** String com 11 caracteres | Obrigatório | Chave primária
- **nome:** String com 60 caracteres | Obrigatório
- **uf:** String com 2 caracteres | Obrigatório
- **dataNasc:** Date | Opcional
- **peso**: Float 3.2 | Opcional
 
## Execução da Aplicação

### Banco de Dados

Essa aplicação persiste os dados em PostgreSQL 11.2, é necessário:

    1. Alterar as configurações do arquivo "resources/aplication.properties":
        - Configurar a porta do banco e a base de dados que será utilizada (a aplicação está utilizando a base GT4W);
        - Configurar o usuário e senha para acesso ao banco.

### Executando a aplicação

```sh
$ cd server
$ mvn spring-boot:run
```