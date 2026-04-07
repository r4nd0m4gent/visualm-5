#!/bin/bash
set -e

# ============================================================
# SSL Setup Script using Let's Encrypt (Certbot)
# Usage: bash setup-ssl.sh your-domain.com [your-email@example.com]
# ============================================================

DOMAIN=${1:-samplemanagementtool.org}
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

# Step 2: Enable HTTPS in nginx.conf
# Uncomment the HTTPS redirect in the HTTP block
sed -i 's|^    # return 301 https://\$host\$request_uri;|    return 301 https://\$host\$request_uri;|' nginx/nginx.conf

# Comment out HTTP content blocks (everything between the redirect and closing brace)
# — users should manually remove the HTTP-served content block and uncomment the SSL server block.

echo ""
echo "=== Certificate obtained! ==="
echo ""
echo "Now edit nginx/nginx.conf:"
echo "  1. Uncomment the HTTPS server block at the bottom"
echo "  2. In the HTTP server block, keep only the ACME challenge + redirect"
echo "     (remove or comment out the root, proxy, and SPA locations)"
echo "  3. Run: docker compose exec frontend nginx -s reload"
echo ""
echo "To auto-renew, add this cron job (sudo crontab -e):"
echo "  0 3 * * * cd $(pwd) && docker compose run --rm certbot renew && docker compose exec frontend nginx -s reload"
echo ""
echo "Visit https://$DOMAIN after completing the steps above."
