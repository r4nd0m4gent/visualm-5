#!/bin/bash
set -e

# ============================================================
# SSL Setup Script using Let's Encrypt (Certbot)
# Usage: bash setup-ssl.sh your-domain.com
# ============================================================

DOMAIN=$1

if [ -z "$DOMAIN" ]; then
    echo "Usage: bash setup-ssl.sh your-domain.com"
    exit 1
fi

echo "=== Setting up SSL for $DOMAIN ==="

# Step 1: Get the certificate
docker compose run --rm certbot certonly \
    --webroot \
    --webroot-path=/var/www/certbot \
    -d "$DOMAIN" \
    --email "admin@$DOMAIN" \
    --agree-tos \
    --no-eff-email

# Step 2: Update nginx config — replace YOUR_DOMAIN and enable SSL block
sed -i "s/YOUR_DOMAIN/$DOMAIN/g" nginx/nginx.conf

# Uncomment the HTTPS redirect
sed -i 's|# \(location / {\)|\1|' nginx/nginx.conf
sed -i 's|# \(    return 301 https://\$host\$request_uri;\)|\1|' nginx/nginx.conf
# Uncomment the SSL server block
sed -i 's/^# \(server {\)/\1/' nginx/nginx.conf
sed -i '/^# --- Uncomment this block/d' nginx/nginx.conf
sed -i 's/^#     /    /g' nginx/nginx.conf
sed -i 's/^# }/}/' nginx/nginx.conf

# Comment out the HTTP content block
sed -i '/# --- Remove everything below this line/,/^}$/{ /# ---/d; /^}/!s/^/# /; }' nginx/nginx.conf

# Step 3: Reload Nginx
docker compose exec frontend nginx -s reload

echo ""
echo "=== SSL setup complete! ==="
echo "Visit https://$DOMAIN"
echo ""
echo "Certificate auto-renewal is handled by the certbot container."
