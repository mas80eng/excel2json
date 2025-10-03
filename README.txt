MainJson2Excel
mvn clean compile exec:java -Dexec.mainClass=${project.groupId}.Main -Dexec.args="excel rug/bymicro.json rug/toconv.json"

MainExcel2Json
mvn clean compile exec:java -Dexec.mainClass=${project.groupId}.Main -Dexec.args="json-u rug/byconv.xlsx rug/tomicro.xlsx"
