document.addEventListener("DOMContentLoaded", function () {

    /**
     * Remove o estado de erro quando o usuário interagir
     * Funciona para input, select e textarea
     */

    const campos = document.querySelectorAll(
        "input.is-invalid, select.is-invalid, textarea.is-invalid"
    );

    campos.forEach(campo => {

        // Ao ganhar foco
        campo.addEventListener("focus", () => limparErro(campo));

        // Ao digitar / alterar valor
        campo.addEventListener("input", () => limparErro(campo));
        campo.addEventListener("change", () => limparErro(campo));
    });

    function limparErro(campo) {
        campo.classList.remove("is-invalid");

        /**
         * Caso esteja dentro de input-group,
         * o invalid-feedback fica como irmão
         */
        const feedback = campo
            .closest(".input-group, .form-group, .col-12, .col-md-3")
            ?.querySelector(".invalid-feedback");

        if (feedback) {
            feedback.style.display = "none";
        }
    }
});
