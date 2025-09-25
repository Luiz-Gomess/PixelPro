# PixelPro - Processador de Imagens Assíncrono

![Java](https://img.shields.io/badge/Java-21-blue) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.5-brightgreen) ![RabbitMQ](https://img.shields.io/badge/RabbitMQ-blueviolet) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue) ![MinIO](https://img.shields.io/badge/MinIO-orange) ![Docker](https://img.shields.io/badge/Docker-blue)

PixelPro é um sistema de processamento de imagens em background, construído com uma arquitetura de microsserviços desacoplada. Ele permite que usuários enviem imagens e solicitem transformações de forma assíncrona, garantindo que a API permaneça sempre responsiva, mesmo durante operações pesadas.

## Conceitos e Tecnologias

O projeto foi desenvolvido para aplicar conceitos avançados de desenvolvimento de software em um cenário prático. As principais tecnologias utilizadas são:

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3
* **Mensageria:** RabbitMQ para comunicação assíncrona entre os componentes.
* **Banco de Dados:** PostgreSQL para persistência dos metadados das tarefas (Jobs).
* **Armazenamento de Objetos:** MinIO (compatível com S3) para armazenar as imagens originais e processadas de forma escalável.
* **Processamento de Imagem:** Biblioteca JavaCV (OpenCV) para manipulação das imagens.
* **Containerização:** Docker e Docker Compose para orquestrar e gerenciar todo o ambiente de desenvolvimento e produção.

## Funcionalidades Atuais

* **Upload e Processamento Assíncrono:** A API recebe uma imagem e uma operação, faz o upload para o MinIO e enfileira a tarefa para um worker, retornando uma resposta `202 Accepted` imediatamente.
* **Operações de Imagem Extensíveis:** Utiliza o Padrão de Projeto **Strategy** para encapsular os algoritmos de processamento. Adicionar novas operações é simples e não exige alterações no worker principal. Operações suportadas no momento:
    * `GRAYSCALE` (Tons de Cinza)
    * `SEPIA` (Filtro Sépia)
    * `INVERT` (Inversão de Cores)
    * `BLUR` (Desfoque)
* **Armazenamento Escalável:** As imagens não são salvas no banco de dados. Elas são armazenadas no MinIO, e o banco de dados contém apenas metadados e o ID da imagem, garantindo performance e escalabilidade.
* **Resiliência com Dead Letter Queue (DLQ):** Mensagens que falham durante o processamento (após esgotarem as tentativas) são automaticamente enviadas para uma DLQ para análise posterior, evitando que a fila principal seja travada.
* **Consulta de Tarefas:** A API fornece endpoints para listar todas as tarefas com paginação e para fazer o download da imagem processada.

## Arquitetura

O sistema é dividido em componentes que se comunicam de forma assíncrona:

1.  **API (Controller):** Ponto de entrada REST. Responsável por receber requisições, validar os dados, enviar a imagem para o MinIO e publicar uma mensagem na fila do RabbitMQ.
2.  **RabbitMQ:** Atua como o *message broker*, gerenciando as filas de trabalho e garantindo a entrega das mensagens para os workers.
3.  **ImageWorker (Consumer):** Um componente que escuta a fila de processamento. Ele baixa a imagem original do MinIO, aplica a transformação necessária e, por fim, faz o upload da imagem resultante de volta para o MinIO, atualizando o status da tarefa no banco de dados.
4.  **PostgreSQL:** Armazena o estado e metadados de cada tarefa (`Job`), como status (`PENDING`, `ON_PROGRESS`, `COMPLETED`, `FAILED`), tipo de operação e IDs dos arquivos.
5.  **MinIO:** O serviço de armazenamento de objetos onde os arquivos de imagem residem.

## Como Executar

Todo o ambiente necessário para rodar a aplicação está containerizado com Docker.

**Pré-requisitos:**
* Docker
* Docker Compose

**Passos:**
1.  Clone este repositório.
2.  Navegue até a raiz do projeto.
3.  Execute o seguinte comando para iniciar todos os serviços (Aplicação, PostgreSQL, RabbitMQ e MinIO):
    ```bash
    docker compose up -d
    ```
4.  A aplicação estará disponível em `http://localhost:8089/api/v1/job`.

## Endpoints da API

* **Submeter uma nova tarefa de processamento**
    * `POST /api/v1/job`
    * **Tipo:** `multipart/form-data`
    * **Partes:**
        1.  `imagem`: O arquivo da imagem (`.jpg`, `.png`, etc.).
        2.  `dados`: Um JSON com o tipo de operação.
            * **Content-Type:** `application/json`
            * **Exemplo de corpo:** `{"operationType":"SEPIA"}`
    * **Resposta de Sucesso:** `202 Accepted`
      
      **OBS.:** O arquivo `requests.sh` contém a estrutura da requisição e realiza 5 chamadas de teste a essa rota.
      

* **Listar todas as tarefas**
    * `GET /api/v1/job`
    * **Query Params (Opcionais):**
        * `pageNo` (padrão: `0`)
        * `pageSize` (padrão: `10`)
    * **Resposta de Sucesso:** `200 OK` com uma página de tarefas.

* **Baixar imagem processada**
    * `GET /api/v1/job/{id}`
    * **Path Param:** `id` (O ID da tarefa).
    * **Resposta de Sucesso:** `200 OK` com o arquivo da imagem (`image/jpeg`).
    * **Resposta de Erro:** `404 Not Found` se a tarefa não for encontrada.
