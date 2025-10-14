<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <title>Gerenciar Proprietários</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; color: #333; }
        .container { max-width: 900px; margin: auto; }
        .form-section, .list-section { border: 1px solid #ccc; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
        h2 { border-bottom: 2px solid #0056b3; padding-bottom: 10px; color: #0056b3; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"] { width: 95%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
        button { padding: 10px 20px; color: white; border: none; cursor: pointer; border-radius: 4px; }
        .btn-salvar { background-color: #28a745; }
        .btn-atualizar { background-color: #007bff; }
        .btn-deletar { background-color: #dc3545; padding: 5px 10px; }
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
    <h1>Painel de Gerenciamento</h1>

    <div class="message-box">
        <c:if test="${not empty mensagem}"><p class="sucesso"><b><c:out value="${mensagem}"/></b></p></c:if>
        <c:if test="${not empty mensagemErro}"><p class="erro"><b><c:out value="${mensagemErro}"/></b></p></c:if>
    </div>

    <div class="form-section">
        <h2>
            <c:choose>
                <c:when test="${not empty proprietarioEncontrado}">Editando Proprietário ID: <c:out value="${proprietarioEncontrado.id}" /></c:when>
                <c:otherwise>Cadastrar Novo Proprietário</c:otherwise>
            </c:choose>
        </h2>
        <form action="${pageContext.request.contextPath}/proprietario" method="post">
            <input type="hidden" name="action" value="<c:out value="${not empty proprietarioEncontrado ? 'update' : 'create'}"/>">
            <c:if test="${not empty proprietarioEncontrado}">
                <input type="hidden" name="id" value="<c:out value='${proprietarioEncontrado.id}'/>">
            </c:if>
            <div class="form-group">
                <label for="cpf_cnpj">CPF/CNPJ:</label>
                <input type="text" id="cpf_cnpj" name="cpf_cnpj" value="<c:out value='${proprietarioEncontrado.cpf_cnpj}'/>" required>
            </div>
            <div class="form-group">
                <label for="nome">Nome Completo:</label>
                <input type="text" id="nome" name="nome" value="<c:out value='${proprietarioEncontrado.nome}'/>" required>
            </div>
            <div class="form-group">
                <label for="endereco">Endereço:</label>
                <input type="text" id="endereco" name="endereco" value="<c:out value='${proprietarioEncontrado.endereco}'/>" required>
            </div>
            <button type="submit" class="<c:out value="${not empty proprietarioEncontrado ? 'btn-atualizar' : 'btn-salvar'}"/>">
                <c:out value="${not empty proprietarioEncontrado ? 'Atualizar' : 'Salvar Novo'}"/>
            </button>
        </form>
    </div>

    <div class="list-section">
        <h2>Proprietários Cadastrados</h2>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>CPF/CNPJ</th>
                <th>Endereço</th>
                <th>Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="prop" items="${listaProprietarios}">
                <tr>
                    <td><c:out value="${prop.id}"/></td>
                    <td><c:out value="${prop.nome}"/></td>
                    <td><c:out value="${prop.cpf_cnpj}"/></td>
                    <td><c:out value="${prop.endereco}"/></td>
                    <td>
                        <form action="${pageContext.request.contextPath}/proprietario" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="<c:out value='${prop.id}'/>">
                            <button type="submit" class="btn-deletar">Excluir</button>
                        </form>
                        <a href="${pageContext.request.contextPath}/proprietario?id=<c:out value='${prop.id}'/>">Editar</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty listaProprietarios}">
                <tr>
                    <td colspan="5">Nenhum proprietário cadastrado.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>