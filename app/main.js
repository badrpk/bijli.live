const { app, BrowserWindow, ipcMain } = require('electron');
const path = require('path');
const axios = require('axios');

const AI_URL = 'http://127.0.0.1:8012';

function createWindow() {
  const win = new BrowserWindow({
    width: 960,
    height: 680,
    webPreferences: { preload: path.join(__dirname, 'preload.js') }
  });
  win.loadFile('index.html');
  // DevTools for debugging (optional — remove later if you want)
  win.webContents.openDevTools({ mode: 'detach' });
}

app.whenReady().then(() => {
  createWindow();
  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) createWindow();
  });
});

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') app.quit();
});

// --- Handlers ---

ipcMain.handle('translate', async (_evt, prompt) => {
  const { data } = await axios.post(`${AI_URL}/v1/translate`, { prompt });
  return data;
});

ipcMain.handle('run-powershell', async (_evt, command) => {
  const { spawn } = require('child_process');
  return await new Promise((resolve) => {
    const child = spawn(
      'powershell.exe',
      ['-NoProfile', '-ExecutionPolicy', 'Bypass', '-Command', command],
      { windowsHide: true }
    );
    let out = '', err = '';
    child.stdout.on('data', d => out += d.toString());
    child.stderr.on('data', d => err += d.toString());
    child.on('close', code => resolve({ code, out, err }));
  });
});

