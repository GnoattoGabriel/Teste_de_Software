# Relatório do grupo

Integrantes:

## Grafos e complexidade

Modelo adotado: cada condição de curto-circuito (`&&`/`||`) é um nó de decisão separado
(operando direito avaliado ou não). Cada `case` de `switch` é uma aresta própria.
Exceções (`throw`/`catch`) têm saída unificada e **não** contam como branch no JaCoCo.
Grafo de chamadas de `fechar`: `PedidoService.fechar` → `Pedido.subtotalCentavos` →
`Pedido.estoqueSuficiente` → `PoliticaDesconto.calcular` → `CalculadoraFrete.calcular`
→ `AnaliseRisco.avaliar` → `PagamentoService.pagar` → `ProcessadorPagamento.autorizar`.

| Método | Nós | Arestas | V(G) | Caminhos independentes | Restrições de viabilidade |
| --- | --- | --- | --- | --- | --- |
| `PoliticaDesconto.calcular` | 14 | 24 | 12 | 1-base VIP; 2-comum ≥50000; 3-comum <50000; 4-cupom nulo/branco; 5-BEMVINDO elegível; 6-BEMVINDO sem histórico; 7-BEMVINDO subtotal baixo; 8-EXTRA10 elegível; 9-EXTRA10 baixo; 10-desconhecido (exceção); 11-teto aplica; 12-teto não aplica | Todos viáveis na unidade |
| `CalculadoraFrete.calcular` | 12 | 20 | 10 | 1-PR/SP/RJ/default; 2-while 0/1/N iterações; 3-gratuidade aplica; 4-gratuidade bloqueada por expresso; 5-VIP metade; 6-expresso +1500; 7-frágil +500 único | `gratuito+expresso` inviável (gratuidade exige `!expresso`); frágil com base zerada só via gratuidade+frágil |
| `AnaliseRisco.avaliar` | 10 | 16 | 8 | 1-bloqueado RECUSADO; 2-novo total alto; 3-novo expresso; 4-novo ambos; 5-novo APROVADO; 6-antigo comum alto REVISAO; 7-antigo VIP alto APROVADO; 8-antigo limite APROVADO | Caminho 6 inviável via `PedidoService` quando total alto já caiu em REVISAO de novo cliente; viável na unidade |
| `PagamentoService.pagar` | 7 | 10 | 5 | 1-sucesso 1ª; 2-recusa `false`; 3-`IllegalStateException`+sucesso; 4-esgotamento `false`; 5-outra exceção propaga | Todos viáveis com stub com contador |
| `PedidoService.fechar` | 8 | 12 | 6 | 1-BLOQUEADO; 2-subtotal zero exceção; 3-SEM_ESTOQUE; 4-REVISAO; 5-PAGO; 6-PAGAMENTO_RECUSADO | REVISAO de cliente antigo com total >500000 não é alcançável sem passar por desconto/frete; cupom desconhecido interrompe antes do frete |
| `Pedido.subtotalCentavos` | 4 | 5 | 3 | 1-lista vazia; 2-só inativos (`continue`); 3-misto | — |
| `Pedido.temFragil` | 4 | 5 | 3 | 1-false; 2-true no fim; 3-inativo ignorado | — |
| `Pedido.estoqueSuficiente` | 4 | 5 | 3 | 1-ok; 2-falta no início (`break`); 3-falta no fim | — |

## Matriz de testes

| ID / método JUnit | Unidade | Entrada e estado do stub | Resultado esperado | Caminho / aresta | Critério atendido |
| --- | --- | --- | --- | --- | --- |
| PD01 `vipRecebeDezPorcentoSemCupom` | Desconto | vip, 10000, null | 1000 | base VIP | ramo T |
| PD02/03 `comum...` | Desconto | comum 60000/10000 | 3000/0 | `>=50000` T/F | fronteira 50000/49999 |
| PD05 `bemvindoElegivel` | Desconto | 0 compras, 10000, BEMVINDO | 2000 | `==0`T+`>=10000`T | decisão |
| PD06 `bemvindoNormalizado` | Desconto | `" bemvindo "` | 2000 | trim+upper | normalização |
| PD07/08 `bemvindoSemElegibilidade*` | Desconto | com compras / 9999 | 0 | operando F (curto-circuito) | MC/DC parcial |
| PD09/10 `extra10*` | Desconto | 20000/19999 | 2000/0 | `>=20000` T/F | fronteira |
| PD12 `tetoLimita` | Desconto | vip novo 10000 BEMVINDO | 2000 (teto) | desconto>teto T | teto |
| PD15/16 exceção | Desconto | cupom desconhecido / subtotal -1 | `IllegalArgumentException` | saída exceção (sem branch) | exceção |
| FR01-03 bases | Frete | PR/SP/RJ/MG peso 1000 | 1200/2000/3000 | switch cases+default | ramos switch |
| FR04-08 peso | Frete | 2000/2001/3000/3001/5000g | 1200/1500/1500/1800/2100 | while 0/1/2/3 iterações | laço + fração |
| FR09 gratuidade | Frete | liq 30000/29999 normal | 0/2100 | `>=30000&&!expresso` | fronteira + curto |
| FR10 expresso impede | Frete | expresso liq 50000 | 2700 | `!expresso`F | curto-circuito |
| FR11-13 VIP/frágil | Frete | vip; gratuito+frágil=500; 2 frágeis=1700 | metade/único | decisões independentes | combinação |
| RS01-05 novo | Risco | bloqueado; 0 compras 100000/100001/expresso | RECUSADO/APROVADO/REVISAO | `\|\|` 4 combos | curto-circuito |
| RS06-08 antigo | Risco | comum 500001/vip 600000/500000 | REVISAO/APROVADO | `&&` combos | fronteira |
| PG01-06 pagamento | Pagamento | true/false/falha+sucesso/esgota/outra exceção | true/false+nº chamadas | do/while 1..3 + catch | iterações + exceção |
| PE01-08 pedido | Pedido | inativo/vazio/falta início/fim/cópia | subtotal/peso/fragil | for+continue+break | laço |
| PS01 exemplo | Serviço | comum, 10000 PR, stub true | PAGO 11200, 1 cobrança | caminho feliz | colaboração |
| PS02 bloqueado | Serviço | bloqueado | BLOQUEADO zeros, 0 chamadas | retorno antecipado | ordem |
| PS04 sem estoque | Serviço | qtd>estoque + cupom INEXISTENTE | SEM_ESTOQUE, sem throw | retorno antes do cupom | ordem |
| PS05/06 revisao | Serviço | novo expresso / novo 150000 | REVISAO c/ valores, 0 chamadas | risco pendente | integração |
| PS07 recusado | Serviço | stub false | PAGAMENTO_RECUSADO, 1 chamada | pagar false | integração |
| PS08/09 vip/cupom | Serviço | vip SP / BEMVINDO | PAGO 36000 / 14200 | desconto+frete | integração |

