const { contextBridge, ipcRenderer } = require('electron');
contextBridge.exposeInMainWorld('bijli', {
  translate: (prompt) => ipcRenderer.invoke('translate', prompt),
  runPS:    (cmd)    => ipcRenderer.invoke('run-powershell', cmd)
});
