document.addEventListener("DOMContentLoaded", function () {

    // ========= FUNÇÃO PADRÃO PARA CAMPOS COM DECIMAL =========
    function formatDecimal(input, maxIntegers) {
        let v = input.value.replace(/\D/g, ""); // Remove tudo que não é número
        
        if (v.length === 0) {
            input.value = "";
            return;
        }

        // garante que sempre existam ao menos 3 dígitos (ex: 00003 -> 0,03)
        v = v.padStart(maxIntegers + 2, "0");

        let integers = v.slice(0, -2);
        let decimals = v.slice(-2);

        // remove zeros à esquerda mas mantém pelo menos um zero
        integers = integers.replace(/^0+(?=\d)/, "");

        // formata com separador de milhar
        integers = integers.replace(/\B(?=(\d{3})+(?!\d))/g, ".");

        input.value = integers + "," + decimals;
    }

    // ========= FUNÇÃO PARA CAMPO DE VALOR (DINHEIRO) =========
    document.querySelectorAll(".money").forEach(function (input) {
        input.addEventListener("input", function () {
            formatDecimal(input, 9); // até bilhões se quiser
        });

        // formata caso já venha preenchido
        input.dispatchEvent(new Event("input"));
    });

    // ========= FUNÇÃO PARA COMISSÃO (%) E TEOR ALCOÓLICO (%) =========
    document.querySelectorAll(".percent").forEach(function (input) {
        input.addEventListener("input", function () {
            formatDecimal(input, 3); // até 999,99
        });

        input.dispatchEvent(new Event("input"));
    });

    // ========= FUNÇÃO PARA ESTOQUE (APENAS NÚMEROS INTEIROS) =========
    document.querySelectorAll(".integer").forEach(function (input) {
        input.addEventListener("input", function () {
            let v = input.value.replace(/\D/g, "");
            input.value = v;
        });
    });

});
