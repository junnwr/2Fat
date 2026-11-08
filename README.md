<table>
  <tr>
    <td align="center">
      <video src="https://github.com/user-attachments/assets/3066e8b3-7704-447c-9161-5d3143e5101e" width="250" controls></video>
    </td>
    <td align="center">
      <video src="https://github.com/user-attachments/assets/ce039c6d-d7ad-4623-832b-d327cb5a9e37" width="250" controls></video>
    </td>
  </tr>
</table>

# 2Fat - Autenticador TOTP Seguro e Offline

Um aplicativo mobile Android que implementa autenticação de dois fatores (2FA) baseado no algoritmo TOTP (Time-Based One-Time Password) conforme RFC 6238, com criptografia AES-256 de banco de dados e operação totalmente offline.

## 🎯 Visão Geral

2Fat é um autenticador de dois fatores desenvolvido como Trabalho de Graduação do curso de Tecnologia em Segurança da Informação da Fatec Ourinhos, com foco em **segurança**, **privacidade** e **alinhamento aos princípios da LGPD**. O aplicativo gera códigos TOTP que se renovam a cada 30 segundos e armazena os secrets criptografados localmente no dispositivo, sem qualquer comunicação com servidores externos.

## ✨ Funcionalidades Principais

- ✅ **Geração de Códigos TOTP** - Códigos de 6 dígitos (HMAC-SHA1) que se renovam a cada 30 segundos
- ✅ **Adição via QR Code** - Escaneie códigos QR de serviços de autenticação (`otpauth://totp/...`)
- ✅ **Adição Manual** - Digite o secret diretamente em formato Base32
- ✅ **Criptografia AES-256** - Banco de dados criptografado com SQLCipher
- ✅ **Operação Offline** - Sem necessidade de conexão com a internet
- ✅ **Armazenamento Seguro** - Passphrase de 256 bits protegida pelo Android Keystore
- ✅ **Sem Backup em Nuvem** - Backup automático do Android desabilitado
- ✅ **Interface Material Design 3** - Tema escuro como padrão
- ✅ **Cópia de Códigos** - Copie para a área de transferência com um toque
- ✅ **Alinhamento à LGPD** - Minimização de dados, armazenamento local e privacidade desde a concepção

## 🛠️ Tecnologias Utilizadas

### Linguagem e Ferramentas

- **Kotlin** 1.9.24 - Linguagem principal
- **Android Studio** 2025 - IDE de desenvolvimento
- **Android Gradle Plugin (AGP)** 8.7.2 - Sistema de build
- **Java** 17 - Compatibilidade de compilação

### Dependências Principais

| Biblioteca | Versão | Finalidade |
|---|---|---|
| `androidx.room:room-runtime` / `room-ktx` | 2.6.1 | Persistência de dados |
| `net.zetetic:android-database-sqlcipher` | 4.5.4 | Criptografia do banco de dados (AES-256) |
| `androidx.security:security-crypto` | 1.1.0 | `EncryptedSharedPreferences` para a passphrase |
| `dev.turingcomplete:kotlin-onetimepassword` | 2.4.1 | Implementação TOTP (RFC 6238) |
| `commons-codec:commons-codec` | 1.16.0 | Codificação/decodificação Base32 |
| `com.journeyapps:zxing-android-embedded` | 4.3.0 | Leitura de QR Code |
| `com.google.android.material:material` | 1.11.0 | Material Design 3 |

### Segurança

- **SQLCipher** - Criptografia transparente AES-256 do banco de dados
- **Android Keystore** - Armazenamento seguro de material criptográfico
- **EncryptedSharedPreferences** - Armazenamento seguro da passphrase
- **SecureRandom** - Geração criptograficamente segura da passphrase

## 📋 Requisitos

- **Android Mínimo:** API 26 (Android 8.0)
- **Android Alvo:** API 36 (Android 16)
- **Compilação:** `compileSdk` 36
- **Espaço:** ~50 MB

## 🚀 Instalação

### Via APK

