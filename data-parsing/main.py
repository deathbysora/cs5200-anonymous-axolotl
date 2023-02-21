from typing import Callable, Any
from datetime import datetime, timezone
import re
import sys
import os

headerMap = {
    "name": "BeerName",
    "beerId": "BeerId",
    "brewerId": "BrewerId",
    "ABV": "ABV",
    "style": "Style",
    "appearance": "Appearance",
    "aroma": "Aroma",
    "palate": "Palate",
    "taste": "Taste",
    "overall": "Overall",
    "time": "Created",
    "profileName": "Username",
    "text": "Text",
}

RecordStrategy = Callable[[dict[str, Any]], str]

class Table:
    def __init__(self, tableName: str, columns: list[str], parseStrategy: RecordStrategy):
        self.tableName = tableName
        self.columns = columns
        self.parseStrategy = parseStrategy
        self.data: set[str] = set()

    def add(self, record: dict[str, Any]) -> None:
        """Adds a record to the table."""
        self.data.add(self.parseStrategy(self.columns, record))

    def writeToFile(self) -> None:
        """Writes the table to a csv file."""
        fileName = self.tableName.lower().replace(" ", "")
        extension = "csv"
        print(f"Writing {fileName}.{extension}... ", end="")
        with open(f"{fileName}.{extension}", "w") as file:
            file.write(",".join(self.columns))
            file.write("\n")
            file.write("\n".join(list(self.data)))
        print("done")

_surroundWithQuotes = lambda x: f'"{x}"'

def _epochToDateTime(text: str) -> str:
    """Converts a number in string format to datetime format \"YY-MM-DD HH:MM:SS\""""
    if not text.isnumeric():
        raise Exception("text isn't an epoch time")
    return datetime.fromtimestamp(int(text), timezone.utc).strftime("%Y-%m-%d %H:%M:%S")

mysqlFormatters: dict[str, Callable[[str], str]] = {
    "BeerName": _surroundWithQuotes,
    "Style": _surroundWithQuotes,
    "Created": _epochToDateTime,
    "Username": _surroundWithQuotes,
    "Text": _surroundWithQuotes,
}

def _keyStrategy(keys: list[str], record: dict[str, Any]) -> str:
    """Extracts all key values from record into a single string formatted for CSV serialization."""
    NULL_VALUES = ["-", ""]

    wanted = []
    for key in keys:
        value = record[key]
        if value in NULL_VALUES:
            wanted.append("NULL")
        elif key in mysqlFormatters:
            wanted.append(mysqlFormatters[key](value))
        else:
            wanted.append(value)

    return ",".join(wanted)

def main(data_path: str, output_directory: str) -> None:
    """Entry point."""
    if not os.path.isfile(data_path):
        print(f"{output_directory} is not a valid file.")
        return

    if not os.path.isdir(output_directory):
        print(f"{output_directory} is not a valid directory.")
        return

    print(f"Opening data file: {data_path}")
    with open(data_path, "r") as file:
        print("Reading file... ", end="")
        lines = file.readlines()
    print("done")

    print("Creating data collection entities")

    entities: list[tuple[str, list[str], RecordStrategy]] = [
        ("Users", ["Username"], _keyStrategy),
        ("Brewers", ["BrewerId"], _keyStrategy),
        ("Beers", 
            [
                "BeerId",
                "BeerName",
                "ABV",
                "BrewerId",
                "Style"
            ],
        _keyStrategy),
        ("BeerReviews",
            [
                # "ReviewId", we'll have MySQL auto generate ReviewId.
                "Appearance",
                "Aroma",
                "Palate",
                "Taste",
                "Overall",
                "Created",
                "Text",
                "Username",
                "BeerId"
            ],
         _keyStrategy),
        ("BeerStyles", ["Style"], _keyStrategy),
    ]

    tables: list[Table] = []
    for tableName, columns, strategy in entities:
        tables.append(Table(tableName, columns, strategy))

    record: dict[str, Any] = {}

    JSON_PATTERN = r"[{\s]'\w+/(\w+)':\s['\"](.+?)['\"][,}]"
    pattern = re.compile(JSON_PATTERN)
    print("Starting parsing")
    for i, line in enumerate(lines):
        if i % 100000 == 0 and i != 0:
            print(f"Parsed {i} lines")
        try:
            regex_result: list[tuple[str, str]] = pattern.findall(line)

            for key, value in regex_result:
                record[headerMap[key]] = value

            for table in tables:
                table.add(record)
        except IndexError as e:
            break
        except Exception as e:
            print(e)
            print(f"Error while parsing line: {i}\nExiting program")
            return

    for table in tables:
        table.writeToFile()

REQUIRED_ARG_LENGTH = 3
if len(sys.argv) != REQUIRED_ARG_LENGTH:
    print("Invalid call. Please provide the source json file and output directory as arguments.")
    exit()

if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2])
