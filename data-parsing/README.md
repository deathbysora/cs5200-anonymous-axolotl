# About
This directory contains a single script, `main.py` that parses a json file where keys and values are separated by `'` (single quotes) and creates 3 csv files...

* `beers.csv`
* `reviews.csv`
* `users.csv`

### Disclaimer
This script only works on ratebeer's json data structure. You can test the script on `testdata.json`.

# How to Run
Type `python main.py path-to-file path-to-directory` to run the script.

For example, with your `pwd` in `/data-parsing`
```
python main.py data/testdata.json ./
```
will read `testdata.json` and output `beers.csv`, `reviews.csv` and `users.csv` to your current directory.