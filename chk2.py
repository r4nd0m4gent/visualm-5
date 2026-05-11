import json

d = json.load(open("/tmp/mats.json"))

archive_ok = []
old_path_ok = []
null_url = []

for m in d:
    url = m.get("overviewURL")
    seq = m.get("sequenceNumberPublished")
    if url is None:
        null_url.append(seq)
    elif url.startswith("/9j/") or url.startswith("iVBOR") or len(url) > 200:
        # it's base64 data being returned - working
        # Try to figure out what path it came from by checking close_up
        archive_ok.append((seq, m.get("name", ""), len(url)))
    else:
        # It's a raw path string - broken (file didn't exist, getFileBase64 returned null... 
        # but wait, if it's a path string that means getFileBase64 returned null and the raw path was returned)
        old_path_ok.append((seq, m.get("name", ""), url))

print(f"Working (base64 returned): {len(archive_ok)}")
print(f"Broken (raw path returned): {len(old_path_ok)}")
print(f"No image (null): {len(null_url)}")

if old_path_ok:
    print("\nSample broken paths:")
    for seq, name, url in old_path_ok[:20]:
        print(f"  seq_pub={seq}  name={name}  path={url}")
