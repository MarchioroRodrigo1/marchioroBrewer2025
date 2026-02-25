const modalExcluir = document.getElementById('modalExcluir');

modalExcluir.addEventListener('show.bs.modal', function (event) {

    const button = event.relatedTarget;

    const id = button.getAttribute('data-id');
    const nome = button.getAttribute('data-nome');

    // mostra nome no modal
    document.getElementById('nomeCliente').textContent = nome;

    // define action do form
    const form = document.getElementById('formExcluir');
    form.action = '/usuario/excluir/' + id;
});