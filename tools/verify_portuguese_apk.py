"""Verify translations inside the final APK, after locale filtering and shrinking.

Usage: python tools/verify_portuguese_apk.py path/to/app.apk --aapt2 path/to/aapt2
This catches a regional translation silently excluded by Gradle localeFilters.
"""
import argparse
from pathlib import Path
import re
import subprocess
import xml.etree.ElementTree as ET

parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument("apk", type=Path)
parser.add_argument("--aapt2", required=True)
args = parser.parse_args()
dump = subprocess.check_output([args.aapt2, "dump", "resources", str(args.apk)], encoding="utf-8")
blocks = {}
for match in re.finditer(r"resource 0x[0-9a-f]+ (string|plurals)/([^\s]+)(.*?)(?=\n\s*resource 0x|\Z)", dump, re.S):
    blocks[(match[1], match[2])] = match[3]
source = Path(__file__).resolve().parents[1] / "app/src/main/res/values-pt/strings.xml"
missing = []
checked = 0
for item in ET.parse(source).getroot():
    key = (item.tag, item.attrib["name"])
    if key not in blocks:  # Unused resources can legitimately be removed by R8.
        continue
    checked += 1
    if not re.search(r"\(pt\)", blocks[key]):
        missing.append(key[1])
assert checked > 200, f"Too few resources verified: {checked}"
assert not missing, "Portuguese missing from APK: " + ", ".join(missing)
for name, expected in {
    "settings_title": "Configurações",
    "library_title": "Roteiros",
    "script_action_rehearse": "Ensaiar",
    "settings_section_voice": "Acompanhar a voz",
    "common_cancel": "Cancelar",
}.items():
    assert f'(pt) "{expected}"' in blocks[("string", name)], name
print(f"PASS: {checked} retained translated resources include Portuguese in the APK.")
print("PASS: library, settings, rehearsal, voice settings and common controls use Portuguese.")
