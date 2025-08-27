# API_Simulador

**Descrição:** Simulacão SAC e PRICE HackCaixa

**Versão:** 1.0

## Acesso
- http://localhost:8080 (**O swagger inicia automaticamente**)

## Configurações da Aplicação

### Datasources

#### SQL Server
- **JDBC URL:** `jdbc:sqlserver://dbhackathon.database.windows.net:1433;database=hack`
- **Usuário:** `hack`
- **Senha:** `Password23`
- **Driver:** `com.microsoft.sqlserver.jdbc.SQLServerDriver`

#### H2 (em memória)
- **JDBC URL:** `jdbc:h2:mem:simulador;DB_CLOSE_DELAY=-1;`
- **Usuário:** `sa`
- **Senha:** *(vazio)*
- **Driver:** `org.h2.Driver`

### Cache (Caffeine)
- **Tipo:** `caffeine`
- **Nomes de Cache:** `obterProdutos`
- **Configuração:** `maximumSize=100,expireAfterWrite=1m`

### Kafka
- **Bootstrap Servers:** `eventhack.servicebus.windows.net:9093`
- **Propriedades:**
    - `sasl.jaas.config`:  
      `org.apache.kafka.common.security.plain.PlainLoginModule required username="$ConnectionString" password="Endpoint=sb://eventhack.servicebus.windows.net/;SharedAccessKeyName=hack;SharedAccessKey=HeHeVaVqyVkntO2FnjQcs2Ilh/4MUDo4y+AEhKp8z+g=";`
    - `sasl.mechanism`: `PLAIN`
    - `security.protocol`: `SASL_SSL`
- **Producer Serializer:**  
  `org.springframework.kafka.support.serializer.JsonSerialize`

## Endpoints

### Simulador Credito
- **Método:** `POST`
- **URL:** `/api/v1/Simulacao`
- **Body:** ['application/json']
- **Respostas:**
    - `200`: successfully
    - `400`: Bad request
    - `500`: Internal Server Error

### Listar Simulações de Credito por produto e por dia
- **Método:** `GET`
- **URL:** `/api/v1/volumesimulado/{data}`
- **Parâmetros:**
    - `data` (path): Data de referência no formato yyyy-MM-dd (Ex: 2025-07-30)
- **Respostas:**
    - `200`: successfully
    - `400`: Bad request
    - `500`: Internal Server Error

### Listar Simulações de Credito por produto e por dia
- **Método:** `GET`
- **URL:** `/api/v1/telemetria/{data}`
- **Parâmetros:**
    - `data` (path): Data de referência no formato yyyy-MM-dd (Ex: 2025-07-30)
- **Respostas:**
    - `200`: successfully
    - `400`: Bad request
    - `500`: Internal Server Error

### Listar Simulações de Credito
- **Método:** `GET`
- **URL:** `/api/v1/listarSimulacoes`
- **Parâmetros:**
    - `pagina` (query):  (Ex: )
    - `qtdRegistrosPagina` (query):  (Ex: )
- **Respostas:**
    - `200`: successfully
    - `400`: Bad request
    - `500`: Internal Server Error

## Schemas

### EntradaSimulacaoDto
- `valorDesejado`: number double
- `prazo`: integer int32

### ParcelaDto
- `numero`: integer int32
- `valorAmortizacao`: number double
- `valorJuros`: number double
- `valorPrestacao`: number double

### ResultadoSimulacaoDto
- `tipo`: string
- `parcelas`: array

### RetornoDto
- `Timestamp`: string date-time
- `Mensagem`: string
- `Violacoes`: array
- `Detalhes`: string

### SimulacaoResumoDto
- `codigoProduto`: integer int32
- `descricaoProduto`: string
- `taxaMediaJuro`: number double
- `valorMedioPrestacao`: number double
- `valorTotalDesejado`: number double
- `valorTotalCredito`: number double

### SimulacoesResumoDto
- `dataReferencia`: string
- `simulacoes`: array

### EndpointInfo
- `id`: integer int64
- `metodo`: string
- `tempo`: integer int64
- `httpStatus`: integer int32
- `telemetria`:

### Telemetria
- `id`: integer int64
- `dataReferencia`: string date
- `listaEndpoints`: array

### SimulacaoPaginadaDto
- `pagina`: integer int32
- `qtdRegistros`: integer int32
- `qtdRegistrosPagina`: integer int32
- `registros`: array

### SimulacaoRegistroDto
- `idSimulacao`: integer int64
- `valorDesejado`: number double
- `prazo`: integer int32
- `valorTotalParcelas`: number double
