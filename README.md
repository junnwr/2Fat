# 2Fat - Autenticador TOTP Seguro e Offline

Um aplicativo mobile Android que implementa autenticação de dois fatores (2FA) baseado no algoritmo TOTP (Time-Based One-Time Password) conforme RFC 6238, com criptografia AES-256 de banco de dados e operação totalmente offline.

## 🎯 Visão Geral

2Fat é um autenticador de dois fatores desenvolvido como trabalho de graduação em Tecnologia, focando em **segurança**, **privacidade** e **conformidade com a LGPD**. O aplicativo gera códigos TOTP que se renovam a cada 30 segundos e armazena secrets criptografados localmente no dispositivo.

## ✨ Funcionalidades Principais

- ✅ **Geração de Códigos TOTP** - Códigos de 6 dígitos que se renovam a cada 30 segundos
- ✅ **Adição via QR Code** - Escaneie códigos QR de serviços de autenticação
- ✅ **Adição Manual** - Digite o secret diretamente em formato Base32
- ✅ **Criptografia AES-256** - Banco de dados totalmente criptografado com SQLCipher
- ✅ **Operação Offline** - Sem necessidade de conexão com internet
- ✅ **Armazenamento Seguro** - Passphrase gerenciada pelo Android Keystore
- ✅ **Interface Material Design 3** - Tema escuro otimizado
- ✅ **Cópia de Códigos** - Copie para clipboard com um toque
- ✅ **Conformidade LGPD** - Minimização de dados, armazenamento local, privacidade por design

## 🛠️ Tecnologias Utilizadas

### Linguagem e Framework
- **Kotlin** 1.9.24 - Linguagem principal
- **Android Studio** 2025 - IDE de desenvolvimento
- **Gradle** 8.7.2 - Build system

### Dependências Principais
- **androidx.room:room-runtime** - Persistência de dados
- **net.zetetic:android-database-sqlcipher** - Criptografia de banco de dados (AES-256)
- **androidx.security:security-crypto** - Gerenciamento seguro de chaves
- **dev.turingcomplete:kotlin-onetimepassword** - Implementação TOTP (RFC 6238)
- **com.journeyapps:zxing-android-embedded** - Scanner de QR Code
- **com.google.android.material:material** - Material Design 3

### Segurança
- **SQLCipher** - Criptografia transparente AES-256
- **Android Keystore** - Armazenamento de chaves seguro
- **EncryptedSharedPreferences** - Armazenamento seguro de preferências
- **SecureRandom** - Geração de passphrases criptograficamente seguras

## 📋 Requisitos

- **Android Mínimo:** API 26 (Android 8.0)
- **Android Alvo:** API 34 (Android 14)
- **RAM:** 100 MB disponível
- **Espaço:** ~50 MB

## 🚀 Instalação

### Via APK
1. Baixe o arquivo APK mais recente em [Releases](https://github.com/junnwr/2fat/releases)
2. Instale no seu dispositivo Android
3. Abra o aplicativo

### Via Compilação Local
1. Clone o repositório:
```bash
git clone https://github.com/seuusuario/2fat.git
cd 2fat
```

2. Abra no Android Studio:
   - File → Open → Selecione a pasta `2fat`
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
4. Aponte a câmera para o QR Code do serviço
5. A conta será adicionada automaticamente

#### Manualmente:
1. Abra o aplicativo
2. Toque no botão **"+"** (FAB)
3. Selecione **"Adicionar Manualmente"**
4. Preencha:
   - **Emissor:** Serviço (ex: GitHub, Google)
   - **Nome da Conta:** Seu email ou usuário
   - **Secret:** Chave Base32 fornecida pelo serviço
5. Toque em **"Adicionar Conta"**

### Usar os Códigos

1. Visualize suas contas na lista principal
2. Cada card mostra:
   - Emissor e nome da conta
   - Código TOTP atual (formato XXX XXX)
   - Tempo restante até renovação (barra de progresso)
3. **Toque** no card para copiar o código para clipboard
4. **Toque longo** no card para deletar a conta

## 🔐 Segurança e Privacidade

### Criptografia
- Todos os secrets TOTP são armazenados criptografados com AES-256
- A chave de criptografia é derivada de uma passphrase de 256 bits
- Derivação de chave usa PBKDF2-HMAC-SHA512 com 256.000 iterações

### Operação Offline
- O aplicativo **não se conecta** à internet
- Não há sincronização em nuvem
- Seus dados permanecem exclusivamente no seu dispositivo

### Conformidade LGPD
- Coleta apenas dados necessários (secrets, nomes de contas)
- Armazenamento local exclusivo
- Sem envio de dados para terceiros
- Usuário mantém controle total dos dados

### Android Keystore
- A passphrase do banco de dados é protegida pelo Android Keystore
- Em dispositivos com hardware seguro (TEE), a chave é isolada de software

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
2fat/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/junnwr/fat/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── AddAccountActivity.kt
│   │   │   │   ├── AccountAdapter.kt
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
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   └── drawable/
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

## 🧪 Testes

### Testes de Compatibilidade
- ✅ API 26 (Android 8.0)
- ✅ API 28 (Android 9)
- ✅ API 30 (Android 11)
- ✅ API 34 (Android 14)

### Testes de Segurança
- ✅ Banco de dados criptografado (não acessível sem passphrase)
- ✅ Armazenamento seguro de passphrase
- ✅ Geração de passphrases com entropia adequada
- ✅ Operação offline (sem conexões de rede)

### Testes Funcionais
- ✅ Geração correta de códigos TOTP
- ✅ Renovação de códigos a cada 30 segundos
- ✅ Adição via QR Code
- ✅ Adição manual de contas
- ✅ Cópia de código para clipboard
- ✅ Exclusão de contas

## 📚 Documentação Acadêmica

Este projeto foi desenvolvido como **Trabalho de Graduação (TG)** em Tecnologia. A documentação acadêmica completa está disponível em:

- **Título:** "Desenvolvimento de Aplicativo Mobile para Autenticação de Dois Fatores TOTP com Criptografia de Banco de Dados: Uma Abordagem Segura e Offline"
- **Instituição:** Faculdade de Tecnologia de Ourinhos (Fatec Ourinhos)
- **Áreas:** Segurança da Informação, Desenvolvimento Mobile, Criptografia

## 📖 Referências Principais

- RFC 6238 - TOTP: Time-Based One-Time Password Algorithm
- RFC 4226 - HOTP: An HMAC-Based One-Time Password Algorithm
- Lei nº 13.709/2018 - Lei Geral de Proteção de Dados (LGPD)
- Stallings, W. - Criptografia e Segurança de Redes: Princípios e Práticas
- Deitel, P. & Deitel, H. - Android: Como Programar

## 📄 Licença

Este projeto está licenciado sob a **MIT License** - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 🤝 Contribuições

Contribuições são bem-vindas! Para contribuir:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 🐛 Reportar Issues

Se encontrar bugs ou problemas, abra uma issue em [Issues](https://github.com/junnwr/2fat/issues).

## 📞 Contato

- **Autor:** [Marcio Jr.]
- **Email:** [marcio.silva168@fatec.sp.gov.br]
- **GitHub:** [@junnwr](https://github.com/junnwr)

## 🙏 Agradecimentos

- Faculdade de Tecnologia de Ourinhos (Fatec Ourinhos)
- Tiago Martins Ferreira
- Internet Engineering Task Force (IETF) - RFC 6238
- Android Development Community

---

**Desenvolvido com ❤️ para segurança e privacidade**
