# Air Mouse - Correções de QA: Atalhos, Persistência, Apresentação e Identidade

Plano de ação para atender integralmente ao relatório de QA: eliminação de atalhos duplicados com garantia de integridade, persistência total de preferências no armazenamento do dispositivo, controle opcional para apresentação de slides e padronização oficial do nome **Air Mouse**.

### User Review & Critical Decisions

> [!IMPORTANT]
> **Decisões confirmadas pelo usuário:**
> - **Controles de Apresentação de Slides**: Desativados por padrão. Um switch nas configurações ("Habilitar controles de apresentação de slides") permite ativá-los a qualquer momento.
> - **Comportamento da Navegação**: Quando desativados, a aba de apresentação/mídia fica oculta da barra de navegação flutuante, deixando a interface limpa e focada no Air Mouse, Trackpad, Atalhos e PC.
> - **Persistência das Configurações**: Todas as preferências (sensibilidade do giroscópio, sensibilidade do trackpad, inversão de eixos X/Y, feedback háptico, IP/portas do PC e o estado do switch de slides) serão salvas de maneira atômica e restauradas automaticamente na inicialização.
> - **Identidade Oficial**: O aplicativo passa a se chamar oficialmente **Air Mouse** no sistema Android, no launcher, nas barras de título e nos metadados.

---

### 1. Overview & Core Concept

O **Air Mouse** transforma o smartphone em um controlador remoto de alta precisão para computadores, utilizando os sensores inerciais (giroscópio) e conexões locais de baixa latência (UDP/TCP). Esta atualização foca na robustez das configurações, consistência de dados e refinamento da experiência do usuário, garantindo que o aplicativo mantenha todas as preferências definidas e apresente uma interface limpa e organizada.

---

### 2. User Experience & Visual Design

- **Experiência de Configurações (Aba PC / Conexão)**:
  - Adição de novo cartão "Apresentação & Recursos" com switch dedicado: *Habilitar controles de apresentação de slides*.
  - Indicação clara de status e persistência imediata ao alterar qualquer valor (sensibilidade, switches, IP ou portas).
- **Barra de Navegação Adaptativa (`IosFloatingNavBar`)**:
  - Quando os controles de apresentação estiverem desativados (padrão), a barra exibe 4 itens essenciais: **Air Mouse**, **Trackpad**, **Atalhos** e **PC**.
  - Quando ativados pelo usuário, a aba **Mídia / Apresentação** é reintroduzida suavemente na barra.
- **Deck de Atalhos e Macros Organizado**:
  - Remoção de duplicidades visuais e estruturais.
  - Grade com cada atalho listado uma única vez, categorizado e com layout consistente.
- **Identidade Visual Consistente**:
  - Nome visível no launcher do Android: **Air Mouse**.
  - Título superior das telas principais e das configurações refletindo a marca oficial.

---

### 3. Key Product Decisions & Trade-Offs

- **Deduplicação de Atalhos (Causa Raiz & Solução)**:
  - *Causa*: O banco Room possuía inserção assíncrona duplicada no callback `onCreate` do banco combinada com chamada redundante em `ensureDefaultShortcuts()` no `init` do ViewModel, sem restrição de unicidade.
  - *Solução*: Unificar a carga inicial garantindo idempotência e adicionar rotina de deduplicação na inicialização do repositório para limpar quaisquer registros duplicados preexistentes, mantendo apenas 1 ocorrência por combinação de comando/título.
- **Mecanismo de Persistência das Configurações**:
  - *Abordagem*: Criar camada de persistência local dedicada (`UserPreferencesRepository` / `SharedPreferences`) para armazenar:
    - Sensibilidade do giroscópio (`gyroSensitivity`)
    - Sensibilidade do touchpad (`touchpadSensitivity`)
    - Inversão de eixo X (`invertX`) e eixo Y (`invertY`)
    - Feedback háptico (`hapticsEnabled`)
    - Exibição de controles de apresentação (`showPresentationControls`)
    - Último IP e portas configurados (`serverIp`, `udpPort`, `tcpPort`)
  - *Vantagem*: Acesso síncrono instantâneo no arranque do app (sem delay de leitura), sem consumo de cotas de rede ou banco em nuvem, e sobrevivência total a reinicializações e updates.
- **Compatibilidade e Versionamento**:
  - Incremento de versão para `versionCode = 4` e `versionName = "1.0.3"`.
  - Geração da APK atualizada salva na pasta `/apk/` (`air-mouse-v1.0.3.apk` e `app-debug.apk`).

---

### 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Air Mouse UI (Jetpack Compose)                  │
└──────────────┬───────────────────────────┬─────────────────────────────┘
               │                           │
       ┌───────▼────────┐         ┌────────▼────────────────┐
       │   Air Mouse    │         │ Configurações / PC      │
       │   & Trackpad   │         │ (Switches, Sensib., IP) │
       └───────┬────────┘         └────────┬────────────────┘
               │                           │
               └─────────────┬─────────────┘
                             │
                    ┌────────▼────────┐
                    │ RemoteViewModel │
                    └────┬────────┬───┘
                         │        │
         ┌───────────────┘        └─────────────────┐
         ▼                                          ▼
┌─────────────────────────────┐        ┌───────────────────────────────┐
│     UserPreferences         │        │       BotanikaDatabase        │
│ (Sensibilidade, Inversão,   │        │     (Atalhos Únicos &         │
│  Haptics, Apresentação ON)  │        │      Histórico de PCs)        │
└─────────────────────────────┘        └───────────────────────────────┘
```

#### Mapeamento de Modificações por Módulo:

1. **Persistência de Preferências**:
   - Criar `UserPreferencesManager.kt` para persistir e carregar todas as opções do usuário.
   - Atualizar `RemoteViewModel.kt` para carregar as preferências no arranque e sincronizar imediatamente a cada alteração.
2. **Deduplicação de Atalhos**:
   - Corrigir `BotanikaDatabase.kt` e `RemoteRepository.kt` eliminando concorrência de seeding.
   - Adicionar método `deduplicateShortcuts()` que remove itens repetidos do banco SQLite local preservando atalhos customizados.
3. **Controles de Apresentação Opcionais**:
   - Adicionar flag `showPresentationControls: Boolean = false` no estado do ViewModel e na persistência.
   - Atualizar `ConnectionSettingsScreen.kt` com Switch para habilitar/desabilitar controles de slides.
   - Atualizar `IosFloatingNavBar.kt` e `MainActivity.kt` para exibir ou ocultar a aba de Mídia condicionalmente conforme a preferência.
4. **Renomeação para "Air Mouse"**:
   - `app/src/main/res/values/strings.xml`: `<string name="app_name">Air Mouse</string>`
   - `metadata.json`: `"name": "Air Mouse"`
   - `settings.gradle.kts`: `rootProject.name = "Air Mouse"`
   - Textos da interface que referenciam o nome anterior.
5. **Build, Testes e Geração de APK**:
   - Atualizar versão no `app/build.gradle.kts` para `versionCode = 4` e `versionName = "1.0.2"` -> `"1.0.3"`.
   - Executar `compile_applet` para validação e salvar a APK gerada em `/apk/air-mouse-v1.0.3.apk`.
