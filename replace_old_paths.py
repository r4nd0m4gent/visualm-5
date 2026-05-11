"""
replace_old_paths.py
Runs on the server. Scans /opt/visualm/images/material/2/ for NNN_1.jpg and
NNN_2.jpg files, finds the matching AUAS archive version, generates SQL UPDATE
statements, and executes them via MySQL.
"""
import os, re, subprocess

OLD_DIR     = "/opt/visualm/images/material/2"
ARCHIVE_DIR = "/opt/visualm/images/material/archive"
SQL_OUT     = "/tmp/replace_old_paths.sql"
MY_CNF      = "/tmp/.my.cnf"
DB          = "msl_db"

# -------------------------------------------------------------------
# Build a lookup: seq_pub_int -> archive filename (prefer _1 then _2)
# -------------------------------------------------------------------
archive = {}   # {seqpub: {1: fname, 2: fname, ...}}
for fname in os.listdir(ARCHIVE_DIR):
    m = re.match(r'^AUAS_0*(\d+)_(\d+)\.jpg$', fname, re.IGNORECASE)
    if m:
        seq, num = int(m.group(1)), int(m.group(2))
        archive.setdefault(seq, {})[num] = fname

# -------------------------------------------------------------------
# Scan old dir, find matches, generate SQL
# -------------------------------------------------------------------
lines = [
    "-- replace_old_paths.sql",
    "-- Replaces old /images/material/2/ paths with archive AUAS versions",
    "",
]

replaced_overview = 0
replaced_closeup  = 0

for fname in sorted(os.listdir(OLD_DIR)):
    m = re.match(r'^0*(\d+)(_\d+)?\.jpg$', fname, re.IGNORECASE)
    if not m:
        continue
    seq = int(m.group(1))
    suffix = m.group(2) or "_1"
    num = int(suffix.lstrip("_"))

    old_path = f"/images/material/2/{fname}"

    if seq not in archive or num not in archive[seq]:
        lines.append(f"-- SKIP: no archive match for {fname} (seq={seq}, num={num})")
        continue

    new_fname = archive[seq][num]
    new_path  = f"/images/material/archive/{new_fname}"

    if num == 1:
        lines.append(f"UPDATE `material` SET `overview_url` = '{new_path}' WHERE `overview_url` = '{old_path}';")
        replaced_overview += 1
    else:
        lines.append(f"UPDATE `material` SET `close_up_url` = '{new_path}' WHERE `close_up_url` = '{old_path}';")
        replaced_closeup += 1

lines.append("")

with open(SQL_OUT, "w") as f:
    f.write("\n".join(lines))

print(f"Generated {replaced_overview} overview + {replaced_closeup} close-up replacements -> {SQL_OUT}")

# -------------------------------------------------------------------
# Write MySQL credentials file and execute
# -------------------------------------------------------------------
with open(MY_CNF, "w") as f:
    f.write("[client]\nuser=visualm\npassword=Visualm2026!\n")
os.chmod(MY_CNF, 0o600)

result = subprocess.run(
    ["mysql", f"--defaults-file={MY_CNF}", DB],
    stdin=open(SQL_OUT),
    capture_output=True, text=True
)
if result.returncode == 0:
    print("MySQL: executed successfully.")
else:
    print("MySQL error:", result.stderr[:500])
