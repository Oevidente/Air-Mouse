package com.example.network

object PythonReceiverCode {
    const val RECEIVER_SCRIPT_FILENAME = "air_mouse_receiver.py"

    val PYTHON_SCRIPT = """
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
\"\"\"
========================================================================
 AIR MOUSE - SERVIDOR RECEPTOR PARA COMPUTADOR (WINDOWS / LINUX)
========================================================================
Receptor ultraleve de baixíssima latência para o aplicativo Air Mouse.
Recebe comandos de mouse por giroscópio (Air Mouse) e gestos via UDP,
e atalhos de teclado/mídia via TCP.

INSTALAÇÃO RÁPIDA DE DEPENDÊNCIAS:
    pip install pynput pyautogui

COMO EXECUTAR:
    Windows: python air_mouse_receiver.py
    Linux:   python3 air_mouse_receiver.py
========================================================================
\"\"\"

import socket
import threading
import json
import time
import sys
import os

try:
    from pynput.mouse import Controller as MouseController, Button
    from pynput.keyboard import Controller as KeyboardController, Key
    PYNPUT_AVAILABLE = True
except ImportError:
    PYNPUT_AVAILABLE = False

try:
    import pyautogui
    pyautogui.FAILSAFE = False
    pyautogui.PAUSE = 0
    PYAUTOGUI_AVAILABLE = True
except ImportError:
    PYAUTOGUI_AVAILABLE = False

if not PYNPUT_AVAILABLE and not PYAUTOGUI_AVAILABLE:
    print("[ERRO] Nenhuma biblioteca de automação encontrada.")
    print("Por favor instale via terminal:")
    print("    pip install pynput pyautogui")
    print("Tentando continuar com suporte limitado...")

UDP_PORT = 5005
TCP_PORT = 5006

if PYNPUT_AVAILABLE:
    mouse = MouseController()
    keyboard = KeyboardController()

def get_local_ip():
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))
        ip = s.getsockname()[0]
        s.close()
        return ip
    except Exception:
        return "127.0.0.1"

# Mapeamento de teclas especiais
KEY_MAP = {
    "f1": Key.f1 if PYNPUT_AVAILABLE else "f1",
    "f2": Key.f2 if PYNPUT_AVAILABLE else "f2",
    "f3": Key.f3 if PYNPUT_AVAILABLE else "f3",
    "f4": Key.f4 if PYNPUT_AVAILABLE else "f4",
    "f5": Key.f5 if PYNPUT_AVAILABLE else "f5",
    "f6": Key.f6 if PYNPUT_AVAILABLE else "f6",
    "f7": Key.f7 if PYNPUT_AVAILABLE else "f7",
    "f8": Key.f8 if PYNPUT_AVAILABLE else "f8",
    "f9": Key.f9 if PYNPUT_AVAILABLE else "f9",
    "f10": Key.f10 if PYNPUT_AVAILABLE else "f10",
    "f11": Key.f11 if PYNPUT_AVAILABLE else "f11",
    "f12": Key.f12 if PYNPUT_AVAILABLE else "f12",
    "esc": Key.esc if PYNPUT_AVAILABLE else "esc",
    "space": Key.space if PYNPUT_AVAILABLE else "space",
    "enter": Key.enter if PYNPUT_AVAILABLE else "enter",
    "tab": Key.tab if PYNPUT_AVAILABLE else "tab",
    "backspace": Key.backspace if PYNPUT_AVAILABLE else "backspace",
    "up": Key.up if PYNPUT_AVAILABLE else "up",
    "down": Key.down if PYNPUT_AVAILABLE else "down",
    "left": Key.left if PYNPUT_AVAILABLE else "left",
    "right": Key.right if PYNPUT_AVAILABLE else "right",
    "ctrl": Key.ctrl if PYNPUT_AVAILABLE else "ctrl",
    "alt": Key.alt if PYNPUT_AVAILABLE else "alt",
    "shift": Key.shift if PYNPUT_AVAILABLE else "shift",
    "win": Key.cmd if PYNPUT_AVAILABLE else "win",
    "cmd": Key.cmd if PYNPUT_AVAILABLE else "win",
}

def execute_mouse_action(data):
    try:
        t = data.get("t")
        if t == "move":
            dx = float(data.get("dx", 0))
            dy = float(data.get("dy", 0))
            if PYNPUT_AVAILABLE:
                mouse.move(dx, dy)
            elif PYAUTOGUI_AVAILABLE:
                pyautogui.moveRel(dx, dy)

        elif t == "click":
            btn_str = data.get("btn", "left")
            if PYNPUT_AVAILABLE:
                b = Button.left if btn_str == "left" else (Button.right if btn_str == "right" else Button.middle)
                mouse.click(b, 1)
            elif PYAUTOGUI_AVAILABLE:
                pyautogui.click(button=btn_str)

        elif t == "m_down":
            btn_str = data.get("btn", "left")
            if PYNPUT_AVAILABLE:
                b = Button.left if btn_str == "left" else Button.right
                mouse.press(b)
            elif PYAUTOGUI_AVAILABLE:
                pyautogui.mouseDown(button=btn_str)

        elif t == "m_up":
            btn_str = data.get("btn", "left")
            if PYNPUT_AVAILABLE:
                b = Button.left if btn_str == "left" else Button.right
                mouse.release(b)
            elif PYAUTOGUI_AVAILABLE:
                pyautogui.mouseUp(button=btn_str)

        elif t == "scroll":
            dy = int(data.get("dy", 0))
            dx = int(data.get("dx", 0))
            if PYNPUT_AVAILABLE:
                mouse.scroll(dx, dy)
            elif PYAUTOGUI_AVAILABLE:
                pyautogui.scroll(dy * 10)

    except Exception as e:
        pass

def execute_key_combo(combo_str):
    try:
        combo = combo_str.strip().lower()
        
        # Mapeamento multimídia
        if combo in ("media_play", "play_pause"):
            if PYAUTOGUI_AVAILABLE:
                pyautogui.press("playpause")
            elif PYNPUT_AVAILABLE:
                keyboard.press(Key.media_play_pause)
                keyboard.release(Key.media_play_pause)
            return
        elif combo in ("vol_up", "volume_up"):
            if PYAUTOGUI_AVAILABLE:
                pyautogui.press("volumeup")
            elif PYNPUT_AVAILABLE:
                keyboard.press(Key.media_volume_up)
                keyboard.release(Key.media_volume_up)
            return
        elif combo in ("vol_down", "volume_down"):
            if PYAUTOGUI_AVAILABLE:
                pyautogui.press("volumedown")
            elif PYNPUT_AVAILABLE:
                keyboard.press(Key.media_volume_down)
                keyboard.release(Key.media_volume_down)
            return
        elif combo == "mute":
            if PYAUTOGUI_AVAILABLE:
                pyautogui.press("volumemute")
            elif PYNPUT_AVAILABLE:
                keyboard.press(Key.media_volume_mute)
                keyboard.release(Key.media_volume_mute)
            return

        # Teclas compostas (ex: CTRL+C, ALT+TAB, SHIFT+F5)
        keys = [k.strip() for k in combo.split("+")]
        if PYNPUT_AVAILABLE:
            pressed_keys = []
            for k in keys:
                target = KEY_MAP.get(k, k)
                keyboard.press(target)
                pressed_keys.append(target)
            time.sleep(0.04)
            for target in reversed(pressed_keys):
                keyboard.release(target)
        elif PYAUTOGUI_AVAILABLE:
            pyautogui.hotkey(*keys)
    except Exception as e:
        print(f"[Aviso] Falha ao executar tecla: {combo_str} -> {e}")

def udp_server_loop():
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.bind(("0.0.0.0", UDP_PORT))
    print(f"[OK] UDP Mouse/AirMouse Server ouvindo na porta {UDP_PORT}")
    while True:
        try:
            data, addr = sock.recvfrom(2048)
            msg = json.loads(data.decode("utf-8"))
            execute_mouse_action(msg)
        except Exception:
            pass

def handle_tcp_client(client_sock, client_addr):
    buffer = ""
    try:
        while True:
            chunk = client_sock.recv(4096)
            if not chunk:
                break
            buffer += chunk.decode("utf-8", errors="ignore")
            while "\n" in buffer:
                line, buffer = buffer.split("\n", 1)
                line = line.strip()
                if not line:
                    continue
                try:
                    msg = json.loads(line)
                    t = msg.get("t")
                    if t == "ping":
                        client_sock.sendall(b'{"t":"pong"}\n')
                    elif t == "key":
                        combo = msg.get("combo", "")
                        execute_key_combo(combo)
                        client_sock.sendall(b'{"t":"ack"}\n')
                    elif t == "type":
                        text = msg.get("text", "")
                        if PYAUTOGUI_AVAILABLE:
                            pyautogui.write(text)
                        elif PYNPUT_AVAILABLE:
                            keyboard.type(text)
                        client_sock.sendall(b'{"t":"ack"}\n')
                except Exception:
                    pass
    except Exception:
        pass
    finally:
        client_sock.close()

def tcp_server_loop():
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server.bind(("0.0.0.0", TCP_PORT))
    server.listen(5)
    print(f"[OK] TCP Key/Shortcut Server ouvindo na porta {TCP_PORT}")
    while True:
        try:
            client, addr = server.accept()
            t = threading.Thread(target=handle_tcp_client, args=(client, addr), daemon=True)
            t.start()
        except Exception:
            pass

def main():
    os.system("cls" if os.name == "nt" else "clear")
    local_ip = get_local_ip()
    print("=" * 60)
    print("        AIR MOUSE - SERVIDOR DO COMPUTADOR")
    print("=" * 60)
    print(f"-> Digite este IP no seu aplicativo Air Mouse:")
    print(f"      IP DO SEU COMPUTADOR:  {local_ip}")
    print(f"      PORTA UDP (Mouse):     {UDP_PORT}")
    print(f"      PORTA TCP (Teclas):    {TCP_PORT}")
    print("=" * 60)
    print("Pronto para receber movimentos de Air Mouse e cliques!")
    print("Pressione Ctrl+C para encerrar o servidor a qualquer momento.")
    print("=" * 60)

    t_udp = threading.Thread(target=udp_server_loop, daemon=True)
    t_tcp = threading.Thread(target=tcp_server_loop, daemon=True)
    t_udp.start()
    t_tcp.start()

    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        print("\nEncerrando servidor Air Mouse...")
        sys.exit(0)

if __name__ == "__main__":
    main()
""".trimIndent()
}
