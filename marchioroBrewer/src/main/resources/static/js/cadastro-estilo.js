// cadastro-estilo.js
(function () {
    // Helper de debug (evita erros se console undefined)
    function debug(...args) {
        if (window.console && console.log) console.log("[cadastro-estilo]", ...args);
    }

    // Tenta ler token CSRF se houver (Spring Security padrão)
    function getCsrf() {
        const metaName = document.querySelector('meta[name="_csrf"]');
        const metaHeader = document.querySelector('meta[name="_csrf_header"]');
        if (metaName) {
            return {
                header: metaHeader ? metaHeader.getAttribute('content') : 'X-CSRF-TOKEN',
                token: metaName.getAttribute('content')
            };
        }
        return null;
    }

    // Função principal
    function init() {
        debug("init() chamado");
        const btnSalvar = document.querySelector("#btnSalvarEstilo");
        const inputNome = document.querySelector("#nomeEstilo");
        const selectEstilo = document.querySelector("#estilo");

        debug("elementos encontrados:", { btnSalvar, inputNome, selectEstilo });

        if (!btnSalvar) {
            // fallback: se botão tiver outro id, tenta encontrar por texto
            const alt = Array.from(document.querySelectorAll("button")).find(b => b.textContent.trim().toLowerCase() === 'salvar');
            if (alt) {
                debug("Botão 'Salvar' encontrado por texto, atribuindo id temporário e listener");
                alt.id = "btnSalvarEstilo";
            } else {
                debug("Botão salvar não encontrado; listener não anexado");
            }
        }

        const btn = document.querySelector("#btnSalvarEstilo");
        if (!btn) {
            debug("Nenhum botão disponível mesmo após fallback. Parando inicialização.");
            return;
        }

        // Adiciona listener (remove duplicados)
        btn.removeEventListener("click", onClickSalvar);
        btn.addEventListener("click", onClickSalvar);

        function onClickSalvar(evt) {
            evt.preventDefault();
            debug("click recebido em btnSalvarEstilo");

            const nome = (inputNome && inputNome.value) ? inputNome.value.trim() : "";
            if (!nome) {
                if (inputNome) inputNome.classList.add("is-invalid");
                debug("nome vazio -> abortando");
                return;
            }
            if (inputNome) inputNome.classList.remove("is-invalid");

            const estilo = { nome };

            const csrf = getCsrf();
            const headers = { "Content-Type": "application/json" };
            if (csrf && csrf.token) headers[csrf.header] = csrf.token;

            debug("Enviando POST /estilos/novo", estilo, headers);

            fetch("/estilos/novo", {
                method: "POST",
                headers,
                body: JSON.stringify(estilo)
            })
            .then(async (response) => {
                debug("Resposta fetch:", response.status, response.statusText);
                if (!response.ok) {
                    const text = await response.text().catch(()=>null);
                    debug("Resposta não OK, corpo:", text);
                    alert("Erro ao salvar estilo. Verifique o servidor (console).");
                    return;
                }
                return response.json();
            })
            .then((salvo) => {
                if (!salvo) return;
                debug("Estilo salvo retornado:", salvo);

                // adiciona no select, seleciona e dispara change
                if (selectEstilo) {
                    const option = document.createElement("option");
                    option.value = salvo.id;
                    option.textContent = salvo.nome;
                    option.selected = true;
                    selectEstilo.appendChild(option);
                    selectEstilo.dispatchEvent(new Event('change', { bubbles: true }));
                }

                // fecha modal com Bootstrap (se disponível)
                try {
                    const modalElement = document.getElementById("modalCadastroRapidoEstilo");
                    if (modalElement) {
                        const modal = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
                        modal.hide();
                    }
                } catch (e) {
                    debug("Bootstrap Modal não disponível ou erro ao fechar modal:", e);
                }

                if (inputNome) inputNome.value = "";
            })
            .catch((err) => {
                debug("Erro fetch catch:", err);
                alert("Erro inesperado. Veja console do navegador.");
            });
        }
    }

    // Se DOM já estiver pronto, inicia imediatamente; senão, anexa listener
    if (document.readyState === "loading") {
        debug("document.loading -> adicionando DOMContentLoaded");
        document.addEventListener("DOMContentLoaded", init);
    } else {
        debug("document já pronto -> init imediato");
        init();
    }

})();
