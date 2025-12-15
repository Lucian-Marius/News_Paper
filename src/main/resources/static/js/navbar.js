// Highlight active navigation link based on current page
document.addEventListener('DOMContentLoaded', function() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.site-nav__link');
    
    navLinks.forEach(link => {
        const linkPath = new URL(link.href).pathname;
        
        // Exact match or if current path starts with link path (for sub-pages)
        if (linkPath === currentPath || (linkPath !== '/' && currentPath.startsWith(linkPath))) {
            link.classList.add('site-nav__link--active');
        }
    });
});
