document.getElementById('estado').addEventListener('change', function () {
    const estadoId = this.value;
    const cidadeSelect = document.getElementById('cidade');

    cidadeSelect.innerHTML = '<option value="">Selecione</option>';

    if (!estadoId) return;

    fetch(`/cidades/por-estado/${estadoId}`)
        .then(r => r.json())
        .then(cidades => {
            cidades.forEach(c => {
                const opt = document.createElement('option');
                opt.value = c.id;
                opt.text = c.nomeCidade;
                cidadeSelect.appendChild(opt);
            });
        });
});
