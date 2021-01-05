# OdiMappingBuilder



## Running from console

- Configure parameters.conf

- Required libraries:

>lib_jdbc=lib/ojdbc8/* --> Download ojdbc8.jar

>lib_poi=lib/poi/* --> download apache poi library jars

>lib_sdk=/home/cihan/Oracle/Middleware/Oracle_Home/odi/sdk/lib/* --> odi installation path

>lib_modules=/home/cihan/Oracle/Middleware/Oracle_Home/oracle_common/modules/* --> odi installation path

- Call OdiMappingBuilder.bat or OdiMappingBuilder.sh



## Running from ODI Studio groovy editor

- Odı Studio --> Tools --> Preferences --> ODI --> System --> Groovy

- Add jar to classpath : C:\OdiMappingBuilder\OdiMappingBuilder.jar

```
import odimappingbuilder.OdiMappingBuilder;
OdiMappingBuilder odiMappingBuilder=new OdiMappingBuilder();
odiMappingBuilder.build("C:\\OdiMappingBuilder\\Mapping.csv", odiInstance);

```

