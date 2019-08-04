<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="ChangePassword.aspx.cs" Inherits="Java21.Net.ChangePassword" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>修改密码</title>
    <link href="css/java.css" rel="stylesheet" />
    <link href="css/bootstrap.css" rel="stylesheet" />
    <script type="text/javascript">
        function checkPasswords() {
            var pass1 = document.getElementById("txtNewPass");
            var pass2 = document.getElementById("txtPassword");

            if (pass1.value != pass2.value)
                pass2.setCustomValidity("两次输入的密码不匹配");
            else
                pass2.setCustomValidity("");
        }
    </script>
</head>
<body>
    <form id="form1" runat="server">
        <div style="width: 100%; padding: 0; margin: 0 auto;">
            <br />
            请输入原密码<br />
            <asp:TextBox ID="txtOldPass" runat="server" required="required" MaxLength="16" TextMode="Password"></asp:TextBox><br />
            请输入新密码<br />
            <asp:TextBox ID="txtNewPass" runat="server" required="required" placeholder="不超过16位的字母数字组合" onchange="checkPasswords()" MaxLength="16" TextMode="Password"></asp:TextBox><br />
            确认新密码<br />
            <asp:TextBox ID="txtPassword" runat="server" required="required" onchange="checkPasswords()" MaxLength="16" TextMode="Password"></asp:TextBox><br />
            <div style="text-align: center;">
                <asp:Button ID="btnChangePass" runat="server" Text="修 改" CssClass="btn btn-info" Width="100px" OnClick="btnChangePass_Click" />
            </div>
        </div>
    </form>
</body>
</html>
