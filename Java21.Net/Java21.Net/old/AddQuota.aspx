<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="AddQuota.aspx.cs" Inherits="Java21.Net.AddQuota" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>添加名言</title>
    <link href="/css/java.css" rel="stylesheet" />
    <link href="/css/bootstrap.css" rel="stylesheet" />
</head>
<body>
    <form id="form1" runat="server">
    <div>
    名言/公告内容：<br />
        <asp:TextBox ID="txtValues" runat="server" Height="100px" TextMode="MultiLine" required="required" Width="92%"></asp:TextBox><br />
        <asp:CheckBox ID="chkTip" runat="server" Text="永久显示" />
        <asp:CheckBox ID="chkDate" runat="server" Text="更新时间" Visible="false"/>
        <br /><asp:Button ID="btnAdd" runat="server" Text="添 加" CssClass="btn btn-info" OnClick="btnAdd_Click" Visible="False"/>
        <asp:Button ID="btnEdit" runat="server" Text="修 改" CssClass="btn btn-info" OnClick="btnEdit_Click" Visible="False"/>
    </div>
    </form>
</body>
</html>
