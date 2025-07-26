# excel2json

<!-- GETTING STARTED -->

## Getting Started

```sh
mvn clean package
```

<!-- USAGE EXAMPLES -->

## Usage

### Syntax

```sh
java -jar jar-file.jar <format-to-convert> <list-of-files>
```

### Example 1 - Json to Excel

```sh
java -jar excel2json-1.0.0.jar excel file1.json file2.json
```

### Example 2 - Excel to Json flat

```sh
java -jar excel2json-1.0.0.jar json-f file1.xlsx file2.xlsx
```

### Example 3 - Excel to Json unflat

```sh
java -jar excel2json-1.0.0.jar json-u file1.xlsx file2.xlsx
```
