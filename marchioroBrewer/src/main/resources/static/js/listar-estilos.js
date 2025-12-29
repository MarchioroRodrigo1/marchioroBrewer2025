console.log("listar-estilos.js carregado");

document.addEventListener("DOMContentLoaded", function () {

    const modalElement = document.getElementById("modalExcluir");
    const nomeSpan = document.getElementById("nomeEstilo");
    const btnConfirmar = document.getElementById("btnConfirmarExclusao");

    if (!modalElement || !nomeSpan || !btnConfirmar) {
        console.warn("Elementos do modal não encontrados");
        return;
    }

    let idParaExcluir = null;

    // instancia correta do modal
    const modal = new bootstrap.Modal(modalElement);

    // ================================
    // ABRIR MODAL
    // ================================
    document.querySelectorAll(".btn-excluir").forEach(button => {
        button.addEventListener("click", function () {

            idParaExcluir = this.dataset.id;
            nomeSpan.textContent = this.dataset.nome;

            modal.show();
        });
    });

    // ================================
    // CONFIRMAR EXCLUSÃO
    // ================================
    btnConfirmar.addEventListener("click", function () {

        if (!idParaExcluir) return;

        fetch(`/estilos/${idParaExcluir}`, {
            method: "DELETE",
            headers: {
                "X-Requested-With": "XMLHttpRequest"
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao excluir");
            }

            document
                .querySelector(`.btn-excluir[data-id='${idParaExcluir}']`)
                .closest("tr")
                .remove();

            modal.hide();
            idParaExcluir = null;
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao excluir estilo");
        });
    });

});
