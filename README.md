# Compilador Simple

[![Java](https://img.shields.io/badge/language-Java-blue.svg)](https://www.java.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Compilador desenvolvido em Java para a linguagem **Simple**, uma linguagem educacional inspirada em versões iniciais da linguagem **BASIC**. Este projeto segue as principais diretrizes do processo de compilação, com base no livro _Crafting Interpreters_, de **Robert Nystrom**.


## 📘 Sobre a Linguagem Simple

A linguagem **Simple** foi projetada com fins didáticos e possui as seguintes características:

- Estrutura de comandos numerados (como `10 print "Hello"`).
- Variáveis com nomes de uma única letra (`A`, `B`, `X`, etc.).
- Suporte a comandos básicos como:
  - `rem` (comentário)
  - `input`
  - `let`
  - `print`
  - `goto`
  - `if` ... `goto`
  - `end`
- Operações apenas com inteiros.
- A linguagem ignora letras maiúsculas em comentários (`rem`), mas as trata como erro em outras instruções.

---

## 💡 Exemplos de Código Simple

### 📥 Exemplo 1 — Entrada, processamento e saída

```basic
10 rem Solicita um número, soma 10 e imprime o resultado
20 input A
30 let B = A + 10
40 print B
50 end
```

### 📥 Exemplo 2 — Loop usando goto
```basic
10 rem Contador de 1 a 5
20 let A = 1
30 print A
40 let A = A + 1
50 if A <= 5 goto 30
60 end
```

### 📥 Exemplo 2 — Condicional if ... goto
```basic
10 rem Verifica se um número é maior que 10
20 input A
30 if A > 10 goto 50
40 print A
45 goto 60
50 print 999
60 end
```

## 🚀 Como Executar

O codigo esta hospedado no seguinte endereço:
https://www.onlinegdb.com/dh-JjH5GC.