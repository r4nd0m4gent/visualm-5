#!/bin/bash
mysql -u visualm -pVisualm2026! msl_db -N --skip-column-names 2>/dev/null \
  -e 'SELECT sequence_number, overview_url FROM material WHERE overview_url IS NOT NULL' > /tmp/urls.txt

while IFS=$'\t' read -r seq url; do
  filepath="/opt/visualm${url}"
  if [ ! -f "$filepath" ]; then
    echo "UPDATE material SET overview_url = NULL WHERE sequence_number = ${seq};"
  fi
done < /tmp/urls.txt > /tmp/fix_missing_images.sql

echo "Total with overview_url:"
wc -l < /tmp/urls.txt
echo "Missing files (will be nulled):"
wc -l < /tmp/fix_missing_images.sql
echo "Preview SQL:"
head -5 /tmp/fix_missing_images.sql
