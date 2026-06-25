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
	
//remover depois
console.log("ENTROU selecionarCliente");
//fim remover teste


//remover depois
console.log("1");
//fim remover teste
    document.getElementById("clienteId").value = c.id;
	//remover depois
	console.log("2");
	//fim remover teste
    document.getElementById("clienteBusca").value = c.nomeCliente;

	//remover depois
	console.log("3");
	//fim remover teste
    document.getElementById("sugestoesClientes").innerHTML = "";
	//remover depois
	console.log("4");
	//fim remover teste
    document.getElementById("dadosCliente").style.display = "block";

    // FORMATANDO
	//remover depois
		console.log("5");
		//fim remover teste
    document.getElementById("clienteDocumento").value = formatarCPF_CNPJ(c.documento);
	//remover depois
		console.log("6");
		//fim remover teste
    document.getElementById("clienteEmail").value = c.email;
	//remover depois
		console.log("7");
		//fim remover teste
    document.getElementById("clienteTelefone").value = formatarTelefone(c.telefone);

    // ENDEREÇO
	//remover depois
		console.log("8");
		//fim remover teste
	let endereco = c.endereco || {};

	document.getElementById("clienteEndereco").value =
	    `${endereco.logradouro || ''}, ${endereco.numero || ''}, ${endereco.cidade.nomeCidade || ''},
		${endereco.cidade.estado.nome || ''}`;
		//remover depois
			console.log("9");
			//fim remover teste

	//document.getElementById("clienteCidade").value =
	  //  endereco.cidade.nomeCidade || '';

	//document.getElementById("clienteEstado").value =
	 //   endereco.cidade?.estado.nome || '';
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

/**
 * Monitora o carregamento da página para buscar e preencher os dados 
 * do cliente automaticamente quando uma venda estiver em modo de edição.
 */
document.addEventListener(
    "DOMContentLoaded",
    carregarClienteEdicao
);

function carregarClienteEdicao() {
	
	
	//remover depois
	console.log("ENTROU carregarClienteEdicao");
		//fim remover depos só para teste
	
    // Captura o ID do cliente inserido no input oculto/renderizado pelo Thymeleaf
    const clienteId =
        document.querySelector("[name='cliente.id']")?.value;

    // Se o campo estiver vazio (ex: criando uma nova venda), aborta a execução
    if (!clienteId) {
        return;
    }

    // Faz a requisição para o backend buscando os dados completos do cliente por ID
    fetch(`/clientes/${clienteId}`)
        .then(response => response.json())
        .then(cliente => {
            // Repassa o objeto JSON do cliente para a função que preenche a tela
            selecionarCliente(cliente);
        })
        .catch(error => {
            console.error(
                "Erro carregando cliente da venda",
                error
            );
        });
}

