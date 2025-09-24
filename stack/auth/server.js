import express from "express";
import jwt from "jsonwebtoken";
import bcrypt from "bcryptjs";
import pg from "pg";
import cors from "cors";

const app = express();
app.use(express.json());
app.use(cors());

const { Pool } = pg;
const pool = new Pool({ connectionString: process.env.DATABASE_URL });
const JWT_SECRET = process.env.JWT_SECRET || "dev_secret";

await pool.query(
CREATE TABLE IF NOT EXISTS users (
  id serial primary key,
  email text unique not null,
  password_hash text not null,
  created_at timestamptz default now()
);
);

app.post("/v1/register", async (req, res) => {
  const { email, password } = req.body || {};
  if (!email || !password) return res.status(400).json({ error: "email & password required" });
  const hash = await bcrypt.hash(password, 10);
  try {
    await pool.query("INSERT INTO users(email, password_hash) VALUES(,)", [email, hash]);
    res.json({ ok: true });
  } catch (e) {
    res.status(400).json({ error: "user_exists_or_invalid" });
  }
});

app.post("/v1/login", async (req, res) => {
  const { email, password } = req.body || {};
  const r = await pool.query("SELECT id,password_hash FROM users WHERE email=", [email]);
  if (!r.rowCount) return res.status(401).json({ error: "invalid_credentials" });
  const ok = await bcrypt.compare(password, r.rows[0].password_hash);
  if (!ok) return res.status(401).json({ error: "invalid_credentials" });
  const token = jwt.sign({ uid: r.rows[0].id, email }, JWT_SECRET, { expiresIn: "7d" });
  res.json({ token });
});

app.get("/healthz", (_, res) => res.json({ ok: true }));

// Proxy-safe: API gateway (Caddy) will forward to /auth/*
app.listen(3000, () => console.log("Auth listening on :3000"));
