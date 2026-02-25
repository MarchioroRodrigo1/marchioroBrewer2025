document.querySelectorAll("input").forEach(input => {
    input.addEventListener("input", () => {
        document.querySelectorAll(".alerta-auto")
            .forEach(e => e.remove());
    });
});

document.addEventListener("DOMContentLoaded", function () {

    // =========================
    // LIMPAR ERRO AO DIGITAR
    // =========================
	document.querySelectorAll(".form-control").forEach(input => {

	        input.addEventListener("input", function () {

	            this.classList.remove("is-invalid");

	            let feedback =
	                this.parentElement.querySelector(
	                    ".invalid-feedback, .text-danger"
	                );

	            if (feedback) {
	                feedback.innerText = "";
	            }
	        });

	    });

	});