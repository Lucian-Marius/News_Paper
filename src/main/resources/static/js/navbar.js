// Highlight active navigation link based on current page
document.addEventListener('DOMContentLoaded', function() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.site-nav__link');
    
    navLinks.forEach(link => {
        // Skip if link doesn't have href
        if (!link.href) return;
        
        try {
            const linkPath = new URL(link.href).pathname;
            
            // Exact match
            if (linkPath === currentPath) {
                link.classList.add('site-nav__link--active');
            }
            // Starts with match (for sub-pages), but not for root
            else if (linkPath !== '/' && currentPath !== '/' && currentPath.startsWith(linkPath)) {
                link.classList.add('site-nav__link--active');
            }
        } catch (e) {
            console.error('Error processing link:', link.href, e);
        }
    });
});
