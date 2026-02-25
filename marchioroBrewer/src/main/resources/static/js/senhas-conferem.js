document.addEventListener("DOMContentLoaded", function () {

    const senha = document.getElementById("senha");
    const confirmar = document.getElementById("confirmacaoSenha");
    const erroSenha = document.getElementById("erroSenha");

    function validarSenha() {

        if (!senha || !confirmar) return true;

        if (senha.value !== confirmar.value) {

            erroSenha.innerText = "As senhas não conferem";
            confirmar.classList.add("is-invalid");

            return false;
        }

        erroSenha.innerText = "";
        confirmar.classList.remove("is-invalid");

        return true;
    }

    confirmar.addEventListener("input", validarSenha);
    senha.addEventListener("input", validarSenha);

    // torna global para o onsubmit
    window.validarSenha = validarSenha;
});