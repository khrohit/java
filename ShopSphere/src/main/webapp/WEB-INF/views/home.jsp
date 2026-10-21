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
    <section class="promo-section shell" aria-label="ShopSphere offers">
        <div class="promo-heading"><span class="eyebrow">Good finds, good timing</span><div class="promo-controls"><button class="promo-arrow" type="button" data-promo-prev aria-label="Previous offer">←</button><button class="promo-arrow" type="button" data-promo-next aria-label="Next offer">→</button></div></div>
        <div class="promo-viewport" data-promo-carousel>
            <div class="promo-track">
                <article class="promo-card promo-violet"><div class="promo-copy"><span class="promo-kicker">A little treat for your everyday</span><h2>Good things<br>are in season.</h2><p>Thoughtful finds, ready when you are.</p><a href="${pageContext.request.contextPath}/products?availability=on-sale&sort=discount-high" class="promo-cta">Explore sale picks <span>↗</span></a></div><div class="promo-art promo-orbit-art" aria-hidden="true"><span>SS</span><i>✦</i></div></article>
                <article class="promo-card promo-lime"><div class="promo-copy"><span class="promo-kicker">The little upgrades</span><h2>Make room<br>for better.</h2><p>Desk, home and daily carry essentials.</p><a href="${pageContext.request.contextPath}/products?q=lamp" class="promo-cta">Shop home finds <span>↗</span></a></div><div class="promo-art promo-sun-art" aria-hidden="true"><span>HOME<br>EDIT</span></div></article>
                <article class="promo-card promo-orange"><div class="promo-copy"><span class="promo-kicker">For your next great idea</span><h2>Your setup,<br>your way.</h2><p>Useful tech for focused days and fun nights.</p><a href="${pageContext.request.contextPath}/products?q=keyboard" class="promo-cta">Explore tech <span>↗</span></a></div><div class="promo-art promo-stack-art" aria-hidden="true"><span>↗</span><i>◎</i></div></article>
            </div>
        </div>
        <div class="promo-dots" data-promo-dots aria-label="Choose offer"></div>
    </section>
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
            <div><span class="eyebrow">A little something for every day</span><h2>Picked for your next find</h2></div>
            <a class="arrow-link" href="${pageContext.request.contextPath}/products">Shop all products <span>↗</span></a>
        </div>
        <div class="product-rail"><c:forEach var="product" items="${products}"><shop:productCard product="${product}"/></c:forEach></div>
    </section>
    <section class="story-band">
        <div class="shell story-content"><span class="eyebrow">The ShopSphere point of view</span><h2>Less scrolling.<br>More “that’s the one.”</h2><p>Good objects earn their place. We bring together considered design, useful details and prices that make sense.</p><a class="button button-light" href="${pageContext.request.contextPath}/products">Find your next favourite <span>↗</span></a></div>
        <div class="story-mark" aria-hidden="true">S<span>.</span></div>
    </section>
</main>
<%@ include file="fragments/footer.jspf" %>
<script>
(() => {
    const viewport = document.querySelector('[data-promo-carousel]');
    const dots = document.querySelector('[data-promo-dots]');
    if (!viewport || !dots) return;
    const cards = [...viewport.querySelectorAll('.promo-card')];
    let current = 0;
    let timer;
    cards.forEach((card, index) => {
        const dot = document.createElement('button');
        dot.type = 'button'; dot.className = 'promo-dot';
        dot.setAttribute('aria-label', `Show offer ${index + 1}`);
        dot.addEventListener('click', () => show(index));
        dots.append(dot);
    });
    const dotButtons = [...dots.children];
    function show(index) {
        current = (index + cards.length) % cards.length;
        viewport.scrollTo({ left: cards[current].offsetLeft - cards[0].offsetLeft, behavior: 'smooth' });
        dotButtons.forEach((dot, i) => dot.setAttribute('aria-current', i === current ? 'true' : 'false'));
    }
    document.querySelector('[data-promo-prev]').addEventListener('click', () => show(current - 1));
    document.querySelector('[data-promo-next]').addEventListener('click', () => show(current + 1));
    const reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    function start() { if (!reducedMotion) timer = window.setInterval(() => show(current + 1), 6000); }
    function stop() { window.clearInterval(timer); }
    viewport.addEventListener('mouseenter', stop); viewport.addEventListener('mouseleave', start);
    viewport.addEventListener('focusin', stop); viewport.addEventListener('focusout', start);
    show(0); start();
})();
</script>
</body>
</html>
