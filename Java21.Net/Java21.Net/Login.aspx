<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Login.aspx.cs" Inherits="Java21.Net.Login" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>21天学通Java-登录</title>
    <meta name="keywords" content="21天学通Java,邠心,邠心工作室,Android软件开发,Java学习,BXA学堂,正德应用,自学Java" />
    <meta name="description" content="《21天学通Java》-邠心工作室-Java学习网站-在线学习，Android软件开发" />
    <meta name="author" content="邠心" />
    <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
    <link href="css/login.css" rel="stylesheet" />
</head>
<body>
    <form id="form1" runat="server">
        <div id="Loginbox">
            <table border="0" cellspacing="0" cellpadding="0" id="Logintable">
                <tr>
                    <td valign="middle" class="tips">
                        <div id="errormessage" runat="server" style="text-align:right;">
                            <asp:Literal ID="litError" runat="server"></asp:Literal></div></td>
                </tr>
                <tr>
                    <td align="right">
                        <div class="inputbox">账&nbsp;&nbsp;&nbsp; 号<asp:TextBox ID="txtUser" runat="server"></asp:TextBox></div>
                    </td>
                </tr>
                <tr>
                    <td align="right">
                        <div class="inputbox">
                            密&nbsp;&nbsp;&nbsp; 码<asp:TextBox ID="txtPwd" runat="server" TextMode="Password"></asp:TextBox>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="right">
                        <asp:HyperLink runat="server" NavigateUrl="~/Register.aspx">注册</asp:HyperLink>
                        <asp:Button ID="btnLogin" runat="server" Text="登  录" CssClass="loginbutton" OnClick="btnLogin_Click" />
                    </td>
                </tr>
            </table>
            <div class="copyrightbox">
                © 2013-2016 邠心工作室
            </div>
        </div>
    </form>
</body>
</html>
