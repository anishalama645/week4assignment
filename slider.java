
"use strict";

document.addEventListener("DOMContentLoaded", () => {
    const slider = document.getElementById("imageSlider");
    const track = document.getElementById("sliderTrack");
    const dotsContainer = document.getElementById("sliderDots");
    const prevBtn = document.getElementById("prevBtn");
    const nextBtn = document.getElementById("nextBtn");
    const caption = document.getElementById("sliderCaption");

    // Store the original slides before creating clones.
    const originalSlides = Array.from(track.children);
    const totalSlides = originalSlides.length;

    if (totalSlides === 0) {
        return;
    }

    // Clone the last and first slides for seamless looping.
    const firstClone = originalSlides[0].cloneNode(true);
    const lastClone = originalSlides[totalSlides - 1].cloneNode(true);

    firstClone.setAttribute("aria-hidden", "true");
    lastClone.setAttribute("aria-hidden", "true");

    track.appendChild(firstClone);
    track.insertBefore(lastClone, originalSlides[0]);

    // Start at the first original slide.
    let currentIndex = 1;
    let isTransitioning = false;

    // Create one clickable dot for each original slide.
    const dots = originalSlides.map((slide, index) => {
        const dot = document.createElement("button");

        dot.type = "button";
        dot.classList.add("dot");
        dot.setAttribute("aria-label", `Go to image ${index + 1}`);
        dot.addEventListener("click", () => {
            goToSlide(index + 1);
        });

        dotsContainer.appendChild(dot);
        return dot;
    });

    // Move the track horizontally.
    function updateSlider(animate = true) {
        track.style.transition = animate
            ? "transform 0.5s ease-in-out"
            : "none";

        track.style.transform =
            `translateX(-${currentIndex * 100}%)`;

        updateDotsAndCaption();
    }

    // Keep the dots and caption synchronized.
    function updateDotsAndCaption() {
        let realIndex = currentIndex - 1;

        if (realIndex < 0) {
            realIndex = totalSlides - 1;
        } else if (realIndex >= totalSlides) {
            realIndex = 0;
        }

        dots.forEach((dot, index) => {
            const isActive = index === realIndex;

            dot.classList.toggle("active", isActive);

            if (isActive) {
                dot.setAttribute("aria-current", "true");
            } else {
                dot.removeAttribute("aria-current");
            }
        });

        caption.textContent =
            `Image ${realIndex + 1} of ${totalSlides}`;
    }

    // Navigate to a specific slide index.
    function goToSlide(index) {
        if (isTransitioning) {
            return;
        }

        currentIndex = index;
        isTransitioning = true;
        updateSlider(true);
    }

    // Next image.
    function nextSlide() {
        if (isTransitioning) {
            return;
        }

        currentIndex++;
        isTransitioning = true;
        updateSlider(true);
    }

    // Previous image.
    function previousSlide() {
        if (isTransitioning) {
            return;
        }

        currentIndex--;
        isTransitioning = true;
        updateSlider(true);
    }

    nextBtn.addEventListener("click", nextSlide);
    prevBtn.addEventListener("click", previousSlide);

    // After reaching a clone, jump invisibly to its real counterpart.
    track.addEventListener("transitionend", (event) => {
        if (event.target !== track ||
            event.propertyName !== "transform") {
            return;
        }

        if (currentIndex === totalSlides + 1) {
            currentIndex = 1;
            updateSlider(false);
        } else if (currentIndex === 0) {
            currentIndex = totalSlides;
            updateSlider(false);
        }

        isTransitioning = false;
    });

    // Initialize the slider without animating from the wrong position.
    updateSlider(false);
});

