from typing import Dict, List, Tuple
import re

HEADER_PATTERN = r"[{\s]'\w+/(\w+)':.+?[,}]"
JSON_PATTERN = r"[{\s]'\w+/(\w+)':\s['\"](.+?)['\"][,}]"

REVIEW_START_INDEX = 5
PROFILE_NAME_INDEX = 11
REGEX_VALUE_INDEX = 1

REVIEW_ATTRIBUTE_COUNT = 9

with open("test.json", "r") as file:
    lines = file.readlines()

headers: List[str] = re.findall(HEADER_PATTERN, lines[0])
headers = [f"{item[0].upper()}{item[1:]}" for item in headers]

beers: Dict[str, object] = {}
beers["header"] = ",".join(headers[:REVIEW_START_INDEX])

reviews: List[str] = []
reviews.append(",".join(["BeerName"] + headers[REVIEW_START_INDEX:]))

users = set()

def format_entry(items: List[str]) -> str:
    return ",".join(f'"{item[REGEX_VALUE_INDEX]}"' for item in items)
    
pattern = re.compile(JSON_PATTERN)
for i, line in enumerate(lines):
    if i % 100000 == 0:
        print(i)
    try:
        regex_result: List[Tuple[str, str]] = pattern.findall(line)

        beer_attributes = regex_result[:REVIEW_START_INDEX]
        entry = format_entry(beer_attributes)

        BEER_NAME_INDEX = 0
        beer_name = beer_attributes[BEER_NAME_INDEX][REGEX_VALUE_INDEX]
        if not beer_name in beers:
            beers[beer_name] = entry 

        review_attributes = [beer_attributes[BEER_NAME_INDEX]] + regex_result[REVIEW_START_INDEX:]
        entry = format_entry(review_attributes)
        reviews.append(entry)

        users.add(regex_result[PROFILE_NAME_INDEX][1])
    except IndexError as e:
        break
    except Exception as e:
        print(e)
        print(f"Error on line: {i}")

with open("beers.csv", "w") as file:
    file.write("\n".join(beers.values()))

with open("reviews.csv", "w") as file:
    file.write("\n".join(reviews))

with open("users.csv", "w") as file:
    file.write("Username\n")
    file.write("\n".join(list(users)))