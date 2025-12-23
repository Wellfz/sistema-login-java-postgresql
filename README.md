# Sistema de Login em Tempo Real – Java + PostgreSQL

Projeto desenvolvido para a disciplina de **Programação Orientada a Objetos II**.

## 📌 Descrição
Este projeto consiste em um sistema **cliente-servidor** desenvolvido em Java, integrado a um banco de dados **PostgreSQL**.

O usuário informa **nome e e-mail** em uma interface cliente.  
Esses dados são persistidos no banco de dados e **sincronizados em tempo real** entre todas as janelas de usuários conectados ao sistema.

Sempre que um usuário é **inserido, alterado ou removido**, a lista de usuários é atualizada automaticamente em todos os clientes.

---

## 🛠 Tecnologias Utilizadas
- Java
- Sockets (Cliente-Servidor)
- Threads (`Runnable`)
- JDBC
- PostgreSQL
- IntelliJ IDEA
- Maven

---

## 🧱 Estrutura do Projeto
- **cliente**: Interface do usuário e comunicação com o servidor
- **servidor**: Gerenciamento de conexões e regras de negócio
- **enti**: Classes de entidade (modelo)
- **dao**: Camada de acesso a dados (DAO)
- **ConexaoDAO**: Gerenciamento da conexão com o banco de dados

---

## 🔄 Funcionalidades
- Inserir usuário (nome e e-mail)
- Atualizar dados do usuário
- Excluir usuário
- Persistência dos dados no PostgreSQL
- Comunicação cliente-servidor via sockets
- Sincronização em tempo real entre múltiplos clientes
- Suporte a múltiplas conexões simultâneas

---

## 🚀 Como Executar o Projeto

### 1️⃣ Banco de Dados
- Crie um banco de dados no PostgreSQL
- Crie a tabela de usuários conforme a estrutura esperada pelo sistema
- Configure as credenciais do banco na classe de conexão

⚠️ **As credenciais do banco não estão versionadas por segurança**

---

### 2️⃣ Executar o Servidor
- Execute a classe principal do servidor
- O servidor ficará aguardando conexões de clientes

---

### 3️⃣ Executar o Cliente
- Execute a aplicação cliente
- É possível abrir múltiplas janelas de cliente simultaneamente
- As alterações serão refletidas em tempo real

---

## 📚 Conceitos Aplicados
- Programação Orientada a Objetos
- Arquitetura Cliente-Servidor
- Comunicação em Rede
- Concorrência com Threads
- Padrão DAO
- Persistência de Dados

---

## 👨‍🎓 Contexto Acadêmico
Projeto desenvolvido com fins educacionais para aplicação prática dos conceitos vistos em sala de aula na disciplina de **POO II**.

---

## 📄 Observações
- O projeto foi desenvolvido para fins acadêmicos
- Melhorias futuras podem incluir autenticação com senha e interface mais robusta
