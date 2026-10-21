<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="product" required="true" type="com.shopsphere.model.Product" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<article class="product-card">
    <a class="product-image" href="${pageContext.request.contextPath}/product?id=${product.productId}">
        <c:if test="${product.discount gt 0}"><span class="discount-pill"><fmt:formatNumber value="${product.discount}" maxFractionDigits="0"/>% off</span></c:if>
        <img src="${fn:escapeXml(product.imageUrl)}" alt="${fn:escapeXml(product.name)}" loading="lazy" decoding="async">
        <span class="image-arrow">↗</span>
    </a>
    <div class="product-meta"><span><c:out value="${product.categoryName}"/></span><span><c:out value="${product.brand}"/></span></div>
    <a class="product-name" href="${pageContext.request.contextPath}/product?id=${product.productId}"><c:out value="${product.name}"/></a>
    <div class="price-line">
        <strong><fmt:formatNumber value="${product.salePrice}" type="currency" currencyCode="INR"/></strong>
        <c:if test="${product.discount gt 0}"><del><fmt:formatNumber value="${product.price}" type="currency" currencyCode="INR"/></del></c:if>
    </div>
    <form action="${pageContext.request.contextPath}/cart" method="post" class="card-add">
        <input type="hidden" name="action" value="add"><input type="hidden" name="productId" value="${product.productId}">
        <input type="hidden" name="quantity" value="1"><button type="submit" ${product.stock le 0 ? 'disabled' : ''}>${product.stock le 0 ? 'Sold out' : 'Add to bag'}</button>
    </form>
</article>
