# Lume

**Lume** é um aplicativo móvel educacional desenvolvido como Trabalho de Conclusão de Curso (TCC) do Curso Técnico em Informática do Colégio Univap - Unidade Centro. O projeto tem como objetivo atuar de forma complementar no processo de alfabetização e na promoção da autonomia social de indivíduos com Transtorno do Espectro Autista (TEA), com foco particular em níveis de suporte leve a moderado.

## Tecnologias Utilizadas

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)

* **Kotlin:** Linguagem de programação nativa, moderna e segura utilizada para o desenvolvimento da lógica e arquitetura do aplicativo.
* **Android Studio:** Ambiente de desenvolvimento integrado (IDE) oficial para o ecossistema Android, utilizado na construção de interfaces, emulação e depuração.
* **Firebase Realtime Database:** Banco de dados NoSQL baseado em nuvem para persistência, criptografia natural de credenciais e sincronização de dados em tempo real com suporte a modo offline.

## Principais Funcionalidades e Arquitetura

* **Estrutura Pedagógica:** O sistema é dividido em cinco unidades didáticas progressivas que englobam desde a escuta de vogais, consoantes, sílabas e palavras cotidianas até a formação e correção de frases e pontuação.
* **Regra de Adaptação por Nível de TEA:**
  * *Nível 1 (Suporte Leve):* Unidades totalmente liberadas desde o primeiro acesso para exploração autônoma.
  * *Nível 2 (Suporte Moderado):* Acesso progressivo liberado a partir da terceira unidade.
  * *Nível 3 (Suporte Substancial):* Percurso pedagógico sequencial e contínuo estruturado desde a primeira unidade.
* **Design Sensorial e Teoria das Cores:** Interface projetada com paleta de cores de baixa saturação (tons pastéis e gradientes suaves) para evitar sobrecarga sensorial e fadiga visual.
* **Recursos Adicionais:** 
  * Sistema de recuperação de senhas integrado ao Firebase.
  * Mecanismos de reforço positivo visual e sonoro com áudios locais calmantes (*brown noise*).
  * Telas tutoriais e botão de interrupção imediata para manejo de crises e sobrecargas sensoriais.

## Como Executar a Aplicação

1. Certifique-se de ter o **Android Studio** instalado em sua máquina.
2. Clone o repositório do projeto para o seu ambiente de trabalho local.
3. Abra o projeto no Android Studio.
4. Sincronize as dependências do projeto com o **Gradle**.
5. Configure a conexão com o **Firebase Realtime Database** utilizando as credenciais do projeto.
6. Inicie um emulador Android compatível ou conecte um dispositivo físico via USB com a depuração ativada.
7. Clique no botão **Run** (ícone de reprodução verde) para compilar e executar o aplicativo.
