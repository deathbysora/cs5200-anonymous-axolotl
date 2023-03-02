# About

This directory contains a single script, `main.py` whose sole purpose is to parse [RateBeer](https://cseweb.ucsd.edu/~jmcauley/datasets.html#multi_aspect), a dataset provided by UCSD, so that we can read it into MySQL for educational purposes. The reason we have to parse the file is because the keys and values are separated by `'`, which MySQL doesn't like. So here we are. 

</br>

# Running the Script
Type...
```
python main.py [data-file] [output-directory]
```
 to run the script.

</br>

For example, with your `pwd` in `/data-parsing`...
```
python main.py data/testdata.json ./
```
The script will parse `data/testdata.json` and the following files will be created in your current directory.

* `beers.csv`
* `beerreviews.csv`
* `beerstyles.csv`
* `brewers.csv`
* `users.csv`

### Disclaimer
This script only works on ratebeer's json data structure. Which you can download [here](https://drive.google.com/file/d/1SHOys2fSU-MnZk-l47fVm5j_J-_JeW_w/view). You can also test the script on the provided `data/testdata.json` which is just the top 10 lines from the dataset.

</br>

For more information on the dataset and its source, you can view [UCSD's full dataset collection](https://cseweb.ucsd.edu/~jmcauley/datasets.html#multi_aspect).

# Additional Data Links

[Randomly generated generic comments](https://drive.google.com/file/d/1ZwurySMqEDbwe4xkJdNLlMuX8Fa-csCZ/view?usp=share_link).
