document.addEventListener("DOMContentLoaded", function () {

    const documento = document.getElementById("documento");
    const telefone = document.getElementById("telefone");

    if (!documento || !telefone) return;

    // ======================================================
    // FUNÇÃO DE FORMATAÇÃO TELEFONE
    // (separada para reutilizar no carregamento da edição)
    // ======================================================
    function formatarTelefone(valor) {

        valor = valor.replace(/\D/g, ""); // remove tudo que não é número

        if (valor.length > 11) {
            valor = valor.substring(0, 11); // limita a 11 dígitos
        }

        if (valor.length <= 10) {
            // TELEFONE FIXO (10 dígitos)
            valor = valor.replace(/^(\d{2})(\d)/g, "($1) $2");
            valor = valor.replace(/(\d{4})(\d)/, "$1-$2");
        } else {
            // CELULAR (11 dígitos)
            valor = valor.replace(/^(\d{2})(\d)/g, "($1) $2");
            valor = valor.replace(/(\d{5})(\d)/, "$1-$2");
        }

        return valor;
    }

    // ======================================================
    // EVENTO AO DIGITAR TELEFONE
    // (Corrigido: antes estava usando variável "input" inexistente)
    // ======================================================
    telefone.addEventListener("input", function () {
        telefone.value = formatarTelefone(telefone.value);
    });

    // ======================================================
    //  APLICA MÁSCARA AO CARREGAR (EDIÇÃO)
    // ======================================================
    if (telefone.value) {
        telefone.value = formatarTelefone(telefone.value);
    }

    // ================= MÁSCARA CPF =================
    function mascaraCPF(valor) {
        valor = valor.replace(/\D/g, '').slice(0, 11);
        return valor.replace(/(\d{3})(\d{3})(\d{3})(\d{0,2})/,
            "$1.$2.$3-$4");
    }

    // ================= MÁSCARA CNPJ =================
    function mascaraCNPJ(valor) {
        valor = valor.replace(/\D/g, '').slice(0, 14);
        return valor.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{0,2})/,
            "$1.$2.$3/$4-$5");
    }

    // ======================================================
    // FUNÇÃO PARA FORMATAR DOCUMENTO (CPF/CNPJ)
    // ======================================================
    function formatarDocumento(valor, tipo) {

        if (!tipo) return valor;

        if (tipo === "FISICA") {
            return mascaraCPF(valor);
        }

        if (tipo === "JURIDICA") {
            return mascaraCNPJ(valor);
        }

        return valor;
    }

    // ======================================================
    // EVENTO AO DIGITAR DOCUMENTO
    // ======================================================
    documento.addEventListener("input", function () {

        let tipoSelecionado =
            document.querySelector("input[name='tipoPessoa']:checked");

        if (!tipoSelecionado) return;

        documento.value = formatarDocumento(
            documento.value,
            tipoSelecionado.value
        );
    });

    // ======================================================
    //  APLICA MÁSCARA DOCUMENTO AO CARREGAR (EDIÇÃO)
    // ======================================================
    let tipoSelecionado =
        document.querySelector("input[name='tipoPessoa']:checked");

    if (documento.value && tipoSelecionado) {
        documento.value = formatarDocumento(
            documento.value,
            tipoSelecionado.value
        );
    }

});
