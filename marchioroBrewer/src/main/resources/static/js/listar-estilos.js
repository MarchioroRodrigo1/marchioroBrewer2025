console.log("listar-estilos.js carregado");

document.addEventListener("DOMContentLoaded", function () {

    const modalElement = document.getElementById("modalExcluir");
    const nomeSpan = document.getElementById("nomeEstilo");
    const btnConfirmar = document.getElementById("btnConfirmarExclusao");

    if (!modalElement || !nomeSpan || !btnConfirmar) {
        console.warn("Elementos do modal não encontrados");
        return;
    }

    // ================================
    // CSRF (necessário para Spring Security)
    // ================================
	const csrfToken = document.querySelector("input[name='_csrf']").value;
	const csrfHeader = "X-CSRF-TOKEN";

    let idParaExcluir = null;

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

    
	});
	
	