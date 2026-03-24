#!/bin/bash
set -e

# ============================================================
# VisualM Deployment Script for Digital Ocean Droplet
# ============================================================
# Prerequisites: Ubuntu 20.04+ Droplet with SSH access
#
# Usage:
#   1. SSH into your droplet
#   2. Clone/upload the project
#   3. Copy .env.example to .env and fill in values
#   4. Run: bash deploy.sh
# ============================================================

echo "=== VisualM Deployment ==="

# Check .env file exists
if [ ! -f .env ]; then
    echo "ERROR: .env file not found. Copy .env.example to .env and fill in your values."
    exit 1
fi

# Install Docker if not present
if ! command -v docker &> /dev/null; then
    echo "Installing Docker..."
    apt-get update
    apt-get install -y ca-certificates curl gnupg
    install -m 0755 -d /etc/apt/keyrings
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg
    chmod a+r /etc/apt/keyrings/docker.gpg
    echo \
      "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
      $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
      tee /etc/apt/sources.list.d/docker.list > /dev/null
    apt-get update
    apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
    echo "Docker installed successfully."
fi

# Create necessary directories
mkdir -p nginx/certbot/conf nginx/certbot/www

# Build and start containers
echo "Building and starting containers..."
docker compose up -d --build

echo ""
echo "=== Deployment complete! ==="
echo ""
echo "The application is now running on port 80."
echo "Visit http://$(curl -s ifconfig.me) to access VisualM."
echo ""
echo "--- Next steps for SSL (recommended) ---"
echo "1. Point your domain DNS to this server's IP"
echo "2. Run: bash setup-ssl.sh your-domain.com"
echo ""
