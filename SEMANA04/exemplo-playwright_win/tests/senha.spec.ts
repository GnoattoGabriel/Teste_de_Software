import { test, expect } from '@playwright/test';

const senhaValida = 'Senha123';
const senhaValidaMaxima = 'Senha123456789012345';
const senhaInvalidaMaxima = 'Senha1234567890123456';

const casosInvalidos = [
  { senha: 'Senha12', confirmacao: 'Senha12', resultado: 'Senha fora do padrão' },
  { senha: senhaInvalidaMaxima, confirmacao: senhaInvalidaMaxima, resultado: 'Senha fora do padrão' },
  { senha: 'senha123', confirmacao: 'senha123', resultado: 'Senha fora do padrão' },
  { senha: 'SENHA123', confirmacao: 'SENHA123', resultado: 'Senha fora do padrão' },
  { senha: 'SenhaABC', confirmacao: 'SenhaABC', resultado: 'Senha fora do padrão' },
  { senha: 'Senha 123', confirmacao: 'Senha 123', resultado: 'Senha fora do padrão' },
  { senha: senhaValida, confirmacao: 'Senha124', resultado: 'As senhas não coincidem' },
];

test.describe('cadastro de senha funcional', () => {

  test('permite cadastrar uma senha válida com 20 caracteres', async ({ page }) => {
    await page.goto('/senha');
    await page.getByLabel('Nova senha').fill(senhaValidaMaxima);
    await page.getByLabel('Confirmar senha').fill(senhaValidaMaxima);
    await page.getByRole('button', { name: 'Cadastrar senha' }).click();

    await expect(page.locator('#resultado')).toHaveText('Senha cadastrada');
    await expect(page.locator('#resultado')).toHaveAttribute('role', 'status');
  });

  for (const caso of casosInvalidos) {
    test(`rejeita a senha "${caso.senha}"`, async ({ page }) => {
      await page.goto('/senha');

      await page.getByLabel('Nova senha').fill(caso.senha);
      await page.getByLabel('Confirmar senha').fill(caso.confirmacao);
      await page.getByRole('button', { name: 'Cadastrar senha' }).click();

      const resultado = page.locator('#resultado');
      await expect(resultado).toBeVisible();
      await expect(resultado).toHaveText(caso.resultado);
      await expect(resultado).toHaveAttribute('role', 'alert');
    });
  }

  test('rejeita senha vazia', async ({ page }) => {
    await page.goto('/senha');
    await page.getByRole('button', { name: 'Cadastrar senha' }).click();

    await expect(page.locator('#resultado')).toHaveText('Senha fora do padrão');
    await expect(page.locator('#resultado')).toHaveAttribute('role', 'alert');
  });
});
