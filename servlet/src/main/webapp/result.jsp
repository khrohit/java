<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registration Result</title>
</head>
<body>
    <h1>Registration Successful</h1>
    <hr>
    <h2>Student Details</h2>
    <p>Name: ${name}</p>
    <p>Email: ${email}</p>
    <p>Course: ${course}</p>
    <p>Age: ${age}</p>
    <p>Phone: ${phone}</p>
    <p><a href="${pageContext.request.contextPath}/student.jsp">Register another student</a></p>
</body>
</html>
