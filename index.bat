echo off
echo NUL>_.class&&del /s /f /q *.class
cls
javac com/krzem/nn_image_classification/Main.java&&java com/krzem/nn_image_classification/Main
start /min cmd /c "echo NUL>_.class&&del /s /f /q *.class"