## Evolução da cobertura

`mvn clean test` (Maven bundled IntelliJ): **79 testes, 0 falhas**.

| Etapa | Testes executados | Linhas | Branches | Métodos | Classes | Lacunas e justificativas |
| --- | --- | --- | --- | --- | --- | --- |
| Inicial | 1 (`PedidoServiceTest` exemplo) | parcial | parcial | parcial | parcial | 7 classes de teste vazias |
| Final | 79 | 108/108 100% | 116/116 100% | 21/21 100% | 9/9 100% | 0 missed (`jacoco.csv`) |
| Detalhe final | — | Boletins por classe: Desconto 14/14, Frete 15/15, Risco 8/8, Pagamento 11/11, Serviço 22/22, Pedido 25/25, Item 9/9, Cliente 3/3, Resultado 1/1 | Desconto 21/21, Frete 17/17, Risco 14/14, Pagamento 8/8, Serviço 10/10, Pedido 24/24, Item 20/20 | 100% | 100% | Cobertura de classe = ≥1 método executado; aqui todos os métodos de negócio foram executados |

## Análise crítica

- Quais combinações faltavam mesmo com os ramos cobertos?
  - `CalculadoraFrete`: cobrir `gratuidade T/F`, `VIP T/F`, `expresso T/F`, `frágil T/F` isoladamente dá 100% de branches com ~4 testes, mas deixa combinações como `gratuito+VIP+frágil` (0/2+500=500) e `peso extra+VIP+expresso+frágil` sem execução. Foram adicionados `vipComGratuidadeZeraAntesDaMetade`, `fragilAdicionaUmaVezMesmoComBaseZerada` e `expressoMaisFragilAcumulam` para cobrir combinações, não só ramos.
- Quais condições não foram avaliadas devido ao curto-circuito?
  - `liquido >= 30000 && !expresso`: com `liquido=10000` o 2º operando não é avaliado (teste `baseParana`); com `expresso=true` e `liquido=50000` o 1º é T mas o 2º é F (teste `expressoImpedeGratuidade`). `total > 100000 \|\| expresso`: com total alto o `expresso` não é avaliado. `cupom == null \|\| isBlank`: com null o `isBlank` não é avaliado.
- Quais caminhos são inviáveis no serviço, mas viáveis na unidade?
  - `AnaliseRisco` com `total > 500000 && !vip` para cliente antigo comum é viável na unidade, mas via `PedidoService` exige subtotal alto + desconto + frete que mudam o total; `BLOQUEADO→SEM_ESTOQUE` nunca coexiste (bloqueado retorna antes do estoque). `gratuito+expresso` inviável por construção.
- Como foram testadas exceções e quantidades de iterações?
  - Exceções com `assertThrows`: subtotal/total/líquido negativos, cupom desconhecido, `Pedido` sem itens ativos, UF/lista inválidas, `ItemPedido`/`Cliente` inválidos, `pagar` com total ≤0 e limite fora de 1-3, NPE pedido/cliente/processador nulos, outra exceção do processador propaga. Laços: `while` do peso com 0 (2000g), 1 (2001/3000g) e N (3001/5000g) iterações; `for` com zero/uma/várias linhas, `continue` (inativo) e `break` (falta no início/fim); `do/while` com 1/2/3 tentativas.
- Qual alteração proposital foi detectada por qual teste? A alteração foi desfeita?
  - (A fazer em aula: ex. trocar `>= 30000` por `> 30000` — detectado por `gratuidadeComLiquidoNaFronteira`; trocar `teto 20%` por `25%` — detectado por `tetoLimitaCombinacaoVipMaisBemvindo`. Desfazer após demonstrar falha.)
