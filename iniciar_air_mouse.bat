@echo off
title Air Mouse - Servidor Receptor PC
color 0A
echo ========================================================
echo        AIR MOUSE - INICIALIZADOR RAPIDO PARA PC
echo ========================================================
echo.

where python >nul 2>nul
if %errorlevel% neq 0 (
    where py >nul 2>nul
    if %errorlevel% neq 0 (
        color 0C
        echo [AVISO] O Python nao foi encontrado instalado neste computador!
        echo.
        echo Como resolver em 1 minuto:
        echo 1. Abra o Prompt de Comando (CMD) e digite: winget install Python.Python.3.11
        echo    OU baixe o instalador oficial em: https://www.python.org/downloads/
        echo    (IMPORTANTE: Marque a caixinha "Add Python to PATH" ao instalar!)
        echo.
        pause
        exit /b 1
    ) else (
        set PY_CMD=py
    )
) else (
    set PY_CMD=python
)

echo [1/2] Verificando dependencias necessarias (pynput, pyautogui)...
%PY_CMD% -m pip install --quiet pynput pyautogui

echo.
echo [2/2] Iniciando o servidor Air Mouse...
echo ========================================================
echo.

if exist "air_mouse_receiver.py" (
    %PY_CMD% air_mouse_receiver.py
) else if exist "botanika_receiver.py" (
    %PY_CMD% botanika_receiver.py
) else (
    echo [ERRO] Arquivo air_mouse_receiver.py nao encontrado na mesma pasta!
    pause
)

pause
