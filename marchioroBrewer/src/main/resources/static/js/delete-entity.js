document.addEventListener("DOMContentLoaded", function () {

    const modalElement = document.getElementById("modalExcluir");
    const nomeSpan = document.getElementById("nomeItem");
    const btnConfirmar = document.getElementById("btnConfirmarExclusao");

    if (!modalElement) return;

    const csrfToken = document.querySelector("meta[name='_csrf']").content;
    const csrfHeader = document.querySelector("meta[name='_csrf_header']").content;

    let id = null;
    let url = null;

    const modal = new bootstrap.Modal(modalElement);

    document.querySelectorAll(".btn-excluir").forEach(btn => {

        btn.addEventListener("click", function () {

            id = this.dataset.id;
            url = this.dataset.url;

            const nome = this.dataset.nome;

            nomeSpan.textContent = nome;

            modal.show();
        });

    });

    btnConfirmar.addEventListener("click", function () {

        if (!id || !url) return;

        fetch(`${url}/${id}`, {
            method: "DELETE",
            headers: {
                "X-Requested-With": "XMLHttpRequest",
                [csrfHeader]: csrfToken
            }
        })
        .then(response => {

            if (!response.ok) {
                throw new Error("Erro ao excluir");
            }

            const linha = document
                .querySelector(`.btn-excluir[data-id='${id}']`)
                .closest("tr");

            linha.style.transition = "opacity 0.3s";
            linha.style.opacity = "0";

            setTimeout(() => linha.remove(), 300);

            modal.hide();

            mostrarToast("Registro excluído com sucesso");

        })
        .catch(() => {

            mostrarToast("Erro ao excluir registro", true);

        });

    });
	
	function mostrarToast(mensagem, erro = false) {

	    const toast = document.createElement("div");

	    toast.innerText = mensagem;

	    toast.style.position = "fixed";
	    toast.style.bottom = "20px";
	    toast.style.right = "20px";
	    toast.style.padding = "10px 20px";
	    toast.style.borderRadius = "5px";
	    toast.style.color = "#fff";
	    toast.style.zIndex = "9999";
	    toast.style.backgroundColor = erro ? "#dc3545" : "#28a745";

	    document.body.appendChild(toast);

	    setTimeout(() => toast.remove(), 3000);
	}

});