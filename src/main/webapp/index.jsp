<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <title>Painel de Gerenciamento Unificado</title>
    <style>
        /* Estilos CSS (manter os originais para consistência visual) */
        body { font-family: Arial, sans-serif; margin: 20px; color: #333; }
        .container { max-width: 1200px; margin: auto; }
        .form-section, .list-section { border: 1px solid #ccc; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
        h2 { border-bottom: 2px solid #0056b3; padding-bottom: 10px; color: #0056b3; }
        .form-group { margin-bottom: 15px; }
        .form-row { display: flex; gap: 20px; }
        .form-row .form-group { flex: 1; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], select { width: 98%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
        button, input[type="submit"] { padding: 10px 20px; color: white; border: none; cursor: pointer; border-radius: 4px; }
        .btn-salvar { background-color: #28a745; }
        .btn-atualizar { background-color: #007bff; }
        .btn-deletar { background-color: #dc3545; padding: 5px 10px; }
        .btn-editar { background-color: #ffc107; color: black; padding: 5px 10px; text-decoration: none; display: inline-block; }
        .btn-limpar { background-color: #6c757d; }
        .message-box { margin-bottom: 20px; padding-top: 10px; }
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

    <%-- Seção para exibir mensagens de sucesso/erro (passadas pelo Servlet via Session) --%>
    <div class="message-box">
        <%-- Exibe mensagem de sucesso se existir --%>
        <c:if test="${not empty sessionScope.mensagem}">
            <p class="sucesso">${sessionScope.mensagem}</p>
            <%-- Limpa a mensagem após exibir --%>
            <c:remove var="mensagem" scope="session"/>
        </c:if>

        <%-- Exibe mensagem de erro se existir --%>
        <c:if test="${not empty sessionScope.mensagemErro}">
            <p class="erro">${sessionScope.mensagemErro}</p>
            <%-- Limpa a mensagem após exibir --%>
            <c:remove var="mensagemErro" scope="session"/>
        </c:if>
    </div>

    <div class="form-section">
        <h2 id="form-title">Cadastrar Novo Proprietário e Veículo</h2>

        <%-- O FORM agora submete diretamente para o Servlet, usando POST --%>
        <form id="management-form" action="${pageContext.request.contextPath}/management" method="POST">

            <%-- Parâmetros Ocultos para Ação e IDs --%>
            <input type="hidden" id="action" name="action" value="createOrUpdate">
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

            <%-- Usa input type="submit" para submissão nativa --%>
            <input type="submit" id="save-button" class="btn-salvar" value="Salvar Novo">
            <button type="button" id="clear-button" class="btn-limpar" onclick="clearForm()">Limpar Formulário</button>
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

            <%-- A lista é carregada diretamente pelo JSTL no doGet do Servlet --%>
            <c:forEach var="veic" items="${listaVeiculos}">
                <tr data-vehicle-id="${veic.id}">
                    <td class="veiculo-id">${veic.id}</td>
                    <td class="veiculo-placa">${veic.placa}</td>
                    <td class="veiculo-renavam">${veic.renavam}</td>
                    <td class="proprietario-id">${veic.proprietario.id}</td>
                    <td class="proprietario-nome">${veic.proprietario.nome}</td>
                    <td class="proprietario-cpfcnpj">${veic.proprietario.cpf_cnpj}</td>
                    <td class="proprietario-endereco">${veic.proprietario.endereco}</td>
                    <td>
                            <%-- Botão Editar: usa JavaScript simples para COPIAR dados para o formulário --%>
                        <button class="btn-editar" type="button" onclick="populateForm(this)">Editar</button>

                            <%-- Botão Excluir: usa um mini-formulário para enviar a ação DELETE via POST --%>
                        <form action="${pageContext.request.contextPath}/management" method="POST" style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="veiculoId" value="${veic.id}">
                            <input type="submit" class="btn-deletar" value="Excluir" onclick="return confirm('Tem certeza que deseja excluir o veículo ${veic.id}?');">
                        </form>
                    </td>
                </tr>
            </c:forEach>

            <c:if test="${empty listaVeiculos}">
                <tr><td colspan="8">Nenhum registro cadastrado.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<script>
    const form = document.getElementById('management-form');
    const saveButton = document.getElementById('save-button');
    const formTitle = document.getElementById('form-title');

    // Mapeamento dos campos do formulário para fácil acesso
    const fields = {
        action: document.getElementById('action'),
        proprietarioId: document.getElementById('proprietarioId'),
        veiculoId: document.getElementById('veiculoId'),
        cpf_cnpj: document.getElementById('cpf_cnpj'),
        nome: document.getElementById('nome'),
        endereco: document.getElementById('endereco'),
        placa: document.getElementById('placa'),
        renavam: document.getElementById('renavam')
    };

    /**
     * Limpa o formulário e o redefine para o modo de criação.
     */
    function clearForm() {
        form.reset();
        fields.proprietarioId.value = '';
        fields.veiculoId.value = '';
        fields.action.value = 'createOrUpdate'; // Volta para a ação principal
        formTitle.textContent = 'Cadastrar Novo Proprietário e Veículo';
        saveButton.value = 'Salvar Novo';
        saveButton.className = 'btn-salvar';
    }

    /**
     * Copia os dados da linha da tabela (tradicional) para o formulário.
     */
    function populateForm(buttonElement) {
        // Encontra o <tr> pai
        const row = buttonElement.closest('tr');

        // Copia os valores das células (tds)
        fields.proprietarioId.value = row.querySelector('.proprietario-id').textContent;
        fields.veiculoId.value = row.querySelector('.veiculo-id').textContent;
        fields.cpf_cnpj.value = row.querySelector('.proprietario-cpfcnpj').textContent;
        fields.nome.value = row.querySelector('.proprietario-nome').textContent;
        fields.endereco.value = row.querySelector('.proprietario-endereco').textContent;
        fields.placa.value = row.querySelector('.veiculo-placa').textContent;
        fields.renavam.value = row.querySelector('.veiculo-renavam').textContent;

        // Altera o estado do formulário para edição
        fields.action.value = 'createOrUpdate'; // A ação já é tratada no Servlet
        formTitle.textContent = `Editando Veículo ID: ${fields.veiculoId.value}`;
        saveButton.value = 'Atualizar';
        saveButton.className = 'btn-atualizar';

        window.scrollTo(0, 0); // Rola a página para o topo
    }

    // Inicia o formulário em modo limpo ao carregar
    window.onload = function() {
        clearForm();
    };
</script>

</body>
</html>