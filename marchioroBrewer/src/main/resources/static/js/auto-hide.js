setTimeout(() => {

    // alertas
    document.querySelectorAll(".alerta-auto").forEach(el => {
        el.style.transition = "opacity 0.5s";
        el.style.opacity = "0";

        setTimeout(() => el.remove(), 500);
    });

    // limpa campos inválidos restantes
    document.querySelectorAll(".is-invalid")
        .forEach(el => el.classList.remove("is-invalid"));

}, 3000);