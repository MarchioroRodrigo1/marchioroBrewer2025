document.addEventListener("DOMContentLoaded", function () {

    const modal = document.getElementById("modalExcluir");
    const nomeSpan = document.getElementById("nomeCerveja");
    const btnExcluir = document.getElementById("btnConfirmarExclusao");

    modal.addEventListener("show.bs.modal", function (event) {
        const button = event.relatedTarget;

        const id = button.getAttribute("data-id");
        const nome = button.getAttribute("data-nome");

        nomeSpan.textContent = nome;
        btnExcluir.href = `/cervejas/excluir/${id}`;
    });

});
