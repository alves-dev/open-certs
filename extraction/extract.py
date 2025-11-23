import json
import re

FILENAME = ""
CERTIFICATION = ""
TOPIC = ""

def parse_md(md_text: str):
    blocks = md_text.split("### ")
    questions = []

    for block in blocks:
        block = block.strip()
        if not block:
            continue

        # primeira linha é a descrição
        lines = block.splitlines()
        description = lines[0].strip()

        responses = []
        correct_indexes = []

        for idx, line in enumerate(lines[1:]):
            line = line.strip()
            if line.startswith("- ["):
                # pega se é correta
                is_correct = "[x]" in line.lower()

                # remove prefixo "- [ ] " ou "- [x] "
                text = re.sub(r"- \[[x ]\]\s*", "", line, flags=re.IGNORECASE)
                responses.append(text)

                if is_correct:
                    correct_indexes.append(len(responses) - 1)

        questions.append({
            "certification": CERTIFICATION,
            "topic": TOPIC,
            "description": description,
            "responses": responses,
            "correctIndexes": correct_indexes
        })

    return questions


if __name__ == "__main__":
    with open(f"{FILENAME}.md", "r", encoding="utf-8") as f:
        md_content = f.read()

    result = parse_md(md_content)

    with open(f"{FILENAME}.json", "w", encoding="utf-8") as f:
        json.dump(result, f, ensure_ascii=False, indent=2)

    print(f"Gerado {FILENAME}.json!")
