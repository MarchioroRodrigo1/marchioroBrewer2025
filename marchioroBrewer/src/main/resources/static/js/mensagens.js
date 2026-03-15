document.addEventListener("DOMContentLoaded", function () {

    const mensagem = document.getElementById("mensagemSucesso");

    if (!mensagem) return;

    mostrarToast(mensagem.value);

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
    toast.style.boxShadow = "0 4px 10px rgba(0,0,0,0.2)";

    document.body.appendChild(toast);

    setTimeout(() => {

        toast.style.opacity = "0";
        toast.style.transition = "opacity 0.5s";

        setTimeout(() => toast.remove(), 500);

    }, 3000);
}