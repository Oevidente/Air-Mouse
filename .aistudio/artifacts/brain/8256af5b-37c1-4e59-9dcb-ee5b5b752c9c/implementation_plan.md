# Air Mouse - Teclado Integrado no Deck de Atalhos

Plano de implementação para adicionar funcionalidade completa de teclado remoto na aba de Atalhos & Macros do Air Mouse, permitindo digitação ágil (modo ao vivo ou envio em bloco) e teclas essenciais de navegação diretamente no celular para controle do PC.

### User Review & Critical Decisions

> [!IMPORTANT]
> **Decisões confirmadas pelo usuário:**
> - **Interface do Teclado**: Barra rápida com campo de texto elegante e teclas essenciais posicionada no topo da aba de Atalhos, permitindo digitar sem esconder ou obstruir a grade de atalhos e macros.
> - **Modo de Envio**: Modo duplo flexível — botão alternador permitindo escolher entre:
>   - *Envio ao Vivo*: cada caractere ou palavra é transmitido em tempo real conforme digitado.
>   - *Envio em Bloco*: digitação livre no campo com envio do texto completo ao tocar no botão Enviar ou pressionar Enter no teclado virtual.
> - **Teclas Especiais & Rápidas**: Barra de chips tácteis com as teclas essenciais solicitadas: **Esc**, **Tab**, **Espaço**, **Backspace**, **Enter** e **Setas direcionais (▲, ▼, ◄, ►)**.
> - **Versionamento & APK**: Incremento para `versionCode = 5` e `versionName = "1.0.4"`, gerando o arquivo executável `/apk/air-mouse-v1.0.4.apk` e `/apk/app-debug.apk`.

---

### 1. Overview & Core Concept

- **O que faz**: Adiciona um módulo de teclado interativo na tela `ShortcutsScreen` do Air Mouse. O usuário pode digitar mensagens, códigos, URLs ou comandos rapidamente e despachá-los para o PC, além de acionar teclas de navegação física (Esc, Tab, Enter, Backspace, Setas) com feedback háptico.
- **Público-alvo**: Usuários que controlam o PC à distância (cama, sofá, apresentação) e precisam digitar termos de busca no YouTube/Netflix, navegar em formulários ou fechar janelas sem precisar levantar para ir ao teclado físico.
- **Valor Principal**: Elimina a necessidade de alternar de tela para digitar, unindo o deck de macros e automação com digitação de texto direta e de baixíssima latência.

---

### 2. User Experience & Visual Design

- **Barra de Digitação Rápida (`KeyboardQuickBar`)**:
  - Posicionada logo abaixo do cabeçalho de atalhos e acima das categorias de filtros.
  - Campo de texto translúcido com estilo dark glassmorphic, bordas sutis neon (`EmeraldNeon` ou `BorderSubtle`) e cantos arredondados de 14.dp.
  - Ícone de teclado à esquerda com indicação de foco.
  - Botão de envio rápido estilizado com ícone `Send` e botão de limpar campo (`Clear`).
  - Switch/Pill seletor discreto para alternar entre modo **"Ao Vivo"** (Live) e **"Bloco"** (Ao Enviar).
- **Fileira de Teclas Essenciais (`QuickKeysToolbar`)**:
  - Fileira horizontal com rolagem suave contendo botões tácteis compactos estilo chave mecânica:
    - `[ ESC ]` `[ TAB ]` `[ ESPAÇO ]` `[ ⌫ BACKSPACE ]` `[ ⏎ ENTER ]`
    - Bloco direcional: `[ ◄ ]` `[ ▲ ]` `[ ▼ ]` `[ ► ]`
  - Feedback tátil com vibração háptica (`hapticManager.click()`) a cada toque.
- **Ajuste Ergonômico Mobile**:
  - Campo compatível com `imeAction = ImeAction.Send` para que o próprio botão Enter do teclado do Android faça o envio do texto em bloco.
  - A grade de atalhos e macros continua acessível logo abaixo, rolando suavemente quando o teclado virtual é aberto (`imePadding` ou rolagem fluida).

