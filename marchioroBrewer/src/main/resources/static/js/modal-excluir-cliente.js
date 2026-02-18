document.addEventListener("DOMContentLoaded", function () {

    const modal = document.getElementById("modalExcluir");

    modal.addEventListener("show.bs.modal", function (event) {

        const button = event.relatedTarget;

        const id = button.getAttribute("data-id");
        const nome = button.getAttribute("data-nome");

        // Mostra o nome no modal
        document.getElementById("nomeCliente").textContent = nome;

        // Define a action dinamicamente
        const form = document.getElementById("formExcluir");
        form.action = "/clientes/excluir/" + id;

    });

});
