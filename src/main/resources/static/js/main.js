// LOADER CODE START
const loader = {
  show: () => {
    document.getElementById('loader').style.display = 'flex';
    setTimeout(() => {
      document.getElementById('loader').style.opacity = '1';
    }, 10);
  },

  hide: () => {
    document.getElementById('loader').style.opacity = '0';
    setTimeout(() => {
      document.getElementById('loader').style.display = 'none';
    }, 300);
  }
};

// LOADER CODE END