<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Information</title>
</head>
<body>
    <h1>Student Information</h1>
    <form action="${pageContext.request.contextPath}/hello" method="post">
        <label>Name: <input type="text" name="name" required></label><br><br>
        <label>Email: <input type="email" name="email" required></label><br><br>
        <label>Course: <input type="text" name="course" required></label><br><br>
        <label>Age: <input type="number" name="age" min="1" required></label><br><br>
        <label>Phone: <input type="tel" name="phone" required></label><br><br>
        <button type="submit">Register</button>
    </form>
</body>
</html>
