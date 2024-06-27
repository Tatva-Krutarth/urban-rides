document.getElementById('back-button').addEventListener('click', function() {
    history.go(-1); /* move back in history on click */
});


function toggleAccordion(event, collapseId) {
    event.stopPropagation(); // Prevent default button behavior
    const collapseElement = document.getElementById(collapseId);
    const bsCollapse = new bootstrap.Collapse(collapseElement, {
        toggle: true
    });
}