let pagina = 0;
let carregando = false;
let fim = false;

let itens = [];

// ============================
// INIT
// ============================
document.addEventListener("DOMContentLoaded", () => {

    carregarCervejas();

    // SCROLL
    document.getElementById("catalogo-container")
        ?.addEventListener("scroll", function () {

            if (this.scrollTop + this.clientHeight >= this.scrollHeight - 50) {
                carregarCervejas(document.getElementById("buscaProduto")?.value || '');
            }
        });

    // BUSCA
    document.getElementById("buscaProduto")
        ?.addEventListener("keyup", function () {

            pagina = 0;
            fim = false;

            document.getElementById("catalogo").innerHTML = "";

            carregarCervejas(this.value);
        });

    // TOTAL
    document.querySelector('[name="valorFrete"]')
        ?.addEventListener("input", calcularTotal);

    document.querySelector('[name="valorDesconto"]')
        ?.addEventListener("input", calcularTotal);

    //  SUBMIT CORRETO
    const form = document.getElementById("formVenda");

    if (form) {
        form.addEventListener("submit", function (event) {

            const input = document.getElementById("itensJson");

            if (!input) {
                console.error("Input itensJson NÃO encontrado!");
                event.preventDefault();
                return;
            }

            if (itens.length === 0) {
                event.preventDefault();
                alert("Adicione pelo menos um item!");
                return;
            }

            input.value = JSON.stringify(itens);

            console.log("JSON enviado:", input.value);
        });
    }
});

// ============================
// CATÁLOGO
// ============================
function carregarCervejas(nome = '') {

    if (carregando || fim) return;

    carregando = true;

    const loading = document.getElementById("loading");
    if (loading) loading.style.display = "block";

    fetch(`/cervejas/buscar?nome=${nome}&page=${pagina}&size=6`)
        .then(res => res.json())
        .then(data => {

            const catalogo = document.getElementById("catalogo");
            if (!catalogo) return;

            data.content.forEach(cerveja => {

                const card = document.createElement("div");
                card.classList.add("col-md-4", "mb-3");

                card.innerHTML = `
                    <div class="card h-100">

                        <img 
                            src="${cerveja.urlImagem 
                                ? '/cervejas/imagem/' + cerveja.urlImagem 
                                : '/images/sem-imagem.png'}"
                            class="card-img-top img-click"
                            style="height:120px; object-fit:contain; cursor:pointer;"
                        >

                        <div class="card-body">

                            <h6 class="mb-1 nome-click" style="cursor:pointer">
                                ${cerveja.nome}
                            </h6>

                            <strong class="text-success">
                                R$ ${parseFloat(cerveja.valor || 0).toFixed(2)}
                            </strong>

                            <div>Estoque: ${cerveja.quantidadeEstoque || 0}</div>

                            <button type="button"
                                class="btn btn-primary w-100 mt-2 btn-add">
                                Adicionar
                            </button>

                        </div>
                    </div>
                `;

                card.querySelector(".btn-add")
                    .addEventListener("click", () => adicionarItem(cerveja));

                card.querySelector(".img-click")
                    .addEventListener("click", () => abrirModal(cerveja));

                card.querySelector(".nome-click")
                    .addEventListener("click", () => abrirModal(cerveja));

                catalogo.appendChild(card);
            });

            fim = data.last;
            pagina++;

        })
        .catch(err => console.error("Erro ao carregar cervejas:", err))
        .finally(() => {
            carregando = false;
            if (loading) loading.style.display = "none";
        });
}

// ============================
// ADICIONAR ITEM
// ============================
function adicionarItem(cerveja) {

    const existente = itens.find(i => i.id === cerveja.id);
    const estoque = cerveja.quantidadeEstoque || 0;

    if (existente) {
        if (existente.quantidade >= estoque) {
            alert("Estoque máximo atingido!");
            return;
        }
        existente.quantidade++;
    } else {
        if (estoque <= 0) {
            alert("Produto sem estoque!");
            return;
        }

        itens.push({
            id: cerveja.id,
            nome: cerveja.nome,
            valor: parseFloat(cerveja.valor || 0),
            quantidade: 1,
            estoque: estoque
        });
    }

    atualizarTabela();
}

// ============================
// TABELA
// ============================
function atualizarTabela() {

    const tbody = document.getElementById("tabela-itens");
    if (!tbody) return;

    tbody.innerHTML = "";

    itens.forEach((item, index) => {

        const total = item.quantidade * item.valor;

        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${item.nome}</td>
            <td>
                <button type="button" class="btn btn-sm btn-secondary btn-diminuir">-</button>
                ${item.quantidade}
                <button type="button" class="btn btn-sm btn-secondary btn-aumentar">+</button>
            </td>
            <td>R$ ${item.valor.toFixed(2)}</td>
            <td>R$ ${total.toFixed(2)}</td>
            <td>
                <button type="button" class="btn btn-sm btn-danger btn-remover">X</button>
            </td>
        `;

        tr.querySelector(".btn-aumentar")
            .addEventListener("click", () => aumentar(index));

        tr.querySelector(".btn-diminuir")
            .addEventListener("click", () => diminuir(index));

        tr.querySelector(".btn-remover")
            .addEventListener("click", () => remover(index));

        tbody.appendChild(tr);
    });

    calcularTotal();
}

// ============================
// CONTROLES
// ============================
function aumentar(i) {
    if (itens[i].quantidade >= itens[i].estoque) {
        alert("Limite de estoque atingido!");
        return;
    }
    itens[i].quantidade++;
    atualizarTabela();
}

function diminuir(i) {
    itens[i].quantidade--;
    if (itens[i].quantidade <= 0) {
        itens.splice(i, 1);
    }
    atualizarTabela();
}

function remover(i) {
    itens.splice(i, 1);
    atualizarTabela();
}

// ============================
// TOTAL
// ============================
function calcularTotal() {

    const totalItens = itens.reduce((acc, item) => acc + (item.quantidade * item.valor), 0);

    const frete = parseFloat(document.querySelector('[name="valorFrete"]')?.value) || 0;
    const desconto = parseFloat(document.querySelector('[name="valorDesconto"]')?.value) || 0;

    const total = totalItens + frete - desconto;

    const totalInput = document.querySelector('[name="valorTotal"]');
    if (totalInput) totalInput.value = total.toFixed(2);
}

// ============================
// MODAL
// ============================
function abrirModal(cerveja) {

    const modalElement = document.getElementById('modalCerveja');
    if (!modalElement) return;

    document.getElementById("modalTitulo").innerText = cerveja.nome;

    document.getElementById("modalImagem").src =
        cerveja.urlImagem
            ? '/cervejas/imagem/' + cerveja.urlImagem
            : '/images/sem-imagem.png';

    document.getElementById("modalDescricao").innerText = cerveja.descricao || 'Sem descrição';
    document.getElementById("modalValor").innerText = cerveja.valor;
    document.getElementById("modalTeor").innerText = cerveja.teorAlcoolico;
    document.getElementById("modalEstoque").innerText = cerveja.quantidadeEstoque;

    bootstrap.Modal.getOrCreateInstance(modalElement).show();
}