/* =========================================================
   Função principal: alterna entre tema claro ↔ escuro
   - Usa atributo data-theme no <html>
   - Salva a escolha no localStorage
   - Atualiza o estado visual do switch
========================================================= */
function toggleTheme() {

    const html = document.documentElement;
    const current = html.getAttribute("data-theme");

    if (current === "dark") {
        // Muda para claro
        html.setAttribute("data-theme", "light");
        localStorage.setItem("theme", "light");

        // Marca o switch como desligado
        const chk = document.getElementById("theme-toggle");
        if (chk) chk.checked = false;

    } else {
        // Muda para escuro
        html.setAttribute("data-theme", "dark");
        localStorage.setItem("theme", "dark");

        // Marca o switch como ligado
        const chk = document.getElementById("theme-toggle");
        if (chk) chk.checked = true;
    }
}


/* =========================================================
   Mantém o tema salvo ao recarregar
   - Lê do localStorage
   - Aplica ao <html>
   - Ajusta visualmente o switch
========================================================= */
document.addEventListener("DOMContentLoaded", () => {

    // Obtém tema salvo (padrão: light)
    const saved = localStorage.getItem("theme") || "light";

    // Aplica ao HTML
    document.documentElement.setAttribute("data-theme", saved);

    // Sincroniza estado do switch
    const chk = document.getElementById("theme-toggle");
    if (chk) chk.checked = (saved === "dark");
});
