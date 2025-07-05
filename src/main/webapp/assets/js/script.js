// JavaScript to enable horizontal scrolling for carousels with arrow buttons

document.addEventListener('DOMContentLoaded', () => {
    const buttons = document.querySelectorAll('.carousel-btn');
  
    buttons.forEach(button => {
      button.addEventListener('click', () => {
        const targetId = button.getAttribute('data-target');
        const carousel = document.getElementById(targetId);
        const track = carousel.querySelector('.carousel-track');
        const cardWidth = track.querySelector('.card').offsetWidth + 16; // card width + gap
  
        if (button.classList.contains('next')) {
          track.scrollBy({ left: cardWidth, behavior: 'smooth' });
        } else {
          track.scrollBy({ left: -cardWidth, behavior: 'smooth' });
        }
      });
    });
  });
  