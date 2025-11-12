document.addEventListener('DOMContentLoaded', () => {
    const body = document.body;
    const themeToggle = document.getElementById('theme-toggle');
//    const sidebar = document.getElementById('sidebar');
//    const mainContent = document.getElementById('main-content');
//    const menuToggle = document.getElementById('menu-toggle');

    // --- 🌙 Ação do Tema (Esta lógica deve rodar em todas as páginas) ---

    // 1. Carregar tema salvo ou usar o padrão do sistema
    const savedTheme = localStorage.getItem('opencerts.theme');
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;

    if (savedTheme === 'dark' || (!savedTheme && prefersDark)) {
        body.classList.add('theme-dark');
        // Apenas marca o checkbox se ele existir na página (não existe no login)
        if (themeToggle) {
            themeToggle.checked = true;
        }
    }

    // Listener para o Toggle (apenas se ele existir)
    if (themeToggle) {
        themeToggle.addEventListener('change', () => {
            if (themeToggle.checked) {
                body.classList.add('theme-dark');
                localStorage.setItem('theme', 'dark');
            } else {
                body.classList.remove('theme-dark');
                localStorage.setItem('theme', 'light');
            }
        });
    }

    // --- 🧭 Ação da Sidebar (para Mobile) ---
    // --- 🧭 Ação da Sidebar (Apenas se o elemento principal existir) ---

    const sidebar = document.getElementById('sidebar');
    const mainContent = document.getElementById('main-content');
    const menuToggle = document.getElementById('menu-toggle');

    // O restante do código da Sidebar só deve rodar se estivermos na área logada
        if (sidebar && mainContent && menuToggle) {
            // 1. Listener para o botão Hambúrguer
            menuToggle.addEventListener('click', () => {
                sidebar.classList.toggle('expanded');
                mainContent.classList.toggle('sidebar-expanded');
            });

            // 2. Listener para fechar a sidebar ao clicar no conteúdo
            mainContent.addEventListener('click', () => {
                if (window.innerWidth <= 768 && sidebar.classList.contains('expanded')) {
                    sidebar.classList.remove('expanded');
                    mainContent.classList.remove('sidebar-expanded');
                }
            });
        }

    // 1. Listener para o novo botão Hambúrguer
//    if (menuToggle) {
//        menuToggle.addEventListener('click', () => {
//            // A lógica de expandir/recolher deve ser a mesma para sidebar e main-content
//            sidebar.classList.toggle('expanded');
//            mainContent.classList.toggle('sidebar-expanded');
//        });
//    }
//
//    // 2. Listener para fechar a sidebar ao clicar no conteúdo (melhora a usabilidade mobile)
//    mainContent.addEventListener('click', () => {
//        // Verifica se a sidebar está expandida e fecha
//        if (window.innerWidth <= 768 && sidebar.classList.contains('expanded')) {
//            sidebar.classList.remove('expanded');
//            mainContent.classList.remove('sidebar-expanded');
//        }
//    });

});