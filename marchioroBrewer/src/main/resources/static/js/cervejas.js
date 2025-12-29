document.addEventListener('DOMContentLoaded', function () {

    const modalExcluir = document.getElementById('modalExcluir');
    const nomeCervejaSpan = document.getElementById('nomeCerveja');
    const formExcluir = document.getElementById('formExcluir');

    // Se não estiver na tela de cervejas, sai silenciosamente
    if (!modalExcluir || !nomeCervejaSpan || !formExcluir) {
        return;
    }

    modalExcluir.addEventListener('show.bs.modal', function (event) {

        const button = event.relatedTarget;

        const id = button.getAttribute('data-id');
        const nome = button.getAttribute('data-nome');

        nomeCervejaSpan.textContent = nome;
        formExcluir.action = `/cervejas/excluir/${id}`;
    });
});