---

### 3. Key Product Decisions & Trade-Offs

- **Posicionamento na Aba de Atalhos**:
  - *Abordagem*: Integrar a barra rápida diretamente no topo de `ShortcutsScreen`, com visual compacto e retrátil/expansível caso o usuário queira minimizar para focar apenas na grade de macros.
  - *Por quê*: Proporciona acesso imediato sem adicionar mais uma aba na barra inferior de navegação móvel, mantendo a navegação limpa e fluida.
- **Protocolo de Comunicação de Texto**:
  - *Abordagem*: Reutilizar o método `sendText(text)` (payload TCP `{"t":"type","text":"..."}`) já implementado com sucesso no cliente Android e suportado por `botanika_receiver.py` (`pyautogui.write` / `keyboard.type`), além de disparar `sendKey(combo)` para teclas individuais (`esc`, `tab`, `space`, `backspace`, `enter`, `up`, `down`, `left`, `right`).
  - *Por quê*: Garante latência mínima, total compatibilidade com o servidor receptor existente e zero consumo de serviços externos ou nuvem.
- **Geração de APK e Versionamento**:
  - *Abordagem*: Atualizar `build.gradle.kts` para versão `1.0.4` (code 5) e compilar APK salvando na pasta `/apk/`.

---

### 4. Technical Architecture & Data Strategy

```
┌─────────────────────────────────────────────────────────────┐
│                      ShortcutsScreen                        │
├─────────────────────────────────────────────────────────────┤
│  ┌───────────────────────────────────────────────────────┐  │
│  │  Top Header: DECK DE ATALHOS & MACROS + [+ Novo]      │  │
│  └───────────────────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  KeyboardQuickBar (Novo Módulo de Teclado)            │  │
│  │  [⌨ Digitar no PC...] [Toggle: Ao Vivo/Bloco] [Enviar] │  │
│  │  QuickKeysRow: [Esc] [Tab] [Space] [⌫] [⏎] [◄ ▲ ▼ ►]   │  │
│  └───────────────────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  CategoryFilterChips (Todos, Apresentação, Mídia...)  │  │
│  └───────────────────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  LazyVerticalGrid (Grade de Cartões de Atalhos)       │  │
│  └───────────────────────────────────────────────────────┘  │
└───────────────────────────────┬─────────────────────────────┘
                                │ onSendText / onSendKey
                                ▼
┌─────────────────────────────────────────────────────────────┐
│                      RemoteViewModel                        │
│   sendText(text) ───► remoteClient.sendText(text) [TCP]     │
│   sendKey(combo) ───► remoteClient.sendKey(combo) [TCP]     │
└───────────────────────────────┬─────────────────────────────┘
                                │ Socket TCP (Port 5006)
                                ▼
┌─────────────────────────────────────────────────────────────┐
│             PC: botanika_receiver.py / air_mouse            │
│   t: "type"  ──► pyautogui.write(text) / keyboard.type()    │
│   t: "key"   ──► KEY_MAP["esc", "tab", "space", "enter"]    │
└─────────────────────────────────────────────────────────────┘
```

#### Modificações de Arquivos Planejadas:
1. `app/src/main/java/com/example/ui/components/KeyboardQuickBar.kt`: Novo componente com visual glassmorphic contendo o campo de texto, botão de envio, chave de alternância ao vivo/bloco e atalhos rápidos tácteis (`esc`, `tab`, `space`, `backspace`, `enter`, setas).
2. `app/src/main/java/com/example/ui/screens/ShortcutsScreen.kt`: Integração de `KeyboardQuickBar` no topo da tela, repassando `onSendText` e `onSendKey`.
3. `app/src/main/java/com/example/MainActivity.kt`: Passagem de `viewModel.sendText(it)` para o `ShortcutsScreen`.
4. `app/build.gradle.kts`: Atualização de `versionCode = 5` e `versionName = "1.0.4"`.
5. Compilação e cópia do novo APK para `/apk/air-mouse-v1.0.4.apk` e `/apk/app-debug.apk`.
