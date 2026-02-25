
const senhaInput = document.getElementById("senha");
const barra = document.getElementById("forcaSenha");
const texto = document.getElementById("textoForca");

senhaInput.addEventListener("input", function () {

    const senha = senhaInput.value;
    let pontos = 0;

    if (senha.length >= 8) pontos++;
    if (/[A-Z]/.test(senha)) pontos++;
    if (/[a-z]/.test(senha)) pontos++;
    if (/[0-9]/.test(senha)) pontos++;
    if (/[^A-Za-z0-9]/.test(senha)) pontos++;

    let largura = pontos * 20;
    barra.style.width = largura + "%";

    if (pontos <= 2) {
        barra.className = "progress-bar bg-danger";
        texto.innerText = "Senha fraca";
    } else if (pontos <= 4) {
        barra.className = "progress-bar bg-warning";
        texto.innerText = "Senha média";
    } else {
        barra.className = "progress-bar bg-success";
        texto.innerText = "Senha forte";
    }
});