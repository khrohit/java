# Java Projects

A collection of Java practice projects, including web applications for Apache Tomcat and standalone desktop/networking examples.

## Projects

| Folder | Description | How it runs |
| --- | --- | --- |
| `ShopSphere/` | E-commerce web app using Jakarta Servlets, JSP, JSTL, JDBC, and MySQL | Tomcat 11 |
| `servlet/` | Servlet/JSP student form example | Tomcat 11 |
| `CALS/` | Swing calculator using a model-view-controller structure | Java desktop app |
| `LOGIN_PG/` | Swing login and signup app backed by MySQL | Java desktop app |
| `RMI/` | Java RMI calculator service and client | Two Java processes |

## Requirements

- JDK 17 or later
- Apache Tomcat 11 for the web projects
- Maven for building the web projects
- MySQL 8 for ShopSphere and LOGIN_PG

The Tomcat installation and local Maven cache are not included in this repository. Configure Java, Maven, Tomcat, and MySQL on your computer before running the projects that need them.

## ShopSphere

1. Start MySQL and run [`ShopSphere/database/shopsphere.sql`](ShopSphere/database/shopsphere.sql) in MySQL Workbench. This creates the `shopsphere` database and sample store data.
2. Copy [`ShopSphere/config/db.properties.example`](ShopSphere/config/db.properties.example) to `<TOMCAT_HOME>/conf/shopsphere-db.properties` and set your local database username and password there. Do not commit the real credentials.
3. From the repository folder, run:

   ```cmd
   ShopSphere\run-shopsphere.cmd
   ```

4. Open <http://localhost:8080/ShopSphere/>.

The checkout is a demo and does not process real payments. To enable admin screens, register an account and then promote it in MySQL:

```sql
USE shopsphere;
UPDATE users SET role = 'ADMIN' WHERE email = 'your-email@example.com';
```

## Servlet example

From the repository folder, run:

```cmd
servlet\run-tomcat11.cmd
```

Then open <http://localhost:8080/servlet/>. The form submits to `/hello`, and the result page displays the submitted values.

Both web apps use the same Tomcat server on port 8080. If Tomcat is already running, the scripts deploy the updated app to it.

## CALS calculator

From the repository folder, compile and launch the Swing app:

```cmd
javac -d CALS CALS\ain.java CALS\Cmodels.java CALS\Cviews.java CALS\Ccontroller.java
java -cp CALS ain
```

## LOGIN_PG

Start MySQL and configure the `login_app` database and `users` table for this app. From the repository folder, compile and launch:

```cmd
javac -cp "LOGIN_PG\lib\*" -d LOGIN_PG LOGIN_PG\Main.java LOGIN_PG\LoginFrame.java LOGIN_PG\SignupFrame.java LOGIN_PG\UserDatabase.java
java -cp "LOGIN_PG;LOGIN_PG\lib\*" Main
```

Before sharing or deploying this project, move database credentials out of `LOGIN_PG/UserDatabase.java` and into a local configuration or environment variables.

## RMI calculator

Compile from the repository folder:

```cmd
javac -d . RMI\*.java
```

In one Command Prompt, start the server:

```cmd
java RMI.Server
```

Keep it running. In a second Command Prompt, from the repository folder, start the client:

```cmd
java RMI.Client
```

## Notes

- The web apps are Java web applications and run on Tomcat. CALS, LOGIN_PG, and RMI are standalone Java programs; they do not run inside Tomcat.
- Keep local secrets, database credentials, generated `.class` files, WAR files, and the Tomcat installation out of source control.
