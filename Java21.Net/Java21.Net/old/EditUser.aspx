<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="EditUser.aspx.cs" Inherits="Java21.Net.EditUser" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>修改用户资料</title>
    <link href="/css/java.css" rel="stylesheet" />
    <link href="/css/bootstrap.css" rel="stylesheet" />
</head>
<body>
    <form id="form1" runat="server">
        <div style="width: 280px; padding: 5px 0; margin: 0 auto; text-align:center;">
            <table>
                <tr>
                    <td class="table_i">用户名：</td>
                    <td class="table_m"><asp:TextBox ID="txtUserName" runat="server" MaxLength="10" required="required" Height="18px" Width="160px" pattern="^[A-Za-z0-9]{3,20}$"></asp:TextBox></td>
                </tr>
                <tr>
                    <td class="table_i">用户角色：</td>
                    <td class="table_m">
                        <asp:DropDownList ID="ddlRole" runat="server" Width="80%">
                            <asp:ListItem Value="1">管理员</asp:ListItem>
                            <asp:ListItem Value="2">普通用户</asp:ListItem>
                            <asp:ListItem Value="4">受限用户</asp:ListItem>
                        </asp:DropDownList>
                    </td>
                </tr>
                <tr runat="server" id="trPass">
                    <td class="table_i">密&nbsp; 码：</td>
                    <td class="table_m">
                        <asp:TextBox ID="txtPassword" runat="server" MaxLength="16" Height="18px" Width="50%"></asp:TextBox>
                        <asp:LinkButton ID="linPassword" runat="server" OnClientClick="return confirm('确定要重置该用户的密码?')" OnClick="linPassword_Click">重置密码</asp:LinkButton>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <asp:Label ID="labPassword" runat="server" Text="已记录密码，按修改按钮确认，放弃修改请关闭本窗口" ForeColor="Red" Visible="False"></asp:Label>
                    </td>
                </tr>
                <tr>
                    <td class="table_i">邮&nbsp; 箱：</td>
                    <td class="table_m">
                        <asp:Label ID="labEmail" runat="server"></asp:Label>
                </tr>
                <tr>
                    <td class="table_i">昵&nbsp; 称：</td>
                    <td class="table_m">
                        <asp:TextBox ID="txtNickName" runat="server" MaxLength="10" required="required" Height="18px" Width="160px"></asp:TextBox></td>
                </tr>
                <tr>
                    <td class="table_i">性&nbsp; 别：</td>
                    <td class="table_m">
                        <asp:DropDownList ID="ddlGender" runat="server" Width="80%">
                            <asp:ListItem Selected="True" Value="0">男</asp:ListItem>
                            <asp:ListItem Value="1">女</asp:ListItem>
                        </asp:DropDownList>
                    </td>
                </tr>
                <tr>
                    <td class="table_i">个人说明：</td>
                    <td class="table_m">
                        <asp:TextBox ID="txtRemark" runat="server" Height="18px" Width="160px"></asp:TextBox></td>
                </tr>
                <tr>
                    <td></td>
                    <td class="table_m">
                        <asp:CheckBox ID="chkValid" runat="server" Text="有效的用户" style="vertical-align:middle; display:inline-block;"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="table_i">
                        <br />
                        <asp:Button ID="btnAmend" runat="server" Text="修 改" CssClass="btn btn-info" Width="100px" OnClick="btnAmend_Click" OnClientClick="return confirm('确定要修改吗？')"/></td>
                </tr>
            </table>
        </div>
    </form>
</body>
</html>
