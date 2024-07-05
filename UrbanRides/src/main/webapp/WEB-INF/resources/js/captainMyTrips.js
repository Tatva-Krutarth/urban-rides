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




//
// document.addEventListener('DOMContentLoaded', function () {
//     const stars = document.querySelectorAll('.give-star');
//     const ratingText = document.getElementById('rating-text-conclude');
//     let rating = 5; // Example rating from backend
//
//     function updateRating(rating) {
//         stars.forEach((star, index) => {
//             star.classList.remove('star-filled', 'star-half-filled');
//             if (index < Math.floor(rating)) {
//                 star.classList.add('star-filled');
//             } else if (index === Math.floor(rating) && rating % 1 !== 0) {
//                 star.classList.add('star-half-filled');
//             }
//         });
//         ratingText.textContent = rating + ' stars';
//     }
//
//     updateRating(rating);
//
//     stars.forEach((star, index) => {
//         star.addEventListener('click', () => {
//             rating = (rating === index + 1) ? index + 0.5 : index + 1;
//             updateRating(rating);
//         });
//     });
// });