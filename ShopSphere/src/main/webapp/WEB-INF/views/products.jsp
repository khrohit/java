<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="shop" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Shop all — ShopSphere</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/site.css">
</head>
<body>
<%@ include file="fragments/header.jspf" %>
<main class="shell page-shell">
    <div class="page-intro">
        <span class="eyebrow">The ShopSphere collection</span>
        <h1>Find your everyday favourite.</h1>
        <p>Useful, well-made pieces chosen to make the everyday a little better.</p>
    </div>
    <form class="filter-bar" method="get" action="${pageContext.request.contextPath}/products" role="search">
        <input type="search" name="q" value="${fn:escapeXml(param.q)}" placeholder="Search by name, brand or detail" aria-label="Search products">
        <select name="category" aria-label="Filter by category">
            <option value="">All categories</option>
            <c:forEach var="category" items="${categories}">
                <option value="${category[0]}" ${param.category eq category[0] ? 'selected' : ''}><c:out value="${category[1]}"/></option>
            </c:forEach>
        </select>
        <select name="sort" aria-label="Sort products">
            <option value="">Recently added</option>
            <option value="price-low" ${param.sort eq 'price-low' ? 'selected' : ''}>Price: low to high</option>
            <option value="price-high" ${param.sort eq 'price-high' ? 'selected' : ''}>Price: high to low</option>
            <option value="name" ${param.sort eq 'name' ? 'selected' : ''}>Name</option>
        </select>
        <button class="button button-dark" type="submit">Apply filters</button>
    </form>
    <div class="results-line">
        <span><c:out value="${totalProducts}"/> products</span>
        <span>Thoughtful finds, no fuss.</span>
    </div>
    <div class="product-grid">
        <c:forEach var="product" items="${products}"><shop:productCard product="${product}"/></c:forEach>
        <c:if test="${empty products}">
            <div class="empty-state">
                <div class="empty-icon" aria-hidden="true">⌕</div>
                <h2>No products found</h2>
                <p>Try another search or browse the full collection.</p>
                <a class="button button-dark" href="${pageContext.request.contextPath}/products">Clear filters</a>
            </div>
        </c:if>
    </div>
    <c:if test="${totalPages gt 1}">
        <nav class="pagination" aria-label="Product pages">
            <c:if test="${currentPage gt 1}">
                <c:url var="previousPageUrl" value="/products">
                    <c:param name="page" value="${currentPage - 1}"/><c:param name="q" value="${param.q}"/>
                    <c:param name="category" value="${param.category}"/><c:param name="sort" value="${param.sort}"/>
                </c:url>
                <a class="page-link" href="${previousPageUrl}" rel="prev">← Previous</a>
            </c:if>
            <span class="page-count">Page <c:out value="${currentPage}"/> of <c:out value="${totalPages}"/></span>
            <c:if test="${currentPage lt totalPages}">
                <c:url var="nextPageUrl" value="/products">
                    <c:param name="page" value="${currentPage + 1}"/><c:param name="q" value="${param.q}"/>
                    <c:param name="category" value="${param.category}"/><c:param name="sort" value="${param.sort}"/>
                </c:url>
                <a class="page-link" href="${nextPageUrl}" rel="next">Next →</a>
            </c:if>
        </nav>
    </c:if>
</main>
<%@ include file="fragments/footer.jspf" %>
</body>
</html>
