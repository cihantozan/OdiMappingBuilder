for /F "tokens=*" %%I in (parameters.conf) do set %%I
java -cp OdiMappingBuilder.jar:%lib_jdbc%:%lib_poi%:%lib_sdk%:%lib_modules% odimappingbuilder.Main
