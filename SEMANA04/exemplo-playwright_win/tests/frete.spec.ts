import { test, expect } from '@playwright/test';

const casosValidos = [
  { cep: '80000000', valor: '199,99', resultado: 'Frete: R$ 15,00' },
  { cep: '01000000', valor: '199.99', resultado: 'Frete: R$ 25,00' },
  { cep: '12345678', valor: '200,00', resultado: 'Frete grátis' },
  { cep: '87654321', valor: '200,01', resultado: 'Frete grátis' },
];

const casosInvalidos = [
  { cep: '', valor: '10,00' },
  { cep: '1234567', valor: '10,00' },
  { cep: '123456789', valor: '10,00' },
  { cep: '12345ABC', valor: '10,00' },
  { cep: '12345678', valor: '' },
  { cep: '12345678', valor: '0' },
  { cep: '12345678', valor: '-1' },
  { cep: '12345678', valor: '10,123' },
  { cep: '12345678', valor: 'abc' },
];

test.describe('calculadora de frete funcional', () => {

  test('aplica o limite de R$ 200,00 para conceder frete grátis', async ({ page }) => {
    await page.goto('/frete');
    await page.getByLabel('CEP').fill('12345678');
    await page.getByLabel('Valor do pedido').fill('199,99');
    await page.getByRole('button', { name: 'Calcular frete' }).click();

    await expect(page.locator('#resultado')).toHaveText('Frete: R$ 25,00');

    await page.getByLabel('Valor do pedido').fill('200,00');
    await page.getByRole('button', { name: 'Calcular frete' }).click();

    await expect(page.locator('#resultado')).toHaveText('Frete grátis');
    await expect(page.locator('#resultado')).toHaveAttribute('role', 'status');
  });

  for (const caso of casosInvalidos) {
    test(`rejeita CEP "${caso.cep || '(vazio)'}" e valor "${caso.valor || '(vazio)'}"`, async ({ page }) => {
      await page.goto('/frete');

      await page.getByLabel('CEP').fill(caso.cep);
      await page.getByLabel('Valor do pedido').fill(caso.valor);
      await page.getByRole('button', { name: 'Calcular frete' }).click();

      const resultado = page.locator('#resultado');
      await expect(resultado).toBeVisible();
      await expect(resultado).toHaveText('Dados inválidos');
      await expect(resultado).toHaveAttribute('role', 'alert');
    });
  }
});
