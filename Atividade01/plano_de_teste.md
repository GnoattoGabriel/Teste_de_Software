# Plano de teste — LoginLab

## Identificação

- Produto/build: LoginLab
- Equipe: 
- Data: 

## Objetivo e decisão apoiada

Validar o funcionamento do sistema de autenticação, garantindo que os requisitos de segurança sejam atendidos e que diferentes cenários de login (sucesso, falha, bloqueio) sejam tratados corretamente.

## Escopo

Testes de login com as três contas conhecidas (aluno01, professor01, bloqueado01), cobrindo requisitos RN-LOGIN-01 a RN-LOGIN-09.

## Fora de escopo

- Testes de interface visual (CSS/HTML)
- Performance de login sob carga massiva
- Integração com sistemas externos além da autenticação

## Riscos priorizados

| ID | Risco | Probabilidade | Impacto | Prioridade |
|----|-------|----------------|---------|------------|
| R1 | Falha em bloquear conta após múltiplas tentativas inválidas | Média | Alta | Alta |
| R2 | Senha aceita com variações de maiúsculas/minúsculas incorretas | Baixa | Alta | Alta |
| R3 | Mensagem de erro vazar informação sensível | Baixa | Alta | Média |
| R4 | Sessão criada mesmo com falha de autenticação | Baixa | Alta | Alta |

## Estratégia

- Nível: Integração
- Tipo: Funcional
- Técnica: Casos de teste descendentes a partir dos requisitos
- Ordem de execução: Prioridade Alta ? Média ? Baixa

## Ambiente e massa de dados

- Ambiente: Web (browser)
- Contas de teste:
  - aluno01 / Teste@123 (ativo)
  - professor01 / Aula@2026 (ativo)
  - bloqueado01 / Segura@456 (bloqueado)

## Critérios

- Entrada: Usuário e senha fornecidos pelos campos de login
- Saída: Sucesso (token/sessão), falha (mensagem genérica 'Usuário ou senha inválidos') ou conta bloqueada ('Conta bloqueada. Procure o suporte.')
- Suspensão: Pausar execução em ambiente instável, retomar quando disponível
- Retomação: Reexecutar testes falhos após correções de bug
