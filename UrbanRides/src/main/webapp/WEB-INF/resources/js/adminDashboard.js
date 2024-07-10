$('#ad-dash-support-types div').on('click', function() {
    $(this).siblings().removeClass('querry-active');
    $(this).toggleClass('querry-active');
});








document.addEventListener('DOMContentLoaded', () => {
    const counter = document.getElementById('counter1');
    const target = +counter.getAttribute('data-target');
    const duration = 2000; // Duration of animation in milliseconds
    const frameRate = 1000 / 60; // Roughly 60 frames per second

    const updateCounter = () => {
        const current = +counter.innerText;
        const increment = target / (duration / frameRate);

        if (current < target) {
            counter.innerText = Math.ceil(current + increment);
            setTimeout(updateCounter, frameRate);
        } else {
            counter.innerText = target;
        }
    };

    updateCounter();
});document.addEventListener('DOMContentLoaded', () => {
    const counter = document.getElementById('counter2');
    const target = +counter.getAttribute('data-target');
    const duration = 2000; // Duration of animation in milliseconds
    const frameRate = 1000 / 60; // Roughly 60 frames per second

    const updateCounter = () => {
        const current = +counter.innerText;
        const increment = target / (duration / frameRate);

        if (current < target) {
            counter.innerText = Math.ceil(current + increment);
            setTimeout(updateCounter, frameRate);
        } else {
            counter.innerText = target;
        }
    };

    updateCounter();
});
document.addEventListener('DOMContentLoaded', () => {
    const counter = document.getElementById('counter3');
    const target = +counter.getAttribute('data-target');
    const duration = 2000; // Duration of animation in milliseconds
    const frameRate = 1000 / 60; // Roughly 60 frames per second

    const updateCounter = () => {
        const current = +counter.innerText;
        const increment = target / (duration / frameRate);

        if (current < target) {
            counter.innerText = Math.ceil(current + increment);
            setTimeout(updateCounter, frameRate);
        } else {
            counter.innerText = target;
        }
    };

    updateCounter();
});
document.addEventListener('DOMContentLoaded', () => {
    const counter = document.getElementById('counter4');
    const target = +counter.getAttribute('data-target');
    const duration = 2000; // Duration of animation in milliseconds
    const frameRate = 1000 / 60; // Roughly 60 frames per second

    const updateCounter = () => {
        const current = +counter.innerText;
        const increment = target / (duration / frameRate);

        if (current < target) {
            counter.innerText = Math.ceil(current + increment);
            setTimeout(updateCounter, frameRate);
        } else {
            counter.innerText = target;
        }
    };

    updateCounter();
});
