# CN-v2020-G06
## Repositório para Computação na Nuvem 2020
### Trabalho Final

### Server
* java -cp TextServiceContract-1.0.jar:server-grpc-1.0-jar-with-dependencies.jar ServerGRPC 8888

### Premium Ocr
* export GOOGLE_APPLICATION_CREDENTIALS="/var/ocr/g06-li62d-v1920-cd7ca2d82c4f.json"
* cd var
* sudo chmod -R 777 ocr
* cd ocr
* java -cp app-ocr-1.0-SNAPSHOT-jar-with-dependencies.jar isel.cn.App premium-ocr-sub

### Free Ocr
* #! /bin/bash
* export GOOGLE_APPLICATION_CREDENTIALS="/var/ocr/g06-li62d-v1920-cd7ca2d82c4f.json"
* cd var
* sudo chmod -R 777 ocr
* cd ocr
* java -cp app-ocr-1.0-SNAPSHOT-jar-with-dependencies.jar isel.cn.App free-ocr-sub

### Premium Translation
* #! /bin/bash
* export GOOGLE_APPLICATION_CREDENTIALS="/var/ocr/g06-li62d-v1920-cd7ca2d82c4f.json"
* cd var
* sudo chmod -R 777 ocr
* cd ocr
* java -cp TranslationApp-1.0-SNAPSHOT-jar-with-dependencies.jar isel.cn.App premium-translation-sub

### Free Translation
* #! /bin/bash
* export GOOGLE_APPLICATION_CREDENTIALS="/var/ocr/g06-li62d-v1920-cd7ca2d82c4f.json"
* cd var
* sudo chmod -R 777 ocr
* cd ocr
* java -cp TranslationApp-1.0-SNAPSHOT-jar-with-dependencies.jar isel.cn.App free-translation-sub
