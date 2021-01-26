@echo off
cls
if exist build rmdir /s /q build
mkdir build
cd src
javac -d ../build com/krzem/nn_image_classification/Main.java&&jar cvmf ../manifest.mf ../build/nn_image_classification.jar -C ../build *&&goto run
cd ..
goto end
:run
cd ..
pushd "build"
for /D %%D in ("*") do (
	rd /S /Q "%%~D"
)
for %%F in ("*") do (
	if /I not "%%~nxF"=="nn_image_classification.jar" del "%%~F"
)
popd
cls
java -jar build/nn_image_classification.jar
:end
