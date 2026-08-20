# Casos de teste — Reserva de Salas

## CT-RES-01 — Reservar sala disponível para turma compatível

- Requisitos: RF-01
- Prioridade: Alta
- Pré-condições: Usuário autenticado, sala disponível, horário dentro do intervalo permitido

### Passos

1. Verificar disponibilidade da sala no horário solicitado
2. Confirmar que a turma solicitante cabe na capacidade da sala
3. Confirmar compatibilidade da turma com a sala (ex: horário de aula, equipamentos)
4. Confirmar que a sala não está em manutenção
5. Confirmar que o horário está entre 07h30 e 22h30
6. Confirmar que o responsável tem perfil adequado para a reserva
7. Confirmar que não há conflitos de reserva existente
8. Executar a reserva

### Resultado esperado

- Reserva efetuada com sucesso
- Sala atribuída à turma compatível
- Mensagem de confirmação exibida

 — Impedir sobreposição na mesma sala

- Requisitos: RF-02
- Prioridade: Alta
- Pré-condições: Sala já reservada para outro horário/grupo

### Passos

1. Tentar reservar uma sala que já tenha reserva no mesmo horário
2. Verificar se o sistema bloqueia a nova reserva
3. Verificar se o sistema exibe mensagem de conflito

### Resultado esperado

- Reserva não efetuada
- Mensagem de conflito de horário exibida: "Já existe reserva para este horário/nesta sala"

 — Impedir turma maior que a capacidade

- Requisitos: RF-03
- Prioridade: Alta
- Pré-condições: Turma com número de alunos informado

### Passos

1. Informar número de alunos maior que a capacidade da sala
2. Tentar realizar a reserva com turma acima da capacidade
3. Verificar se o sistema bloqueia a reserva

### Resultado esperado

- Reserva não efetuada
- Mensagem de erro exibida: "Turma excede capacidade da sala. Capacidade máxima: X lugares."

 — Bloquear sala em manutenção

- Requisitos: RF-04
- Prioridade: Alta
- Pré-condições: Sala marcada como em manutenção

### Passos

1. Verificar status da sala (em manutenção)
2. Tentar reservar a sala em manutenção
3. Verificar se o sistema bloqueia a reserva

### Resultado esperado

- Reserva não efetuada
- Mensagem de erro exibida: "Sala indisponível. Em manutenção."

 — Permitir reservas entre 07h30 e 22h30

- Requisitos: RF-05
- Prioridade: Alta
- Pré-condições: Horário solicitado fora do intervalo 07h30-22h30

### Passos

1. Tentar reservar sala para horário anterior às 07h30 (ex: 07h00)
2. Tentar reservar sala para horário depois das 22h30 (ex: 23h00)
3. Tentar reservar sala no intervalo permitido (ex: 08h00 ou 22h00)
4. Verificar se o sistema aceita/rejeita cada caso

### Resultado esperado

- Reservas antes das 07h30 são rejeitadas
- Reservas depois das 22h30 são rejeitadas
- Reservas entre 07h30 e 22h30 são aceitas

 — Só coordenação altera reserva de outro professor

- Requisitos: RF-06
- Prioridade: Alta
- Pré-condições: Usuário professor tentando alterar reserva de outro professor

### Passos

1. Login como professor comum
2. Tentar alterar reserva de outro professor (outro ID de professor)
3. Verificar se o sistema bloqueia a alteração
4. Login como coordenação
5. Tentar alterar reserva de outro professor
6. Verificar se a coordenação consegue alterar

### Resultado esperado

- Profesor comum não consegue alterar reserva de outro professor
- Coordenação consegue alterar reserva de qualquer professor

 — Cancelamento libera horário, registra histórico e gera notificação

- Requisitos: RF-07 e RF-08
- Prioridade: Média
- Pré-condições: Reserva existente para cancelamento

### Passos

1. Cancelar uma reserva existente
2. Verificar se o horário da sala fica disponível para nova reserva
3. Verificar se o histórico de cancelamento foi registrado com data, horário e responsável
4. Verificar se notificação foi enviada para o responsável da reserva
5. Verificar canal de notificação (email/sistema) e se contém detalhes da mudança

### Resultado esperado

- Horário da sala fica disponível para nova reserva
- Histórico de cancelamento registrado com data, horário e responsável
- Notificação enviada para o responsável com detalhes da alteração/cancelamento
- Histórico reflete a mudança

 — Alteração de data/hora gera notificação

- Requisitos: RF-08
- Prioridade: Média
- Pré-condições: Reserva existente com data/horário para alteração

### Passos

1. Alterar a data ou hora da reserva existente
2. Verificar se notificação foi enviada para o responsável
3. Verificar se a notificação contém os novos dados da data/hora
4. Verificar canal de notificação (email/sistema)

### Resultado esperado

- Notificação enviada para o responsável da reserva
- Notificação contém os novos dados da data/hora alterada
- Histórico reflete a mudança de data/hora

 — Alteração de turma gera notificação

- Requisitos: RF-08
- Prioridade: Média
- Pré-condições: Reserva existente com turma para alteração

### Passos

1. Alterar a turma da reserva existente
2. Verificar se notificação foi enviada para o responsável
3. Verificar se a notificação contém o novo nome da turma
4. Verificar canal de notificação (email/sistema)

### Resultado esperado

- Notificação enviada para o responsável da reserva
- Notificação contém o novo nome da turma alterada
- Histórico reflete a mudança de turma

 — Alteração de responsável gera notificação

- Requisitos: RF-08
- Prioridade: Média
- Pré-condições: Reserva existente com responsável para alteração

### Passos

1. Alterar o responsável da reserva existente
2. Verificar se notificação foi enviada para o novo responsável
3. Verificar se a notificação contém o novo nome do responsável
4. Verificar se o responsável original foi removido/notificado
5. Verificar canal de notificação (email/sistema)

### Resultado esperado

- Notificação enviada para o novo responsável da reserva
- Notificação contém o novo nome do responsável alterado
- Histórico reflete a mudança de responsável
