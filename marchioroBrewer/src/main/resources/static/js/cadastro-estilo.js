// ======================================================================
// cadastro-estilo.js
// Controle do Cadastro Rápido de Estilo via modal
// Inclui: CSRF, loading spinner, limpeza do modal, efeitos visuais,
// debug seguro e fallback para botão.
// ======================================================================

(function () {

    // ---------------------------------------------------------------
    //  DEBUG SEGURO (evita erros se console estiver desativado)
    // ---------------------------------------------------------------
    function debug(...args) {
        if (window.console && console.log) console.log("[cadastro-estilo]", ...args);
    }

    // ---------------------------------------------------------------
    //  LÊ O TOKEN CSRF DO SPRING SECURITY (se existir)
    // ---------------------------------------------------------------
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

    // ======================================================================
    //  FUNÇÃO PRINCIPAL
    // ======================================================================
    function init() {
        debug("init() chamado");

        // Seletores principais
        const btnSalvar = document.querySelector("#btnSalvarEstilo");
        const inputNome = document.querySelector("#nomeEstilo");
        const selectEstilo = document.querySelector("#estilo");
        const modalElement = document.getElementById("modalCadastroRapidoEstilo");
        const feedback = document.querySelector("#nomeEstilo + .invalid-feedback");

        debug("elementos encontrados:", { btnSalvar, inputNome, selectEstilo });

        // ---------------------------------------------------------------
        // FALLBACK — SE O BOTÃO NÃO TIVER ID EXATO
        // ---------------------------------------------------------------
        if (!btnSalvar) {
            const alt = Array.from(document.querySelectorAll("button"))
                .find(b => b.textContent.trim().toLowerCase() === "salvar");

            if (alt) {
                debug("Botão salvar encontrado por texto");
                alt.id = "btnSalvarEstilo";
            } else {
                debug("Nenhum botão 'Salvar' encontrado.");
            }
        }

        const btn = document.querySelector("#btnSalvarEstilo");
        if (!btn) {
            debug("Abortando init: não há botão salvar.");
            return;
        }

        // Remove listeners antigos e adiciona o novo
        btn.removeEventListener("click", onClickSalvar);
        btn.addEventListener("click", onClickSalvar);

        // ======================================================================
        //  LIMPAR MODAL AO FECHAR (INCLUÍDO POR VOCÊ PEDIU)
        // ======================================================================
        if (modalElement) {
            modalElement.addEventListener("hidden.bs.modal", () => {

                debug("Modal fechado — limpando campos");

                if (inputNome) {
                    inputNome.value = "";
                    inputNome.classList.remove("is-invalid");
                }

                if (feedback) feedback.textContent = "";
            });
        }

        // ======================================================================
        //  EVENTO DO BOTÃO SALVAR
        // ======================================================================
        function onClickSalvar(e) {
            e.preventDefault();
            debug("Clique no salvar");

            const nome = inputNome.value.trim();

            // ---------------------------------------------------------------
            //  VALIDAÇÃO BÁSICA
            // ---------------------------------------------------------------
            if (!nome) {
                inputNome.classList.add("is-invalid");
                if (feedback) feedback.textContent = "Informe um nome válido.";
                return;
            }

            inputNome.classList.remove("is-invalid");
            if (feedback) feedback.textContent = "";

            const estilo = { nome };

            // ---------------------------------------------------------------
            //  PREPARA SPINNER DE LOADING (ADICIONADO)
            // ---------------------------------------------------------------
            btn.disabled = true;
            btn.dataset.originalText = btn.innerHTML;
            btn.innerHTML = `
                <span class="spinner-border spinner-border-sm" role="status"></span>
                Salvando...
            `;

            // ---------------------------------------------------------------
            //  PREPARA HEADERS (CSRF + JSON)
            // ---------------------------------------------------------------
            const csrf = getCsrf();
            const headers = { "Content-Type": "application/json" };
            if (csrf) headers[csrf.header] = csrf.token;

            debug("POST /estilos/novo", estilo, headers);

            // ==================================================================
            //  FETCH → envia estilo para o backend
            // ==================================================================
            fetch("/estilos/novo", {
                method: "POST",
                headers,
                body: JSON.stringify(estilo)
            })
                .then(async response => {

                    debug("Resposta do servidor:", response.status);

                    if (!response.ok) {

                        // Se backend retornar texto (ex: "Estilo já existe")
                        const msg = await response.text();

                        inputNome.classList.add("is-invalid");
                        if (feedback) feedback.textContent = msg || "Erro ao salvar estilo.";

                        inputNome.focus();
                        return null;
                    }

                    return response.json();
                })
                .then(salvo => {

                    if (!salvo) return;

                    debug("Estilo salvo:", salvo);

                    // ==================================================================
                    //  EFEITO VISUAL AO ADICIONAR (ADICIONADO)
                    // ==================================================================
					const option = document.createElement("option");
					option.value = salvo.id;
					option.textContent = salvo.nome;

					selectEstilo.appendChild(option);

					// seleciona corretamente
					selectEstilo.value = salvo.id;

					// força o Spring/Thymeleaf a reconhecer
					selectEstilo.dispatchEvent(new Event("change", { bubbles: true }));

                    // remove classe após animação
                    setTimeout(() => option.classList.remove("option-flash"), 1200);

                    // ==================================================================
                    //  FECHA MODAL COM BOOTSTRAP
                    // ==================================================================
					const modalInstance = bootstrap.Modal.getOrCreateInstance(modalElement);
					modalInstance.hide();

					//  GARANTIA TOTAL: remove backdrop e estado travado
					setTimeout(() => {
					    document.body.classList.remove("modal-open");

					    document.querySelectorAll(".modal-backdrop").forEach(b => b.remove());
					}, 300);

                    // ==================================================================
                    //  RESTAURA BOTÃO (ADICIONADO)
                    // ==================================================================
                    btn.disabled = false;
                    btn.innerHTML = btn.dataset.originalText;
                });
        }
    }

    // ======================================================================
    //  INICIALIZADOR
    // ======================================================================
    if (document.readyState === "loading") {
        debug("Aguardando DOM...");
        document.addEventListener("DOMContentLoaded", init);
    } else {
        debug("DOM pronto — iniciando");
        init();
    }

})();
