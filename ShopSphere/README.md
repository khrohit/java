# ShopSphere

ShopSphere is a Java 17+ e-commerce capstone built with Jakarta Servlets, JSP, JSTL, JDBC and MySQL for Apache Tomcat 11. It keeps its web MVC flow in layers: JSP views → servlet controllers → services → DAO classes → MySQL.

## Included core flow

- Customer registration, PBKDF2 password hashing, sign-in, sign-out and session-based access filters.
- Product catalogue with search, category filtering, sorting and product details.
- Persistent cart, wishlist, profile updates, delivery address, coupon validation and mock checkout.
- Transactional order placement with stock checks, order history and mock payment records.
- Admin dashboard, product add/edit/archive, and order status management.
- JSP EL/JSTL, tag files, fragments, servlet context and session listeners.

Mock checkout does not collect or process real payment details.

## 1. Set up MySQL

MySQL 8 must be running. In MySQL Workbench, connect to your local MySQL server, open `database/shopsphere.sql`, and execute the full script. It creates the schema, example products and a `WELCOME10` demo coupon.

Copy `config/db.properties.example` to:

```text
C:\Users\Naman\OneDrive\Desktop\java\apache-tomcat-11.0.27\conf\shopsphere-db.properties
```

Set `db.username` and `db.password` in that Tomcat configuration file to your local MySQL credentials. Keep the real password out of source control. If your local `root` account has no password, the example values can be used as-is.

To enable the admin screens, create a normal account in the app, then run this in Workbench with that email address:

```sql
USE shopsphere;
UPDATE users SET role = 'ADMIN' WHERE email = 'your-email@example.com';
```

## 2. Build and run on Tomcat 11

From the workspace root (`C:\Users\Naman\OneDrive\Desktop\java`), run:

```powershell
.\ShopSphere\run-shopsphere.cmd
```

The command builds `ShopSphere/target/ShopSphere.war`, deploys it to Tomcat 11's `webapps` directory, and starts Tomcat if port 8080 is free. When Tomcat is already running, it deploys the changed WAR automatically.

Open <http://localhost:8080/ShopSphere/>. Create a customer account, browse products, add items to your bag, and use `WELCOME10` at mock checkout. Use the admin steps above for the admin dashboard.

## Configuration

The app reads `SHOPSPHERE_DB_URL`, `SHOPSPHERE_DB_USER`, and `SHOPSPHERE_DB_PASSWORD` environment variables first. Otherwise it reads `shopsphere-db.properties` from Tomcat's `conf` folder. The default connection is `127.0.0.1:3306/shopsphere`.

The separate `labs` folder holds Advanced Java learning examples. Those are intentionally separate from the customer web workflow.
