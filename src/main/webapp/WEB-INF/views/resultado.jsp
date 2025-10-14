<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Importa a biblioteca de tags do JSTL (muito útil) --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Resultado do Cadastro</title>
</head>
<body>

<%-- Usa a tag <c:if> do JSTL para verificar se existe uma mensagem de erro --%>
<c:if test="${not empty mensagemErro}">
    <h1 style="color: red;">Ocorreu um Erro</h1>
    <p>${mensagemErro}</p>
</c:if>

<%-- Verifica se existe uma mensagem de sucesso --%>
<c:if test="${not empty mensagem}">
    <h1>${mensagem}</h1>
    <h3>Detalhes do Cadastro:</h3>
    <ul>
        <li><strong>ID:</strong> ${proprietarioSalvo.id}</li>
        <li><strong>Nome:</strong> ${proprietarioSalvo.nome}</li>
        <li><strong>CPF/CNPJ:</strong> ${proprietarioSalvo.cpf_cnpj}</li>
        <li><strong>Endereço:</strong> ${proprietarioSalvo.endereco}</li>
    </ul>
</c:if>

<br>
<a href="../../index.jsp">Voltar para o Cadastro</a>

</body>
</html>