<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Register.aspx.cs" Inherits="Java21.Net.Register" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="keywords" content="21天学通Java,邠心,邠心工作室,Android软件开发,Java学习,BXA学堂,正德应用,自学Java" />
    <meta name="description" content="《21天学通Java》-邠心工作室-Java学习网站-在线学习，Android软件开发" />
    <meta name="author" content="邠心" />
    <title>注册</title>
    <link href="css/java.css" rel="stylesheet" />
    <link href="css/bootstrap.css" rel="stylesheet" />
    <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
    <script type="text/javascript">
        function checkMail(email) {
            if (email.length <= 0)
            {
                
            }
            else if (!checkemail(email.toLowerCase())) {
                document.getElementById("txtEmail").focus();
                alert("邮箱格式不正确");
            }
        }

        function checkUserID(id) {
            var Expression = /^[A-Za-z0-9]*$/;
            if (id.length <= 0) {
                document.getElementById("txtUserName").focus();
                alert("请输入用户名");
            }
            else if (id.length < 3)
            {
                document.getElementById("txtUserName").focus();
                alert("用户名长度不足");
            }
            else if (!Expression.test(id)) {
                document.getElementById("txtUserName").focus();
                alert("用户名输入不正确");
            }
        }
           
        function checkemail(email) {
            var Expression = /^(?:[a-z\d]+[_\-\+\.]?)*[a-z\d]+@(?:([a-z\d]+\-?)*[a-z\d]+\.)+([a-z]{2,})+$/;
            if (Expression.test(email) == true) {
                return true;
            } else {
                return false;
            }
        }
    </script>
</head>
<body>
    <form id="form1" runat="server">
        <div class="pagea">
            <table>
                <tr>
                    <th colspan="2" style="width:100%; padding-bottom:10px; font-size:16px;">注册用户</th>
                </tr>
                <tr>
                    <td>用 户 名：</td>
                    <td>
                        <asp:TextBox ID="txtUserName" runat="server" MaxLength="20" placeholder="3-11位的数字或字母" required="required" pattern="^[A-Za-z0-9]{3,11}$"></asp:TextBox></td>
                </tr>
                <tr>
                    <td>密&nbsp; 码：</td>
                    <td>
                        <asp:TextBox ID="txtPassword" runat="server" MaxLength="16" TextMode="Password" required="required" placeholder="不超过16位的字母和数字组合"></asp:TextBox></td>
                </tr>
                <tr>
                    <td>邮&nbsp; 箱：</td>
                    <td>
                        <asp:TextBox ID="txtEmail" runat="server" MaxLength="100" pattern="^(?:[a-z\d]+[_\-\+\.]?)*[a-z\d]+@(?:([a-z\d]+\-?)*[a-z\d]+\.)+([a-z]{2,})+$" required="required" placeholder="email@example.com"></asp:TextBox></td>
                </tr>
                <tr>
                    <td>昵&nbsp; 称：</td>
                    <td>
                        <asp:TextBox ID="txtName" runat="server" MaxLength="10" required="required" placeholder="汉字或字母"></asp:TextBox></td>
                </tr>
                <tr>
                    <td>性&nbsp; 别：</td>
                    <td style="text-align:left;">
                        <asp:DropDownList ID="ddlGender" runat="server" Width="80%">
                            <asp:ListItem Selected="True" Value="0">男</asp:ListItem>
                            <asp:ListItem Value="1">女</asp:ListItem>
                        </asp:DropDownList></td>
                </tr>
                <tr>
                    <td>个人说明：</td>
                    <td>
                        <asp:TextBox ID="txtRemark" runat="server"></asp:TextBox></td>
                </tr>
                <tr>
                    <td>注册码：</td>
                    <td>
                        <asp:TextBox ID="txtKey" runat="server" required="required" placeholder="可在APP中获取" MaxLength="23"></asp:TextBox></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <br />
                        <a href="Login.aspx">返回登录</a>
                        <asp:Button ID="btnRegister" runat="server" Text="注册" CssClass="btn btn-info" Width="100px" OnClick="btnRegister_Click" /></td>
                </tr>
            </table>
        </div>
    </form>
</body>
</html>
