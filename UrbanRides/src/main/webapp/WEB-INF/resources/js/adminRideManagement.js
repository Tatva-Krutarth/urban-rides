
function toggleAccordion(event, collapseId) {
    event.stopPropagation(); // Prevent default button behavior
    const collapseElement = document.getElementById(collapseId);
    const bsCollapse = new bootstrap.Collapse(collapseElement, {
        toggle: true
    });
}