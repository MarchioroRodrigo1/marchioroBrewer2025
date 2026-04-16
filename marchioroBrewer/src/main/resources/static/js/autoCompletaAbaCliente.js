document.getElementById("clienteBusca").addEventListener("keyup", function () {

    let nome = this.value;

    if (nome.length < 2) return;

    fetch("/clientes/buscar?nome=" + nome)
        .then(response => response.json())
        .then(clientes => {

            let lista = document.getElementById("sugestoesClientes");
            lista.innerHTML = "";

            clientes.forEach(cliente => {

                let item = document.createElement("a");
                item.classList.add("list-group-item", "list-group-item-action");
                item.textContent = cliente.nomeCliente;              
				item.onclick = () => selecionarCliente(cliente);
                lista.appendChild(item);
            });

        });
});

function selecionarCliente(c) {

    document.getElementById("clienteId").value = c.id;
    document.getElementById("clienteBusca").value = c.nomeCliente;

    document.getElementById("sugestoesClientes").innerHTML = "";
    document.getElementById("dadosCliente").style.display = "block";

    // FORMATANDO
    document.getElementById("clienteDocumento").value = formatarCPF_CNPJ(c.documento);
    document.getElementById("clienteEmail").value = c.email;
    document.getElementById("clienteTelefone").value = formatarTelefone(c.telefone);

    // ENDEREÇO
	let endereco = c.endereco || {};

	document.getElementById("clienteEndereco").value =
	    `${endereco.logradouro || ''}, ${endereco.numero || ''}, ${endereco.cidade.nomeCidade || ''},
		${endereco.cidade.estado.nome || ''}`;

	document.getElementById("clienteCidade").value =
	    endereco.cidade.nomeCidade || '';

	document.getElementById("clienteEstado").value =
	    endereco.cidade?.estado.nome || '';
}
function formatarCPF_CNPJ(valor) {
    if (!valor) return '';

    valor = valor.replace(/\D/g, '');

    if (valor.length === 11) {
        return valor.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/,
            '$1.$2.$3-$4');
    }

    if (valor.length === 14) {
        return valor.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/,
            '$1.$2.$3/$4-$5');
    }

    return valor;
}

function formatarTelefone(valor) {
    if (!valor) return '';

    valor = valor.replace(/\D/g, '');

    return valor.replace(/(\d{2})(\d{5})(\d{4})/,
        '($1) $2-$3');
}


