MainJson2Excel
mvn clean compile exec:java -Dexec.mainClass=${project.groupId}.Main -Dexec.args="excel rug/lang2.json rug/lang1.json"

MainExcel2Json
mvn clean compile exec:java -Dexec.mainClass=${project.groupId}.Main -Dexec.args="json-u rug/micro1.xlsx rug/mcro2.xlsx"
