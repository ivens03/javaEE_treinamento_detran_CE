<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <title>Painel de Gerenciamento Unificado</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; color: #333; }
        .container { max-width: 1200px; margin: auto; }
        .form-section, .list-section { border: 1px solid #ccc; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
        h2 { border-bottom: 2px solid #0056b3; padding-bottom: 10px; color: #0056b3; }
        .form-group { margin-bottom: 15px; }
        .form-row { display: flex; gap: 20px; }
        .form-row .form-group { flex: 1; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], select { width: 98%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
        button { padding: 10px 20px; color: white; border: none; cursor: pointer; border-radius: 4px; }
        .btn-salvar { background-color: #28a745; }
        .btn-atualizar { background-color: #007bff; }
        .btn-deletar { background-color: #dc3545; padding: 5px 10px; }
        .btn-editar { background-color: #ffc107; color: black; padding: 5px 10px; text-decoration: none; display: inline-block; }
        .btn-limpar { background-color: #6c757d; }
        .message-box { margin-bottom: 20px; padding-top: 10px; display: none; } /* Começa escondido */
        .erro { color: #d9534f; background-color: #f2dede; padding: 15px; border-radius: 4px; }
        .sucesso { color: #28a745; background-color: #d4edda; padding: 15px; border-radius: 4px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 8px; border: 1px solid #ddd; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>

<div class="container">
    <h1>Painel de Gerenciamento Unificado</h1>

    <%-- Seção unificada para exibir mensagens, controlada pelo JavaScript --%>
    <div id="message-box" class="message-box">
        <p id="message-text"></p>
    </div>

    <div class="form-section">
        <h2 id="form-title">Cadastrar Novo Proprietário e Veículo</h2>
        <form id="management-form">
            <input type="hidden" id="proprietarioId" name="proprietarioId">
            <input type="hidden" id="veiculoId" name="veiculoId">

            <div class="form-row">
                <div class="form-group">
                    <label for="cpf_cnpj">CPF/CNPJ do Proprietário:</label>
                    <input type="text" id="cpf_cnpj" name="cpf_cnpj" required>
                </div>
                <div class="form-group">
                    <label for="nome">Nome Completo do Proprietário:</label>
                    <input type="text" id="nome" name="nome" required>
                </div>
            </div>
            <div class="form-group">
                <label for="endereco">Endereço do Proprietário:</label>
                <input type="text" id="endereco" name="endereco" required>
            </div>
            <hr>
            <div class="form-row">
                <div class="form-group">
                    <label for="placa">Placa do Veículo:</label>
                    <input type="text" id="placa" name="placa" required>
                </div>
                <div class="form-group">
                    <label for="renavam">Renavam do Veículo:</label>
                    <input type="text" id="renavam" name="renavam" required>
                </div>
            </div>

            <button type="button" id="save-button" class="btn-salvar">Salvar Novo</button>
            <button type="button" id="clear-button" class="btn-limpar">Limpar Formulário</button>
        </form>
    </div>


    <div class="list-section">
        <h2>Registros Cadastrados</h2>
        <table>
            <thead>
            <tr>
                <th>ID Veículo</th>
                <th>Placa</th>
                <th>Renavam</th>
                <th>ID Proprietário</th>
                <th>Nome Proprietário</th>
                <th>CPF/CNPJ</th>
                <th>Endereço</th>
                <th>Ações</th>
            </tr>
            </thead>
            <tbody id="vehicle-table-body">
            <%-- O conteúdo desta tabela será gerado pelo JavaScript --%>
            <%-- Exemplo inicial carregado pelo JSTL --%>
            <c:forEach var="veic" items="${listaVeiculos}">
                <tr data-vehicle-id="${veic.id}">
                    <td>${veic.id}</td>
                    <td>${veic.placa}</td>
                    <td>${veic.renavam}</td>
                    <td>${veic.proprietario.id}</td>
                    <td>${veic.proprietario.nome}</td>
                    <td>${veic.proprietario.cpf_cnpj}</td>
                    <td>${veic.proprietario.endereco}</td>
                    <td>
                        <button class="btn-editar" onclick="populateForm(${veic.id})">Editar</button>
                        <button class="btn-deletar" onclick="deleteVehicle(${veic.id})">Excluir</button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script>
    // URL base da sua nova API (Servlet de Gerenciamento)
    const API_URL = '${pageContext.request.contextPath}/management';
    const form = document.getElementById('management-form');
    const saveButton = document.getElementById('save-button');
    const clearButton = document.getElementById('clear-button');
    const formTitle = document.getElementById('form-title');

    // Mapeamento dos campos do formulário para fácil acesso
    const fields = {
        proprietarioId: document.getElementById('proprietarioId'),
        veiculoId: document.getElementById('veiculoId'),
        cpf_cnpj: document.getElementById('cpf_cnpj'),
        nome: document.getElementById('nome'),
        endereco: document.getElementById('endereco'),
        placa: document.getElementById('placa'),
        renavam: document.getElementById('renavam')
    };

    // Event Listeners
    saveButton.addEventListener('click', saveChanges);
    clearButton.addEventListener('click', clearForm);

    /**
     * Limpa o formulário e o redefine para o modo de criação.
     */
    function clearForm() {
        form.reset();
        fields.proprietarioId.value = '';
        fields.veiculoId.value = '';
        formTitle.textContent = 'Cadastrar Novo Proprietário e Veículo';
        saveButton.textContent = 'Salvar Novo';
        saveButton.className = 'btn-salvar';
        hideMessage();
    }

    /**
     * Preenche o formulário com dados de um veículo existente para edição.
     * Esta função busca os dados completos do servidor para garantir que estão atualizados.
     */
    async function populateForm(vehicleId) {
        hideMessage();
        try {
            const response = await fetch(`${API_URL}?veiculoId=${vehicleId}`);
            if (!response.ok) throw new Error('Falha ao buscar dados para edição.');

            const veiculo = await response.json();

            fields.proprietarioId.value = veiculo.proprietario.id;
            fields.veiculoId.value = veiculo.id;
            fields.cpf_cnpj.value = veiculo.proprietario.cpf_cnpj;
            fields.nome.value = veiculo.proprietario.nome;
            fields.endereco.value = veiculo.proprietario.endereco;
            fields.placa.value = veiculo.placa;
            fields.renavam.value = veiculo.renavam;

            formTitle.textContent = `Editando Veículo ID: ${veiculo.id}`;
            saveButton.textContent = 'Atualizar';
            saveButton.className = 'btn-atualizar';
            window.scrollTo(0, 0); // Rola a página para o topo
        } catch (error) {
            showMessage(error.message, true);
        }
    }

    /**
     * Coleta os dados do formulário e os envia para o servidor.
     * Decide se é uma operação de criação (POST) ou atualização (PUT).
     */
    async function saveChanges() {
        const isUpdate = !!fields.veiculoId.value;
        const url = isUpdate ? `${API_URL}?veiculoId=${fields.veiculoId.value}` : API_URL;
        const method = isUpdate ? 'PUT' : 'POST';

        const formData = {
            proprietarioId: fields.proprietarioId.value,
            veiculoId: fields.veiculoId.value,
            cpf_cnpj: fields.cpf_cnpj.value,
            nome: fields.nome.value,
            endereco: fields.endereco.value,
            placa: fields.placa.value,
            renavam: fields.renavam.value,
        };

        try {
            const response = await fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData)
            });

            const result = await response.json();

            if (!response.ok) {
                throw new Error(result.message || 'Ocorreu um erro no servidor.');
            }

            showMessage(result.message, false);
            clearForm();
            await refreshTable();

        } catch (error) {
            showMessage(error.message, true);
        }
    }

    /**
     * Envia uma requisição para deletar um veículo.
     */
    async function deleteVehicle(vehicleId) {
        if (!confirm(`Tem certeza que deseja excluir o veículo de ID ${vehicleId}?`)) return;

        try {
            const response = await fetch(`${API_URL}?veiculoId=${vehicleId}`, {
                method: 'DELETE'
            });

            const result = await response.json();

            if (!response.ok) {
                throw new Error(result.message || 'Ocorreu um erro ao deletar.');
            }

            showMessage(result.message, false);
            await refreshTable();

        } catch (error) {
            showMessage(error.message, true);
        }
    }

    /**
     * Busca a lista atualizada de veículos e redesenha a tabela.
     */
    async function refreshTable() {
        try {
            const response = await fetch(API_URL);
            if (!response.ok) throw new Error('Não foi possível recarregar a lista.');

            const veiculos = await response.json();
            const tableBody = document.getElementById('vehicle-table-body');
            tableBody.innerHTML = ''; // Limpa a tabela

            if (veiculos.length === 0) {
                tableBody.innerHTML = '<tr><td colspan="8">Nenhum registro cadastrado.</td></tr>';
            } else {
                veiculos.forEach(veic => {
                    const row = `
                        <tr data-vehicle-id="${veic.id}">
                            <td>${veic.id}</td>
                            <td>${veic.placa}</td>
                            <td>${veic.renavam}</td>
                            <td>${veic.proprietario.id}</td>
                            <td>${veic.proprietario.nome}</td>
                            <td>${veic.proprietario.cpf_cnpj}</td>
                            <td>${veic.proprietario.endereco}</td>
                            <td>
                                <button class="btn-editar" onclick="populateForm(${veic.id})">Editar</button>
                                <button class="btn-deletar" onclick="deleteVehicle(${veic.id})">Excluir</button>
                            </td>
                        </tr>
                    `;
                    tableBody.innerHTML += row;
                });
            }
        } catch(error) {
            showMessage(error.message, true);
        }
    }

    /**
     * Exibe uma mensagem de sucesso ou erro para o usuário.
     */
    function showMessage(message, isError) {
        const messageBox = document.getElementById('message-box');
        const messageText = document.getElementById('message-text');

        messageText.textContent = message;
        messageBox.className = isError ? 'message-box erro' : 'message-box sucesso';
        messageBox.style.display = 'block';
    }

    /**
     * Esconde a caixa de mensagem.
     */
    function hideMessage() {
        document.getElementById('message-box').style.display = 'none';
    }

</script>

</body>
</html>