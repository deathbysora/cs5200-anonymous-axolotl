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

    def writeToFile(self, outDirectory: str) -> None:
        """Writes the table to a csv file."""
        if outDirectory[-1] != "/":
            outDirectory += "/"
            
        fileName = self.tableName.lower().replace(" ", "")
        extension = "csv"
        print(f"Writing {fileName}.{extension}... ", end="")
        with open(f"{outDirectory}{fileName}.{extension}", "w") as file:
            file.write(",".join(self.columns))
            file.write("\n")
            file.write("\n".join(list(self.data)))
        print("done")

def _wrapWithQuotes(text: str) -> str:
    """Surrounds a string with double quotes."""
    return f'"{text}"'

def _extractNumerator(text: str) -> str:
    """Returns the numerator of a fraction passed as a string."""
    return text[:text.find("/", 0)]

def _baseConversion(text: str, callback: Callable[[int], int]) -> str:
    """Applies the callback to the integer passed as a string."""
    numerator = _extractNumerator(text)
    return str(callback(numerator))
    
def _base5To10(text: str) -> str:
    """Converts fractions of base 5 to base 10."""
    return _baseConversion(text, lambda x: int(x) * 2)

def _base20to10(text: str) -> str:
    """Converts fractions of base 20 to base 10."""
    return _baseConversion(text, lambda x: int(x) / 2)

def _epochToDateTime(text: str) -> str:
    """Converts a number in string format to datetime format \"YY-MM-DD HH:MM:SS\""""
    if not text.isnumeric():
        raise Exception("text isn't an epoch time")
    return datetime.fromtimestamp(int(text), timezone.utc).strftime("%Y-%m-%d %H:%M:%S")

mysqlFormatters: dict[str, Callable[[str], str]] = {
    "BeerName": _wrapWithQuotes,
    "Style": _wrapWithQuotes,
    "Appearance": _base5To10,
    "Aroma": _extractNumerator,
    "Palate": _base5To10,
    "Taste": _extractNumerator,
    "Overall": _base20to10,
    "Created": _epochToDateTime,
    "Username": _wrapWithQuotes,
    "Text": _wrapWithQuotes,
}

def _getRecordValues(keys: list[str], record: dict[str, Any]) -> list[str]:
    NULL_VALUES = ["-", ""]

    result = []
    for key in keys:
        value = record[key]
        if value in NULL_VALUES:
            result.append("NULL")
        elif key in mysqlFormatters:
            result.append(mysqlFormatters[key](value))
        else:
            result.append(value)

    return result


def _keyStrategy(keys: list[str], record: dict[str, Any]) -> str:
    """Extracts all key values from record into a single string formatted for CSV serialization."""
    result = _getRecordValues(keys, record)
    return ",".join(result) 

def main(dataPath: str, outDirectory: str) -> None:
    """Entry point."""
    if not os.path.isfile(dataPath):
        print(f"{outDirectory} is not a valid file.")
        return

    if not os.path.isdir(outDirectory):
        print(f"{outDirectory} is not a valid directory.")
        return

    print(f"Opening data file: {dataPath}")
    with open(dataPath, "r") as file:
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
        table.writeToFile(outDirectory)

REQUIRED_ARG_LENGTH = 3
if len(sys.argv) != REQUIRED_ARG_LENGTH:
    print("Invalid call. Please provide the source json file and output directory as arguments.")
    exit()

if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2])
