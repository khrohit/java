<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Products — ShopSphere Admin</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/site.css"></head>
<body><%@ include file="../fragments/header.jspf" %><main class="shell page-shell admin-page">
    <div class="page-intro"><div><span class="eyebrow">Catalogue management</span><h1>Products.</h1></div></div>
    <nav class="admin-nav"><a href="${pageContext.request.contextPath}/admin">Overview</a><a href="${pageContext.request.contextPath}/admin/products">Products</a><a href="${pageContext.request.contextPath}/admin/categories">Categories</a><a href="${pageContext.request.contextPath}/admin/coupons">Coupons</a><a href="${pageContext.request.contextPath}/admin/users">Customers</a><a href="${pageContext.request.contextPath}/admin/orders">Orders</a></nav>
    <c:if test="${not empty notice}"><div class="alert alert-success"><c:out value="${notice}"/></div></c:if>
    <section class="admin-section"><span class="eyebrow">Add to the collection</span><h2>New product</h2>
        <form method="post" action="${pageContext.request.contextPath}/admin/products" class="admin-form">
            <input type="hidden" name="action" value="save"><label>Product name<input name="name" required maxlength="180"></label><label>Brand<input name="brand" maxlength="100"></label>
            <label>Category<select name="categoryId" required><c:forEach var="category" items="${categories}"><option value="${category[0]}"><c:out value="${category[1]}"/></option></c:forEach></select></label>
            <label>Price<input type="number" name="price" min="0" step="0.01" required></label><label>Discount %<input type="number" name="discount" min="0" max="100" step="0.01" value="0" required></label><label>Stock<input type="number" name="stock" min="0" value="0" required></label><label>Image URL<input name="imageUrl" type="url" maxlength="1000"></label><label class="wide">Description<textarea name="description" maxlength="4000"></textarea></label><button class="button button-dark" type="submit">Add product <span>↗</span></button>
        </form>
    </section>
    <section class="admin-section"><span class="eyebrow">Keep it fresh</span><h2>Current products</h2><div class="table-wrap"><table class="data-table"><thead><tr><th>Product</th><th>Category</th><th>Price</th><th>Stock</th><th>Manage</th></tr></thead><tbody>
        <c:forEach var="product" items="${products}"><tr><td><strong><c:out value="${product.name}"/></strong><br><small><c:out value="${product.brand}"/></small><details><summary>Edit details</summary>
            <form method="post" action="${pageContext.request.contextPath}/admin/products" class="admin-form admin-edit"><input type="hidden" name="action" value="save"><input type="hidden" name="productId" value="${product.productId}">
                <label>Name<input name="name" value="${fn:escapeXml(product.name)}" required></label><label>Brand<input name="brand" value="${fn:escapeXml(product.brand)}"></label>
                <label>Category<select name="categoryId"><c:forEach var="category" items="${categories}"><option value="${category[0]}" ${product.categoryId eq category[0] ? 'selected' : ''}><c:out value="${category[1]}"/></option></c:forEach></select></label>
                <label>Price<input type="number" name="price" step="0.01" value="${product.price}" required></label><label>Discount<input type="number" name="discount" step="0.01" value="${product.discount}" required></label><label>Stock<input type="number" name="stock" value="${product.stock}" required></label>
                <label class="wide">Image URL<input name="imageUrl" value="${fn:escapeXml(product.imageUrl)}"></label><label class="wide">Description<textarea name="description"><c:out value="${product.description}"/></textarea></label><button class="button button-dark" type="submit">Save changes</button>
            </form></details></td><td><c:out value="${product.categoryName}"/></td><td><fmt:formatNumber value="${product.salePrice}" type="currency" currencyCode="INR"/></td><td><c:out value="${product.stock}"/></td><td><form method="post" action="${pageContext.request.contextPath}/admin/products"><input type="hidden" name="action" value="archive"><input type="hidden" name="productId" value="${product.productId}"><button class="text-button" type="submit" onclick="return confirm('Remove this product from the shop?')">Archive</button></form></td></tr></c:forEach>
    </tbody></table></div></section>
</main><%@ include file="../fragments/footer.jspf" %></body></html>
