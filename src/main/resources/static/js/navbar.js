// Highlight active navigation link based on current page
document.addEventListener('DOMContentLoaded', function() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.site-nav__link');
    
    navLinks.forEach(link => {
        const linkPath = new URL(link.href).pathname;
        
        // Exact match for home page, or starts with for sub-pages (but not root for other pages)
        if (linkPath === currentPath || 
            (linkPath !== '/' && currentPath !== '/' && currentPath.startsWith(linkPath))) {
            link.classList.add('site-nav__link--active');
        }
    });
});
