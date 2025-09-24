from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse
import httpx, traceback
from subprocess import run, PIPE

BUILD = "safeparse-5"

app = FastAPI(title="Bijli AI (Local) – NoRegexGuards")

# ---- Regex-free danger checks (token/substring based) ----
# Each entry = list of tokens that must all be present (case-insensitive)
DANGER_TOKEN_SETS = [
  ["remove-item", "-recurse", "-force", "c:\\"],
  ["format-volume"],
  ["clear-disk"],
  ["stop-computer"],
  ["set-executionpolicy", "unrestricted"],
  ["disable", "firewall"],
  ["cipher", "/w:"],
  ["del", "/s", "/q", "c:\\"],
  ["set-mppreference", "-disablerealtimemonitoring", "True"],
]

def danger_reason(cmd: str) -> str | None:
  c = (cmd or "").lower()
  for tokens in DANGER_TOKEN_SETS:
    if all(t in c for t in tokens):
      return "Matches dangerous token set: " + " ".join(tokens)
  return None

def strip_code_fences(text: str) -> str:
  t = (text or "").strip()
  if t.startswith("`"):
    if t.endswith("`"):
      t = t[3:-3].strip()
    else:
      t = t.replace("","")
  for line in t.splitlines():
    line = line.strip()
    if line:
      return line
  return ""

def normalize_cmd(cmd: str) -> str:
  c = (cmd or "").strip()
  if c.lower().startswith("get-process") and "sort-object ws" not in c.lower():
    return "Get-Process | Sort-Object WS -Descending | Select-Object -First 5 Name,Id,WS"
  return c

SYSTEM = (
  "You convert a user's plain-English Windows request into ONE safe PowerShell command. "
  "Prefer a single pipeline. Prefer read-only/system inspection by default. "
  "For process memory, use: Get-Process | Sort-Object WS -Descending | Select-Object -First 5 Name,Id,WS. "
  "Never include destructive actions, reboots, disk format, firewall disable, or writing to C:. "
  "Output ONLY ONE PowerShell command. No explanations."
)

async def call_ollama(prompt: str) -> str:
  body = {
    "model": "llama3.2:3b",
    "prompt": f"{SYSTEM}\nRequest: {prompt}\nCommand:",
    "stream": False,
    "options": {"temperature": 0.2}
  }
  async with httpx.AsyncClient(timeout=60) as client:
    r = await client.post("http://127.0.0.1:11434/api/generate", json=body)
    r.raise_for_status()
    data = r.json()

  text = ""
  if isinstance(data, dict):
    if isinstance(data.get("response"), str):
      text = data["response"]
    elif isinstance(data.get("message"), dict):
      m = data["message"].get("content")
      if isinstance(m, str):
        text = m

  return strip_code_fences(text)

@app.get("/healthz")
def healthz():
  return {"ok": True, "build": BUILD, "guards": "token-based"}

@app.post("/v1/translate")
async def translate(payload: dict):
  prompt = str(payload.get("prompt") or "").strip()
  if not prompt:
    return {"command":"", "blocked": True, "reason": "Missing prompt"}

  try:
    cmd = await call_ollama(prompt)
  except Exception as e:
    return {"command":"", "blocked": True, "reason": f"Ollama error: {e}"}

  if not cmd:
    return {"command":"", "blocked": True, "reason": "Model returned empty text"}

  if len(cmd) > 2000:
    return {"command":"", "blocked": True, "reason": "Command too long"}

  cmd_norm = normalize_cmd(cmd)

  bad = danger_reason(cmd_norm)
  if bad:
    return {"command":"", "blocked": True, "reason": bad, "raw": cmd, "norm": cmd_norm}

  return {"command": cmd_norm, "blocked": False, "reason": None, "raw": cmd}

@app.get("/v1/selftest")
async def selftest():
  try:
    cmd = await call_ollama("List the top 5 processes by memory usage (PowerShell)")
  except Exception as e:
    return {"ok": False, "blocked": True, "reason": f"Ollama error: {e}", "command": ""}

  if not cmd:
    return {"ok": False, "blocked": True, "reason": "Model returned empty text", "command": ""}

  cmd_norm = normalize_cmd(cmd)
  bad = danger_reason(cmd_norm)
  if bad:
    return {"ok": False, "blocked": True, "reason": bad, "command": cmd_norm}

  r = run(["powershell.exe","-NoProfile","-ExecutionPolicy","Bypass","-Command", cmd_norm],
          stdout=PIPE, stderr=PIPE, text=True)
  return {"ok": r.returncode == 0, "blocked": False, "command": cmd_norm,
          "stdout": r.stdout, "stderr": r.stderr, "code": r.returncode}

@app.exception_handler(Exception)
async def all_exceptions(request: Request, exc: Exception):
  return JSONResponse(status_code=500, content={"ok": False, "error": str(exc), "build": BUILD})
