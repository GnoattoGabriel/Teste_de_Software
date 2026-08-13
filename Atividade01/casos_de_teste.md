# Casos de teste — LoginLab

## CT-LOGIN-01 — Campos obrigatórios

- Requisitos: RN-LOGIN-01
- Riscos: R1 (Falha em bloquear após tentativas inválidas)
- Prioridade: Alta
- Pré-condições: Página de login exibida
- Usuário: (vazio)
- Senha: (vazia)

### Passos

1. Limpar campo usuário
2. Limpar campo senha
3. Clicar em "Entrar"

### Resultado esperado

- Mensagem: "Usuário ou senha inválidos"
- Sessão criada: não

### Execução

- Resultado observado: 
- Status: 
- Evidência: 

---

## CT-LOGIN-02 — Apenas usuário em branco

- Requisitos: RN-LOGIN-01
- Riscos: R1
- Prioridade: Alta
- Pré-condições: Página de login exibida
- Usuário: (vazio)
- Senha: Teste@123

### Passos

1. Limpar campo usuário
2. Preencher senha com Teste@123
3. Clicar em "Entrar"

### Resultado esperado

- Mensagem: "Usuário ou senha inválidos"
- Sessão criada: não

### Execução

- Resultado observado: 
- Status: 
- Evidência: 

---

## CT-LOGIN-03 — Apenas senha em branco

- Requisitos: RN-LOGIN-01
- Riscos: R1
- Prioridade: Alta
- Pré-condições: Página de login exibida
- Usuário: aluno01
- Senha: (vazia)

### Passos

1. Preencher campo usuário com aluno01
2. Limpar campo senha
3. Clicar em "Entrar"

### Resultado esperado

- Mensagem: "Usuário ou senha inválidos"
- Sessão criada: não

### Execução

- Resultado observado: 
- Status: 
- Evidência: 

---

## CT-LOGIN-04 — Login bem-sucedido (aluno01)

- Requisitos: RN-LOGIN-02
- Riscos: Nenhum crítico
- Prioridade: Alta
- Pré-condições: Página de login exibida
- Usuário: aluno01
- Senha: Teste@123

### Passos

1. Preencher campo usuário com aluno01
2. Preencher campo senha com Teste@123
3. Clicar em "Entrar"

### Resultado esperado

- Mensagem: (nenhuma, ou redirecionamento)
- Sessão criada: sim

### Execução

- Resultado observado: 
- Status: 
- Evidência: 

---

## CT-LOGIN-05 — Login bem-sucedido (professor01)

- Requisitos: RN-LOGIN-02
- Riscos: Nenhum crítico
- Prioridade: Alta
- Pré-condições: Página de login exibida
- Usuário: professor01
- Senha: Aula@2026

### Passos

1. Preencher campo usuário com professor01
2. Preencher campo senha com Aula@2026
3. Clicar em "Entrar"

### Resultado esperado

- Mensagem: (nenhuma, ou redirecionamento)
- Sessão criada: sim

### Execução

- Resultado observado: 
- Status: 
- Evidência: 

---

## CT-LOGIN-06 — Senha incorreta para o usuário

- Requisitos: RN-LOGIN-03
- Riscos: R2, R3
- Prioridade: Média
- Pré-condições: Página de login exibida
- Usuário: aluno01
- Senha: SenhaErrada

### Passos

1. Preencher campo usuário com aluno01
2. Preencher campo senha com SenhaErrada
3. Clicar em "Entrar"

### Resultado esperado

- Mensagem: "Usuário ou senha inválidos"
- Sessão criada: não

### Execução

- Resultado observado: 
- Status: 
- Evidência: 

---

## CT-LOGIN-07 — Senha incorreta (cross-credencial)

- Requisitos: RN-LOGIN-03
- Riscos: R2, R3
- Prioridade: Média
- Pré-condições: Página de login exibida
- Usuário: aluno01
- Senha: Aula@2026 (senha do professor)

### Passos

1. Preencher campo usuário com aluno01
2. Preencher campo senha com Aula@2026
3. Clicar em "Entrar"

### Resultado esperado

- Mensagem: "Usuário ou senha inválidos"
- Sessão criada: não

### Execução

- Resultado observado: 
- Status: 
- Evidência: 

---

## CT-LOGIN-08 — Senha correta para usuário bloqueado

- Requisitos: RN-LOGIN-05
- Riscos: R1, R4
- Prioridade: Alta
- Pré-condições: Página de login exibida
- Usuário: bloqueado01
- Senha: Segura@456

