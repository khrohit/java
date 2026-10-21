<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="shop" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>ShopSphere — Thoughtful finds</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/site.css">
</head>
<body>
<%@ include file="fragments/header.jspf" %>
<main id="main-content">
    <section class="hero shell">
        <div class="hero-copy">
            <span class="eyebrow"><fmt:message key="home.eyebrow" bundle="${msg}"/></span>
            <h1><fmt:message key="home.title" bundle="${msg}"/></h1>
            <p><fmt:message key="home.subtitle" bundle="${msg}"/></p>
            <a class="button button-dark" href="${pageContext.request.contextPath}/products"><fmt:message key="home.cta" bundle="${msg}"/> <span>↗</span></a>
            <div class="hero-note"><span class="note-dot"></span> Useful things, thoughtfully selected</div>
        </div>
        <div class="hero-art">
            <div class="hero-orbit orbit-one"></div>
            <div class="hero-orbit orbit-two"></div>
            <div class="hero-sticker">GOOD<br>THINGS<br>LIVE HERE</div>
            <div class="hero-product">
                <img src="https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=1000&q=90" alt="Over-ear headphones" fetchpriority="high" decoding="async">
                <span class="hero-product-caption">Sound, considered.</span>
            </div>
            <div class="hero-sparkle" aria-hidden="true">✳</div>
        </div>
    </section>
    <section class="benefit-strip" aria-label="ShopSphere benefits">
        <div class="shell benefit-grid"><span>✦&nbsp; Carefully curated</span><span>↗&nbsp; Easy, secure checkout</span><span>◎&nbsp; Here for everyday</span></div>
    </section>
    <section class="section shell" id="featured">
        <div class="section-heading">
            <div><span class="eyebrow">A little something for every day</span><h2><fmt:message key="home.featured" bundle="${msg}"/></h2></div>
            <a class="arrow-link" href="${pageContext.request.contextPath}/products">Shop all products <span>↗</span></a>
        </div>
        <div class="product-grid"><c:forEach var="product" items="${products}"><shop:productCard product="${product}"/></c:forEach></div>
    </section>
    <section class="story-band">
        <div class="shell story-content"><span class="eyebrow">The ShopSphere point of view</span><h2>Less scrolling.<br>More “that’s the one.”</h2><p>Good objects earn their place. We bring together considered design, useful details and prices that make sense.</p><a class="button button-light" href="${pageContext.request.contextPath}/products">Find your next favourite <span>↗</span></a></div>
        <div class="story-mark" aria-hidden="true">S<span>.</span></div>
    </section>
</main>
<%@ include file="fragments/footer.jspf" %>
</body>
</html>
