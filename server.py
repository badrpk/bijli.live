#!/usr/bin/env python3
"""
Simple HTTP Server for Bijli.live Web App
Runs on localhost:3002
"""

import http.server
import socketserver
import webbrowser
import os
import sys
from pathlib import Path

# Configuration
PORT = 3002
HOST = 'localhost'

class CustomHTTPRequestHandler(http.server.SimpleHTTPRequestHandler):
    def end_headers(self):
        # Add CORS headers for development
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type')
        super().end_headers()

    def do_GET(self):
        # Serve index.html for root path
        if self.path == '/':
            self.path = '/index.html'
        return super().do_GET()

def start_server():
    """Start the HTTP server on localhost:3002"""
    
    # Change to the directory containing the web files
    web_dir = Path(__file__).parent
    os.chdir(web_dir)
    
    # Create server
    with socketserver.TCPServer((HOST, PORT), CustomHTTPRequestHandler) as httpd:
        print(f"🚀 Bijli.live Web Server Starting...")
        print(f"📍 Server running at: http://{HOST}:{PORT}")
        print(f"📁 Serving files from: {web_dir}")
        print(f"🌐 Open your browser to: http://localhost:{PORT}")
        print(f"⏹️  Press Ctrl+C to stop the server")
        print("-" * 50)
        
        # Open browser automatically
        try:
            webbrowser.open(f'http://{HOST}:{PORT}')
            print(f"✅ Browser opened automatically!")
        except Exception as e:
            print(f"⚠️  Could not open browser automatically: {e}")
            print(f"🌐 Please manually open: http://localhost:{PORT}")
        
        print("-" * 50)
        
        # Start serving
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print(f"\n🛑 Server stopped by user")
            print(f"👋 Thank you for using Bijli.live!")

if __name__ == "__main__":
    start_server()
