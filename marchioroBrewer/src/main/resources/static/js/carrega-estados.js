document.getElementById('regiao').addEventListener('change', function () {
    const regiaoId = this.value;
    const estadoSelect = document.getElementById('estado');
    const cidadeSelect = document.getElementById('cidade');

    estadoSelect.innerHTML = '<option value="">Selecione</option>';
    cidadeSelect.innerHTML = '<option value="">Selecione</option>';

    if (!regiaoId) return;

    fetch(`/estados/por-regiao/${regiaoId}`)
        .then(r => r.json())
        .then(estados => {
            estados.forEach(e => {
                const opt = document.createElement('option');
                opt.value = e.id;
                opt.text = e.nome;
                estadoSelect.appendChild(opt);
            });
        });
});
