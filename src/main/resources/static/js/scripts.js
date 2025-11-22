document.addEventListener("DOMContentLoaded", function() {
    const menuToggle = document.getElementById('menu-toggle');
    const sidebar = document.getElementById('sidebar');

    if(menuToggle && sidebar) {
        menuToggle.addEventListener('click', function() {
            // Alterna a classe que controla a visibilidade
            // Sugestão: No mobile a sidebar começa oculta, então usamos 'open'
            // No desktop ela começa visível, podemos usar 'closed' para esconder
            if (window.innerWidth <= 768) {
                sidebar.classList.toggle('open');
            } else {
                sidebar.classList.toggle('closed');
            }
        });
    }
});

document.addEventListener("DOMContentLoaded", () => {
    const STORAGE_KEY = "opencerts.theme";
    const themeToggle = document.getElementById("theme-toggle");

    // Recupera tema salvo
    const savedTheme = localStorage.getItem(STORAGE_KEY);
    if (savedTheme === "light") {
        document.body.classList.add("light-theme");
        themeToggle.checked = true;
    }

    // Listener do toggle
    themeToggle.addEventListener("change", () => {
        const isLight = themeToggle.checked;

        document.body.classList.toggle("light-theme", isLight);

        // Salva no localStorage
        localStorage.setItem(STORAGE_KEY, isLight ? "light" : "dark");
    });
});