### Passos

1. Preencher campo usuário com bloqueado01
2. Preencher campo senha com Segura@456
3. Clicar em "Entrar"

### Resultado esperado

- Mensagem: "Conta bloqueada. Procure o suporte."
- Sessão criada: não

### Execução

- Resultado observado: 
- Status: 
- Evidência: 

---

## CT-LOGIN-09 — Senha correta de outra conta (bloqueado)

- Requisitos: RN-LOGIN-05
- Riscos: R1, R4
- Prioridade: Alta
- Pré-condições: Página de login exibida
- Usuário: bloqueado01
- Senha: Teste@123 (senha do aluno)

### Passos

1. Preencher campo usuário com bloqueado01
2. Preencher campo senha com Teste@123
3. Clicar em "Entrar"

### Resultado esperado

- Mensagem: "Conta bloqueada. Procure o suporte."
- Sessão criada: não

### Execução

- Resultado observado: 
- Status: 
- Evidência: 

---

## CT-LOGIN-10 — Usuário inexistente

- Requisitos: RN-LOGIN-04
- Riscos: R1
- Prioridade: Alta
- Pré-condições: Página de login exibida
- Usuário: usuario_inexistente
- Senha: Qualquer

### Passos

1. Preencher campo usuário com usuário_inexistente
2. Preencher campo senha com qualquer valor
3. Clicar em "Entrar"

### Resultado esperado

- Mensagem: "Usuário ou senha inválidos"
- Sessão criada: não

### Execução

- Resultado observado: 
- Status: 
- Evidência: 

---

## CT-LOGIN-11 — Mensagem genérica para credenciais inválidas

- Requisitos: RN-LOGIN-06
- Riscos: R3
- Prioridade: Média
- Pré-condições: Página de login exibida
- Usuário: aluno01
- Senha: SenhaErrada

### Passos

1. Preencher campo usuário com aluno01
2. Preencher campo senha com SenhaErrada
3. Clicar em "Entrar"

### Resultado esperado

- Mensagem: "Usuário ou senha inválidos"
- Sessão criada: não

### Execução

- Resultado observado: 
- Status: 
- Evidência: 

---

## CT-LOGIN-12 — Conta bloqueada exibe mensagem específica

- Requisitos: RN-LOGIN-07
- Riscos: R1
- Prioridade: Alta
- Pré-condições: Página de login exibida
- Usuário: bloqueado01
- Senha: Teste@123

### Passos

1. Preencher campo usuário com bloqueado01
2. Preencher campo senha com Teste@123
3. Clicar em "Entrar"

### Resultado esperado

- Mensagem: "Conta bloqueada. Procure o suporte."
- Sessão criada: não

### Execução

- Resultado observado: 
- Status: 
- Evidência: 

---

## CT-LOGIN-13 — Maiúsculas vs minúsculas (senha correta)

- Requisitos: RN-LOGIN-08
- Riscos: R2
- Prioridade: Média
- Pré-condições: Página de login exibida
- Usuário: aluno01
- Senha: teste@123 (todas minúsculas)

### Passos

1. Preencher campo usuário com aluno01
2. Preencher campo senha com teste@123
3. Clicar em "Entrar"

### Resultado esperado

- Mensagem: (sucesso ou redirecionamento)
- Sessão criada: sim

### Execução

- Resultado observado: 
- Status: 
- Evidência: 

---

## CT-LOGIN-14 — Maiúsculas vs minúsculas (senha incorreta)

- Requisitos: RN-LOGIN-08
- Riscos: R2
- Prioridade: Média
- Pré-condições: Página de login exibida
- Usuário: aluno01
- Senha: TESTE@123 (todas maiúsculas)

### Passos

1. Preencher campo usuário com aluno01
2. Preencher campo senha com TESTE@123
3. Clicar em "Entrar"

### Resultado esperado

- Mensagem: "Usuário ou senha inválidos"
- Sessão criada: não

### Execução

- Resultado observado: 
- Status: 
- Evidência: 

---

## CT-LOGIN-15 — Falhas não criam sessão (coleta)

- Requisitos: RN-LOGIN-09
- Riscos: R4
- Prioridade: Alta
- Pré-condições: Página de login exibida
- (Variados: CT-06 a CT-12)

### Passos

1. Executar todas as falhas (senha errada, usuário inexistente, conta bloqueada)
2. Verificar se sessão ativa não foi criada

### Resultado esperado

- Sessão criada: não para todos os casos de falha

### Execução

- Resultado observado: 
- Status: 
- Evidência: