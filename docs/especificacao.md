# ReTech — especificação inicial para o PDF

## Contexto
A ReTech é uma CitizenTech proposta para São Paulo que estimula a destinação consciente de pequenos equipamentos eletrônicos e busca ampliar o reaproveitamento por instituições parceiras.

## Problema
O descarte incorreto de eletrônicos e a desigualdade de acesso a equipamentos tecnológicos são desafios urbanos. O projeto propõe uma máquina de coleta integrada a uma API capaz de registrar descartes e recompensar cidadãos.

## Objetivo geral
Implementar uma API RESTful em Java com Spring Boot e Oracle FIAP para registrar usuários, máquinas, categorias, instituições e descartes.

## Objetivos específicos
- Aplicar arquitetura em camadas Model, DAO, BO, Controller e tratamento de exceções.
- Manter as cinco tabelas e as quatro relações do DER definido pelo grupo.
- Pontuar registros conforme categoria e quantidade, atualizando o saldo do cidadão de forma transacional.
- Disponibilizar operações HTTP GET, POST, PUT e DELETE.
- Apresentar indicadores de descarte e de participação para o protótipo.

## Limitações
A pontuação é uma simulação e o sistema não confirma reconhecimento por câmera, condição real do eletrônico ou efetivação da reciclagem. O DER atual não registra conclusão física, nem permite resgatar pontos ou autenticar usuários.

## Para finalizar o documento
Adicionar capa com nomes completos/RMs, índice, diagrama de classes, DER, prints de protótipos, tabela de endpoints, passo a passo, versões, GitHub e URL do Render. Consultar `README.md` e `docs/checklist-entrega.md`.
