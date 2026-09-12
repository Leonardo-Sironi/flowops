# FlowOps

O **FlowOps** é uma plataforma de automação de processos operacionais e gerenciamento de workflows, criada com o objetivo de reduzir trabalho manual, melhorar a visibilidade das tarefas e automatizar fluxos internos de empresas.

> O código, nomes de classes, métodos, variáveis, commits, documentação técnica e futuras estruturas de API e banco de dados são desenvolvidos em inglês para aumentar a familiaridade com o inglês técnico utilizado no desenvolvimento de software.

---

## Objetivo do projeto

Muitas empresas ainda organizam processos operacionais usando:

- planilhas;
- e-mails;
- aplicativos de mensagens;
- acompanhamento manual;
- sistemas separados.

Isso pode gerar problemas como:

- tarefas esquecidas;
- atrasos;
- retrabalho;
- falta de visibilidade;
- dificuldade para acompanhar produtividade;
- dificuldade para identificar gargalos;
- processos lentos e pouco padronizados.

O FlowOps nasce com a proposta de centralizar e, progressivamente, automatizar esses processos.

---

## Fluxo principal

Um fluxo do sistema pode funcionar assim:

Solicitação recebida  
→ Tarefa criada  
→ Funcionário responsável atribuído  
→ Tarefa iniciada  
→ Prazo acompanhado  
→ Tarefa concluída  
→ Revisão do gerente  
→ Aprovada ou rejeitada

---

## Estado atual do projeto

A primeira versão foi desenvolvida em **Java puro**, com foco em revisar e consolidar conceitos de **Programação Orientada a Objetos** antes da evolução para Spring Boot.

Atualmente o projeto já possui:

- criação de tarefas;
- atribuição de funcionários;
- controle de status;
- prioridades;
- controle de prazo;
- detecção de tarefas atrasadas;
- fluxo de revisão por gerente;
- aprovação e rejeição de tarefas;
- filtros por status;
- filtros por prioridade;
- métricas básicas;
- exceptions personalizadas;
- abstração de notificações;
- simulação de notificação por e-mail;
- gerenciamento de múltiplas tarefas com collections.

---

## Conceitos de POO aplicados

Nesta primeira etapa foram utilizados conceitos como:

- Classes e objetos
- Atributos e métodos
- Construtores
- Encapsulamento
- Herança
- Polimorfismo
- Interfaces
- Enums
- Collections
- Exceptions
- Relacionamento entre objetos
- Separação de responsabilidades

---

## Estrutura atual

flowops  
├── domain  
│   ├── Task  
│   ├── Employee  
│   ├── Manager  
│   ├── Status  
│   ├── Priority  
│   └── RevisionStatus  
│  
├── service  
│   ├── TaskService  
│   ├── TaskManager  
│   ├── NotificationService  
│   └── EmailNotificationService  
│  
├── exception  
│   └── InvalidTaskOperationException  
│  
└── Main

---

## Tecnologias atuais

- Java
- Programação Orientada a Objetos
- Java Collections
- Java Time API
- Git
- GitHub

---

## Roadmap

O FlowOps será evoluído gradualmente.

A ideia é adicionar novas tecnologias conforme o projeto realmente precisar delas.

Java + POO  
→ Spring Boot  
→ REST API  
→ PostgreSQL + SQL  
→ Spring Data JPA + Hibernate  
→ DTOs + Validation  
→ Global Exception Handling  
→ Swagger / OpenAPI  
→ JUnit + Mockito  
→ Spring Security + JWT  
→ Flyway  
→ Docker + Docker Compose  
→ RabbitMQ  
→ GitHub Actions  
→ Deploy / Cloud  
→ Performance + Observability

---

## Tecnologias previstas

Ao longo da evolução do projeto, pretendo trabalhar com:

- Java
- Spring Boot
- Spring Web
- REST
- PostgreSQL
- SQL
- Spring Data JPA
- Hibernate
- DTOs
- Validation
- Swagger / OpenAPI
- JUnit 5
- Mockito
- Testcontainers
- Spring Security
- JWT
- Flyway
- Docker
- Docker Compose
- RabbitMQ
- GitHub Actions
- Cloud
- Performance testing
- Observability

---

## Estratégia de aprendizado

O FlowOps também está sendo utilizado como projeto prático de revisão e evolução técnica.

A abordagem é:

**Aprender → Aplicar → Construir → Testar → Melhorar → Evoluir**

A ideia é evitar estudar tecnologias isoladamente sem aplicação prática.

Cada conceito aprendido é aplicado diretamente no projeto.

---

## Objetivo profissional

O projeto faz parte da minha preparação para conquistar minha primeira oportunidade profissional como:

- Desenvolvedor Java Júnior
- Desenvolvedor Backend Júnior
- Estagiário Java
- Estagiário Backend
- Desenvolvedor de Software

O objetivo final é evoluir o FlowOps para uma aplicação backend completa, capaz de demonstrar conhecimento prático em:

- Java
- APIs REST
- banco de dados
- arquitetura
- testes
- segurança
- mensageria
- containers
- CI/CD
- performance
- desenvolvimento backend

Mais do que apenas utilizar tecnologias, a meta é entender e conseguir explicar as decisões técnicas utilizadas durante o desenvolvimento.

---

## Status

🚧 Projeto em desenvolvimento.

A primeira etapa em **Java + POO** está concluída e o próximo passo será iniciar a evolução do FlowOps utilizando **Spring Boot**.