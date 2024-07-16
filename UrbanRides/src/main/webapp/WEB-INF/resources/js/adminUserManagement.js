
// Get all the div elements inside the parent div
const divElements = document.querySelectorAll('#ad-dash-support-types > div');

// Add click event listeners to each div element
divElements.forEach(div => {
    div.addEventListener('click', function() {
        // Remove 'active' class from all div elements
        divElements.forEach(d => {
            d.classList.remove('user-active');
        });

        // Add 'active' class to the clicked div element
        this.classList.add('user-active');
    });
});
document.addEventListener('DOMContentLoaded', () => {
    const accordionBtns = document.querySelectorAll('.accordion-btn');

    accordionBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            const targetId = btn.getAttribute('data-bs-target');
            const accordionContent = document.querySelector(targetId);

            // Toggle the 'show' class on accordionContent
            accordionContent.classList.toggle('show');

            // Toggle the text content of the button
            if (accordionContent.classList.contains('show')) {
                btn.textContent = 'Hide';
            } else {
                btn.textContent = 'View';
            }

            // Collapse other accordion items
            accordionBtns.forEach(otherBtn => {
                if (otherBtn !== btn) {
                    const otherTargetId = otherBtn.getAttribute('data-bs-target');
                    const otherAccordionContent = document.querySelector(otherTargetId);

                    // Hide other accordion content
                    otherAccordionContent.classList.remove('show');
                    otherBtn.textContent = 'View';
                }
            });
        });
    });
});
