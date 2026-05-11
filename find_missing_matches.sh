#!/bin/bash
# Find materials with no overview_url but matching files in the archive
# Uses sequence_number_published to match against AUAS_XXXXX_*.jpg filenames

ARCHIVE="/opt/visualm/images/material/archive"
OUTPUT_SQL="/tmp/update_missing_images.sql"
REPORT="/tmp/missing_matches_report.txt"

> "$OUTPUT_SQL"
> "$REPORT"

mysql -u visualm -pVisualm2026! msl_db -N --skip-column-names 2>/dev/null \
  -e 'SELECT sequence_number, sequence_number_published FROM material WHERE overview_url IS NULL AND sequence_number_published IS NOT NULL ORDER BY sequence_number_published' \
  > /tmp/unmatched.txt

matched=0
no_file=0

while IFS=$'\t' read -r seq pub; do
  # Format the AUAS number with 5-digit zero padding
  auas=$(printf "AUAS_%05d" "$pub")

  # Try _1.jpg first, then _2.jpg, then any numbered file
  found_file=""
  for suffix in 1 2 3 4 5; do
    candidate="${ARCHIVE}/${auas}_${suffix}.jpg"
    if [ -f "$candidate" ]; then
      found_file="/images/material/archive/${auas}_${suffix}.jpg"
      break
    fi
  done

  if [ -n "$found_file" ]; then
    echo "UPDATE material SET overview_url = '${found_file}' WHERE sequence_number = ${seq};" >> "$OUTPUT_SQL"
    echo "MATCH: seq=$seq pub=$pub -> $found_file" >> "$REPORT"
    ((matched++))
  else
    echo "NO FILE: seq=$seq pub=$pub (looked for ${auas}_1.jpg .. _5.jpg)" >> "$REPORT"
    ((no_file++))
  fi
done < /tmp/unmatched.txt

echo ""
echo "=== SUMMARY ==="
echo "Materials that CAN be matched to archive images: $matched"
echo "Materials with no matching archive file:         $no_file"
echo "SQL written to: $OUTPUT_SQL"
echo ""
echo "=== SAMPLE MATCHES ==="
grep "^MATCH" "$REPORT" | head -20
