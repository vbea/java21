<%@ Page Title="" Language="C#" MasterPageFile="~/Java.Master" AutoEventWireup="true" CodeBehind="Settings.aspx.cs" Inherits="Java21.Net.Settings" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <title>设置</title>
    <link href="script/asyncbox/skins/ZCMS/asyncbox.css" rel="stylesheet" />
    <script type="text/javascript" src="script/cooke.js"></script>
    <script type="text/javascript" src="script/asyncbox/AsyncBox.v1.4.5.js"></script>
    <script type="text/javascript">
        function showEditPassword() {
            asyncbox.open({
                id: "edit_pass",
                url: 'ChangePassword.aspx',
                width: 300,
                height: 300,
                data: { "action": "amend" },
                title: '修改密码',
            });
        }
        function closePasswordDialog()
        {
            asyncbox.close("edit_pass");
            document.location.href = 'Login.aspx';
        }
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <p>更改账户信息</p>
    <table>
        <tr>
            <td class="table_i">用 户 名：</td>
            <td class="table_m">
                <asp:Label ID="labUserName" runat="server" style="width:100%; text-align:left;"></asp:Label></td>
        </tr>
        <tr>
            <td class="table_i">用户角色：</td>
            <td class="table_m">
                <asp:Label ID="labRole" runat="server" style="width:100%; text-align:left;"></asp:Label></td>
        </tr>
        <tr>
            <td class="table_i">密&nbsp; 码：</td>
            <td class="table_m">
                <asp:UpdatePanel runat="server">
                    <ContentTemplate>
                        <asp:LinkButton ID="linPassword" runat="server" OnClientClick="showEditPassword();">修改密码</asp:LinkButton>
                    </ContentTemplate>
                </asp:UpdatePanel>
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
                <asp:TextBox ID="txtName" runat="server" MaxLength="10" required="required" Height="18px" Width="160px"></asp:TextBox></td>
        </tr>
        <tr>
            <td class="table_i">性&nbsp; 别：</td>
            <td class="table_m">
                <asp:DropDownList ID="ddlGender" runat="server" Width="80%">
                    <asp:ListItem Selected="True" Value="0">男</asp:ListItem>
                    <asp:ListItem Value="1">女</asp:ListItem>
                </asp:DropDownList></td>
        </tr>
        <tr>
            <td class="table_i">个人说明：</td>
            <td class="table_m">
                <asp:TextBox ID="txtRemark" runat="server" Height="18px" Width="160px"></asp:TextBox></td>
        </tr>
        <tr>
            <td colspan="2" class="table_i">
                <br />
                <asp:Button ID="btnAmend" runat="server" Text="修 改" CssClass="btn btn-info" Width="100px" OnClick="btnAmend_Click" OnClientClick="return confirm('确定要修改吗？')"/></td>
        </tr>
    </table>
</asp:Content>
