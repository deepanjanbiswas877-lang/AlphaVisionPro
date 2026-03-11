@if "%DEBUG%"=="" @echo off
@rem Gradle startup script for Windows
set JAVA_OPTS=-Xmx64m -Xms64m
"%JAVA_HOME%\bin\java.exe" -classpath "%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
