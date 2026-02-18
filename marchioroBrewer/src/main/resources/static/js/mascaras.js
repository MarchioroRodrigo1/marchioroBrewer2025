    document.addEventListener("DOMContentLoaded", function () {

        // Máscara CPF/CNPJ
        document.querySelectorAll(".documento").forEach(function (el) {
            let value = el.innerText.replace(/\D/g, '');

            if (value.length === 11) {
                el.innerText = value.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/,
                    "$1.$2.$3-$4");
            }

            if (value.length === 14) {
                el.innerText = value.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/,
                    "$1.$2.$3/$4-$5");
            }
        });

        // Máscara Telefone
        document.querySelectorAll(".telefone").forEach(function (el) {
            let value = el.innerText.replace(/\D/g, '');

            if (value.length === 11) {
                el.innerText = value.replace(/(\d{2})(\d{5})(\d{4})/,
                    "($1) $2-$3");
            }
			
			if (value.length === 10) {
			                el.innerText = value.replace(/(\d{2})(\d{4})(\d{4})/,
			                    "($1) $2-$3");
			            }
			
        });

    });
