document.addEventListener('DOMContentLoaded', function () {

    const modalExcluir = document.getElementById('modalExcluir');

    modalExcluir.addEventListener('show.bs.modal', function (event) {

        const button = event.relatedTarget;

        const id = button.getAttribute('data-id');
        const nome = button.getAttribute('data-nome');

        // Atualiza nome no modal
        document.getElementById('nomeCerveja').textContent = nome;

        // Atualiza action do form
        const form = document.getElementById('formExcluir');
        form.action = `/cervejas/excluir/${id}`;
    });
});
