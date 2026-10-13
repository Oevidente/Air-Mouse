package com.example.data.local

object DefaultShortcuts {
    fun getInitialShortcuts(): List<ShortcutEntity> = listOf(
        // Slides
        ShortcutEntity(
            title = "Iniciar Slides",
            category = "Apresentação",
            keyCombo = "F5",
            iconKey = "presentation",
            colorHex = "#00E676",
            isSystemDefault = true,
            orderIndex = 0
        ),
        ShortcutEntity(
            title = "Slide Atual",
            category = "Apresentação",
            keyCombo = "SHIFT+F5",
            iconKey = "play",
            colorHex = "#2979FF",
            isSystemDefault = true,
            orderIndex = 1
        ),
        ShortcutEntity(
            title = "Tela Preta",
            category = "Apresentação",
            keyCombo = "B",
            iconKey = "brightness",
            colorHex = "#757575",
            isSystemDefault = true,
            orderIndex = 2
        ),
        ShortcutEntity(
            title = "Sair Slide",
            category = "Apresentação",
            keyCombo = "ESC",
            iconKey = "close",
            colorHex = "#FF5252",
            isSystemDefault = true,
            orderIndex = 3
        ),
        // Multimídia
        ShortcutEntity(
            title = "Play / Pause",
            category = "Multimídia",
            keyCombo = "SPACE",
            iconKey = "play_pause",
            colorHex = "#00E676",
            isSystemDefault = true,
            orderIndex = 4
        ),
        ShortcutEntity(
            title = "Mudo / Silenciar",
            category = "Multimídia",
            keyCombo = "MUTE",
            iconKey = "volume_off",
            colorHex = "#FF5252",
            isSystemDefault = true,
            orderIndex = 5
        ),
        ShortcutEntity(
            title = "Aumentar Volume",
            category = "Multimídia",
            keyCombo = "VOL_UP",
            iconKey = "volume_up",
            colorHex = "#2979FF",
            isSystemDefault = true,
            orderIndex = 6
        ),
        ShortcutEntity(
            title = "Diminuir Volume",
            category = "Multimídia",
            keyCombo = "VOL_DOWN",
            iconKey = "volume_down",
            colorHex = "#2979FF",
            isSystemDefault = true,
            orderIndex = 7
        ),
        // Produtividade & Sistema
        ShortcutEntity(
            title = "Alternar Janelas",
            category = "Produtividade",
            keyCombo = "ALT+TAB",
            iconKey = "flip",
            colorHex = "#FFAB00",
            isSystemDefault = true,
            orderIndex = 8
        ),
        ShortcutEntity(
            title = "Mostrar Desktop",
            category = "Produtividade",
            keyCombo = "WIN+D",
            iconKey = "desktop",
            colorHex = "#00E5FF",
            isSystemDefault = true,
            orderIndex = 9
        ),
        ShortcutEntity(
            title = "Print Screen",
            category = "Produtividade",
            keyCombo = "WIN+SHIFT+S",
            iconKey = "crop",
            colorHex = "#E040FB",
            isSystemDefault = true,
            orderIndex = 10
        ),
        ShortcutEntity(
            title = "Desfazer (Undo)",
            category = "Produtividade",
            keyCombo = "CTRL+Z",
            iconKey = "undo",
            colorHex = "#FF9100",
            isSystemDefault = true,
            orderIndex = 11
        ),
        ShortcutEntity(
            title = "Copiar",
            category = "Produtividade",
            keyCombo = "CTRL+C",
            iconKey = "copy",
            colorHex = "#64DD17",
            isSystemDefault = true,
            orderIndex = 12
        ),
        ShortcutEntity(
            title = "Colar",
            category = "Produtividade",
            keyCombo = "CTRL+V",
            iconKey = "paste",
            colorHex = "#64DD17",
            isSystemDefault = true,
            orderIndex = 13
        ),
        ShortcutEntity(
            title = "Gerenciador Tarefas",
            category = "Sistema",
            keyCombo = "CTRL+SHIFT+ESC",
            iconKey = "build",
            colorHex = "#FF1744",
            isSystemDefault = true,
            orderIndex = 14
        ),
        ShortcutEntity(
            title = "Tela Cheia",
            category = "Sistema",
            keyCombo = "F11",
            iconKey = "fullscreen",
            colorHex = "#00B0FF",
            isSystemDefault = true,
            orderIndex = 15
        )
    )
}
