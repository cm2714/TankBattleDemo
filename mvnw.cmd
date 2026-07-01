@REM ----------------------------------------------------------------------------
@REM Maven Wrapper
@REM ----------------------------------------------------------------------------
@set WRAPPER_JAR=%~dp0\.mvn\wrapper\maven-wrapper.jar
@if exist "%WRAPPER_JAR%" (
    set JAVA_TOOL_OPTIONS=-Dmaven.multiModuleProjectDirectory="%~dp0"
    java --class-path "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
) else (
    @echo Maven Wrapper JAR not found.
    exit /b 1
)
