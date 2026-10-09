#!/bin/bash
set -e

# ============================================================
# SSL Setup Script using Let's Encrypt (Certbot)
# Usage: bash setup-ssl.sh your-domain.com [your-email@example.com]
# ============================================================

DOMAIN=${1:-ecomaterialscollective.org}
EMAIL=${2:-admin@$DOMAIN}

echo "=== Setting up SSL for $DOMAIN ==="

# Ensure certbot directories exist
mkdir -p nginx/certbot/conf nginx/certbot/www

# Make sure the frontend (nginx) is running so it can serve the ACME challenge
docker compose up -d frontend

echo "Waiting for Nginx to be ready..."
sleep 5

# Step 1: Get the certificate
docker compose run --rm certbot certonly \
    --webroot \
    --webroot-path=/var/www/certbot \
    -d "$DOMAIN" \
    -d "www.$DOMAIN" \
    --email "$EMAIL" \
    --agree-tos \
    --no-eff-email

# Step 2: Switch to the HTTPS configuration only after Certbot succeeds.
cp nginx/nginx-https.conf nginx/nginx.conf
docker compose exec frontend nginx -t
docker compose exec frontend nginx -s reload

echo ""
echo "=== Certificate obtained! ==="
echo ""
echo "HTTPS is enabled and Nginx has been reloaded."
echo ""
echo "To auto-renew, add this cron job (sudo crontab -e):"
echo "  0 3 * * * cd $(pwd) && docker compose run --rm certbot renew && docker compose exec frontend nginx -s reload"
echo ""
echo "Visit https://$DOMAIN after completing the steps above."