1. Baixe o arquivo APK mais recente em [Releases](https://github.com/junnwr/2Fat/releases)
2. Permita a instalação de fontes externas no seu dispositivo, se solicitado
3. Instale o APK e abra o aplicativo

### Via Compilação Local

1. Clone o repositório:

```bash
git clone https://github.com/junnwr/2Fat.git
cd 2Fat
```

2. Abra no Android Studio:
   - File → Open → Selecione a pasta `2Fat`
   - Aguarde o Gradle sincronizar

3. Compile e execute:
   - Conecte um dispositivo ou abra um emulador
   - Pressione `Shift + F10` ou clique em Run

## 📖 Como Usar

### Adicionar uma Conta

#### Via QR Code:
1. Abra o aplicativo
2. Toque no botão **"+"** (FAB)
3. Selecione **"Escanear QR Code"**
4. Conceda a permissão de câmera, se solicitado
5. Aponte a câmera para o QR Code do serviço
6. A conta será adicionada automaticamente

#### Manualmente:
1. Abra o aplicativo
2. Toque no botão **"+"** (FAB)
3. Selecione **"Adicionar Manualmente"**
4. Preencha:
   - **Emissor:** Serviço (ex: GitHub, Google)
   - **Nome da Conta:** Seu e-mail ou usuário
   - **Secret:** Chave Base32 fornecida pelo serviço
5. Toque em **"Adicionar Conta"**

### Usar os Códigos

1. Visualize suas contas na lista principal
2. Cada card mostra:
   - Emissor e nome da conta
   - Código TOTP atual (formato XXX XXX)
   - Tempo restante até a renovação (barra de progresso)
3. **Toque** no card para copiar o código para a área de transferência
4. **Toque longo** no card para excluir a conta (com confirmação)

## 🔐 Segurança e Privacidade

### Criptografia
- Todos os secrets TOTP são armazenados em um banco de dados criptografado com AES-256 (SQLCipher)
- A chave de criptografia é derivada de uma passphrase aleatória de 256 bits
- A derivação de chave do SQLCipher usa PBKDF2-HMAC-SHA512 com 256.000 iterações (configuração padrão)

### Operação Offline
- O aplicativo **não declara a permissão `INTERNET`** e não se conecta à rede
- Não há sincronização em nuvem
- O backup automático do Android está desabilitado (`android:allowBackup="false"`)
- Os dados permanecem exclusivamente no dispositivo; ao desinstalar o aplicativo, o banco local é removido pelo sistema

### Alinhamento à LGPD
- Coleta apenas os dados estritamente necessários (emissor, nome da conta e secret)
- Sem geolocalização, identificadores de dispositivo ou metadados de uso
- Sem envio de dados a terceiros
- Código-fonte público, permitindo auditoria

### Android Keystore
- A passphrase do banco de dados é protegida pelo Android Keystore, por meio do `EncryptedSharedPreferences`
- Em dispositivos com hardware seguro (TEE), o material criptográfico fica isolado do sistema

### Escopo e Limitações
O modelo de ameaças considera acesso físico não autorizado ao dispositivo e tentativas de leitura do arquivo de banco de dados sem a passphrase. Ficam fora do escopo: dispositivos com acesso *root*, firmware comprometido, extração física avançada de memória e vulnerabilidades do próprio sistema Android.

Limitações conhecidas:
- Não há função de backup e restauração de contas (decisão de projeto para reduzir a superfície de ataque)
- Não há bloqueio biométrico de abertura do aplicativo
- Os testes têm caráter funcional básico, sem ferramentas formais de análise estática ou dinâmica e sem testes de penetração
- A biblioteca `androidx.security:security-crypto` foi descontinuada pelo Google; a migração para o uso direto do Android Keystore é um possível trabalho futuro

## 📱 Arquitetura

```
┌─────────────────────────────────────┐
│   Camada de Apresentação            │
│  MainActivity, AddAccountActivity   │
│        AccountAdapter (UI)          │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│   Camada de Lógica de Negócio       │
│   Geração TOTP (RFC 6238)           │
│   HMAC-SHA1, Base32, Timestamp      │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│   Camada de Persistência            │
│   Room Database + AccountDao        │
│   TotpAccount Entity                │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│   Camada de Segurança               │
│   SQLCipher (AES-256)               │
│   Android Keystore, SecureRandom    │
└─────────────────────────────────────┘
```

## 📂 Estrutura do Projeto

```
2Fat/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/junnwr/fat/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── AddAccountActivity.kt
│   │   │   │   ├── AccountAdapter.kt
│   │   │   │   ├── ui/
│   │   │   │   └── db/
│   │   │   │       ├── AppDatabase.kt
│   │   │   │       ├── TotpAccount.kt
│   │   │   │       └── AccountDao.kt
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   ├── activity_add_account.xml
│   │   │   │   │   └── item_account.xml
│   │   │   │   ├── values/
│   │   │   │   ├── xml/
│   │   │   │   ├── mipmap/
│   │   │   │   └── drawable/
│   │   │   └── AndroidManifest.xml
│   │   ├── test/
│   │   └── androidTest/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── LICENSE
└── README.md
```

## 🧪 Testes

Os testes realizados têm caráter funcional básico e de verificação de segurança, delimitados pelo modelo de ameaças do trabalho.

### Testes Funcionais
- ✅ Geração correta de códigos TOTP, validados contra implementações de referência
- ✅ Renovação de códigos a cada 30 segundos
- ✅ Adição de contas via QR Code
- ✅ Adição manual de contas com secrets Base32 válidos
- ✅ Exibição do tempo restante com atualização em tempo real
- ✅ Cópia do código para a área de transferência
- ✅ Exclusão de contas com confirmação

### Testes de Segurança
- ✅ Arquivo de banco de dados não abre em ferramenta de inspeção SQLite sem a passphrase
- ✅ Passphrase armazenada no `EncryptedSharedPreferences`
- ✅ Passphrase gerada com `SecureRandom`
- ✅ Operação com todas as conexões de rede desativadas

## 📚 Documentação Acadêmica

Este projeto foi desenvolvido como **Trabalho de Graduação (TG)** do curso de Tecnologia em Segurança da Informação.

- **Título:** "Desenvolvimento de Aplicativo Mobile para Autenticação de Dois Fatores TOTP com Criptografia de Banco de Dados: Uma Abordagem Segura e Offline"
- **Autor:** Marcio Junior da Silva
- **Orientador:** Prof. Esp. Tiago Martins Ferreira
- **Instituição:** Faculdade de Tecnologia de Ourinhos (Fatec Ourinhos)
- **Áreas:** Segurança da Informação, Desenvolvimento Mobile, Criptografia

## 📖 Referências Principais

- M'RAIHI, D. et al. **RFC 6238** - TOTP: Time-Based One-Time Password Algorithm. IETF, 2011.
- M'RAIHI, D. et al. **RFC 4226** - HOTP: An HMAC-Based One-Time Password Algorithm. IETF, 2005.
- BRASIL. **Lei nº 13.709/2018** - Lei Geral de Proteção de Dados Pessoais (LGPD).
- GILSENAN, C. et al. Security and privacy failures in popular 2FA apps. In: **32nd USENIX Security Symposium**, 2023.
- BRASIL. Gabinete de Segurança Institucional da Presidência da República. **Autenticação Multifator (MFA) e seus desafios**. Brasília, 2023.
- BIONI, B. R. **Proteção de Dados Pessoais: a função e os limites do consentimento**. 2. ed. Rio de Janeiro: Forense, 2020.
- MENEZES, A.; OORSCHOT, P. v.; VANSTONE, S. **Manual de Criptografia Aplicada**. Rio de Janeiro: LTC, 2018.
- ZETETIC LLC. **SQLCipher** - Full Database Encryption for SQLite. 2024.

## 📄 Licença

Este projeto está licenciado sob a **MIT License** - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 🤝 Contribuições

Contribuições são bem-vindas! Para contribuir:

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Faça commit das suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Envie para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 🐛 Reportar Issues

Se encontrar bugs ou problemas, abra uma issue em [Issues](https://github.com/junnwr/2Fat/issues).

## 📞 Contato

- **Autor:** Marcio Junior da Silva
- **E-mail:** marcio.silva11@aluno.cps.sp.gov.br
- **GitHub:** [@junnwr](https://github.com/junnwr)

## 🙏 Agradecimentos

- Faculdade de Tecnologia de Ourinhos (Fatec Ourinhos)
- Prof. Esp. Tiago Martins Ferreira
- Internet Engineering Task Force (IETF) - RFC 6238
- Comunidade de desenvolvimento Android

---

**Desenvolvido com ❤️ para segurança e privacidade**
