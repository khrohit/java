# Servlet project on Apache Tomcat 11

This project uses Jakarta Servlet 6.1 and JSP 4.0, so it is configured for Tomcat 11. Java 17 or later is required; this workspace has Java 21. The student form is in `student.jsp`, `/hello` handles the POST, and `result.jsp` displays submitted values with JSP Expression Language.

## Run on Windows

From the project root, open PowerShell and run the command file. It handles PowerShell's default script policy for this run:

```powershell
.\servlet\run-tomcat11.cmd
```

The script packages the WAR with Maven, copies it to Tomcat's `webapps` folder, and starts Tomcat if port 8080 is free. Open <http://localhost:8080/servlet/>. Stop the server with `..\apache-tomcat-11.0.27\bin\shutdown.bat`.

Tomcat 11.0.27 is installed in `../apache-tomcat-11.0.27` relative to this project. If you move or replace that folder, update the script's `$TomcatHome` value.
