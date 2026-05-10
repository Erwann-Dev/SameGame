# Compiling the Report to PDF

The report is written in LaTeX (`report.tex`). Two options:

---

## Option 1 — Terminal (local LaTeX)

### Prerequisites

Install a LaTeX distribution if not already present:

- **macOS**: [MacTeX](https://www.tug.org/mactex/) (`brew install --cask mactex`)
- **Linux (Debian/Ubuntu)**: `sudo apt install texlive-full`
- **Windows**: [MiKTeX](https://miktex.org/) or [TeX Live](https://www.tug.org/texlive/)

### Compile

Run `pdflatex` **twice** (the second pass resolves the table of contents and
internal references):

```bash
cd /path/to/SameGame
pdflatex report.tex
pdflatex report.tex
```

The output is `report.pdf` in the same directory.

Intermediate build files (`report.aux`, `report.log`, `report.toc`) can be
deleted after compilation:

```bash
rm -f report.aux report.log report.toc report.out
```

### Single-command compile + clean

```bash
pdflatex report.tex && pdflatex report.tex && rm -f report.aux report.log report.toc report.out
```

---

## Option 2 — Overleaf (browser, no local install)

1. Go to <https://www.overleaf.com> and sign in (or create a free account).
2. Click **New Project → Upload Project**.
3. Upload `report.tex` as a `.zip` or drag-and-drop the file directly.
4. Overleaf auto-compiles on upload. The PDF appears in the right pane.
5. To recompile after edits click the green **Recompile** button.
6. To download the PDF: **Menu → Download → PDF**.

> **Note:** The report uses only standard LaTeX packages (`geometry`,
> `hyperref`, `listings`, `xcolor`, `tikz`, `booktabs`, `microtype`,
> `lmodern`, `parskip`). All are available by default on Overleaf and in any
> full TeX Live / MacTeX installation. No additional packages need to be
> installed.

---

## Troubleshooting

| Error | Fix |
|-------|-----|
| `command not found: pdflatex` | LaTeX not installed — use Option 2 or install a distribution |
| `! LaTeX Error: File 'tikz.sty' not found` | Install `texlive-pictures` (Linux) or upgrade to `texlive-full` |
| PDF missing table of contents | Run `pdflatex` a second time |
