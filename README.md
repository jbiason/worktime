# DESAFIO DE PROGRAMAÇÃO CWI

## Descrição

Fazer um programa que receba a hora de entrada na empresa e, considerando o
horário de almoço e turnos de 6 horas, mostre o horário esperado de saída para
completar as 08h30min diários e indicar quantas horas/minutos faltam para
alcançar o horário de saída ou quantas horas/minutos está acima do horário de
saída.

O script irá receber as seguintes entradas:

* Entrada na empresa - obrigatório;
* Saída para almoço - opcional, se não especificada, considera que ainda não
  saiu para almoço;
* Retorno almoço - opcional, se não especificada, considera que houve 1 hora de
  almoço.

Todas as entradas deverão estar no formato `%H:%M` (horas com dois dígitos,
minutos com dois dígitos).

Está permitido utilizar pacotes externos à STL da linguagem selecionada,
utilizando o gerenciador de pacotes padrão da mesma (e.g., PIP para Python,
Gems para Ruby, etc).

## Exemplos

```
(14:05) $ worktime 08:59
04:06 so far, exit at 18:29, 04:23 left
```

## Distribuição de linguagens

| Pessoa  | Linguagem |
| ------  | --------- |
| Andrei  | Lua       |
| Cássio  | Haskell   |
| Cleiton | Go        |
| Felipe  | Erlang    |
| Júlio   | Swift     |
| Lucas   | Scala     |
| Paulo   | Julia     |
| Vítor   | Rust      |
