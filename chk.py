import json, sys
d = json.load(open("/tmp/mats.json"))
have_img = [x for x in d if x.get("overviewURL")]
print(str(len(have_img)) + " of " + str(len(d)) + " materials have overviewURL set")
if have_img:
    v = str(have_img[0]["overviewURL"])
    print("sample prefix: " + v[:60])
else:
    print("No images found - checking raw entries:")
    print(json.dumps(d[0] if d else {}, indent=2)[:500])
